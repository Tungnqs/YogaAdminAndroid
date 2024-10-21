package com.example.adminyoga.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Course::class, yogaClass::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun courseDao(): CourseDao;
    abstract fun classDao(): ClassDao;
}