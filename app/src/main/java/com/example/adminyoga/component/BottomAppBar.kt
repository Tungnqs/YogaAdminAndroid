package com.example.adminyoga.component

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.adminyoga.LocalNavigate

@Composable
fun BottomAppBar() {
    val navigate = LocalNavigate.current;
    BottomAppBar(
        actions = {
            IconButton(onClick = { navigate.navigate("courseList") }) {
                Icon(Icons.Filled.List, contentDescription = "Courses list",modifier = Modifier.size(200.dp))
            }
            IconButton(onClick = { navigate.navigate("searchClass") }) {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = "Search course",
                    modifier = Modifier.size(200.dp)
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigate.navigate("addCourse") },
                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
            ) {
                Icon(Icons.Filled.Add, "Add course")
            }
        }
    )

}