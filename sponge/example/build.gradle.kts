plugins {
    id("org.spongepowered.plugin")
}

version = "0.2.1"

tasks.withType<Jar> {
    baseName = "FileInventoriesExample"
}

dependencies {
    compileOnly(parent!!)
}

sponge.plugin.id = "file-inv-example"
