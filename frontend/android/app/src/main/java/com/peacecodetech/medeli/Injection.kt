package com.peacecodetech.medeli

import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import com.peacecodetech.medeli.data.repository.FirebaseAuthRepository
import com.peacecodetech.medeli.viewmodel.ViewModelFactory

/**
 * Class that handles object creation.
 * Like this, objects can be passed as parameters in the constructors and then replaced for
 * testing, where needed.
 */
object Injection {

    /**
     * Creates an instance of [ArticleRepository]
     */


    private fun provideUSerRepository(): FirebaseAuthRepository = FirebaseAuthRepository()

    /**
     * Provides the [ViewModelProvider.Factory] that is then used to get a reference to
     * [ViewModel] objects.
     */
    fun provideViewModelFactory(owner: SavedStateRegistryOwner): ViewModelProvider.Factory {
        return ViewModelFactory(owner, provideUSerRepository())
    }
}