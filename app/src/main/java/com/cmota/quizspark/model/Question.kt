package com.cmota.quizspark.model

import kotlinx.serialization.Serializable

@Serializable
data class Question(
  val question: String = "",
  val options: List<String> = emptyList(),
  val correct: Int = 0
)