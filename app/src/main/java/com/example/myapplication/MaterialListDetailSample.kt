package com.example.myapplication

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.myapplication.route.Detail
import com.example.myapplication.route.DetailScreen
import com.example.myapplication.route.Home
import com.example.myapplication.route.HomeScreen
import com.example.myapplication.route.List
import com.example.myapplication.route.ListScreen
import com.example.myapplication.route.SettingScreen
import com.example.myapplication.route.Settings

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun MaterialListDetailSample(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(Home)
    val windowAdaptiveInfo = currentWindowAdaptiveInfo()
    val directive = remember(windowAdaptiveInfo) {
        calculatePaneScaffoldDirective(windowAdaptiveInfo)
            .copy(horizontalPartitionSpacerSize = 0.dp)
    }
    val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>(directive = directive)
    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        sceneStrategy = listDetailStrategy,
        entryProvider = entryProvider {
            entry<Home> {
                HomeScreen(
                    onNavigate = { backStack.add(it) }
                )
            }
            entry<List>(
                metadata = ListDetailSceneStrategy.listPane(
                    sceneKey = "list-detail-key",
                    detailPlaceholder = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Choose an item from the list")
                        }
                    }
                )
            ) {
                ListScreen(
                    onBack = { backStack.removeLastOrNull() },
                    onClickSetting = { backStack.add(Settings) },
                    onClickDetail = { backStack.add(Detail(it)) }
                )
            }
            entry<Detail>(
                metadata = ListDetailSceneStrategy.detailPane(
                    sceneKey = "list-detail-key"
                )
            ) { entry ->
                val id = entry.id
                val isPartitioned = directive.maxHorizontalPartitions > 1
                DetailScreen(
                    id = id,
                    onBack = if (isPartitioned) { null } else {
                        { backStack.removeLastOrNull() }
                    },
                    onClickSetting = if (isPartitioned) { null } else {
                        { backStack.add(it) }
                    }
                )
            }
            entry<Settings> {
                SettingScreen(
                    onBack = { backStack.removeLastOrNull() },
                )
            }
        }
    )
}