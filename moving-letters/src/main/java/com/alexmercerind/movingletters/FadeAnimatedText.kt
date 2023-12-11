package com.alexmercerind.movingletters

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import kotlinx.coroutines.Dispatchers
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.DurationUnit

@Composable
fun FadeAnimatedText(
    modifier: Modifier = Modifier,
    state: AnimatedTextState? = null,
    text: String,
    style: TextStyle? = null,
    easing: Easing = EaseInOut,
    animationDuration: Duration = 200.milliseconds,
    intermediateDuration: Duration = 50.milliseconds,
    animateOnMount: Boolean = true
) {
    val animationSpec: FiniteAnimationSpec<Float> =
        tween(animationDuration.toDouble(DurationUnit.MILLISECONDS).toInt(), 0, easing)

    val currentStyle = style ?: LocalTextStyle.current
    val currentState = state ?: rememberAnimatedTextState()
    if (!currentState.attached) {
        currentState.attached = true

        currentState.visibility = text.map { mutableStateOf(false) }
        currentState.lineHeight = text.map { 0.0F }.toMutableList()
        currentState.transformOrigin = text.map { TransformOrigin.Center }.toMutableList()

        currentState.animationDuration = animationDuration
        currentState.intermediateDuration = intermediateDuration
    }

    LaunchedEffect("FadeAnimatedText", Dispatchers.IO) {
        if (animateOnMount) {
            currentState.start()
        }
    }

    Box(modifier = modifier.clipToBounds()) {
        Text(
            text = text,
            style = currentStyle.copy(color = Color.Transparent),
            onTextLayout = {
                for (offset in text.indices) {
                    val x = it.multiParagraph.getBoundingBox(offset).width / 2 + it.multiParagraph.getHorizontalPosition(offset, true)
                    var y = 0.0F
                    val line = it.multiParagraph.getLineForOffset(offset)
                    for (i in 0..line) {
                        y += if (i == line) it.multiParagraph.getLineHeight(i) / 2 else it.multiParagraph.getLineHeight(i)
                    }
                    currentState.transformOrigin[offset] = TransformOrigin(
                        x / it.multiParagraph.width,
                        y / it.multiParagraph.height
                    )
                    currentState.lineHeight[offset] = it.multiParagraph.getLineHeight(line)
                }
                currentState.layout.complete(Any())
            }
        )
        if (currentState.current >= 0) {
            Text(
                text = buildAnnotatedString {
                    addStyle(currentStyle.toSpanStyle(), 0, currentState.current)
                    addStyle(currentStyle.copy(color = Color.Transparent).toSpanStyle(), currentState.current + 1, text.length)
                    append(text)
                },
                style = currentStyle,
            )
        }
        for (i in text.indices) {
            AnimatedVisibility(
                visible = currentState.visibility[i].value,
                enter = fadeIn(animationSpec = animationSpec),
                exit = fadeOut(animationSpec = tween(0))
            ) {
                Text(
                    text = buildAnnotatedString {
                        addStyle(currentStyle.toSpanStyle().copy(color = Color.Transparent), 0, i)
                        addStyle(currentStyle.toSpanStyle().copy(color = Color.Transparent), i + 1, text.length)
                        append(text)
                    },
                    style = currentStyle,
                )
            }
        }
    }
}
