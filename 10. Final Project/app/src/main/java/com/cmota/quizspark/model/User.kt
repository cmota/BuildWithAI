package com.cmota.quizspark.model

import com.google.firebase.firestore.DocumentId

data class User(
  @DocumentId val uid: String? = null,
  val username: String = "",
  val score: Long = 0L
)