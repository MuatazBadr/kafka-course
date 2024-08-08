plugins {
    id("java")
}

group = "conduktor.demos"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // https://mvnrepository.com/artifact/org.apache.kafka/kafka-clients
    implementation("org.apache.kafka:kafka-clients:3.7.0")

    // https://mvnrepository.com/artifact/org.slf4j/slf4j-api
    implementation("org.slf4j:slf4j-api:2.0.13")

    // https://mvnrepository.com/artifact/org.slf4j/slf4j-simple
    implementation("org.slf4j:slf4j-simple:2.0.13")

    //https://central.sonatype.com/artifact/org.opensearch.client/opensearch-rest-high-level-client/overview
    implementation("org.opensearch.client:opensearch-rest-high-level-client:2.15.0")

    //https://central.sonatype.com/artifact/org.kie.modules/com-google-code-gson
    implementation("org.kie.modules:com-google-code-gson:6.5.0.Final")

}

tasks.test {
    useJUnitPlatform()
}