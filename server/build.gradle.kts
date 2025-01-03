plugins {
    alias(libs.plugins.ktor)
}

application.mainClass = "ru.sliva.zapp.server.Server"

dependencies {
    implementation(project(":data"))

    implementation(libs.ktor.core)
    implementation(libs.ktor.netty)

    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.h2)
    implementation(libs.postgres)

    implementation(libs.logback)
}