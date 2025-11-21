package com.cmota.quizspark.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cmota.quizspark.R
import com.cmota.quizspark.model.User
import com.cmota.quizspark.ui.leaderboard.LeaderboardViewModel
import com.cmota.quizspark.ui.leaderboard.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(
  leaderboardViewModel: LeaderboardViewModel,
  navigateToHome: () -> Unit
) {

  val leaderboard = leaderboardViewModel.leaderboard.collectAsState()

  Scaffold(
    topBar = {
      CenterAlignedTopAppBar(
        title = {
          Text(
            text = stringResource(R.string.leaderboard)
          )
        },
      )
    },
    bottomBar = {
      Button(
        onClick = { navigateToHome() },
        modifier = Modifier
          .fillMaxWidth()
          .padding(start = 16.dp, end = 16.dp, bottom = 32.dp),
        shape = RoundedCornerShape(50)
      ) {
        Text(
          text = stringResource(R.string.leaderboard_play),
          style = MaterialTheme.typography.titleLarge,
          modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
          fontWeight = FontWeight.Bold
        )
      }
    }
  ) { innerPadding ->
    LeaderboardContent(
      modifier = Modifier.padding(innerPadding),
      leaderboard = leaderboard.value
    )
  }
}

@Composable
private fun LeaderboardContent(
  modifier: Modifier,
  leaderboard: UiState
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {

    when (leaderboard) {
      is UiState.Initial,
      is UiState.Loading -> LeaderboardLoading()
      is UiState.Success -> LeaderboardSuccess(leaderboard.users)
      is UiState.Error -> LeaderboardError()
    }
  }
}

@Composable
private fun LeaderboardLoading() {
  LinearProgressIndicator(
    modifier = Modifier.fillMaxWidth()
  )
}

@Composable
private fun LeaderboardSuccess(
  users: List<User>
) {
  LazyColumn {
    itemsIndexed(users) { index, user ->
      LeaderboardUser("${index + 1}", user.username, user.score)

      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}

@Composable
private fun LeaderboardError() {
  Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center
  ) {
    Text(
      text = stringResource(R.string.leaderboard_error),
      style = MaterialTheme.typography.titleLarge
    )
  }
}

@Composable
private fun LeaderboardUser(
  id: String,
  username: String,
  points: Long
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(25))
      .background(MaterialTheme.colorScheme.secondary)
      .border(2.dp, MaterialTheme.colorScheme.secondary, RoundedCornerShape(25))
      .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {

    Text(
      text = id,
      style = MaterialTheme.typography.titleLarge
    )

    Spacer(modifier = Modifier.width(16.dp))

    Text(
      text = username,
      style = MaterialTheme.typography.titleLarge
    )

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(top = 16.dp, bottom = 16.dp),
      horizontalArrangement = Arrangement.End
    ) {
      Text(
        text = stringResource(R.string.leaderboard_points, points),
        style = MaterialTheme.typography.titleLarge
      )
    }
  }
}
