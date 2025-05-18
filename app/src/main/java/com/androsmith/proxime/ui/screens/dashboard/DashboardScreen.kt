package com.androsmith.proxime.ui.screens.dashboard

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.androsmith.proxime.R
import com.androsmith.proxime.data.model.SensorData
import com.androsmith.proxime.domain.model.Resource
import com.androsmith.proxime.ui.screens.dashboard.composables.DashboardAppBar
import com.androsmith.proxime.ui.screens.dashboard.composables.DashboardContent
import com.androsmith.proxime.ui.composables.ErrorContainer
import com.androsmith.proxime.ui.composables.LoadingContainer

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel(),
) {


    val sensorData by viewModel.sensorDataState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { DashboardAppBar(onBack = onBack) },
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->

        when (sensorData) {
            is Resource.Error -> ErrorContainer()


            is Resource.Loading -> LoadingContainer(
                message = sensorData.message ?: stringResource(R.string.loading)
            )


            is Resource.Success -> DashboardContent(
                sensorData = sensorData.data ?: SensorData.default(),
                modifier = Modifier.padding(innerPadding)
            )
        }

    }
}
