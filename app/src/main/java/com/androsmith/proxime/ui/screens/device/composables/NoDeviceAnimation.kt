package com.androsmith.proxime.ui.screens.device.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.androsmith.proxime.R

@Composable
fun NoDeviceAnimation(modifier: Modifier = Modifier) {
    val lottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            R.raw.no_device
        )
    )

    val progress by animateLottieCompositionAsState(
        lottieComposition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true
    )


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
    ) {
        LottieAnimation(
            composition = lottieComposition,
            progress = { progress },
            modifier = Modifier
                .size(240.dp)
        )
        Text("No nearby devices available",
        fontSize = 20.sp,
        )
        Spacer(
            Modifier.height(12.dp)
        )
        Text(
            "Make sure your Bluetooth is on and try again.",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(
                alpha = 0.8F
            )
            )
    }
}

