package com.alexmercerind.example

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.alexmercerind.movingletters.AnimatedTextState
import com.alexmercerind.movingletters.rememberAnimatedTextState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay

@Composable
fun AnimatedTextScreen(
    name: String,
    content: @Composable (state: AnimatedTextState, text: String) -> Unit,
    contentColor: Color,
    containerColor: Color
) {
    Scaffold(
        contentColor = contentColor, containerColor = containerColor
    ) { padding ->
        val text = stringResource(id = R.string.text)
        val state = rememberAnimatedTextState()

        LaunchedEffect("AnimatedTextScreen", Dispatchers.IO) {
            delay(200L)
            state.start()
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 32.dp,
                bottom = padding.calculateBottomPadding() + 32.dp,
                end = padding.calculateEndPadding(LocalLayoutDirection.current) + 32.dp,
                start = padding.calculateStartPadding(LocalLayoutDirection.current) + 32.dp
            )
        ) {
            item {
                content(state, text)
            }
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
            item {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = state::start) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = stringResource(id = R.string.start)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = state::stop) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_stop_24),
                            contentDescription = stringResource(id = R.string.stop)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = state::resume) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = stringResource(id = R.string.resume)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = state::pause) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_pause_24),
                            contentDescription = stringResource(id = R.string.pause)
                        )
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
            item {
                ElevatedCard(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFFFFF), contentColor = Color(0xFF000000)
                    ), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_code_24),
                            contentDescription = stringResource(id = R.string.code)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = stringResource(id = R.string.code),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    HorizontalDivider()
                    LazyRow(contentPadding = PaddingValues(16.dp)) {
                        item {
                            Text(
                                text = listOf(
                                    "$name(", "  text = \"$text\"", ")"
                                ).joinToString("\n"),
                                style = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace)
                            )
                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
            item {
                val playing by state.playing.collectAsState(false)
                val ongoing by state.ongoing.collectAsState(false)
                val paused by state.paused.collectAsState(false)
                val stopped by state.stopped.collectAsState(false)
                ElevatedCard(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFFFFF), contentColor = Color(0xFF000000)
                    ), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_shutter_speed_24),
                            contentDescription = stringResource(id = R.string.state)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = stringResource(id = R.string.state),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    HorizontalDivider()
                    mapOf(
                        "PLAYING" to playing,
                        "ONGOING" to ongoing,
                        "PAUSED" to paused,
                        "STOPPED" to stopped
                    ).forEach {
                        Row(
                            modifier = Modifier.padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier.weight(1.0F),
                                text = it.key,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace)
                            )
                            VerticalDivider()
                            Text(
                                modifier = Modifier.weight(1.0F),
                                text = it.value.toString().uppercase(),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace)
                            )
                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
