package com.example.adminyoga.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.adminyoga.LocalCourseDao
import com.example.adminyoga.component.BottomAppBar
import com.example.adminyoga.component.CenterAlignedTopAppBar
import com.example.adminyoga.database.Course
import androidx.compose.runtime.getValue
import com.example.adminyoga.LocalNavigate

@Composable
fun CourseList(modifier: Modifier = Modifier) {
    val courseDao = LocalCourseDao.current;
    val navigate = LocalNavigate.current;
    val allCourses by courseDao.getAll().collectAsState(initial = emptyList())

    fun handleDeleteCourse(course: Course) {
        try {
            val deleteId = courseDao.delete(course);
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
        topBar = { CenterAlignedTopAppBar(pageTitle = "Course list", backPage = "courseList") },
        bottomBar = { BottomAppBar() },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(allCourses) { course ->
                Card(
                    border = BorderStroke(1.dp, Color.Black),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 5.dp),
                ) {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .clickable {
                            navigate.navigate("course/${course.id}/classes")
                        }
                    )
                    {
                        Text(
                            text = course.courseName ?: "",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .padding(vertical = 4.dp, horizontal = 16.dp),
                        )
                        Text(
                            text = "Type of class: ${course.type}",
                            modifier = Modifier
                                .padding(vertical = 4.dp, horizontal = 16.dp),
                        )
                        Text(
                            text = "Description: ${course.description}",
                            modifier = Modifier
                                .padding(vertical = 4.dp, horizontal = 16.dp),
                        )
                        Text(
                            text = "Day of teaching: ${course.dayOfWeek}",
                            modifier = Modifier
                                .padding(vertical = 4.dp, horizontal = 16.dp),
                        )
                        Text(
                            text = "Starting Time: ${course.time}",
                            modifier = Modifier
                                .padding(vertical = 4.dp, horizontal = 16.dp),
                        )
                        Text(
                            text = "Duration: " + course.duration,
                            modifier = Modifier
                                .padding(vertical = 4.dp, horizontal = 16.dp),
                        )
                        Text(
                            text = "Capacity: ${course.capacity} (people)",
                            modifier = Modifier
                                .padding(vertical = 4.dp, horizontal = 16.dp),
                        )
                        Text(
                            text = "Price: ${course.price} (USD)",
                            modifier = Modifier
                                .padding(vertical = 4.dp, horizontal = 16.dp),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Row(
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .padding(horizontal = 8.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.End),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                navigate.navigate("editCourse/${course.id}")
                            },
                            modifier = Modifier
                                .background(Color.LightGray, shape = CircleShape)
                        ) {
                            Icon(
                                rememberVectorPainter(image = Icons.Filled.Edit),
                                contentDescription = "Edit course",
                                tint = Color.Black
                            )
                        }
                        IconButton(
                            onClick = { handleDeleteCourse(course) },
                            modifier = Modifier
                                .background(Color.LightGray, shape = CircleShape)
                        ) {
                            Icon(
                                rememberVectorPainter(image = Icons.Filled.Delete),
                                contentDescription = "Delete course",
                                tint = Color.Red
                            )
                        }
                    }
                }
            }
        }
    }
}