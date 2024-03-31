pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven(url = "https://jcenter.bintray.com")
    }
}
dependencyResolutionManagement {
//    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
        mavenCentral()
        maven(url = "https://jcenter.bintray.com")
    }
}

rootProject.name = "my_medicos"
include(":app")
