package com.example.jettrivia.component

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jettrivia.screens.QuestionsViewModel
import com.example.jettrivia.util.AppColors


@Composable
fun Questions(viewModel: QuestionsViewModel) {
    val questions = viewModel.data.value.data?.toMutableStateList()

    if(viewModel.data.value.loading == true) {

        CircularProgressIndicator()

        Log.d("loading", "Questions: loading...")
    } else {
        Log.d("SIZE", "Questions: loading... STOPED...")
        questions?.forEach { question ->
            Log.d("Result", "Question: ${question.choices}")
        }
    }
}

@Preview
@Composable
fun QuestionsDisplay() {
    Surface(modifier = Modifier
        .fillMaxSize()
        .fillMaxSize()
        .padding(4.dp),
        color = AppColors.mDarkPurple) {
        Column(modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start) {
            QuestionTracker()
        }
    }
}

@Composable
fun QuestionTracker(counter: Int = 10, outOf: Int = 100) {
    Row(modifier = Modifier.padding(10.dp),) {
        Text(
            text = "Question $counter/$outOf",
            color = AppColors.mLightGray,
            fontWeight = FontWeight.Bold,
            fontSize = 27.sp
        )
    }
}