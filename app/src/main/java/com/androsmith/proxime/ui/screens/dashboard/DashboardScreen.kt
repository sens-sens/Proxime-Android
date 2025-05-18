package com.androsmith.proxime.ui.screens.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.androsmith.proxime.data.model.ConnectionState
import com.androsmith.proxime.data.model.SensorData
import com.androsmith.proxime.domain.model.Resource
import com.androsmith.proxime.ui.screens.dashboard.composables.DashboardAppBar
import com.androsmith.proxime.ui.screens.dashboard.composables.IRVisualization

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel(),
) {


    val sensorData by viewModel.sensorDataState.collectAsStateWithLifecycle()

    when (sensorData) {
        is Resource.Error -> {
            Box(
                modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is Resource.Loading -> {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier.fillMaxSize()
            ) {
                CircularProgressIndicator()
                Spacer(Modifier.height(20.dp))
                Text(sensorData.message ?: "Loading")
            }
        }

        is Resource.Success -> {
            val data = sensorData.data ?: SensorData(
                isProximityBlocked = false,
                isButtonPressed = false,
                connectionState = ConnectionState.Disconnected
            )
            Scaffold(
                topBar = {
                    DashboardAppBar(
                        onBack = onBack
                    )
                },
                modifier = modifier.fillMaxSize(),
            ) { innerPadding ->


                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(innerPadding)
                ) {


                    IRVisualization(
                        isBlocked = data.isProximityBlocked
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )

                    Spacer(Modifier.height(40.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 40.dp)
                    ) {
                        Text(
                            "Button state", fontSize = 28.sp
                        )

                        Switch(
                            checked = data.isButtonPressed,
                            onCheckedChange = {},
                            modifier = Modifier.scale(1.5F)
                        )
                    }

                    Spacer(Modifier.height(40.dp))
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )



                    Spacer(Modifier.height(40.dp))


                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 40.dp)
                    ) {
                        Text(
                            "Device      :   ${if (data.connectionState == ConnectionState.Connected) "Connected" else "Disconnected"}",
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7F)
                        )
                        Text(
                            "IR sensor  :   ${if (data.isProximityBlocked) "Blocked" else "Unblocked"}",
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7F)
                        )
                        Text(
                            "Button      :   ${if (data.isButtonPressed) "Pressed" else "Released"}",
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7F)
                        )
                    }

                    Spacer(Modifier.height(40.dp))

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                }
            }


        }
    }
}
