package com.ralphevmanzano.mutwits.di

import com.google.firebase.firestore.FirebaseFirestore
import com.ralphevmanzano.mutwits.data.remote.Oauth1SigningInterceptor
import com.ralphevmanzano.mutwits.data.remote.services.TwitterApiService
import com.ralphevmanzano.mutwits.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object NetworkModule {

    @Provides
    @Singleton
    fun providesTwitterApiService(retrofit: Retrofit): TwitterApiService = retrofit.create(
        TwitterApiService::class.java
    )

    @Provides
    @Singleton
    fun providesRetrofit(
        moshiConverterFactory: MoshiConverterFactory,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(moshiConverterFactory)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(
        oauth1SigningInterceptor: Oauth1SigningInterceptor
    ): OkHttpClient {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(logger)
            .addNetworkInterceptor(oauth1SigningInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun providesMoshiConverterFactory(): MoshiConverterFactory {
        return MoshiConverterFactory.create()
    }

    @Provides
    @Singleton
    fun providesOAuth1SigningInterceptor(): Oauth1SigningInterceptor {
        return Oauth1SigningInterceptor()
    }

}