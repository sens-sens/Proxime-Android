package com.androsmith.proxime.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.androsmith.proxime.ui.navigation.ProximeNavigation
import com.androsmith.proxime.ui.theme.ProximeTheme

@Composable
fun ProximeApp(modifier: Modifier = Modifier) {

    ProximeTheme {
        val navController = rememberNavController()

        ProximeNavigation(
            navController = navController
        )
    }
}