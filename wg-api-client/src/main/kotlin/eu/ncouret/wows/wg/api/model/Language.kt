package eu.ncouret.wows.wg.api.model

import com.fasterxml.jackson.annotation.JsonValue

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