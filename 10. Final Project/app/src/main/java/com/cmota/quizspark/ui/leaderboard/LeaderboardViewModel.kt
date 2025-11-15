package com.cmota.quizspark.ui.leaderboard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmota.quizspark.model.User
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

private const val TAG = "LeaderboardViewModel"

class LeaderboardViewModel : ViewModel() {

  private val _leaderboard = MutableStateFlow<UiState>(UiState.Initial)
  val leaderboard: StateFlow<UiState> = _leaderboard.asStateFlow()


  fun fetchLeaderboard() {
    viewModelScope.launch {
      _leaderboard.value = UiState.Loading

      try {
        val query = Firebase.firestore
          .collection("leaderboard")
          .orderBy("score", Query.Direction.DESCENDING)

        val snapshot = query.get().await()

        val leaderboardList = snapshot.toObjects(User::class.java)

        _leaderboard.value = UiState.Success(leaderboardList)

      } catch (e: Exception) {
        Log.d(TAG, "Error: ${e.message}")
        _leaderboard.value = UiState.Error(e.message ?: "Unknown error")
      }
    }
  }

  fun savePoints(username: String, correct: Int) {
    val score = correct * 10
    val db = Firebase.firestore
    val entry = mapOf(
      "username" to username,
      "score" to score,
      "timestamp" to FieldValue.serverTimestamp()
    )

    db.collection("leaderboard").add(entry)
  }
}
