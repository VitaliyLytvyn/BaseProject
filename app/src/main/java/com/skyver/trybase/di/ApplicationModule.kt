package com.skyver.trybase.di

import android.content.Context
import com.skyver.trybase.App
import com.skyver.trybase.BuildConfig
import com.skyver.trybase.data.AuthenticatorFirebaseImpl
import com.skyver.trybase.data.NetworkRepository
import com.skyver.trybase.domain.Authenticator
import com.skyver.trybase.domain.ReposRepository

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ApplicationModule {

    @Provides @Singleton fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .client(createClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    private fun createClient(): OkHttpClient {
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            okHttpClientBuilder.addInterceptor(loggingInterceptor)
        }
        return okHttpClientBuilder.build()
    }

    @Provides @Singleton fun provideRepoesRepository(dataSource: NetworkRepository): ReposRepository = dataSource

    @Provides @Singleton fun provideAuthenticator(authenticator: AuthenticatorFirebaseImpl): Authenticator = authenticator
}
