package com.example.adminyoga.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "yogaCourse")
data class Course (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "courseName") val courseName: String?,
    @ColumnInfo(name = "time") val time: String?,
    @ColumnInfo(name = "dayOfWeek") val dayOfWeek: String?,
    @ColumnInfo(name = "capacity") val capacity: String?,
    @ColumnInfo(name = "duration") val duration: String?,
    @ColumnInfo(name = "price") val price: String?,
    @ColumnInfo(name = "type") val type: String?,
    @ColumnInfo(name = "description") val description: String?,
)