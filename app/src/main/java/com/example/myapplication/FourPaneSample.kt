package com.example.myapplication


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.Scene
import androidx.navigation3.ui.SceneStrategy
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND
import com.example.myapplication.route.Detail
import com.example.myapplication.route.DetailScreen
import com.example.myapplication.route.Home
import com.example.myapplication.route.HomeScreen
import com.example.myapplication.route.ListScreen
import com.example.myapplication.route.SettingScreen
import com.example.myapplication.route.Settings

@Composable
fun FourPaneSample(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(
        Home, com.example.myapplication.route.List, Detail("hoge"), Settings
    )
    val strategy = remember { FourPaneStrategy<Any>() }
    NavDisplay(
        backStack = backStack,
        onBack = { keysToRemove -> repeat(keysToRemove) { backStack.removeLastOrNull() } },
        sceneStrategy = strategy,
        entryProvider = entryProvider {
            entry<Home>(
                metadata = FourPaneScene.twoPane()
            ) {
                HomeScreen(
                    onNavigate = { backStack.add(it) }
                )
            }
            entry<com.example.myapplication.route.List>(
                metadata = FourPaneScene.twoPane()
            ) {
                ListScreen(
                    onBack = { backStack.removeAt(backStack.lastIndex) },
                    onClickSetting = { backStack.add(Settings) },
                    onClickDetail = { backStack.add(Detail(it)) }
                )
            }
            entry<Detail>(
                metadata = FourPaneScene.twoPane()
            ) { entry ->
                val id = entry.id
                DetailScreen(
                    id = id,
                    onBack = { backStack.removeAt(backStack.lastIndex) },
                    onClickSetting = { backStack.add(it) }
                )
            }
            entry<Settings>(
                metadata = FourPaneScene.twoPane()
            ) {
                SettingScreen(
                    onBack = { backStack.removeAt(backStack.lastIndex) },
                )
            }
        }
    )
}

class FourPaneStrategy<T: Any>: SceneStrategy<T> {
    @OptIn(ExperimentalMaterial3AdaptiveApi::class) // Opt-in for adaptive and window size class APIs
    @Composable
    override fun calculateScene(
        entries: List<NavEntry<T>>,
        onBack: (Int) -> Unit
    ): Scene<T>? {
        val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

        if (!windowSizeClass.isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND)) {
            return null
        }

        val lastTwoEntries = entries.takeLast(4)

        return if (lastTwoEntries.size == 4
            && lastTwoEntries.all { it.metadata.containsKey(FourPaneScene.Four_PANE_KEY) }
        ) {
            val firstEntry = lastTwoEntries.first()
            val secondEntry = lastTwoEntries.get(1)
            val thirdEntry = lastTwoEntries.get(2)
            val forthEntry = lastTwoEntries.last()

            val sceneKey = listOf(firstEntry.contentKey, secondEntry.contentKey, thirdEntry.contentKey, forthEntry.contentKey)

            FourPaneScene(
                key = sceneKey,
                previousEntries = entries.dropLast(1),
                firstEntry = firstEntry,
                secondEntry = secondEntry,
                thirdEntry = thirdEntry,
                forthEntry = forthEntry,
            )
        } else {
            null
        }
    }
}

class FourPaneScene<T : Any>(
    override val key: Any,
    override val previousEntries: List<NavEntry<T>>,
    val firstEntry: NavEntry<T>,
    val secondEntry: NavEntry<T>,
    val thirdEntry: NavEntry<T>,
    val forthEntry: NavEntry<T>
) : Scene<T> {
    override val entries: List<NavEntry<T>> = listOf(firstEntry, secondEntry,thirdEntry,forthEntry)
    override val content: @Composable (() -> Unit) = {
        Column(Modifier.fillMaxSize()) {
            Row(Modifier.weight(1f).fillMaxWidth()) {
                Box(Modifier.weight(1f).fillMaxHeight()) { firstEntry.Content() }
                Box(Modifier.weight(1f).fillMaxHeight()) { secondEntry.Content() }
            }
            Row(Modifier.weight(1f).fillMaxWidth()) {
                Box(Modifier.weight(1f).fillMaxHeight()) { thirdEntry.Content() }
                Box(Modifier.weight(1f).fillMaxHeight()) { forthEntry.Content() }
            }
        }
    }

    companion object {
        internal const val Four_PANE_KEY = "TwoPane"
        /**
         * Helper function to add metadata to a [NavEntry] indicating it can be displayed
         * in a two-pane layout.
         */
        fun twoPane() = mapOf(Four_PANE_KEY to true)
    }
}