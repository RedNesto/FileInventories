import org.apache.tools.ant.filters.ReplaceTokens

version = "0.2"

repositories {
    maven {
        name = "spigotmc-repo"
        url = uri("https://hub.spigotmc.org/nexus/content/groups/public/")
    }
    maven {
        name = "sonatype"
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
}

dependencies {
    compileOnly(parent!!)
}

tasks.withType<Jar> {
    baseName = "FileInventoriesExample"
}

tasks.withType<ProcessResources> {
    from(sourceSets.getByName("main").resources.srcDirs) {
        filter<ReplaceTokens>("tokens" to mapOf("version" to version))
    }
}
