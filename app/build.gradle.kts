plugins {
    id ("java")
    id ("checkstyle")
    id ("com.adarshr.test-logger") version "3.0.0"
    id ("com.github.ben-manes.versions") version "0.39.0"
}

group = "hexlet.code"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories { mavenCentral() }

dependencies {

}