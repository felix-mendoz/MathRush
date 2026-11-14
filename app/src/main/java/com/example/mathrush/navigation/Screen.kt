package com.example.mathrush.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object GameConfig : Screen("game_config")
    object GamePlay : Screen("game_play")
    object History : Screen("history")
}