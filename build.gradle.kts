plugins {
    kotlin("jvm") version "1.9.22"
    `java-library`
}

group = "com.github.maksonic"
version = "2.0.0-kotlin"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
}

kotlin {
    jvmToolchain(11)
}

tasks.test {
    useJUnitPlatform()
}
