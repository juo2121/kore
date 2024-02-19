package json.juo

fun String.trimAll() = this.trimIndent().replace("\n","").replace(" ","")