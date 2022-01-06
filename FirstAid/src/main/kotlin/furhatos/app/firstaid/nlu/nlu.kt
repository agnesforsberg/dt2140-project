package furhatos.app.firstaid.nlu

import furhatos.nlu.EnumEntity
import furhatos.nlu.Intent
import furhatos.util.Language

class Emergencies : EnumEntity(stemming = true, speechRecPhrases = true) {
    override fun getEnum(lang: Language): List<String> {
        return listOf("shock", "bleeding", "wound")
    }
}

class ExplainEmergency(var emergeny : Emergencies? = null) : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("@emergency", "I have @emergencies", "I am suffering from @emergencies",
                "My friend has @emergencies", "My friend is suffering from @emergencies")
    }
}

class DontKnow : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("I don't know", "Maybe", "Possibly", "I'm not sure", "What does it mean?")
    }
}

class Shock : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("shock", "my patient has shock", "they are shocked")
    }
}

class Wound : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("wound", "bleeding", "they are bleeding", "they have a wound", "my patient has a wound",
                "my patient is bleeding")
    }
}