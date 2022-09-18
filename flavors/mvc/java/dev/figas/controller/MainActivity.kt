package dev.figas.controller

import android.annotation.SuppressLint
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import dev.figas.R
import dev.figas.model.Person
import dev.figas.model.PersonModel
import dev.figas.model.PersonModelContract

class MainActivity : AppCompatActivity() {

    private lateinit var model: PersonModelContract
    private lateinit var nameEt: EditText
    private lateinit var progressPb: ProgressBar

    private val requests = mutableListOf<AsyncTask<*, *, *>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        model = PersonModel(this)

        setContentView(R.layout.activity_main)
        setupUI()
        fetchPerson()
    }

    private fun setupUI() {
        nameEt = findViewById(R.id.name_et)
        progressPb = findViewById(R.id.progress_pb)
        findViewById<View>(R.id.submit_bt).setOnClickListener {
            injectPerson()
        }
    }

    @SuppressLint("StaticFieldLeak")
    private fun injectPerson() {
        requests.add(
            model.injectPerson(
                Person(nameEt.text.toString()),
                onPreExecute = {
                    showLoading()
                },
                onPostExecute = { person ->
                    hideLoading()
                    onSavePerson(person)
                }
            )

        )

    }

    @SuppressLint("StaticFieldLeak")
    private fun fetchPerson() {
        requests.add(
            model.providePerson(
                onPreExecute = {
                    showLoading()
                },
                onPostExecute = { person ->
                    hideLoading()
                    onReceivePerson(person)
                }
            )
        )
    }

    private fun onReceivePerson(person: Person) {
        nameEt.setText(person.name)
    }

    private fun onSavePerson(person: Person) {
        Toast.makeText(
            this,
            getString(R.string.submit_success_message, person.name),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showLoading() {
        progressPb.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        progressPb.visibility = View.INVISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        requests.forEach {
            it.cancel(true)
        }
    }
}

/*open class InjectPerson(private val model: PersonModelContract) : AsyncTask<String, Void, Person>() {
    override fun doInBackground(vararg name: String) = model.injectPerson(Person(name[0]))
}

open class ProvidePerson(private val model: PersonModelContract) : AsyncTask<Void, Void, Person>() {
    override fun doInBackground(vararg noArg: Void?) = model.providePerson()
}*/