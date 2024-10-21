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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
fun ClassesInACourse(modifier: Modifier = Modifier, courseId: Int) {
    val classDao = LocalClassDao.current;
    val navigate = LocalNavigate.current;
    val courseDao = LocalCourseDao.current;
    val classes by classDao.getClassesByCourseId(courseId).collectAsState(initial = emptyList())
    val currentCourse = courseDao.findById(courseId);

    Log.d("All Classes", "classes: ${classes}")

    fun handleDeleteClass(yogaClass: yogaClass) {
        try {
            val deleteId = classDao.delete(yogaClass);
            if (deleteId > 0) {
                Log.d("Delete Successful", "Course deleted with ID: $deleteId")
            } else {
                Log.d("Delete Failed", "Course deleted failed")
            }
        } catch (e: Exception) {
            Log.d("Exception", "Error: ${e.message}")
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                pageTitle = "Classes in: ${currentCourse.courseName}",
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
                Button(
                    onClick = { navigate.navigate("createClass/${courseId}") },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 8.dp),
                ) {
                    Text("Add Class", fontSize = 20.sp)
                }
            }
            items(classes) { classItem ->
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
                            text = classItem.className ?: "",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .padding(vertical = 4.dp, horizontal = 16.dp),
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
                    Row(
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .padding(horizontal = 8.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(
                            10.dp,
                            Alignment.Start
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                navigate.navigate("editClass/${courseId}/${classItem.classId}")
                            },
                            modifier = Modifier
                                .background(Color.LightGray, shape = CircleShape)
                        ) {
                            Icon(
                                rememberVectorPainter(image = Icons.Filled.Edit),
                                contentDescription = "Edit class",
                                tint = Color.Black
                            )
                        }
                        IconButton(
                            onClick = { handleDeleteClass(classItem) },
                            modifier = Modifier
                                .background(Color.LightGray, shape = CircleShape)
                        ) {
                            Icon(
                                rememberVectorPainter(image = Icons.Filled.Delete),
                                contentDescription = "Delete class",
                                tint = Color.Red
                            )
                        }
                    }
                }
            }
        }
    }
}