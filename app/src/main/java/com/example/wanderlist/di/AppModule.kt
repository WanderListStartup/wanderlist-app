package com.example.wanderlist.di

import android.content.Context
import com.example.wanderlist.R
import com.example.wanderlist.model.AuthDataStore
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideGoogleOAuthClientID(@ApplicationContext context: Context): String {
        return context.getString(R.string.googleOAuthClientID)
    }

}