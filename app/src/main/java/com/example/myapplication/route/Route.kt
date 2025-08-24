@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.myapplication.route

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object Home : NavKey
@Serializable
data object List : NavKey
@Serializable
data class Detail(val id: String) : NavKey
@Serializable
data object Settings : NavKey

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigate: (NavKey) -> Unit,
) {
    AppContainer(
        title = "Home",
        onClickAction = { onNavigate(Settings) },
    ) { innerPadding ->
        Box(modifier.fillMaxSize().padding(innerPadding)) {
            Column(Modifier.align(Alignment.Center)) {
                TextButton(onClick = { onNavigate(List) }) {
                    Text("To List")
                }
            }
        }
    }
}

@Composable
fun ListScreen(
    modifier: Modifier = Modifier,
    onClickSetting: (() -> Unit)?,
    onClickDetail: (id: String) -> Unit,
    onBack: (() -> Unit)?
) {
    val list = (0..100).map { "Item $it" }
    AppContainer(
        title = "List",
        onClickBack = onBack,
        onClickAction = onClickSetting?.let { { onClickSetting() } },
    ) { innerPadding ->
        LazyColumn(modifier = modifier.fillMaxSize().padding(innerPadding), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(list) {
                ListItem(headlineContent = { Text(it) }, modifier = Modifier.clickable {
                    onClickDetail(it)
                })
            }
        }
    }
}

@Composable
fun DetailScreen(
    id: String,
    modifier: Modifier = Modifier,
    onClickSetting: ((NavKey) -> Unit)?,
    onBack: (() -> Unit)?
) {
    AppContainer(
        title = "Detail",
        onClickBack = onBack,
        onClickAction = onClickSetting?.let { { onClickSetting(Settings) } },
    ) { innerPadding ->
        Box(modifier.fillMaxSize().padding(innerPadding)) {
            Text("Detail $id", modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
  onBack: () -> Unit
) {
    AppContainer(
        title = "Setting",
        onClickBack = onBack,
    ) { innerPadding ->
        Box(modifier.fillMaxSize().padding(innerPadding)) {
            Text("Setting", modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
private fun AppContainer(
    title: String,
    onClickAction: (() -> Unit)? = null,
    onClickBack: (() -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    onClickBack?.let {
                        IconButton(onClick = onClickBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                },
                actions = {
                    onClickAction?.let {
                        IconButton(onClick = onClickAction) {
                            Icon(Icons.Default.Settings, contentDescription = "Settings")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        content(innerPadding)
    }
}