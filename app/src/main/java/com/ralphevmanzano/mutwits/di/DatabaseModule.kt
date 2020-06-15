package com.ralphevmanzano.mutwits.di

import android.content.Context
import androidx.room.Room
import com.ralphevmanzano.mutwits.data.local.AppDatabase
import com.ralphevmanzano.mutwits.data.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context) : AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "mutwits-db").build()
    }

    @Provides
    @Singleton
    fun providesUserDao(db: AppDatabase): UserDao {
        return db.userDao()
    }

}