pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://repo.maxmind.com/repository/release") }
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "local_network_Scanner"
include(":app")
