val implementation by configurations
val testImplementation by configurations

val integrationTestImplementation by configurations.creating {
    extendsFrom(testImplementation)
}

val kotlinVersion: String by extra
val junitVersion: String by extra
val jacksonVersion: String by extra
val springDataVersion : String by extra
val springBootVersion: String by extra
val springVersion: String by extra
val dokkaVersion: String by extra
val nucleusVersion: String by extra

data class Package(
    val groupId: String,
    val artifactId: String,
    val version: String
)

val jacksonPackages: Array<Package> = arrayOf(
    Package("com.fasterxml.jackson.module", "jackson-module-kotlin", jacksonVersion),
    Package("com.fasterxml.jackson.dataformat", "jackson-dataformat-yaml", jacksonVersion)
)

val springPackages: Array<Package> = arrayOf(
    Package("org.springframework.data", "spring-data-jpa", springDataVersion),
    Package("org.springframework", "spring-orm", springVersion),
    Package("org.springframework", "spring-web", springVersion),
    Package("org.springframework", "spring-webmvc", springVersion)
)

val springBootPackages: Array<Package> = arrayOf(
    Package("org.springframework.boot", "spring-boot-autoconfigure", springBootVersion),
    Package("org.springframework.boot", "spring-boot", springBootVersion),
    Package("org.springframework.boot", "spring-boot-starter-tomcat", springBootVersion)
)

val databasePackages: Array<Package> = arrayOf(
    Package("org.postgresql", "postgresql", "42.2.5"),
    Package("org.hibernate", "hibernate-core", "5.3.7.Final"),
    Package("org.hibernate.validator", "hibernate-validator", "6.0.13.Final")
)

val jUnitPackages: Array<Package> = arrayOf(
    Package("org.junit.jupiter", "junit-jupiter-api", junitVersion),
    Package("org.junit.jupiter", "junit-jupiter-params", junitVersion),
    Package("org.junit.jupiter", "junit-jupiter-engine", junitVersion),

    Package("io.mockk", "mockk", "1.8.13"),
    Package("org.amshove.kluent", "kluent", "1.49")
)

val springTestPackages: Array<Package> = arrayOf(
    Package("org.springframework", "spring-test", springVersion),
    Package("org.springframework.boot", "spring-boot-test", springBootVersion),
    Package("org.springframework.boot", "spring-boot-test-autoconfigure", springBootVersion)
)

val nucleusPackages: Array<Package> = arrayOf(
    Package("com.github.immanuelqrw", "nucleus-api", nucleusVersion)
)

val packages: Array<Package> = arrayOf(
    Package("org.jetbrains.kotlin", "kotlin-reflect", kotlinVersion),
    Package("org.jetbrains.dokka", "dokka-gradle-plugin", dokkaVersion),
    *nucleusPackages,
    *jacksonPackages,
    *springPackages,
    *springBootPackages,
    *springTestPackages,
    *databasePackages,
    *jUnitPackages
)

val testPackages: Array<Package> = arrayOf(
    Package("org.hibernate", "hibernate-testing", "5.3.7.Final"),
    *jUnitPackages,
    *springTestPackages
)

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    packages.forEach { `package` ->
        implementation(`package`.groupId, `package`.artifactId, `package`.version)
    }

    testPackages.forEach { testPackage ->
        testImplementation(testPackage.groupId, testPackage.artifactId, testPackage.version)
    }
}
