package com.example.languagelearningapp.ui.screens.home_screen.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeHeader(
    @DrawableRes drawable: Int,
    @StringRes title: Int,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
) {
    TopAppBar(
        title = { Text(stringResource(title)) },
        scrollBehavior = scrollBehavior
    )
    /*Box(
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
    }*/
}