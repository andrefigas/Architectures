package dev.figas.presenter

import android.os.AsyncTask
import dev.figas.domain.models.Person
import dev.figas.domain.usecases.GetPersonUseCase
import dev.figas.domain.usecases.UpdatePersonUseCase
import dev.figas.view.PersonView

class PersonPresenter(private val view : PersonView,
                      private val getPersonUseCase: GetPersonUseCase,
                      private val updatePersonUseCase: UpdatePersonUseCase) :
    PersonPresenterContract {


    private val requests = mutableListOf<AsyncTask<*, *, *>>()

    override fun injectPerson(name : String) {
        requests.add(
            updatePersonUseCase.execute(
                Person(name),
                onPreExecute = {
                    view.showLoading()
                },
                onPostExecute = { person->
                    view.hideLoading()
                    view.showSavedPerson(person.name)
                }
            )

        )

    }

    override fun fetchPerson() {
        requests.add(
            getPersonUseCase.execute(
                onPreExecute = {
                    view.showLoading()
                },
                onPostExecute = { person->
                    view.hideLoading()
                    view.showPersonName(person.name)
                })
        )
    }

    override fun release(){
        requests.forEach {
            it.cancel(true)
        }
    }

}

interface PersonPresenterContract{
    fun injectPerson(name: String)
    fun fetchPerson()
    fun release()
}
