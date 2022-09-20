package dev.figas.presenter

import android.os.AsyncTask
import dev.figas.model.Person
import dev.figas.model.PersonModelContract
import dev.figas.view.PersonView

class PersonPresenter(private val view : PersonView, private val model: PersonModelContract) : PersonPresenterContract {


    private val requests = mutableListOf<AsyncTask<*, *, *>>()

    override fun injectPerson(name : String) {
        requests.add(
            model.injectPerson(Person(name),
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
            model.providePerson(
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
