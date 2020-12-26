package eu.ncouret.wows.namecheckers.model

import com.fasterxml.jackson.annotation.JsonValue
import eu.ncouret.wows.namecheckers.model.EnumValue

enum class Language(@JsonValue val code: String) : EnumValue {
    EN("en"),
    FR("fr"),
    JA("ja"),
    DE("de"),
    CS("cs"),
    ES("es"),
    PL("pl"),
    RU("ru"),
    TH("th"),
    TW("zh-tw");

    override fun getValue(): String {
        return code
    }
}