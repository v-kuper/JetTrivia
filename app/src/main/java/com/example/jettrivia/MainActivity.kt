package com.example.jettrivia

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.jettrivia.screens.QuestionsViewModel
import com.example.jettrivia.ui.theme.JetTriviaTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JetTriviaTheme {
                TriviaHome()
            }
        }
    }
}

@Composable
fun TriviaHome(viewModel: QuestionsViewModel = hiltViewModel(), ) {
    Question(viewModel)
}

@Composable
fun Question(viewModel: QuestionsViewModel) {
    val questions = viewModel.data.value.data?.toMutableStateList()

    if(viewModel.data.value.loading == true) {
        Log.d("loading", "Questions: loading...")
    } else {
        Log.d("SIZE", "Questions: loading... STOPED...")
        questions?.forEach { question ->
            Log.d("Result", "Question: ${question.choices}")
        }
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JetTriviaTheme {}
}