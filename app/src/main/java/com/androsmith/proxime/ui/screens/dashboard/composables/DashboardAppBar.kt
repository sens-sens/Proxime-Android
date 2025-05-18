package com.androsmith.proxime.ui.screens.dashboard.composables

import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
fun DashboardAppBar(
    onBack: () -> Unit, modifier: Modifier = Modifier
) {

    TopAppBar(
        navigationIcon = {
        IconButton(
            onClick = onBack
        ) {
            Icon(
                painter = painterResource(R.drawable.arrow_back),
                contentDescription = stringResource(R.string.navigate_back),
                modifier = Modifier.size(22.dp)
            )
        }
    }, title = { Text(stringResource(R.string.dashboard)) }, modifier = modifier

    )
}