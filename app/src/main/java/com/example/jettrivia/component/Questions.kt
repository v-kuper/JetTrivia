package com.example.jettrivia.component

import android.util.Log
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.toMutableStateList
import com.example.jettrivia.screens.QuestionsViewModel


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