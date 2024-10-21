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
interface ClassDao {
    @Query("SELECT * FROM yogaClass")
    fun getAll(): Flow<List<yogaClass>>

    @Query("SELECT * FROM yogaClass WHERE classId LIKE (:classId)")
    fun findById(classId: Int): yogaClass

    @Query("SELECT * FROM yogaClass WHERE className LIKE :className LIMIT 1")
    fun findByName(className: String): yogaClass

    @Query("SELECT * FROM yogaClass WHERE courseId = :courseId")
    fun getClassesByCourseId(courseId: Int): Flow<List<yogaClass>>

    @Insert
    fun insert(yogaClass: yogaClass): Long;

    @Delete
    fun delete(yogaClass: yogaClass): Int;

    @Query(
        """
        UPDATE yogaClass 
        SET className = :newClassName ,date = :newDate, teacher = :newTeacher, comments = :newComments
        WHERE classId = :classId
    """
    )
    fun updateClassById(
        classId: Int,
        newClassName: String?,
        newDate: String?,
        newTeacher: String?,
        newComments: String?,
    ): Int
}

interface ClassService {
    @POST("syncClasses")
    suspend fun syncClasses(@Body classes: List<yogaClass>): Response<Void>
}