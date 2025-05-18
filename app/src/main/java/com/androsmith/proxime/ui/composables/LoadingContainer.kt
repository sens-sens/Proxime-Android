package com.androsmith.proxime.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.androsmith.proxime.R

@Composable
fun LoadingContainer(
    message: String,
    modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize(),
    ) {
        CircularProgressIndicator()
        Spacer(Modifier.height(28.dp))
        Text(message)
    }
}

@Preview(showBackground = true)
@Composable
private fun LoadingContainerPreview() {
    LoadingContainer(
        message = stringResource(R.string.loading)
    )

}