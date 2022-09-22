package dev.figas.interactor

import android.os.AsyncTask
import dev.figas.model.Person
import dev.figas.model.PersonModelContract

class MainInteractor( private val model: PersonModelContract) : MainInteractorContract{

    private val requests = mutableListOf<AsyncTask<*, *, *>>()

    override fun injectPerson(name: String,
                              onPreExecute : ()->Unit,
                              onPostExecute : (Person)->Unit) {
        requests.add(model.injectPerson(Person(name),
                onPreExecute,
                onPostExecute
            ))
    }

    override fun fetchPerson(onPreExecute : ()->Unit,
                             onPostExecute : (Person)->Unit) {
        requests.add(
            model.providePerson(onPreExecute, onPostExecute)
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
