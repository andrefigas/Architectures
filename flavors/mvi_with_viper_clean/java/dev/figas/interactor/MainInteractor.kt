package dev.figas.interactor

import android.os.AsyncTask
import dev.figas.domain.models.Person
import dev.figas.domain.usecases.GetPersonUseCase
import dev.figas.domain.usecases.UpdatePersonUseCase

class MainInteractor( private val getPersonUseCase: GetPersonUseCase,
                        val updatePersonUseCase: UpdatePersonUseCase) : MainInteractorContract{

    private val requests = mutableListOf<AsyncTask<*, *, *>>()

    override fun injectPerson(name: String,
                              onPreExecute : ()->Unit,
                              onPostExecute : (Person)->Unit) {
        requests.add(updatePersonUseCase.execute(Person(name),
                onPreExecute,
                onPostExecute
            ))
    }

    override fun fetchPerson(onPreExecute : ()->Unit,
                             onPostExecute : (Person)->Unit) {
        requests.add(
            getPersonUseCase.execute(onPreExecute, onPostExecute)
        )
    }

    override fun release() {
        requests.forEach {
            it.cancel(true)
        }
    }

}

interface MainInteractorContract{
    fun injectPerson(name: String,
                     onPreExecute : ()->Unit,
                     onPostExecute : (Person)->Unit)
    fun fetchPerson(onPreExecute : ()->Unit,
                    onPostExecute : (Person)->Unit)
    fun release()
}
