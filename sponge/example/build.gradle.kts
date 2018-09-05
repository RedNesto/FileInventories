import com.qixalite.spongestart.SpongeStartExtension

buildscript {
    configurations.getByName("classpath").resolutionStrategy.cacheChangingModulesFor(0, TimeUnit.SECONDS)

    repositories {
        mavenLocal()
    }
    dependencies {
        classpath("com.qixalite:SpongeStart:3.2.3-SNAPSHOT")
    }
}

plugins {
    id("org.spongepowered.plugin")
}

apply {
    plugin("com.qixalite.spongestart2")
}

version = "0.2"

tasks.withType<Jar> {
    baseName = "FileInventoriesExample"
}

dependencies {
    compileOnly(parent!!)
}

sponge.plugin.id = "file-inv-example"

configure<SpongeStartExtension> {
    minecraft = "1.12.2"
    api = "7.1.0"

    spongeVanilla = "1.12.2-7.1.0-BETA-111"

    forge = "14.23.4.2705"
    spongeForge = "1.12.2-2705-7.1.0-BETA-3422"
}
