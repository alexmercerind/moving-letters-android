package com.alexmercerind.movingletters

import androidx.compose.animation.core.Easing
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.text.TextStyle
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration

/**
 * A state object that can be hoisted to control and observe the animated text state.
 * In most cases, this state will be created via [rememberAnimatedTextState].
 */
class AnimatedTextState() {
    /** Whether this instance is attached to an animated text. */
    internal var attached = false

    /** Whether initial text layout has been done. */
    internal val layout = CompletableDeferred<Any>()

    private var _visibility: List<MutableState<Boolean>>? = null
    private var _transformOrigin: MutableList<TransformOrigin>? = null

    private var _style: TextStyle? = null
    private var _easing: Easing? = null
    private var _animationDuration: Duration? = null
    private var _intermediateDuration: Duration? = null

    internal var visibility: List<MutableState<Boolean>>
        get() = _visibility!!
        set(value) {
            _visibility = value
        }
    internal var transformOrigin: MutableList<TransformOrigin>
        get() = _transformOrigin!!
        set(value) {
            _transformOrigin = value
        }
    internal var style: TextStyle
        get() = _style!!
        set(value) {
            _style = value
        }
    internal var easing: Easing
        get() = _easing!!
        set(value) {
            _easing = value
        }
    internal var animationDuration: Duration
        get() = _animationDuration!!
        set(value) {
            _animationDuration = value
        }
    internal var intermediateDuration: Duration
        get() = _intermediateDuration!!
        set(value) {
            _intermediateDuration = value
        }

    internal var current by mutableStateOf(-1)

    suspend fun reset() {
        layout.await()
        current = -1
        for (i in visibility.indices) {
            visibility[i].value = false
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun start() {
        layout.await()
        reset()
        for (i in visibility.indices) {
            delay(intermediateDuration)
            visibility[i].value = true
            GlobalScope.launch(Dispatchers.IO) {
                delay(animationDuration)
                current = i
                delay(100)
                visibility[i].value = false
            }
        }
    }
}


/**
 * Creates a [AnimatedTextState] that is remembered across compositions.
 */
@Composable
fun rememberAnimatedTextState() = remember { AnimatedTextState() }
