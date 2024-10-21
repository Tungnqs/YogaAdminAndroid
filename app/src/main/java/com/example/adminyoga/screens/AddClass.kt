package com.example.adminyoga.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.adminyoga.LocalClassDao
import com.example.adminyoga.LocalCourseDao
import com.example.adminyoga.LocalNavigate
import com.example.adminyoga.component.BottomAppBar
import com.example.adminyoga.component.CenterAlignedTopAppBar
import com.example.adminyoga.database.ClassDao
import com.example.adminyoga.database.yogaClass
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddClass(modifier: Modifier = Modifier, courseId: Int) {
    val courseDao = LocalCourseDao.current;
    val classDao = LocalClassDao.current;
    val navigate = LocalNavigate.current;

    val currentCourse = courseDao.findById(courseId);

    val classNameState = remember {
        mutableStateOf("")
    }

    val teacherState = remember {
        mutableStateOf("")
    }

    val commentState = remember {
        mutableStateOf("")
    }

    val datePickerState = rememberDatePickerState(initialDisplayMode = DisplayMode.Input)

    val isDatePickerVisible = remember { mutableStateOf(false) }
    val selectedDateState = remember { mutableStateOf<String?>(null) }

    fun onDismiss() {
        isDatePickerVisible.value = false
    }

    val weekDayOfChosenDate = remember {
        mutableStateOf("")
    }

    fun onDateSelected(selectedDateMillis: Long?) {
        selectedDateMillis?.let {
            val selectedDate = DateFormat.getDateInstance().format(Date(it))
            selectedDateState.value = selectedDate

            // Get the day of the week
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = it
            val dayOfWeek = SimpleDateFormat(
                "EEEE",
                Locale.getDefault()
            ).format(calendar.time) // Get the full name of the day
            weekDayOfChosenDate.value = dayOfWeek;
        }
    }

    fun handleAddNewClass() {
        try {
            if (classNameState.value == ""
                || teacherState.value == ""
                || commentState.value == ""
                || selectedDateState.value == ""
                || weekDayOfChosenDate.value == ""
                || weekDayOfChosenDate.value != currentCourse.dayOfWeek
            ) {
                return; }
            val newClass = yogaClass(
                courseId = currentCourse.id,
                className = classNameState.value,
                date = selectedDateState.value,
                comments = commentState.value,
                teacher = teacherState.value,
            )
            val creationId = classDao.insert(newClass);
            if (creationId > 0) {
                Log.d("Created Success", "Class created with ID: $creationId")
                navigate.navigate("course/${courseId}/classes");
            } else {
                Log.d("Created Failed", "Class created failed")
            }
        } catch (e: Exception) {
            Log.d("Exception error", "Exception error: ${e.message}")
        }
    }

    //screen
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { CenterAlignedTopAppBar(pageTitle = "Add class in: ${currentCourse.courseName}", backPage = "course/${courseId}/classes") },
        bottomBar = { BottomAppBar() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { handleAddNewClass() },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Course")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = classNameState.value,
                        onValueChange = { newValue ->
                            classNameState.value = newValue
                        },
                        label = { Text("Class name") },
                    )
                }
            }
            item {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = selectedDateState.value ?: "",
                        onValueChange = { },
                        label = { Text("Date") },
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = {
                                isDatePickerVisible.value = !isDatePickerVisible.value
                            }) {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "Select date"
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .clickable { isDatePickerVisible.value = !isDatePickerVisible.value }
                    )
                    if (weekDayOfChosenDate.value != "" && weekDayOfChosenDate.value != currentCourse.dayOfWeek) {
                        Text(
                            text = "Weekday of selected date does match weekday of the course. Required: ${currentCourse.dayOfWeek}",
                            color = Color.Red,
                            fontWeight = FontWeight.SemiBold
                        )
                    } else if(weekDayOfChosenDate.value != "" && weekDayOfChosenDate.value == currentCourse.dayOfWeek){
                        Text(
                            text = "Valid day of week: ${currentCourse.dayOfWeek}",
                            color = Color.Green,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
            item {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = teacherState.value,
                        onValueChange = { newValue ->
                            teacherState.value = newValue
                        },
                        label = { Text("Teacher") },
                    )
                }
            }
            item {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = commentState.value,
                        maxLines = 4,
                        onValueChange = { newValue ->
                            commentState.value = newValue
                        },
                        label = { Text("Comments") },
                    )
                }
            }
        }
        if (isDatePickerVisible.value) {
            DatePickerDialog(
                onDismissRequest = { onDismiss() },
                confirmButton = {
                    TextButton(onClick = {
                        onDateSelected(datePickerState.selectedDateMillis)
                        onDismiss()
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { onDismiss() }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}

