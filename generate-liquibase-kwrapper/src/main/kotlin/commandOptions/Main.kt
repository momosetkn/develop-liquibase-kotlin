package momosetkn.commandOptions

import java.io.File
import java.io.PrintWriter

fun main() {
    /*
    // https://docs.liquibase.com/change-types/home.html
console.log(Array.from(document.querySelectorAll("#mc-main-content a")).reduce((acc, cur) => acc + "\n"+(cur.href),""))
     */
    val codeString = code()

    writeFile(codeString)
}

private fun code(): String {
    val codeString =
        listOf(
            generateCommand("https://docs.liquibase.com/parameters/home.html"),
        ).joinToString("\n")
    return codeString
}

private fun writeFile(codeString: String) {
    val file = File("generate-liquibase-kwrapper/src/main/kotlin/commandOptions", "LiquibaseCommandOptions.kt")
    PrintWriter(file).use { out ->
        out.println(
            """
            package momosetkn.liquibase

            import momosetkn.liquibase.LiquibaseCommandInstance.executeLiquibaseCommandLine

            @Suppress("LargeClass", "TooManyFunctions")
            object LiquibaseCommandOptions {
            """.trimIndent()
        )
        out.println(codeString)
        out.println("}")
    }
}
