package com.alexmercerind.movingletters

import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import kotlinx.coroutines.Dispatchers
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.DurationUnit

@Composable
fun RotateAnimatedText(
    modifier: Modifier = Modifier,
    state: AnimatedTextState? = null,
    text: String,
    style: TextStyle? = null,
    easing: Easing = EaseOut,
    animationDuration: Duration = 300.milliseconds,
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

    LaunchedEffect("RotateAnimatedText", Dispatchers.IO) {
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
                    val x = it.multiParagraph.getBoundingBox(offset).left
                    var y = 0.0F
                    val line = it.multiParagraph.getLineForOffset(offset)
                    for (i in 0..line) {
                        y += it.multiParagraph.getLineHeight(i)
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
                    addStyle(
                        currentStyle.copy(color = Color.Transparent).toSpanStyle(),
                        currentState.current + 1,
                        text.length
                    )
                    append(text)
                },
                style = currentStyle,
            )
        }
        for (i in text.indices) {
            if (currentState.visibility[i].value) {
                RotatedContent(
                    animationSpec = animationSpec,
                    transformOrigin = currentState.transformOrigin[i]
                ) {
                    Text(
                        text = buildAnnotatedString {
                            addStyle(
                                currentStyle.toSpanStyle().copy(color = Color.Transparent),
                                0,
                                i
                            )
                            addStyle(
                                currentStyle.toSpanStyle().copy(color = Color.Transparent),
                                i + 1,
                                text.length
                            )
                            append(text)
                        },
                        style = currentStyle,
                    )
                }
            }
        }
    }
}

@Composable
fun RotatedContent(
    animationSpec: FiniteAnimationSpec<Float>,
    transformOrigin: TransformOrigin,
    content: @Composable () -> Unit
) {

    // animate*AsState(targetValue = *)
    // updateTransition(*)

    var reset by remember { mutableStateOf(false) }

    val transition = updateTransition(reset, label = "RotatedContent::transition")
    
    val alpha by transition.animateFloat(
        label = "RotatedContent::alpha",
        transitionSpec = { animationSpec },
        targetValueByState = {
            if (it) 1.0F else 0.0F
        }
    )
    val rotationZ by transition.animateFloat(
        label = "RotatedContent::rotationZ",
        transitionSpec = { animationSpec },
        targetValueByState = {
            if (it) 0.0F else 45.0F
        }
    )

    LaunchedEffect("RotatedContent::LaunchedEffect", Dispatchers.IO) {
        reset = true
    }

    Box(
        modifier = Modifier
            .alpha(alpha)
            // The .rotate modifier does not allow transform origin.
            .graphicsLayer(
                rotationZ = rotationZ,
                transformOrigin = transformOrigin
            )
    ) {
        content()
    }
}
