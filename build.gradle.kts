import groovy.util.Node
import groovy.xml.QName
import org.gradle.api.JavaVersion.VERSION_1_8
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    idea
    jacoco
    signing
    publishing
    `maven-publish`
    kotlin("kapt") version "1.6.0"
}

buildscript {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:_")
        classpath("com.github.jengelman.gradle.plugins:shadow:_")
        classpath("io.codearte.nexus-staging:io.codearte.nexus-staging.gradle.plugin:_")
    }
}

apply(plugin = "io.codearte.nexus-staging")

allprojects {
    apply(plugin = "java")
    apply(plugin = "kotlin")
    apply(plugin = "kotlin-kapt")
//    apply(plugin = "org.jetbrains.dokka")
//    apply(plugin = "com.github.kt3k.coveralls")

    apply(plugin = "org.gradle.jacoco")

    repositories {
        mavenCentral()
    }

    version = project.getProperties()["releaseVersion"] ?: "LOCAL"
    group = "org.http4k"

    jacoco {
        toolVersion = "0.8.7"
    }

    tasks {
        withType<KotlinCompile> {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }

        java {
            sourceCompatibility = VERSION_1_8
            targetCompatibility = VERSION_1_8
        }

        withType<Test> {
            useJUnitPlatform()
        }

        withType<GenerateModuleMetadata> {
            enabled = false
        }

        if (hasCodeCoverage(project)) {
            named<JacocoReport>("jacocoTestReport") {
                reports {
                    html.isEnabled = true
                    xml.isEnabled = true
                }
//            afterEvaluate {
////                classDirectories.setFrom(files(classDirectories.files.collect {
////                    fileTree(dir = it, exclude = "**/Kotshi**/**")
////                    fileTree(dir = it, exclude = "**/**Extensions**")
////                }))
//            }
//        }
            }
        }

    }

    val http4k_version = project.getProperties()["http4k_version"] ?: "LOCAL"

    dependencies {
        implementation(platform("org.http4k:http4k-bom:$http4k_version")) // manually set because of auto-upgrading
        implementation(platform(Libs.forkhandles_bom))
        api("org.http4k:http4k-core")
        api("dev.forkhandles:result4k")

        testImplementation(platform(Libs.junit_bom))
        testImplementation("org.http4k:http4k-testing-hamkrest")
        testImplementation("org.http4k:http4k-testing-approval")

        testImplementation("org.junit.jupiter:junit-jupiter-api")
        testImplementation("org.junit.jupiter:junit-jupiter-engine")
        testImplementation(platform(Libs.testcontainers_bom))
        testImplementation("org.junit.jupiter:junit-jupiter-params")
        testImplementation("org.testcontainers:junit-jupiter")
        testImplementation("org.testcontainers:testcontainers")

//        dokkaHtmlPlugin(Libs.org_jetbrains_dokka_gradle_plugin)

        if (project.name.endsWith("core-fake")) {
            api(project(":http4k-connect-core"))
        } else if (project.name.endsWith("fake")) {
            api(project(":http4k-connect-core-fake"))
            api(project(":${project.name.substring(0, project.name.length - 5)}"))
            testImplementation(
                project(
                    path = ":${project.name.substring(0, project.name.length - 5)}",
                    configuration = "testArtifacts"
                )
            )
            testImplementation(project(path = ":http4k-connect-core-fake", configuration = "testArtifacts"))
        } else if (project.name.startsWith("http4k-connect-storage-core")) {
            // bom - no code
        } else if (project.name.startsWith("http4k-connect-storage")) {
            api(project(":http4k-connect-storage-core"))
            testImplementation(project(path = ":http4k-connect-core-fake", configuration = "testArtifacts"))
            testImplementation(project(path = ":http4k-connect-storage-core", configuration = "testArtifacts"))
        } else if (project.name == "http4k-connect") {
            rootProject.subprojects.forEach {
                testImplementation(project(it.name))
            }
        } else if (project.name == "http4k-connect-bom") {
            // bom - no code
        } else if (project.name == "http4k-connect-kapt-generator") {
            api(project(":http4k-connect-core"))
        } else if (project.name != "http4k-connect-core") {
            api("org.http4k:http4k-cloudnative")
            api(project(":http4k-connect-core"))
            kapt(project(path =":http4k-connect-kapt-generator"))
            testImplementation(project(path = ":http4k-connect-core-fake", configuration = "testArtifacts"))
        }
    }

}

