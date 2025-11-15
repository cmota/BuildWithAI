package com.cmota.quizspark.ui.leaderboard

import com.cmota.quizspark.model.User

sealed interface UiState {

  data object Initial : UiState

  data object Loading : UiState

  data class Success(val users: List<User>) : UiState

  data class Error(val message: String) : UiState
}