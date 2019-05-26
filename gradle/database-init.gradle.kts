val databaseBuild by tasks.creating(Exec::class) {
    workingDir("./script")
    commandLine("python", "instantiate_database.py")
}

val testDatabaseBuild: Exec by tasks.creating(Exec::class) {
    workingDir("./script")
    commandLine("python", "construct_database.py")
}
