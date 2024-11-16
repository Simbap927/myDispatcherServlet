plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("javax.servlet:javax.servlet-api:4.0.1") // Servlet API 추가
    implementation("org.slf4j:slf4j-api:2.0.9")           // 로깅을 위한 SLF4J API
    implementation("org.slf4j:slf4j-simple:2.0.9")        // 간단한 SLF4J 구현체
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}