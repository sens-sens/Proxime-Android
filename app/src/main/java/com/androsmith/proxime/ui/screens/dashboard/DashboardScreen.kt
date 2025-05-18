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
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.androsmith.proxime.ui.screens.dashboard.composables.DashboardAppBar
import com.androsmith.proxime.ui.screens.dashboard.composables.IRVisualization
import com.androsmith.proxime.ui.screens.device.DeviceList
import com.androsmith.proxime.ui.screens.device.composables.NoDeviceAnimation
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel(),
) {

    val isConnected = viewModel.isConnected.collectAsStateWithLifecycle()



    val ir = viewModel.ir.collectAsStateWithLifecycle()
    val button = viewModel.button.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
            delay(1000)
            viewModel.discover()
            delay(500)
            viewModel.startNotify()
    }


    Scaffold(
        topBar = { DashboardAppBar(
            onBack = onBack
        ) },
    modifier = modifier.fillMaxSize(),
    ) { innerPadding ->


        if (!isConnected.value) {

            Box(
                modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
            ) {


                IRVisualization(
                    isBlocked = ir.value
                )
                HorizontalDivider(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                )

                Spacer(Modifier.height(40.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp)
                ) {
                    Text("Button state",
                    fontSize = 28.sp
                    )

                    Switch(
                        checked = button.value,
                        onCheckedChange = {},
                        modifier = Modifier.scale(1.5F)
                    )
                }

                Spacer(Modifier.height(40.dp))
                HorizontalDivider(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                )



                Spacer(Modifier.height(40.dp))


             Column(
                 horizontalAlignment = Alignment.Start,
                 modifier = Modifier.fillMaxWidth()
                     .padding(horizontal = 40.dp)
             ) {
                 Text("Device      :   ${if(isConnected.value) "Connected" else "Disconnected"}", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7F))
                 Text("IR sensor  :   ${if(ir.value) "Blocked" else "Unblocked"}", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7F))
                 Text("Button      :   ${if(button.value) "Pressed" else "Released"}", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7F))
             }

                Spacer(Modifier.height(40.dp))

                HorizontalDivider(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                )
            }
        }

    }
}

