package dev.figas.domain.usecases

import dev.figas.domain.models.Person
import dev.figas.domain.repositories.PersonRepoContract

class UpdatePersonUseCase(private val repoContract: PersonRepoContract) {

    fun execute(person: Person,onPreExecute : ()->Unit,
                onPostExecute : (Person)->Unit) =
        repoContract.injectPerson(person, onPreExecute, onPostExecute)

}