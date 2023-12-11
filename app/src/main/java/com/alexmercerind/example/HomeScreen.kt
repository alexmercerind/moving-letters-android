package com.alexmercerind.example

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    effects: Map<Destinations, Pair<Color, Color>>
) {

    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        state = topAppBarState,
        snapAnimationSpec = null
    )
    Scaffold(
        containerColor = Color.Black,
        topBar = {
            LargeTopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White,
                    scrolledContainerColor = Color.Black,
                    actionIconContentColor = Color.White
                ),
                actions = {
                    val context = LocalContext.current
                    IconButton(
                        onClick = {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://github.com/alexmercerind/moving-letters-android")
                            )
                            context.startActivity(intent)
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.github),
                            contentDescription = stringResource(id = R.string.github)
                        )
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            val list = effects.entries.withIndex().toList()
            for (i in list) {
                item {
                    ListItem(
                        modifier = Modifier.clickable {
                            navController.navigate(list[i.index].value.key.value)
                        },
                        leadingContent = {
                            Surface(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .clipToBounds(),
                                contentColor = Color.Black
                            ) {
                                Text(
                                    modifier = Modifier.wrapContentSize(Alignment.Center),
                                    text = (i.index + 1).toString()
                                )
                            }
                        },
                        headlineContent = {
                            Text(text = stringResource(R.string.effect, i.index + 1))
                        },
                        colors = ListItemDefaults.colors(
                            headlineColor = list[i.index].value.value.first,
                            containerColor = list[i.index].value.value.second,
                        )
                    )
                }
            }
        }
    }
}