subprojects {
    apply(plugin = "idea")

    val sourcesJar by tasks.creating(Jar::class) {
        archiveClassifier.set("sources")
        from(project.the<SourceSetContainer>()["main"].allSource)
        dependsOn(tasks.named("classes"))
    }

    val javadocJar by tasks.creating(Jar::class) {
        archiveClassifier.set("javadoc")
        from(tasks.named<Javadoc>("javadoc").get().destinationDir)
        dependsOn(tasks.named("javadoc"))
    }

    tasks {
        named<Jar>("jar") {
            manifest {

                attributes(
                    mapOf(
                        "http4k_version" to archiveVersion,
                        "Implementation-Title" to project.name,
                        "Implementation-Vendor" to "org.http4k",
                        "Implementation-Version" to project.version
                    )
                )
            }
        }

        val testJar by creating(Jar::class) {
            archiveClassifier.set("test")
            from(project.the<SourceSetContainer>()["test"].output)
        }

        configurations.create("testArtifacts") {
            extendsFrom(configurations["testApi"])
        }
        artifacts {
            add("testArtifacts", testJar)
            archives(sourcesJar)
            archives(javadocJar)
        }
    }

    val enableSigning = project.findProperty("sign") == "true"

    apply(plugin = "maven-publish") // required to upload to sonatype

    if (enableSigning) { // when added it expects signing keys to be configured
        apply(plugin = "signing")
        signing {
            val signingKey: String? by project
            val signingPassword: String? by project
            useInMemoryPgpKeys(signingKey, signingPassword)
            sign(publishing.publications)
        }
    }

    val nexusUsername: String? by project
    val nexusPassword: String? by project

    publishing {
        repositories {
            maven {
                name = "SonatypeStaging"
                setUrl("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
                credentials {
                    username = nexusUsername
                    password = nexusPassword
                }
            }
            maven {
                name = "SonatypeSnapshot"
                setUrl("https://oss.sonatype.org/content/repositories/snapshots/")
                credentials {
                    username = nexusUsername
                    password = nexusPassword
                }
            }
        }
        publications {
            val archivesBaseName = tasks.jar.get().archiveBaseName.get()
            create<MavenPublication>("mavenJava") {
                artifactId = archivesBaseName
                pom.withXml {
                    asNode().appendNode("name", archivesBaseName)
                    asNode().appendNode("description", description)
                    asNode().appendNode("url", "https://http4k.org")
                    asNode().appendNode("developers")
                        .appendNode("developer").appendNode("name", "David Denton").parent()
                        .appendNode("email", "david@http4k.org")
                        .parent().parent()
                        .appendNode("developer").appendNode("name", "Albert Latacz").parent()
                        .appendNode("email", "albert@http4k.org")
                        .parent().parent()
                        .appendNode("developer").appendNode("name", "Ivan Sanchez").parent()
                        .appendNode("email", "ivan@http4k.org")
                    asNode().appendNode("scm")
                        .appendNode("url", "git@github.com:http4k/$archivesBaseName.git").parent()
                        .appendNode("connection", "scm:git:git@github.com:http4k/http4k-connect.git").parent()
                        .appendNode("developerConnection", "scm:git:git@github.com:http4k/http4k-connect.git")
                    asNode().appendNode("licenses").appendNode("license")
                        .appendNode("name", "Apache License, Version 2.0").parent()
                        .appendNode("url", "http://www.apache.org/licenses/LICENSE-2.0.html")
                }
                from(components["java"])

                artifact(sourcesJar)
                artifact(javadocJar)
            }
        }
    }

    sourceSets {
        named("test") {
            withConvention(KotlinSourceSet::class) {
                kotlin.srcDir("$projectDir/src/examples/kotlin")
            }
        }
    }
}

tasks.register<JacocoReport>("jacocoRootReport") {
    dependsOn(subprojects.map { it.tasks.named<Test>("test").get() })

    sourceDirectories.from(subprojects.flatMap { it.the<SourceSetContainer>()["main"].allSource.srcDirs })
    classDirectories.from(subprojects.map { it.the<SourceSetContainer>()["main"].output })
    executionData.from(subprojects
        .filter { it.name != "http4k-bom" }
        .map {
            it.tasks.named<JacocoReport>("jacocoTestReport").get().executionData
        }
    )

    reports {
        html.isEnabled = true
        xml.isEnabled = true
        csv.isEnabled = false
        xml.destination = file("${buildDir}/reports/jacoco/test/jacocoRootReport.xml")
    }
}

dependencies {
    subprojects
        .forEach {
            api(project(it.name))
            testImplementation(project(path = it.name, configuration = "testArtifacts"))
        }

    implementation(platform(Libs.bom))
    implementation("software.amazon.awssdk:cloudfront")
    implementation("software.amazon.awssdk:cognitoidentityprovider")
    implementation("software.amazon.awssdk:dynamodb")
    implementation("software.amazon.awssdk:kms")
    implementation("software.amazon.awssdk:lambda")
    implementation("software.amazon.awssdk:s3")
    implementation("software.amazon.awssdk:secretsmanager")
    implementation("software.amazon.awssdk:ses")
    implementation("software.amazon.awssdk:sns")
    implementation("software.amazon.awssdk:sqs")
    implementation("software.amazon.awssdk:ssm")
    implementation("software.amazon.awssdk:sts")
}

sourceSets {
    named("test") {
        withConvention(KotlinSourceSet::class) {
            kotlin.srcDir("$projectDir/src/docs")
            resources.srcDir("$projectDir/src/docs")
        }
    }
}

tasks.register("listProjects") {
    doLast {
        subprojects
            .forEach { System.err.println(it.name) }
    }
}

fun Node.childrenCalled(wanted: String) = children()
    .filterIsInstance<Node>()
    .filter {
        val name = it.name()
        (name is QName) && name.localPart == wanted
    }

tasks.named<KotlinCompile>("compileTestKotlin") {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = freeCompilerArgs + listOf("-Xjvm-default=all")
    }
}

fun hasCodeCoverage(project: Project) =
    project.name != "http4k-connect-bom" && project.name != "http4k-connect-kapt-adapter"
