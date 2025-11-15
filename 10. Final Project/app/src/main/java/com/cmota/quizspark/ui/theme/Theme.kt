package com.cmota.quizspark.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val darkColorScheme = darkColorScheme(
  primary = brightGreen,
  onPrimary = darkGreenBackground,
  secondary = mediumGreen,
  background = darkGreenBackground,
  surface = darkGreenBackground,
  onBackground = Color.White,
  onSurface = Color.White,
  onSurfaceVariant = hintGray,
  outline = borderGray
)

@Composable
fun QuizSparkTheme(
  content: @Composable () -> Unit
) {
  MaterialTheme(
    colorScheme = darkColorScheme,
    typography = Typography,
    content = content
  )
}