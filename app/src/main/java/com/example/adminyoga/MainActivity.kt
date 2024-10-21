package com.example.adminyoga

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.adminyoga.component.BottomAppBar
import com.example.adminyoga.component.CenterAlignedTopAppBar
import com.example.adminyoga.database.AppDatabase
import com.example.adminyoga.database.ClassDao
import com.example.adminyoga.database.ClassService
import com.example.adminyoga.database.CourseDao
import com.example.adminyoga.database.CourseService
import com.example.adminyoga.screens.AddClass
import com.example.adminyoga.screens.AddCourse
import com.example.adminyoga.screens.ClassesInACourse
import com.example.adminyoga.screens.CourseList
import com.example.adminyoga.screens.EditClass
import com.example.adminyoga.screens.EditCourse
import com.example.adminyoga.screens.SearchClass
import com.example.adminyoga.ui.theme.AdminYogaTheme
import kotlinx.coroutines.flow.first
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AdminYogaTheme {
                App()
            }
        }
    }
}

val LocalNavigate = compositionLocalOf<NavHostController> { error("Navigation error") }
val LocalCourseDao = compositionLocalOf<CourseDao> { error("Course Dao error") }
val LocalClassDao = compositionLocalOf<ClassDao> { error("Course Dao error") }

private lateinit var db: AppDatabase

@Composable
fun App() {
    val navigate = rememberNavController();
    db = Room.databaseBuilder(
        klass = AppDatabase::class.java, name = "yoga-admin", context = LocalContext.current
    ).allowMainThreadQueries().fallbackToDestructiveMigration().build()

    val courseDao = db.courseDao();
    val classDao = db.classDao();

    val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.45:3000")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val courseService = retrofit.create(CourseService ::class.java);
    val classService = retrofit.create(ClassService ::class.java);

    suspend fun syncData(){
        try {
            //sync course data to cloud
            val allCourses = courseDao.getAll().first()
            val courseRes = courseService.syncCourses(allCourses)
            if (courseRes.isSuccessful){
                Log.d("Sync course data", "Synchronize course data successfully")
            }else{
                Log.e("Sync course data", "Fail to synchronize course data")
            }

            //sync class data to cloud
            val allClasses = classDao.getAll().first()
            val classRes = classService.syncClasses(allClasses)
            if (classRes.isSuccessful){
                Log.d("Sync class data", "Synchronize class data successfully")
            }else{
                Log.e("Sync class data", "Fail to synchronize class data $classRes")
            }
        }catch (e: Exception){
            Log.e("Upload to cloud database", "Exception: ${e.message}")
        }
    }

    LaunchedEffect(Unit) {
        syncData();
    }

    CompositionLocalProvider(
        LocalNavigate provides navigate,
        LocalCourseDao provides courseDao,
        LocalClassDao provides classDao
        ) {
        NavHost(navController = navigate, startDestination = "courseList") {
            composable(route = "addCourse") { AddCourse()}
            composable(route = "courseList") { CourseList()}
            composable(route = "editCourse/{courseId}") { backStackEntry ->
                val courseId = backStackEntry.arguments?.getString("courseId")?.toIntOrNull()
                courseId?.let {
                    EditCourse(courseId = it)
                }
            }
            composable(route = "course/{courseId}/classes") { backStackEntry ->
                val courseId = backStackEntry.arguments?.getString("courseId")?.toIntOrNull()
                courseId?.let {
                    ClassesInACourse(courseId = it)
                }
            }
            composable(route = "createClass/{courseId}") { backStackEntry ->
                val courseId = backStackEntry.arguments?.getString("courseId")?.toIntOrNull()
                courseId?.let {
                    AddClass(courseId = it)
                }
            }
            composable(route = "editClass/{courseId}/{classId}") { backStackEntry ->
                val classId = backStackEntry.arguments?.getString("classId")?.toIntOrNull()
                val courseId = backStackEntry.arguments?.getString("courseId")?.toIntOrNull()
                if (courseId != null && classId != null) {
                    EditClass(courseId = courseId, classId = classId)
                }
            }
            composable(route = "searchClass") { SearchClass() }
        }
    }
}


