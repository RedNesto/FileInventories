import net.minecrell.gradle.licenser.LicenseExtension

buildscript {
    repositories {
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }
    dependencies {
        classpath("gradle.plugin.net.minecrell:licenser:0.4.1")
    }
}

allprojects {
    group = "io.github.rednesto"
}

subprojects {
    apply {
        plugin("java")
        plugin("net.minecrell.licenser")
    }

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    repositories {
        mavenCentral()
    }

    configure<LicenseExtension> {
        header = rootProject.file("LICENSE")

        include("**/*.java")

        newLine = false
    }
}
