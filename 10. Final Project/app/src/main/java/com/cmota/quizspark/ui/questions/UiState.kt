package com.cmota.quizspark.ui.questions

import com.cmota.quizspark.model.Question

sealed interface UiState {

  data object Initial : UiState

  data object Loading : UiState

  data class Success(val questions: List<Question>) : UiState

  data class Error(val message: String) : UiState
}