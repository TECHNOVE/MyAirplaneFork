pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        maven("https://papermc.io/repo/repository/maven-public/")
    }
}

rootProject.name = "MyAirplaneFork"

include("MyAirplaneFork-API", "MyAirplaneFork-Server")