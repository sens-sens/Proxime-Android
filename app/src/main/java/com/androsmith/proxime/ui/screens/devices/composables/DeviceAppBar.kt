package com.androsmith.proxime.ui.screens.devices.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.androsmith.proxime.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceAppBar(modifier: Modifier = Modifier) {

    TopAppBar(
        navigationIcon = {
            Icon(
                painter = painterResource(R.drawable.menu),
                contentDescription = null,
                modifier = Modifier.size(28.dp)
            )
        },
        title = {
            Text(
                stringResource(R.string.devices), modifier = Modifier.padding(start = 8.dp)
            )
        },
        modifier = modifier.padding(horizontal = 12.dp),
    )
}