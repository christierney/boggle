plugins {
    kotlin("jvm") version "1.3.11"
    application
}

repositories {
    jcenter()
}

dependencies {
    compile(kotlin("stdlib-jdk8"))
    compile("io.ktor:ktor-server-netty:1.1.1")

    testCompile(kotlin("test"))
    testCompile(kotlin("test-junit"))
    testCompile("io.ktor:ktor-server-test-host:1.1.1")
}

application {
    mainClassName = "boggle.ServerKt"
}
