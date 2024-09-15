package momosetkn.preconditions

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
            generateCommand("https://docs.liquibase.com/concepts/changelogs/preconditions.html#"),
        ).joinToString("\n")
    return codeString
}

private fun writeFile(codeString: String) {
    val file = File("generate-liquibase-kwrapper/src/main/resources", "PreConditionDsl.kt")
    PrintWriter(file).use { out ->
        out.println(
            """
            package momosetkn.liquibase

            @Suppress("LargeClass", "TooManyFunctions")
            class PreConditionDsl {
            """.trimIndent()
        )
        out.println(codeString)
        out.println("}")
    }
}
