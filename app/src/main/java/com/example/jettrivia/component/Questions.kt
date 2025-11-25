package com.example.jettrivia.component

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
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
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    Surface(modifier = Modifier
        .fillMaxSize()
        .fillMaxSize()
        .padding(4.dp),
        color = AppColors.mDarkPurple) {
        Column(modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start) {
            QuestionTracker()
            DrawDottedLine(pathEffect = pathEffect)
        }
    }
}

@Composable
fun DrawDottedLine(pathEffect: PathEffect) {
    Canvas(modifier = Modifier.fillMaxWidth().height(1.dp)
        ) {
        drawLine(
            color = AppColors.mLightGray,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            pathEffect = pathEffect
        )
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