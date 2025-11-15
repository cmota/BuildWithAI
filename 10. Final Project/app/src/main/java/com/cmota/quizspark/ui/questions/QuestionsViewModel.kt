package com.cmota.quizspark.ui.questions

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmota.quizspark.model.Question
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.generationConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

private const val QUESTIONS = 10

private const val TAG = "QuestionsViewModel"

class QuestionsViewModel: ViewModel() {

  private val _question = MutableStateFlow(Question())
  val question: StateFlow<Question> = _question.asStateFlow()

  private val _questions = MutableStateFlow<UiState>(UiState.Initial)
  val questions: StateFlow<UiState> = _questions.asStateFlow()

  private val _indexCurrent = MutableStateFlow(0)
  val indexCurrent: StateFlow<Int> = _indexCurrent.asStateFlow()

  private val _indexLast = MutableStateFlow(QUESTIONS)
  val indexLast: StateFlow<Int> = _indexLast.asStateFlow()

  private val answered = mutableListOf<Int>()

  private val generationConfig = generationConfig {
    responseMimeType = "application/json"
  }

  private val systemInstruction = """
    You are a quiz generation assistant.
    Your response MUST be a single, valid JSON array.
    The array must contain exactly $QUESTIONS JSON objects.
    Do not add any other text, explanation, or markdown formatting around the JSON.

    Each JSON object in the array must match this exact structure:
    {
      "question": "The question text as a string.",
      "options": "A list of 4 strings for the answers.",
      "correct": "The 0-based index (integer) of the correct answer."
    }
    """

  private val generativeModel by lazy {
    Firebase.ai(backend = GenerativeBackend.googleAI())
      .generativeModel(
        modelName = "gemini-2.5-flash-lite",
        generationConfig = generationConfig,
        systemInstruction = content {
          text(systemInstruction)
        }
      )
  }

  private val jsonParser = Json { ignoreUnknownKeys = true }

  fun generateQuiz(topic: String) {
    viewModelScope.launch {
      try {
        answered.clear()

        _questions.value = UiState.Loading

        val prompt = "Generate $QUESTIONS question about $topic."

        val response = generativeModel.generateContent(prompt)
        val questions = jsonParser.decodeFromString<List<Question>>(response.text ?: "")

        _question.value = questions.first()
        _questions.value = UiState.Success(questions)

        _indexLast.value = questions.size
        _indexCurrent.value = 1

      } catch (e: Exception) {
        Log.d(TAG, "Error: ${e.message}")
        _questions.value = UiState.Error(e.message ?: "An unknown error occurred")
      }
    }
  }

  fun answerQuestion(answer: Int) {
    val data = _questions.value
    if (data !is UiState.Success) {
      return
    }

    answered.add(answer)
    if (answered.size < data.questions.size) {
      _question.value = data.questions[answered.size]
      _indexCurrent.value = answered.size + 1
    }
  }

  fun score(): Int {
    val data = _questions.value
    if (data !is UiState.Success) {
      return 0
    }

    return answered.zip(data.questions)
      .count { (answer, question) ->
        answer == question.correct
      }
  }
}
