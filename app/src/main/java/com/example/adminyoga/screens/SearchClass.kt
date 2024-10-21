package com.example.adminyoga.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.adminyoga.LocalClassDao
import com.example.adminyoga.LocalCourseDao
import com.example.adminyoga.LocalNavigate
import com.example.adminyoga.component.BottomAppBar
import com.example.adminyoga.component.CenterAlignedTopAppBar
import com.example.adminyoga.database.yogaClass

@Composable
fun SearchClass(modifier: Modifier = Modifier) {
    val classDao = LocalClassDao.current;
    val courseDao = LocalCourseDao.current;
    val classes by classDao.getAll().collectAsState(initial = emptyList())
    val allCourses by courseDao.getAll().collectAsState(initial = emptyList())

    fun getCourseNameByCourseId(courseId: Int): String{
        var courseName = "";
        for (item in allCourses) {
            if (courseId == item.id){
                courseName = item.courseName ?: "";
            }
        }
        return courseName;
    }

    val teacherKeyWord = remember {
        mutableStateOf("");
    }

    val filteredClasses = classes.filter { classItem ->
        classItem.teacher?.contains(teacherKeyWord.value, ignoreCase = true) == true
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                pageTitle = "All classes in the system",
                backPage = "courseList"
            )
        },
        bottomBar = { BottomAppBar() },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 5.dp),
                    value = teacherKeyWord.value,
                    onValueChange = { newValue ->
                        teacherKeyWord.value = newValue
                    },
                    label = { Text("Search class by teacher") }
                )
            }
            items(filteredClasses) { classItem ->
                ElevatedCard(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 5.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )
                    {
                        Text(
                            text = "Class: ${classItem.className}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .padding(vertical = 4.dp, horizontal = 16.dp),
                        )
                        Text(
                            text = "Course: ${getCourseNameByCourseId(classItem.courseId)}",
                            modifier = Modifier
                                .padding(vertical = 4.dp, horizontal = 16.dp),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Blue
                        )
                        Text(
                            text = "Date: ${classItem.date}",
                            modifier = Modifier
                                .padding(vertical = 4.dp, horizontal = 16.dp),
                        )
                        Text(
                            text = "Teacher: ${classItem.teacher}",
                            modifier = Modifier
                                .padding(vertical = 4.dp, horizontal = 16.dp),
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Comment: ${classItem.comments}",
                            modifier = Modifier
                                .padding(vertical = 4.dp, horizontal = 16.dp),
                        )
                    }
                }
            }
        }
    }
}