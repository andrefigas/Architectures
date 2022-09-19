package dev.figas.model

import android.content.Context
import android.content.SharedPreferences
import android.os.AsyncTask

class PersonModel(context : Context) : PersonModelContract{

    companion object{
        const val PREFS = "prefs"
        const val PREF_KEY_NAME = "name"
        const val DELAY = 3000L
    }

    private val preferences : SharedPreferences

    init{
        preferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
    }

    override fun providePerson(onPreExecute : ()->Unit, onPostExecute : (Person)->Unit) : AsyncTask<Void, Void, Person> {
        return BaseAsyncTask<Void, Person>(onPreExecute,
            doInBackground = {
            Thread.sleep(DELAY)
            Person(preferences.getString(PREF_KEY_NAME,"") as String)
        }, onPostExecute).execute()

    }

    override fun injectPerson(person : Person,
                              onPreExecute : ()->Unit,
                              onPostExecute : (Person)->Unit): AsyncTask<String, Void, Person> {
        return BaseAsyncTask<String , Person>(
            onPreExecute,
            doInBackground = { name ->
            Thread.sleep(DELAY)
            val person = Person(name as String)
            preferences.edit().putString(PREF_KEY_NAME, person.name).commit()
            person
        }, onPostExecute).execute(person.name)
    }

}

class BaseAsyncTask<Params,T>(private val onPreExecute : ()->Unit?,
                                       private val doInBackground : (Params?)-> T,
                                       private val onPostExecute : (T)->Unit) :
    AsyncTask<Params, Void, T>() {
    override fun onPreExecute() {
        super.onPreExecute()
        onPreExecute.invoke()
    }

    override fun doInBackground(vararg params: Params): T =
        doInBackground.invoke(if (params.isEmpty()) null else params[0])

    override fun onPostExecute(result: T) {
        super.onPostExecute(result)
        onPostExecute.invoke(result)
    }

}

interface PersonModelContract{

    fun providePerson(onPreExecute : ()->Unit, onPostExecute : (Person)->Unit) : AsyncTask<Void, Void, Person>

    fun injectPerson(person : Person, onPreExecute : ()->Unit, onPostExecute : (Person)->Unit) : AsyncTask<String, Void, Person>

}