plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.shadow) apply false
    alias(libs.plugins.kotlinx.serialization) apply false
}

tasks.jar { enabled = false }

repositories {
    mavenCentral()
}

subprojects {
    group = "ru.sliva"
    version = "1.0-SNAPSHOT"

    val libs = rootProject.libs

    repositories {
        mavenCentral()
    }

    applyPlugins(
        libs.plugins.kotlin,
        libs.plugins.kotlinx.serialization,
        libs.plugins.shadow
    )

    kotlin.jvmToolchain(21)

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release = 21
    }

    tasks.withType<Jar> {
        destinationDirectory = file("$rootDir/build")
    }

    sourceSets.main {
        kotlin.srcDir("src")
        resources.srcDir("resources")
    }
}

fun Project.applyPlugins(vararg plugins: Provider<PluginDependency>) {
    plugins.mapNotNull { it.orNull?.pluginId }.forEach {
        apply {
            plugin(it)
        }
    }
}
