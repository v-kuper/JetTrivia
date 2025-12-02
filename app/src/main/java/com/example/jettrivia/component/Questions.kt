package com.example.jettrivia.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jettrivia.model.QuestionItem
import com.example.jettrivia.screens.QuestionsViewModel
import com.example.jettrivia.util.AppColors

@Composable
fun Questions(viewModel: QuestionsViewModel) {
    // Достаём текущее состояние данных из вьюмодели
    val state = viewModel.data.value
    // Если дата = null, возвращаем пустой список, чтобы не работать с nullable
    val questions = state.data ?: emptyList()

    // Состояние текущего индекса вопроса
    var questionIndex by remember { mutableIntStateOf(0) }

    // Показ индикатора загрузки
    if (state.loading == true) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(color = AppColors.mOffWhite)
        }
        return
    }

    // Если вопросов нет, можно показать заглушку или просто выйти
    if (questions.isEmpty()) {
        Text(text = "Нет доступных вопросов", color = AppColors.mOffWhite)
        return
    }

    // Проверяем, что индекс в пределах списка
    val question = questions.getOrNull(questionIndex)
    if (question == null) {
        // Здесь можно показать экран завершения квиза
        Text(text = "Вопросы закончились", color = AppColors.mOffWhite)
        return
    }

    QuestionsDisplay(
        question = question,
        questionIndex = questionIndex,
        size = questions.size,
        onNextClicked = {
            // Переход к следующему вопросу, если ещё не конец
            if (questionIndex < questions.lastIndex) {
                questionIndex++
            } else {
                // Здесь можно обработать завершение квиза
                // например, показать результаты
            }
        }
    )
}

//@Preview
@Composable
fun QuestionsDisplay(
    question: QuestionItem,
    questionIndex: Int,
    size: Int,
    onNextClicked: () -> Unit
) {
    // Локальное состояние выбранного ответа: индекс или null, если ещё не выбрали
    var selectedIndex by remember(question) { mutableStateOf<Int?>(null) }

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = AppColors.mDarkPurple
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            ShowProgress(score = questionIndex, maxScore = size)

            // Трекер "вопрос X из Y"
            QuestionTracker(counter = questionIndex, outOf = size)

            // Пунктирная линия под заголовком
            DrawDottedLine()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 0.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    // Текст вопроса
                    Text(
                        modifier = Modifier
                            .padding(6.dp)
                            .align(alignment = Alignment.Start)
                            .fillMaxHeight(0.2f),
                        text = question.question,
                        color = AppColors.mLightGray,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 22.sp
                    )

                    // Отрисовываем варианты ответов
                    question.choices.forEachIndexed { index, answerText ->
                        Row(
                            modifier = Modifier
                                .padding(3.dp)
                                .fillMaxWidth()
                                .height(45.dp)
                                .border(
                                    width = 4.dp,
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            AppColors.mOffDarkPurple,
                                            AppColors.mDarkPurple
                                        )
                                    ),
                                    shape = RoundedCornerShape(15.dp)
                                )
                                .clip(
                                    RoundedCornerShape(
                                        topStartPercent = 50,
                                        topEndPercent = 50,
                                        bottomEndPercent = 50,
                                        bottomStartPercent = 50
                                    )
                                )
                                .background(Color.Transparent),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val isSelected = selectedIndex == index
                            val isCorrectChoice = answerText == question.answer

                            val selectedRadioColor = when {
                                isSelected && isCorrectChoice -> Color.Green.copy(alpha = 0.4f)
                                isSelected && !isCorrectChoice -> Color.Red.copy(alpha = 0.4f)
                                else -> AppColors.mOffWhite.copy(alpha = 0.4f)
                            }

                            RadioButton(
                                selected = isSelected,
                                onClick = { selectedIndex = index },
                                modifier = Modifier.padding(start = 16.dp),
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = selectedRadioColor
                                )
                            )

                            val annotatedString = buildAnnotatedString {
                                val textColor = when {
                                    isSelected && isCorrectChoice -> Color.Green
                                    isSelected && !isCorrectChoice -> Color.Red
                                    else -> AppColors.mOffWhite
                                }

                                withStyle(
                                    style = SpanStyle(
                                        fontWeight = FontWeight.Light,
                                        color = textColor
                                    )
                                ) {
                                    append(answerText)
                                }
                            }

                            Text(
                                text = annotatedString,
                                modifier = Modifier.padding(6.dp)
                            )
                        }
                    }
                }

                Button(
                    onClick = {
                        onNextClicked()
                        selectedIndex = null
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding( 16.dp)
                        .align(alignment = Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(34.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppColors.mBlue
                    ),
                    enabled = selectedIndex != null
                ) {
                    Text(
                        text = "Next",
                        modifier = Modifier.padding(4.dp),
                        color = AppColors.mOffWhite,
                        fontSize = 17.sp
                    )
                }
            }
        }
    }
}

@Composable
fun DrawDottedLine() {
    // pathEffect создаём один раз и переиспользуем при последующих композициях
    val pathEffect = remember {
        PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
    ) {
        // Рисуем пунктирную линию по ширине Canvas
        drawLine(
            color = AppColors.mLightGray,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            pathEffect = pathEffect
        )
    }
}

@Composable
fun QuestionTracker(counter: Int, outOf: Int) {
    Row(
        modifier = Modifier.padding(10.dp)
    ) {
        // Добавляем +1, так как индекс начинается с 0
        Text(
            text = "Question ${counter + 1}/$outOf",
            color = AppColors.mLightGray,
            fontWeight = FontWeight.Bold,
            fontSize = 27.sp
        )
    }
}

@Preview
@Composable
fun ShowProgressPreview() {
    ShowProgress(score = 12, maxScore = 100)
}

@Composable
fun ShowProgress(
    score: Int,
    maxScore: Int = 100,
    modifier: Modifier = Modifier
) {
    val clampedScore = score.coerceIn(0, maxScore)
    val targetProgress = (clampedScore.toFloat() / maxScore.toFloat())
        .coerceIn(0f, 1f)

    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "progressAnimation"
    )

    val gradient = Brush.linearGradient(
        listOf(
            Color(0xFFF95075),
            Color(0xFFBE6BE5)
        )
    )

    Box(
        modifier = modifier
            .padding(3.dp)
            .fillMaxWidth()
            .height(45.dp)
            .border(
                width = 4.dp,
                brush = Brush.linearGradient(
                    colors = listOf(AppColors.mLightPurple, AppColors.mLightPurple)
                ),
                shape = RoundedCornerShape(34.dp)
            )
            .clip(RoundedCornerShape(34.dp))
    ) {
        // Трек + заливка прогресса
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Transparent)
        ) {
            // Заливка прогресса
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(animatedProgress)
                    .background(brush = gradient)
            )
        }

        // Текст поверх всего
        Text(
            text = "$clampedScore",
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 8.dp),
            color = AppColors.mOffWhite,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Clip
        )
    }
}