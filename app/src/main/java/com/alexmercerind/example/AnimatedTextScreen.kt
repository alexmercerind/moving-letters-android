package com.alexmercerind.example

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.alexmercerind.movingletters.AnimatedTextState
import com.alexmercerind.movingletters.rememberAnimatedTextState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedTextScreen(
    name: String,
    content: @Composable (state: AnimatedTextState, text: String) -> Unit,
    contentColor: Color,
    containerColor: Color
) {
    Scaffold(
        contentColor = contentColor,
        containerColor = containerColor
    ) { padding ->
        val text = stringResource(id = R.string.sample_text)

        val scope = rememberCoroutineScope()
        val state = rememberAnimatedTextState()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(32.dp)
        ) {
            item {
                content(state, text)
            }
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
            item {
                IconButton(onClick = { scope.launch { state.start() } }) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = stringResource(id = R.string.restart)
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
            item {
                ElevatedCard(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFFFFF),
                        contentColor = Color(0xFF000000)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                    Divider()
                    LazyRow(contentPadding = PaddingValues(16.dp)) {
                        item {
                            Text(
                                text = listOf(
                                    "$name(",
                                    "  text = \"$text\"",
                                    ")"
                                ).joinToString("\n"),
                                style = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace)
                            )
                        }
                    }
                }
            }
        }
    }
}
