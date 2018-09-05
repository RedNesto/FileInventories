plugins {
    id("org.spongepowered.plugin") version "0.9.0"
}

version = extra["pluginVersion"] as String

dependencies {
    compile("org.spongepowered:spongeapi:7.1.0-SNAPSHOT")
}

sponge.plugin.id = extra["pluginId"] as String

tasks.withType<Jar> {
    baseName = "FileInventories-Sponge"
}
