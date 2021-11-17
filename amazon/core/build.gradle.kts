dependencies {
    api(Libs.http4k_aws)

    compileOnly(Libs.http4k_format_moshi) {
        exclude("org.jetbrains.kotlin", "kotlin-reflect")
    }

    implementation(Libs.api)
    ksp("com.google.devtools.ksp:symbol-processing-api:1.5.31-1.0.1")
    ksp(Libs.se_ansman_kotshi_compiler)

    implementation(Libs.http4k_format_core)
}


ksp {
    arg("option1", "value1")
    arg("option2", "value2")
}
