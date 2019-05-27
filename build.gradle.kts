import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val projectGroup = "com.immanuelqrw.core"
val projectArtifact = "nucleus-util"
val projectVersion = "0.0.1-pre-alpha"

group = projectGroup
version = projectVersion

apply(from = "gradle/constants.gradle.kts")

plugins {
    java
    kotlin("jvm") version "1.3.31"
    id("org.jetbrains.kotlin.plugin.noarg") version "1.3.31"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.3.31"
    id("org.jetbrains.kotlin.plugin.spring") version "1.3.31"
    id("org.sonarqube") version "2.6"
    id("org.jetbrains.dokka") version "0.9.17"
    idea
    `maven-publish`
}

repositories {
    mavenCentral()
    jcenter()
    maven(url = "http://localhost:8081/repository/maven-public/")
}


apply(from = "gradle/dependencies.gradle.kts")

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    withType<Test> {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }

    withType<Wrapper> {
        gradleVersion = "5.0"
    }

    withType<DokkaTask> {
        outputFormat = "html"
        outputDirectory = "$buildDir/docs/dokka"
    }
}

apply(from = "gradle/database-init.gradle.kts")

apply(from = "gradle/integration-test.gradle.kts")

val sonarHostUrl: String by project
val sonarOrganization: String by project
val sonarLogin: String by project

sonarqube {
    properties {
        property("sonar.host.url", sonarHostUrl)
        property("sonar.organization", sonarOrganization)
        property("sonar.login", sonarLogin)

        property("sonar.projectKey", "immanuelqrw_Nucleus-Util")
        property("sonar.projectName", "Nucleus-Util")
        property("sonar.projectVersion", version)
    }
}

val sonar: Task = tasks["sonarqube"]

val integrationTest by tasks
val check by tasks.getting {
    dependsOn(integrationTest)
    dependsOn(sonar)
}

val sourcesJar by tasks.registering(Jar::class) {
    classifier = "sources"
    from(sourceSets["main"].allSource)
}

publishing {
    repositories {
        maven {
            url = uri("http://localhost:8081/repository/maven-releases/")
            credentials {
                username = "admin"
                password = "admin"
            }
        }
    }
    publications {
        register("mavenJava", MavenPublication::class) {
            groupId = projectGroup
            artifactId = projectArtifact
            version = projectVersion
            from(components["java"])
            artifact(sourcesJar.get())
        }
    }
}