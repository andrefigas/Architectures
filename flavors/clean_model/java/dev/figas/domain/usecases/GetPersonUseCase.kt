package dev.figas.domain.usecases

import dev.figas.domain.models.Person
import dev.figas.domain.repositories.PersonRepoContract

class GetPersonUseCase(private val repoContract: PersonRepoContract) {

    fun execute(onPreExecute : ()->Unit,
                onPostExecute : (Person)->Unit) =
        repoContract.providePerson(onPreExecute, onPostExecute)

}