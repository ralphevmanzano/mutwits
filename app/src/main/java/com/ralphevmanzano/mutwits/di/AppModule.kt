package com.ralphevmanzano.mutwits.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.firestore.FirebaseFirestore
import com.ralphevmanzano.mutwits.data.local.AppDatabase
import com.ralphevmanzano.mutwits.data.local.dao.UserDao
import com.ralphevmanzano.mutwits.data.remote.Oauth1SigningInterceptor
import com.ralphevmanzano.mutwits.data.remote.TwitterService
import com.ralphevmanzano.mutwits.util.Constants.BASE_URL
import com.ralphevmanzano.mutwits.util.Prefs
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class AppModule {

  @Provides
  @Singleton
  fun providesTwitterService(retrofit: Retrofit): TwitterService =
    retrofit.create(TwitterService::class.java)

  @Provides
  @Singleton
  fun providesRetrofit(
    moshiConverterFactory: MoshiConverterFactory,
    okHttpClient: OkHttpClient
  ): Retrofit {
    return Retrofit.Builder()
      .baseUrl(BASE_URL)
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
  fun providesFirestore(): FirebaseFirestore {
    return FirebaseFirestore.getInstance()
  }

  @Provides
  @Singleton
  fun providesOAuth1SigningInterceptor(): Oauth1SigningInterceptor {
    return Oauth1SigningInterceptor()
  }

  @Provides
  @Singleton
  fun providesDatabase(context: Context) : AppDatabase {
    return Room.databaseBuilder(context, AppDatabase::class.java, "mutwits-db").build()
  }

  @Provides
  @Singleton
  fun providesUserDao(db: AppDatabase): UserDao {
    return db.userDao()
  }
}