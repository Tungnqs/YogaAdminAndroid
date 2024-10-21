package com.example.adminyoga.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.adminyoga.LocalCourseDao
import com.example.adminyoga.LocalNavigate
import com.example.adminyoga.component.BottomAppBar
import com.example.adminyoga.component.CenterAlignedTopAppBar
import com.example.adminyoga.database.Course
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCourse(modifier: Modifier = Modifier, courseId: Int) {
    val courseDao = LocalCourseDao.current
    val currentCourse = remember { courseDao.findById(courseId) }

    val navigate = LocalNavigate.current;
    //course name state
    val courseName = remember {
        mutableStateOf(currentCourse.courseName ?: "")
    }

    //drop down state
    val isOpenWeekDayDropDown = remember {
        mutableStateOf(false)
    }

    val dayOfWeek =
        listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

    val initialDatePosition = dayOfWeek.indexOf(currentCourse.dayOfWeek)

    val weekDayPosition = remember {
        mutableStateOf(initialDatePosition)
    }

    //time picker state
    val timeParts = currentCourse.time?.split(":") ?: listOf("0", "0")
    val initialHour = timeParts[0].toInt() // Extract the hour
    val initialMinute = timeParts[1].toInt() // Extract the minute

    val courseTimeState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = true,
    )

    Log.d("courseTimeState", "courseTimeState: hour = ${courseTimeState.hour}, minute = ${courseTimeState.minute}")

    //Capacity state
    val capacity = remember {
        mutableStateOf(currentCourse.capacity ?: "")
    }

    //Duration state
    val duration = remember {
        mutableStateOf(currentCourse.duration ?: "")
    }

    //Price state
    val price = remember {
        mutableStateOf(currentCourse.price ?: "")
    }

    //drop down state
    val isOpenTypeDropdown = remember {
        mutableStateOf(false)
    }

    val classType =
        listOf("Flow Yoga", "Aerial Yoga", "Family Yoga")

    val initialTypePosition = classType.indexOf(currentCourse.type)

    val typePosition = remember {
        mutableStateOf(initialTypePosition)
    }

    //description state
    val description = remember {
        mutableStateOf(currentCourse.description ?: "")
    }

    fun handleEditCourse() {
        try {
            if (courseName.value != "" && capacity.value != "" && duration.value != "" && description.value != "") {
                val formattedHour = String.format("%02d", courseTimeState.hour)
                val formattedMinute = String.format("%02d", courseTimeState.minute)
                val formattedTime = "$formattedHour:$formattedMinute"

                // Insert the course and get the updated row ID
                val updateId = courseDao.updateCourseById(
                    courseId = currentCourse.id,
                    courseName = courseName.value,
                    dayOfWeek = dayOfWeek[weekDayPosition.value],
                    time = formattedTime,
                    type = classType[typePosition.value],
                    price = price.value,
                    description =  description.value,
                    capacity = capacity.value,
                    duration = duration.value,
                )

                // Check if the insertion was successful
                if (updateId > 0) {
                    Log.d("Update Success", "Course inserted with ID: $updateId")
                    navigate.navigate("courseList");
                } else {
                    Log.d("Update Failed", "Course insertion failed")
                }
            }else{
                return
            }
        } catch (e: Exception) {
            Log.d("Exception error", "Exception error: ${e.message}")
        }
    }

    //screen
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { CenterAlignedTopAppBar(pageTitle = "Editing course", backPage = "courseList") },
        bottomBar = { BottomAppBar() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { handleEditCourse() },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Text(text = "Save")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(10.dp) // Add spacing between items
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
                        value = courseName.value,
                        onValueChange = { newValue ->
                            courseName.value = newValue
                        },
                        label = { Text("Course name") },
                    )
                }
            }
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Day of the week:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                    )
                    Box(modifier = Modifier.background(Color.LightGray)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    isOpenWeekDayDropDown.value = true
                                }
                                .padding(horizontal = 16.dp)
                                .padding(vertical = 16.dp)
                        ) {
                            Text(
                                text = dayOfWeek[weekDayPosition.value],
                                modifier = Modifier.weight(1f)
                            )
                            Icon(Icons.Filled.ArrowDropDown, "Localized description")
                        }
                        DropdownMenu(
                            expanded = isOpenWeekDayDropDown.value,
                            onDismissRequest = { isOpenWeekDayDropDown.value = false },
                            modifier = Modifier
                                .fillMaxWidth(),
                        ) {
                            dayOfWeek.forEachIndexed { index, day ->
                                DropdownMenuItem(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    text = { Text(text = day) },
                                    onClick = {
                                        isOpenWeekDayDropDown.value = false
                                        weekDayPosition.value = index
                                    }
                                )
                            }
                        }
                    }
                }
            }
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Time of course:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TimeInput(
                            state = courseTimeState,
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
                        value = capacity.value,
                        onValueChange = { newValue ->
                            // Only allow digits (0-9)
                            if (newValue.all { it.isDigit() }) {
                                capacity.value = newValue
                            }
                        },
                        label = { Text("Capacity") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
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
                        value = duration.value,
                        onValueChange = { newValue ->
                            // Only allow digits (0-9)
                            if (newValue.all { it.isDigit() }) {
                                val intValue = newValue.toIntOrNull()
                                if (intValue == null || intValue in 0..120) {
                                    duration.value = newValue
                                }
                            }
                        },
                        label = { Text("Duration (max: 120 minutes)") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
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
                        value = price.value,
                        onValueChange = { newValue ->
                            // Only allow digits (0-9)
                            if (newValue.all { it.isDigit() }) {
                                price.value = newValue
                            }
                        },
                        label = { Text("Price ($):") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    )
                }
            }
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Class type:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                    )
                    Box(modifier = Modifier.background(Color.LightGray)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    isOpenTypeDropdown.value = true
                                }
                                .padding(horizontal = 16.dp)
                                .padding(vertical = 16.dp)
                        ) {
                            Text(
                                text = classType[typePosition.value],
                                modifier = Modifier.weight(1f)
                            )
                            Icon(Icons.Filled.ArrowDropDown, "Localized description")
                        }
                        DropdownMenu(
                            expanded = isOpenTypeDropdown.value,
                            onDismissRequest = { isOpenTypeDropdown.value = false },
                            modifier = Modifier
                                .fillMaxWidth(),
                        ) {
                            classType.forEachIndexed { index, day ->
                                DropdownMenuItem(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    text = { Text(text = day) },
                                    onClick = {
                                        isOpenTypeDropdown.value = false
                                        typePosition.value = index
                                    }
                                )
                            }
                        }
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
                        value = description.value,
                        maxLines = 4,
                        onValueChange = { newValue ->
                            description.value = newValue
                        },
                        label = { Text("Description") },
                    )
                }
            }
        }
    }
}
