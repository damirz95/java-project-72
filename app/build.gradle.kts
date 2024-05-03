plugins {
    application
    checkstyle
    id ("java")
    id("io.freefair.lombok") version "8.6"
    id ("com.adarshr.test-logger") version "3.0.0"
    id ("com.github.ben-manes.versions") version "0.39.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "hexlet.code"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

application {
    mainClass.set("hexlet.code.App")
}

repositories { mavenCentral() }

dependencies {
    implementation("gg.jte:jte:3.1.9")
    implementation("org.slf4j:slf4j-simple:2.0.9")
    implementation("io.javalin:javalin:6.1.3")
    implementation("io.javalin:javalin-bundle:6.1.3")
    implementation("io.javalin:javalin-rendering:6.1.3")

}