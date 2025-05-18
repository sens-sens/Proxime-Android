package com.androsmith.proxime.ui.screens.dashboard.composables

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.androsmith.proxime.R

@Composable
fun ProximityContainer(
    isBlocked: Boolean,
    modifier: Modifier = Modifier
) {


    val offset = animateDpAsState(
        targetValue = if (isBlocked) 60.dp else 260.dp,
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(360.dp),
        contentAlignment = Alignment.Center
    ){
//        Text("IR Sensor",
//            fontSize = 24.sp,
//            modifier = Modifier
//                .align(
//                    alignment = Alignment.TopCenter
//                )
//                .padding(40.dp)
//        )
        Image(
            painter = painterResource(R.drawable.ir),
            contentDescription = "IR sensor",
            modifier = Modifier
                .size(220.dp)
                .padding(end = 90.dp)
                .rotate(90F)
        )

        Image(
            painter = painterResource(R.drawable.hand),
            contentDescription = "Hand",
            modifier = Modifier
                .size(360.dp)
                .offset(x = offset.value, y = -10.dp)
                .rotate(-90F)
        )
    }
}