plugins {
    alias(libs.plugins.ktor)
}

application.mainClass = "ru.sliva.zapp.client.Client"

dependencies {
    implementation(project(":data"))

    implementation(libs.ktor.core)
    implementation(libs.ktor.netty)

    implementation(libs.logback)
}