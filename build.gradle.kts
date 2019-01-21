plugins {
    kotlin("jvm") version "1.3.11"
    application
    id("com.github.johnrengelman.shadow") version "4.0.4"
}

repositories {
    jcenter()
}

dependencies {
    compile(kotlin("stdlib-jdk8"))
    compile("io.ktor:ktor-server-netty:1.1.1")
    compile("io.ktor:ktor-gson:1.1.1")
    compile("ch.qos.logback:logback-classic:1.2.3")

    testCompile(kotlin("test"))
    testCompile(kotlin("test-junit"))
    testCompile("io.ktor:ktor-server-test-host:1.1.1")
    testCompile("io.mockk:mockk:1.9")
}

application {
    mainClassName = "boggle.ServerKt"
}

// Heroku requires a "stage" task
task("stage").dependsOn("build")