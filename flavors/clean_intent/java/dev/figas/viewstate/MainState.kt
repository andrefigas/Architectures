package dev.figas.viewstate

import dev.figas.domain.models.Person

sealed class PersonState {

    object Idle : PersonState()
    object Loading : PersonState()
    data class Data(val user: Person) : PersonState()

}