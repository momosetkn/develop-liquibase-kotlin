package momosetkn.preconditions

import org.jsoup.Jsoup

fun generateCommand(uri: String): String {
    val doc = Jsoup.connect(uri).get()
    val main = doc.select("div#mc-main-content")
    val allInMain = main[0].select("*")

    var methodName = ""
    val itemsOuter= allInMain.mapNotNull {
        when(it.tag().name) {
            "h3" -> {
                methodName = it.text()
                null
            }
            "table" -> {
                val items = it.select("tr").mapNotNull { tr ->
                    val td =                 tr.select("td")
                    if( td.isEmpty()) {
                        null
                    } else {
                        val attr = td[0].text()
                        val required = td.getOrNull(2)?.text()
                        val isRequired = required?.startsWith("Required") ?: false
                        attr to isRequired
                    }
                }
                methodName to items
            }
            else -> null
        }
    }
   val s =  itemsOuter.map { (methodName, items) ->
        val args = items.map { (attr, required) ->
            if( required ) {
                "$attr : String"
            }else {
                "$attr : String? = null"
            }
        }

        val setArgs = items.map { (attr, required) ->
            if( required ) {
                "precondition.$attr = $attr.evalExpressions(changeLog)"
            }else {
                "$attr?.also { precondition.$attr = it.evalExpressions(changeLog) }"
            }
        }

        """
                fun $methodName(
                    ${args.joinToString(",\n")},
                ) {
                    val precondition = ${methodName.replaceFirstChar { it.uppercaseChar() }}Precondition()
                    ${setArgs.joinToString("\n")}
                    _preConditions.add(precondition)
                }
        """.trimIndent()
    }
    return s.joinToString("\n\n")
}
