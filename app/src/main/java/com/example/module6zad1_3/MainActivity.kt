package com.example.module6zad1_3

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.module6zad1_3.feature_task1.presentation.Task1Activity
import com.example.module6zad1_3.feature_task2.presentation.Task2Activity
import com.example.module6zad1_3.feature_task3.presentation.Task3Activity
import com.example.module6zad1_3.ui.theme.Module6Zad13Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Module6Zad13Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TaskSelectionScreen(
                        modifier = Modifier.padding(innerPadding),
                        onTask1Click = { startTask1() },
                        onTask2Click = { startTask2() },
                        onTask3Click = { startTask3() }
                    )
                }
            }
        }
    }

    private fun startTask1() {
        startActivity(Intent(this, Task1Activity::class.java))
    }

    private fun startTask2() {
        startActivity(Intent(this, Task2Activity::class.java))
    }

    private fun startTask3() {
        startActivity(Intent(this, Task3Activity::class.java))
    }
}

@Composable
fun TaskSelectionScreen(
    modifier: Modifier = Modifier,
    onTask1Click: () -> Unit,
    onTask2Click: () -> Unit,
    onTask3Click: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Module 6 - 3 Tasks",
            fontSize = 28.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Button(
            onClick = onTask1Click,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            Text(text = "Task 1: Photo Catalog (Retrofit)", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onTask2Click,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            Text(text = "Task 2: Nobel Laureates (Ktor)", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onTask3Click,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            Text(text = "Task 3: Auth & Users (Ktor)", fontSize = 16.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskSelectionScreenPreview() {
    Module6Zad13Theme {
        TaskSelectionScreen(
            onTask1Click = {},
            onTask2Click = {},
            onTask3Click = {}
        )
    }
}