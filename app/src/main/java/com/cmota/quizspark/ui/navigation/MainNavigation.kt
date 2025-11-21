package com.cmota.quizspark.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.cmota.quizspark.ui.leaderboard.LeaderboardViewModel
import com.cmota.quizspark.ui.questions.QuestionsViewModel
import com.cmota.quizspark.ui.screens.IntroScreen
import com.cmota.quizspark.ui.screens.LeaderboardScreen
import com.cmota.quizspark.ui.screens.QuizScreen

@Composable
fun MainNavigation(
  questionsViewModel: QuestionsViewModel,
  leaderboardViewModel: LeaderboardViewModel
) {
  val backStack = rememberNavBackStack(Intro)

  NavDisplay(
    backStack = backStack,
    onBack = { backStack.removeLastOrNull() },
    entryProvider = entryProvider {
      entry<Intro> {
        IntroScreen(
          navigateToQuiz = { topic ->
            backStack.add(Quiz)
            questionsViewModel.generateQuiz(topic)
          }
        )
      }

      entry<Quiz> {
        QuizScreen(
          questionsViewModel = questionsViewModel,
          leaderboardViewModel = leaderboardViewModel,
          navigateToHome = { backStack.remove(Quiz) },
          navigateToLeaderboard = { backStack.add(Leaderboard) }
        )
      }

      entry<Leaderboard> {
        leaderboardViewModel.fetchLeaderboard()

        LeaderboardScreen(
          leaderboardViewModel = leaderboardViewModel,
          navigateToHome = {
            backStack.remove(Leaderboard)
            backStack.remove(Quiz)
          }
        )
      }
    }
  )
}
