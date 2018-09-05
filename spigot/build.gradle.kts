import org.apache.tools.ant.filters.ReplaceTokens
import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter.include

version = extra["pluginVersion"] as String

dependencies {
    compile(files("libs/spigot-1.8.8.jar", "libs/spigot-1.9.4.jar", "libs/spigot-1.10.2.jar", "libs/spigot-1.11.2.jar", "libs/spigot-1.12.2.jar"))
}

tasks.withType<Jar> {
    baseName = "FileInventories-Spigot"
}

tasks.withType<ProcessResources> {
    from(sourceSets.getByName("main").resources.srcDirs) {
        filter<ReplaceTokens>("tokens" to mapOf("version" to version))
    }
}
