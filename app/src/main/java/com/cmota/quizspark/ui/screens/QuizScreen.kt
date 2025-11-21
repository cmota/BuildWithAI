package com.cmota.quizspark.ui.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cmota.quizspark.R
import com.cmota.quizspark.model.Question
import com.cmota.quizspark.ui.leaderboard.LeaderboardViewModel
import com.cmota.quizspark.ui.questions.QuestionsViewModel
import com.cmota.quizspark.ui.questions.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
  questionsViewModel: QuestionsViewModel,
  leaderboardViewModel: LeaderboardViewModel,
  navigateToHome: () -> Unit,
  navigateToLeaderboard: () -> Unit
) {

  val question = questionsViewModel.question.collectAsState()
  val questions = questionsViewModel.questions.collectAsState()

  val indexLast = questionsViewModel.indexLast.collectAsState()
  val indexCurrent = questionsViewModel.indexCurrent.collectAsState()

  val selectedOption = remember { mutableStateOf<Int?>(null) }
  val showUsernameDialog = remember { mutableStateOf(false) }

  Scaffold(
    topBar = {
      CenterAlignedTopAppBar(
        title = {
          Text(
            text = stringResource(R.string.quiz_question, indexCurrent.value, indexLast.value)
          )
        },
        navigationIcon = {
          IconButton(
            onClick = { navigateToHome() }
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = stringResource(R.string.description_close_quiz)
            )
          }
        }
      )
    },
    bottomBar = {
      if (indexCurrent.value == indexLast.value) {
        QuizBottomAction(
          actionResId = R.string.quiz_submit,
          enabled = questions.value is UiState.Success && selectedOption.value != null,
          onClick = { showUsernameDialog.value = true }
        )
      } else {
        QuizBottomAction(
          actionResId = R.string.quiz_next,
          enabled = questions.value is UiState.Success && selectedOption.value != null,
          onClick = { selectedOption.value?.let {
            questionsViewModel.answerQuestion(it)
            selectedOption.value = null
          }}
        )
      }
    }
  ) { innerPadding ->
    QuizContent(
      modifier = Modifier.padding(innerPadding),
      question = question.value,
      questions = questions.value,
      selectedOption = selectedOption
    )

    if (showUsernameDialog.value) {
      UsernameDialog(
        confirmAction = { username ->
          val score = questionsViewModel.score()
          leaderboardViewModel.savePoints(username, score)
          showUsernameDialog.value = false
          navigateToLeaderboard()
        },
        dismissAction = { showUsernameDialog.value = false }
      )
    }
  }
}

@Composable
private fun QuizContent(
  modifier: Modifier,
  question: Question,
  questions: UiState,
  selectedOption: MutableState<Int?>
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {

    when(questions) {
      is UiState.Initial,
      is UiState.Loading -> QuizContentLoading()
      is UiState.Success -> QuizContentSuccess(
          question = question,
          selectedOption = selectedOption
      )
      is UiState.Error -> QuizContentError()
    }
  }
}

@Composable
private fun QuizOption(
  id: Int,
  option: String,
  isSelected: Boolean,
  onOptionSelected: (Int) -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(50))
      .clickable(onClick = { onOptionSelected(id) })
      .background(MaterialTheme.colorScheme.secondary)
      .border(
        2.dp, if (isSelected) {
          MaterialTheme.colorScheme.primary
        } else {
          MaterialTheme.colorScheme.secondary
        }, RoundedCornerShape(50)
      )
      .padding(8.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {

    Row(
      modifier = Modifier
        .size(45.dp)
        .background(
          if (isSelected) {
            MaterialTheme.colorScheme.primary
          } else {
            MaterialTheme.colorScheme.background
          }, shape = CircleShape
        ),
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "$id",
        style = MaterialTheme.typography.titleLarge
      )
    }

    Spacer(modifier = Modifier.width(16.dp))

    Text(
      text = option,
      style = MaterialTheme.typography.titleLarge
    )
  }
}

@Composable
private fun QuizContentLoading() {
  LinearProgressIndicator(
    modifier = Modifier.fillMaxWidth()
  )
}

@Composable
private fun QuizContentSuccess(
  question: Question,
  selectedOption: MutableState<Int?>
) {
  Spacer(modifier = Modifier.height(32.dp))

  Text(
    text = question.question,
    style = MaterialTheme.typography.displayMedium
  )

  Spacer(modifier = Modifier.height(32.dp))

  question.options.forEachIndexed { index, option ->
    QuizOption(
      id = index,
      option = option,
      isSelected = selectedOption.value == index,
      onOptionSelected = { selectedOption.value = it }
    )

    Spacer(modifier = Modifier.height(16.dp))
  }
}


@Composable
private fun QuizContentError() {
  Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center
  ) {
    Text(
      text = stringResource(R.string.quiz_error),
      style = MaterialTheme.typography.titleLarge
    )
  }
}

@Composable
private fun QuizBottomAction(
  @StringRes actionResId: Int,
  enabled: Boolean,
  onClick: () -> Unit
) {
  Button(
    onClick = { onClick() },
    modifier = Modifier
      .fillMaxWidth()
      .padding(start = 16.dp, end = 16.dp, bottom = 32.dp),
    shape = RoundedCornerShape(50),
    enabled = enabled
  ) {
    Text(
      text = stringResource(actionResId),
      style = MaterialTheme.typography.titleLarge,
      modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
      fontWeight = FontWeight.Bold
    )
  }
}

@Composable
private fun UsernameDialog(
  confirmAction: (String) -> Unit,
  dismissAction: () -> Unit
) {
  val username = remember { mutableStateOf("") }

  AlertDialog(
    title = {
      Text(stringResource(R.string.quiz_dialog_submit_score))
    },
    text = {
      OutlinedTextField(
        value = username.value,
        onValueChange = { username.value = it },
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
          Text(
            text = stringResource(R.string.quiz_dialog_username)
          )
        },
        shape = RoundedCornerShape(50),
        singleLine = true
      )
    },
    confirmButton = {
      TextButton(
        onClick = { confirmAction(username.value) },
        enabled = username.value.isNotEmpty()
      ) {
        Text(stringResource(R.string.quiz_dialog_submit_score))
      }
    },
    dismissButton = {
      TextButton(onClick = { dismissAction() }) {
        Text(stringResource(R.string.quiz_dialog_cancel))
      }
    },
    onDismissRequest = { dismissAction () }
  )
}
