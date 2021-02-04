import xyz.jpenilla.toothpick.gitCmd
import xyz.jpenilla.toothpick.toothpick

plugins {
    `java-library`
    id("xyz.jpenilla.toothpick") version "1.0.0-SNAPSHOT"
}

toothpick {
    forkName = "MyAirplaneFork"
    groupId = "com.mygroupid"
    val versionTag = System.getenv("BUILD_NUMBER")
        ?: "\"${gitCmd("rev-parse", "--short", "HEAD").output}\""
    forkVersion = "git-$forkName-$versionTag"
    forkUrl = "https://github.com/MyName/MyAirplaneFork"

    minecraftVersion = "1.16.5"
    nmsPackage = "1_16_R3"
    nmsRevision = "R0.1-SNAPSHOT"

    upstream = "Airplane"
    upstreamBranch = "origin/master"

    paperclipName = "launcher-myairplanefork"

    server {
        project = project(":$forkNameLowercase-server")
        patchesDir = rootProject.projectDir.resolve("patches/server")
    }
    api {
        project = project(":$forkNameLowercase-api")
        patchesDir = rootProject.projectDir.resolve("patches/api")
    }
}

subprojects {
    repositories {
        mavenCentral()
        maven("https://repo.aikar.co/content/groups/aikar/")
        maven("https://nexus.velocitypowered.com/repository/velocity-artifacts-snapshots/")
        maven("https://libraries.minecraft.net")
        mavenLocal()
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
        withSourcesJar()
    }
}
