package dev.figas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.figas.model.PersonModelContract

class PersonViewModelFactory(val model : PersonModelContract) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(PersonModelContract::class.java)
            .newInstance(model)
    }

}