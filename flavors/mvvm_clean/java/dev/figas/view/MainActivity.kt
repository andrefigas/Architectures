package dev.figas.view

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import dev.figas.R
import dev.figas.data.mappers.PersonMapper
import dev.figas.data.repositories.PersonRepository
import dev.figas.domain.repositories.PersonRepoContract
import dev.figas.domain.usecases.GetPersonUseCase
import dev.figas.domain.usecases.UpdatePersonUseCase
import dev.figas.viewmodel.PersonViewModel
import dev.figas.viewmodel.PersonViewModelFactory

class MainActivity : AppCompatActivity(), PersonView {

    private lateinit var viewModel: PersonViewModel
    private lateinit var nameEt: EditText
    private lateinit var progressPb: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repo : PersonRepoContract = PersonRepository(this, PersonMapper())
        viewModel = ViewModelProviders.of(
            this, PersonViewModelFactory(
                GetPersonUseCase(repo),
                UpdatePersonUseCase(repo)
            )
        ).get(PersonViewModel::class.java)

        setContentView(R.layout.activity_main)
        setupUI()

        viewModel.data.observe(this) { person ->
            hideLoading()
            showPersonName(person.name)
        }
        viewModel.insert.observe(this) { person ->
            hideLoading()
            showSavedPerson(person.name)
        }

        showLoading()
        viewModel.fetchPerson()
    }

    private fun setupUI() {
        nameEt = findViewById(R.id.name_et)
        progressPb = findViewById(R.id.progress_pb)
        findViewById<View>(R.id.submit_bt).setOnClickListener {
            showLoading()
            viewModel.injectPerson(nameEt.text.toString())
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

}

interface PersonView {
    fun showSavedPerson(name: String)
    fun hideLoading()
    fun showLoading()
    fun showPersonName(name: String)
}
