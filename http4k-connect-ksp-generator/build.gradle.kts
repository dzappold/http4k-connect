import org.http4k.internal.ModuleLicense.Apache2

val license by project.extra { Apache2 }

plugins {
    id("org.http4k.module")
}

dependencies {
    implementation("org.http4k:http4k-format-moshi")
    implementation("com.squareup:kotlinpoet:_")
    implementation("com.squareup:kotlinpoet-metadata:_")
    implementation("com.squareup:kotlinpoet-ksp:_")
    implementation(libs.symbol.processing.api)

    testFixturesApi("se.ansman.kotshi:api:_")
    testFixturesApi("org.http4k:http4k-format-moshi")
    testFixturesApi(libs.result4k)

    kspTestFixtures(project(":http4k-connect-ksp-generator"))
}

