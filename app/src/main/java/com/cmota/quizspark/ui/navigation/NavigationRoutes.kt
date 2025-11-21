package com.cmota.quizspark.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object Intro: NavKey

@Serializable
data object Quiz: NavKey

@Serializable
data object Leaderboard: NavKey
