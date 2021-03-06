package com.ralphevmanzano.mutwits.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ralphevmanzano.mutwits.data.local.dao.UserDao
import com.ralphevmanzano.mutwits.data.models.User

@Database(entities = [User::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
  abstract fun userDao(): UserDao
}