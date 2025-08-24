package com.example.myapplication

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun SimpleSample(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(Home)
    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        entryProvider = entryProvider {
            entry<Home> {
                HomeScreen(
                    onNavigate = { backStack.add(it) }
                )
            }
            entry<List> {
                ListScreen(
                    onBack = { backStack.removeAt(backStack.lastIndex) },
                    onClickSetting = { backStack.add(Settings) },
                    onClickDetail = { backStack.add(Detail(it)) }
                )
            }
            entry<Detail> { entry ->
                val id = entry.id
                DetailScreen(
                    id = id,
                    onBack = { backStack.removeAt(backStack.lastIndex) },
                    onClickSetting = { backStack.add(it) }
                )
            }
            entry<Settings> {
                SettingScreen(
                    onBack = { backStack.removeAt(backStack.lastIndex) },
                )
            }
        }
    )
}