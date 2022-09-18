package dev.figas.data.mappers

import dev.figas.data.models.PersonDataModel
import dev.figas.domain.models.Person

class PersonMapper {

    fun toPerson(personDataModel: PersonDataModel) = Person(personDataModel.name)

}