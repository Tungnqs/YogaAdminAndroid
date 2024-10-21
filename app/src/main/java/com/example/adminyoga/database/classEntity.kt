package com.example.adminyoga.database

import androidx.compose.material3.SnackbarDuration
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "yogaClass",
    foreignKeys = [ForeignKey(
        entity = Course::class,
        parentColumns = ["id"],
        childColumns = ["courseId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class yogaClass(
    @PrimaryKey(autoGenerate = true) val classId: Int = 0,
    @ColumnInfo(name = "courseId") val courseId: Int,
    @ColumnInfo(name = "className") val className: String?,
    @ColumnInfo(name = "date") val date: String?,
    @ColumnInfo(name = "teacher") val teacher: String?,
    @ColumnInfo(name = "comments") val comments: String?
)