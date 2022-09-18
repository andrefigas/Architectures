package dev.figas.vieweffect

import dev.figas.domain.models.Person

sealed class PersonEffect {

    data class OnPersonSaved(val person: Person) : PersonEffect()

}