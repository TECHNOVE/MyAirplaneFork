import io.papermc.paperweight.util.Constants

plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.0.0" apply false
    id("io.papermc.paperweight.patcher") version "1.1.6"
}

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/") {
        content { onlyForConfigurations(Constants.PAPERCLIP_CONFIG) }
    }
    maven("https://maven.quiltmc.org/repository/release/") {
        content { onlyForConfigurations(Constants.REMAPPER_CONFIG) }
    }
}

dependencies {
    remapper("org.quiltmc:tiny-remapper:0.4.1")
    paperclip("io.papermc:paperclip:2.0.1")
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "maven-publish")

    java { toolchain { languageVersion.set(JavaLanguageVersion.of(16)) } }

    tasks.withType<JavaCompile> { options.isFork = true; options.isIncremental = true; options.encoding = Charsets.UTF_8.name(); options.release.set(16) }
    
    tasks.withType<Javadoc> { options.encoding = Charsets.UTF_8.name() }
    
    tasks.withType<ProcessResources> { filteringCharset = Charsets.UTF_8.name() }
    
    repositories {
        mavenCentral()
        maven("https://libraries.minecraft.net/")
        maven("https://repo.codemc.io/repository/maven-public/")
        maven("https://oss.sonatype.org/content/groups/public/")
        maven("https://papermc.io/repo/repository/maven-public/")
        maven("https://ci.emc.gs/nexus/content/groups/aikar/")
        maven("https://repo.aikar.co/content/groups/aikar")
        maven("https://repo.md-5.net/content/repositories/releases/")
        maven("https://hub.spigotmc.org/nexus/content/groups/public/")
        maven("https://jitpack.io")
    }
    configure<PublishingExtension> {
        repositories.maven {
            name = "maven"
            url = uri("https://my_mvn_repo.org/repository/maven-snapshots/")
            credentials(PasswordCredentials::class)
        }
    }
}

paperweight {
    serverProject.set(project(":MyAirplaneFork-Server"))

    useStandardUpstream("Airplane") {
        url.set(github("TECHNOVE", "Airplane"))
        ref.set(providers.gradleProperty("AirplaneCommit"))

        withStandardPatcher {
            baseName("Airplane")

            apiOutputDir.set(layout.projectDirectory.dir("MyAirplaneFork-API"))
            serverOutputDir.set(layout.projectDirectory.dir("MyAirplaneFork-Server"))
        }
    }
}

tasks.paperclipJar {
    destinationDirectory.set(rootProject.layout.projectDirectory)
    archiveFileName.set("myairplanefork-paperclip.jar")
}
