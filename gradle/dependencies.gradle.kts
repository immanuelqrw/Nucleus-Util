val implementation by configurations
val testImplementation by configurations

val integrationTestImplementation by configurations.creating {
    extendsFrom(testImplementation)
}

val kotlinVersion: String by extra
val junitVersion: String by extra
val jacksonVersion: String by extra
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

val jUnitPackages: Array<Package> = arrayOf(
    Package("org.junit.jupiter", "junit-jupiter-api", junitVersion),
    Package("org.junit.jupiter", "junit-jupiter-params", junitVersion),
    Package("org.junit.jupiter", "junit-jupiter-engine", junitVersion),

    Package("io.mockk", "mockk", "1.8.13"),
    Package("org.amshove.kluent", "kluent", "1.49")
)

val nucleusPackages: Array<Package> = arrayOf()

val packages: Array<Package> = arrayOf(
    Package("org.jetbrains.kotlin", "kotlin-reflect", kotlinVersion),
    Package("org.jetbrains.dokka", "dokka-gradle-plugin", dokkaVersion),
    *nucleusPackages,
    *jacksonPackages,
    *jUnitPackages,
    Package("io.github.microutils", "kotlin-logging", "1.8.3")
)

val testPackages: Array<Package> = arrayOf(
    *jUnitPackages
)

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("test"))
    testImplementation(kotlin("test-junit5"))

    packages.forEach { `package` ->
        implementation(`package`.groupId, `package`.artifactId, `package`.version)
    }

    testPackages.forEach { testPackage ->
        testImplementation(testPackage.groupId, testPackage.artifactId, testPackage.version)
    }
}
