package momosetkn.commandOptions

import StringUtils.toCamelCase
import org.jsoup.Jsoup

fun generateCommand(uri: String): String {
    val doc =
        Jsoup
            .connect(
                uri,
            ).get()
    val table = doc.select("table")
    val items = table.map { t ->
        val tr = t.select("tr")
        tr.mapNotNull { tr2 ->
            val td = tr2.select("td")
            td.isEmpty() && return@mapNotNull null

            val args = td[0].text().substringBefore("\r\n")
            val prop = args.removePrefix("--").toCamelCase()
            val type = typeName(td[1].text())
            Item(
                liquibaseName = args,
                name = prop,
                type = type,
            )
        }
    }
   val s =  items.mapIndexed { index, items ->
        val props = items.map {
            "val ${it.name} : ${it.type}? = null,"
        }.joinToString("\n")
        val createMap = items.map {
            val body = "\"${it.liquibaseName}=\$it\""
            "${it.name}?.let { $body },"
        }.joinToString("\n")
        """
            data class LiquibaseInfoOptions${index}(
            // options
                $props
            ) {
                fun serialize(): List<String> {
                    return listOfNotNull(
                        $createMap                
                    )
                }
            }
        """.trimIndent()
    }
    return s.joinToString("\n\n")
}

fun typeName(raw: String): String {
    val s =  mapOf(
        "String" to "String",
        "Boolean" to "Boolean",
        "Integer" to "Int",
    )[raw]
    return s ?: "Unknown"
}

private data class Item(
    val liquibaseName: String,
    val name: String,
    val type: String,
)
