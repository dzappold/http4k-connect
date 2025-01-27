import org.http4k.internal.ModuleLicense.Apache2

val license by project.extra { Apache2 }

plugins {
    id("org.http4k.module")
    id("org.http4k.connect.module")
    id("org.http4k.connect.storage")
}

dependencies {
    api("org.http4k:http4k-format-moshi:${rootProject.properties["http4k_version"]}")
    api("org.jetbrains.exposed:exposed-core:_")
    api("org.jetbrains.exposed:exposed-jdbc:_")

    testFixturesApi("com.zaxxer:HikariCP:_")
    testFixturesApi("com.h2database:h2:_")
}
