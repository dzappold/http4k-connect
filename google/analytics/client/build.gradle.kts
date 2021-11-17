dependencies {
    implementation(Libs.api)
    ksp("com.google.devtools.ksp:symbol-processing-api:1.5.31-1.0.1")
    ksp(Libs.se_ansman_kotshi_compiler)
}
