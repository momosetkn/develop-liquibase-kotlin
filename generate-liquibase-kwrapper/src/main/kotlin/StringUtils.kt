import momosetkn.command.delimiter

object StringUtils {
    fun String.toCamelCase(): String {
        return this.split(delimiter)
            .joinToString("") {
                it.replaceFirstChar(Char::uppercaseChar)
            }
            .replaceFirstChar { it.lowercase() }
    }
}