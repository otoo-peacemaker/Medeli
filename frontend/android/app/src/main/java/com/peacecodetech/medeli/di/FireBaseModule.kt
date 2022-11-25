package com.peacecodetech.medeli.di

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.peacecodetech.medeli.R
import com.peacecodetech.medeli.data.repository.FirebaseRealtimeDBImpl
import com.peacecodetech.medeli.data.repository.firebase.FirebaseAuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FireBaseModule {

    @Provides
    @Singleton
    fun provideFireBaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFireStore() = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFireStoreRef(query: Query) : CollectionReference {
    }

    @Provides
    @Singleton
    fun provideGso(@ApplicationContext context: Context) =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.web_client_id))
            .requestEmail()
            .build()

    @Provides
    @Singleton
    fun provideGoogleClient(@ApplicationContext context: Context, gso: GoogleSignInOptions) =
        GoogleSignIn.getClient(context, gso)

    @Provides
    fun provideFirebaseAuthRepository(
        auth: FirebaseAuth,
        /*signInClient: SignInClient,*/
        fireStore: FirebaseFirestore,
    ): FirebaseAuthRepository {
        return FirebaseAuthRepository(auth, fireStore)
    }

    @Provides
    fun provideFirebaseRTDB(
        ref: CollectionReference,
        query: Query,
    ): FirebaseRealtimeDBImpl {
        return FirebaseRealtimeDBImpl(ref, query)
    }
}