plugins {
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.serialization") version "1.9.22"
    application
    jacoco
}

group = "com.example"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:2.3.7")
    implementation("io.ktor:ktor-server-netty:2.3.7")
    implementation("io.ktor:ktor-server-content-negotiation:2.3.7")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.7")
    implementation("ch.qos.logback:logback-classic:1.4.14")
    testImplementation("io.ktor:ktor-server-test-host:2.3.7")
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.exposed:exposed-core:0.50.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.50.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.50.1")
    implementation("org.postgresql:postgresql:42.7.8")
    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation("org.jetbrains.exposed:exposed-java-time:0.50.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    testImplementation("io.mockk:mockk:1.13.9")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("io.ktor:ktor-server-auth:2.3.7")
    implementation("io.ktor:ktor-server-auth-jwt:2.3.7")
    implementation("com.auth0:java-jwt:4.4.0")
    implementation("org.mindrot:jbcrypt:0.4")
    implementation("com.github.ben-manes.caffeine:caffeine:3.1.8")
    implementation("io.ktor:ktor-server-status-pages:2.3.7")
    implementation("io.ktor:ktor-server-call-logging:2.3.7")
    implementation("io.ktor:ktor-server-metrics-micrometer:2.3.7")
    implementation("io.micrometer:micrometer-registry-prometheus:1.12.2")
}
tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("com.example.ApplicationKt")
}
kotlin {
    jvmToolchain(21)
}
tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}
tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.example.ApplicationKt"
    }
    from(configurations.runtimeClasspath.get().map {
        if (it.isDirectory) it else zipTree(it)
    })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
