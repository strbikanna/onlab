package com.example.languagelearningapp.ui.screens.homescreen.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun HomeHeader(
    @DrawableRes drawable: Int,
    @StringRes title: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(drawable),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = modifier
        )
        Text(
            text = stringResource(title),
            style = MaterialTheme.typography.h1,
            modifier = Modifier
                .padding(20.dp)
                .align(Alignment.BottomStart)
        )
    }
}