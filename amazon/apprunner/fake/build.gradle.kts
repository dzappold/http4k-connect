import org.http4k.internal.ModuleLicense.Apache2

val license by project.extra { Apache2 }

plugins {
    id("org.http4k.module")
}

dependencies {
    testFixturesApi("org.http4k:http4k-format-moshi")
    testFixturesApi(project(path = ":http4k-connect-amazon-iamidentitycenter"))
    testFixturesApi(testFixtures(project(":http4k-connect-amazon-core")))
}
