package com.cmota.quizspark.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cmota.quizspark.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IntroScreen(
  navigateToQuiz: (String) -> Unit
) {
  Scaffold(
    topBar = {
      CenterAlignedTopAppBar(
        title = {
          Text(
            text = stringResource(R.string.app_name)
          )
        }
      )
    }
  ) { innerPadding ->
    IntroContent(
      modifier = Modifier.padding(innerPadding),
      navigateToQuiz = navigateToQuiz
    )
  }
}

@Composable
private fun IntroContent(
  modifier: Modifier,
  navigateToQuiz: (String) -> Unit
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.Center
  ) {

    Column(
      modifier = Modifier.fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = stringResource(R.string.intro_create_your_challenge),
        style = MaterialTheme.typography.displayMedium
      )

      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = stringResource(R.string.intro_create_your_challenge_hint),
        style = MaterialTheme.typography.titleLarge
      )
    }

    Spacer(modifier = Modifier.height(64.dp))

    Text(
      text = stringResource(R.string.intro_topic)
    )

    Spacer(modifier = Modifier.height(8.dp))

    val topic = remember { mutableStateOf("") }

    OutlinedTextField(
      value = topic.value,
      onValueChange = { topic.value = it },
      modifier = Modifier.fillMaxWidth(),
      placeholder = {
        Text(
          text = stringResource(R.string.intro_topic_hint)
        )
      },
      shape = RoundedCornerShape(50),
      singleLine = true
    )

    Spacer(modifier = Modifier.height(16.dp))

    Row {
      Icon(
        painter = painterResource(R.drawable.ic_generated),
        stringResource(R.string.description_generated)
      )

      Spacer(modifier = Modifier.width(8.dp))

      Text(
        text = stringResource(R.string.intro_ai_info)
      )
    }

    Spacer(modifier = Modifier.height(64.dp))

    Button(
      onClick = { navigateToQuiz(topic.value) },
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(50),
      enabled = topic.value.isNotEmpty()
    ) {
      Text(
        text = stringResource(R.string.intro_start),
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
        fontWeight = FontWeight.Bold
      )
    }
  }
}