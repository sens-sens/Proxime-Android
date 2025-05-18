package com.androsmith.proxime.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.androsmith.proxime.ui.screens.dashboard.DashboardScreen
import com.androsmith.proxime.ui.screens.devices.DeviceScreen
import com.androsmith.proxime.ui.screens.permission.PermissionScreen

@Composable
fun ProximeNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {


    NavHost(navController, startDestination = Screens.Permission) {
        composable<Screens.Permission> {
            PermissionScreen(
                onPermissionGranted = {
                    navController.navigate(Screens.Device)
                })
        }
        composable<Screens.Device> {
            DeviceScreen(
                navigateToDashBoard = {
                    navController.navigate(Screens.Dashboard)
                }
            )
        }
        composable<Screens.Dashboard> {
            DashboardScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}