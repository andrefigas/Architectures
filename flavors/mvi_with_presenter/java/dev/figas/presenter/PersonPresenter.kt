package dev.figas.presenter

import android.os.AsyncTask
import dev.figas.domain.models.Person
import dev.figas.domain.usecases.GetPersonUseCase
import dev.figas.domain.usecases.UpdatePersonUseCase
import dev.figas.intent.PersonIntent
import dev.figas.view.PersonView
import dev.figas.vieweffect.PersonEffect
import dev.figas.viewstate.PersonState

class PersonPresenter(private val view : PersonView,
                      private val getPersonUseCase: GetPersonUseCase,
                      private val updatePersonUseCase: UpdatePersonUseCase
) : PersonPresenterContract {

    private val requests = mutableListOf<AsyncTask<*, *, *>>()

     override fun processIntent(personIntent: PersonIntent){
        when(personIntent){
            is PersonIntent.OnSubmitClicked -> {
                injectPerson(personIntent.name)
            }

            is PersonIntent.OnLoad -> {
                fetchPerson()
            }

            is PersonIntent.OnRelease -> {
                release()
            }
        }
    }

    private fun injectPerson(name : String) {
        requests.add(
            updatePersonUseCase.execute(
                Person(name),
                onPreExecute = {
                    view.processPageState(PersonState.Loading)
                },
                onPostExecute = { person->
                    view.processEffect(PersonEffect.OnPersonSaved(person))
                }
            )

        )

    }

    private fun fetchPerson() {
        requests.add(
            getPersonUseCase.execute(
                onPreExecute = {
                    view.processPageState(PersonState.Loading)
                },
                onPostExecute = { person->
                    view.processPageState(PersonState.Data(person))
                })
        )
    }

    private fun release(){
        requests.forEach {
            it.cancel(true)
        }
    }

}

interface PersonPresenterContract{
    fun processIntent(personIntent: PersonIntent)
}
