plugins {
    kotlin("jvm")
    id("org.http4k.project-metadata")
    id("org.http4k.nexus")
    id("com.google.devtools.ksp")
    id("org.http4k.connect.module")
}

metadata {
    developers = mapOf(
        "David Denton" to "david@http4k.org",
        "Ivan Sanchez" to "ivan@http4k.org",
        "Albert Latacz" to "albert@http4k.org"
    )
}
