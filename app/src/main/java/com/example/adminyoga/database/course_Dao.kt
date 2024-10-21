package com.example.adminyoga.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

@Dao
interface CourseDao {
    @Query("SELECT * FROM yogaCourse")
    fun getAll(): Flow<List<Course>>

    @Query("SELECT * FROM yogaCourse WHERE id LIKE (:courseId)")
    fun findById(courseId: Int): Course

    @Query("SELECT * FROM yogaCourse WHERE id LIKE :courseName LIMIT 1")
    fun findByName(courseName: String): Course

    @Insert
    fun insert(course: Course): Long;

    @Delete
    fun delete(course: Course): Int;

    @Query(
        """
        UPDATE yogaCourse 
        SET courseName = :courseName, time = :time, dayOfWeek = :dayOfWeek, 
            capacity = :capacity, duration = :duration, price = :price, 
            type = :type, description = :description 
        WHERE id = :courseId
    """
    )
    fun updateCourseById(
        courseId: Int,
        courseName: String?,
        time: String?,
        dayOfWeek: String?,
        capacity: String?,
        duration: String?,
        price: String?,
        type: String?,
        description: String?
    ): Int
}

interface CourseService {
    @POST("syncCourses")
    suspend fun syncCourses(@Body courses: List<Course>): Response<Void>
}