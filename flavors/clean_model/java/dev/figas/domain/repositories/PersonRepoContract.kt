package dev.figas.domain.repositories

import android.os.AsyncTask
import dev.figas.domain.models.Person

interface PersonRepoContract {

    fun providePerson(onPreExecute : ()->Unit, onPostExecute : (Person)->Unit) : AsyncTask<Void, Void, Person>

    fun injectPerson(person : Person, onPreExecute : ()->Unit, onPostExecute : (Person)->Unit) : AsyncTask<String, Void, Person>

}