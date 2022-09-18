package dev.figas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.figas.domain.usecases.GetPersonUseCase
import dev.figas.domain.usecases.UpdatePersonUseCase


class PersonViewModelFactory(val getPersonUseCase: GetPersonUseCase,
        val updatePersonUseCase: UpdatePersonUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(GetPersonUseCase::class.java,
            UpdatePersonUseCase::class.java)
            .newInstance(getPersonUseCase, updatePersonUseCase)
    }

}