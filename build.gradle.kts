plugins {
    kotlin("jvm") version "1.3.11"
    application
}

repositories {
    jcenter()
}

dependencies {
    compile(kotlin("stdlib-jdk8"))

    testCompile(kotlin("test"))

    testCompile(kotlin("test-junit"))
}

application {
    mainClassName = "boggle.AppKt"
}
