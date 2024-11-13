import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.http4k.internal.ModuleLicense.Apache2

val license by project.extra { Apache2 }

plugins {
    id("org.http4k.module")
    id("com.github.davidmc24.gradle.plugin.avro")
}

dependencies {
    api("org.http4k:http4k-format-moshi") {
        exclude("org.jetbrains.kotlin", "kotlin-reflect")
    }

    implementation("org.apache.avro:avro:_")

    implementation("se.ansman.kotshi:api:_")

    testFixturesApi(libs.kotlin.reflect)

    testFixturesImplementation("org.apache.avro:avro:_")

    testFixturesApi("se.ansman.kotshi:api:_")
}

tasks {
    withType<KotlinCompile>().configureEach {
        dependsOn("generateTestFixturesAvroJava")
    }
}
