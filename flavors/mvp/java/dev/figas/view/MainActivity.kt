package dev.figas.view

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dev.figas.R
import dev.figas.model.PersonModel
import dev.figas.presenter.PersonPresenter
import dev.figas.presenter.PersonPresenterContract

class MainActivity : AppCompatActivity(), PersonView {

    private lateinit var presenter: PersonPresenterContract
    private lateinit var nameEt: EditText
    private lateinit var progressPb: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = PersonPresenter(this, PersonModel(this))

        setContentView(R.layout.activity_main)
        setupUI()

        presenter.fetchPerson()
    }

    private fun setupUI() {
        nameEt = findViewById(R.id.name_et)
        progressPb = findViewById(R.id.progress_pb)
        findViewById<View>(R.id.submit_bt).setOnClickListener {
            presenter.injectPerson(nameEt.text.toString())
        }
    }

    override fun showPersonName(name: String) {
        nameEt.setText(name)
    }

    override fun showSavedPerson(name: String) {
        Toast.makeText(
            this,
            getString(R.string.submit_success_message, name),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun showLoading() {
        progressPb.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressPb.visibility = View.INVISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.release()
    }
}

interface PersonView {
    fun showSavedPerson(name: String)
    fun hideLoading()
    fun showLoading()
    fun showPersonName(name: String)
}
