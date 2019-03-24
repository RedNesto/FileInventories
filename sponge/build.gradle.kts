plugins {
    id("maven")
    id("org.spongepowered.plugin") version "0.9.0"
}

version = extra["pluginVersion"] as String

dependencies {
    compile("org.spongepowered:spongeapi:7.1.0")
}

sponge.plugin.id = extra["pluginId"] as String

tasks.withType<Jar> {
    baseName = "FileInventories-Sponge"
}

val sourcesJar = tasks.create("sourcesJar", Jar::class) {
    dependsOn("classes")
    classifier = "sources"
    afterEvaluate {
        from(the<SourceSetContainer>()["main"].allSource)
    }
}

val javadocJar = tasks.create("javadocJar", Jar::class) {
    dependsOn("javadoc")
    classifier = "javadoc"
    from(tasks.getByName("javadoc", Javadoc::class).destinationDir)
}

artifacts {
    add("compile", sourcesJar)
    add("compile", javadocJar)
}
