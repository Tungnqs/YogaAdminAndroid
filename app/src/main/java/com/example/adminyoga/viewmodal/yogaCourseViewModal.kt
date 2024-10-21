//package com.example.adminyoga.viewmodel
//
//import android.util.Log
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.viewModelScope
//import com.example.adminyoga.database.Course
//import com.example.adminyoga.database.CourseDao
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.launch
//
//class YogaClassAppViewModel(private val CourseDaoClass: CourseDao) : ViewModel() {
//    val getAllYogaCourse: Flow<List<Course>> = CourseDaoClass.getAll();
//
//    fun insertYogaClass(yogaCourse: YogaCourse) {
//        viewModelScope.launch {
//            try {
//                CourseDao.insert(YogaCourse)
//                Log.d("YogaClassAppViewModel", "Yoga class inserted successfully")
//            } catch (e: Exception) {
//                Log.e("YogaClassAppViewModel", "Error inserting yoga class", e)
//                throw e
//            }
//        }
//    }
//}
//
//class YogaClassAppViewModelFactory(private val yogaClassDao: YogaCourseDao) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(YogaClassAppViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return YogaClassAppViewModel(yogaClassDao) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}