package com.cmota.quizspark

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cmota.quizspark.ui.leaderboard.LeaderboardViewModel
import com.cmota.quizspark.ui.navigation.MainNavigation
import com.cmota.quizspark.ui.questions.QuestionsViewModel
import com.cmota.quizspark.ui.theme.QuizSparkTheme

class MainActivity : ComponentActivity() {

  private val questionsViewModel: QuestionsViewModel by viewModels()
  private val leaderboardViewModel: LeaderboardViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      QuizSparkTheme {
        MainNavigation(
          questionsViewModel = questionsViewModel,
          leaderboardViewModel = leaderboardViewModel
        )
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
  QuizSparkTheme {
    MainNavigation(
      questionsViewModel = QuestionsViewModel(),
      leaderboardViewModel = LeaderboardViewModel()
    )
  }
}