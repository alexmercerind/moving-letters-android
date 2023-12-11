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
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration

/**
 * A state object that can be hoisted to control and observe the animated text state.
 * In most cases, this state will be created via [rememberAnimatedTextState].
 */
@OptIn(DelicateCoroutinesApi::class)
class AnimatedTextState() {
    /** Whether animation is currently paused. */
    val paused: StateFlow<Boolean> get() = _paused

    /** Whether animation is currently stopped. */
    val stopped: StateFlow<Boolean> get() = _stopped

    /** Whether animation is currently playing (not paused). */
    val playing get() = paused.map { !it }

    /** Whether animation is currently ongoing (not stopped). */
    val ongoing get() = stopped.map { !it }

    /** Whether this instance is attached to an animated text. */
    internal var attached = false

    /** Whether initial text layout has been done. */
    internal val layout = CompletableDeferred<Any>()

    /** Current visible character index. */
    internal var current by mutableStateOf(-1)

    /** Current animation coroutine job. */
    private var job: Job? = null

    /** Current animation coroutine jobs. */
    private var jobs: MutableSet<Job> = mutableSetOf()

    private var _paused = MutableStateFlow(true)
    private var _stopped = MutableStateFlow(true)

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

    fun start() {
        stop()
        resume()
        _stopped.update { false }
    }

    fun stop() {
        pause()
        current = -1
        for (i in visibility.indices) {
            visibility[i].value = false
        }
        _stopped.update { true }
    }

    fun resume() {
        if (paused.value) {
            job = GlobalScope.launch(Dispatchers.IO) {
                layout.await()
                for (i in current.coerceIn(0, 1 shl 16) until visibility.size) {
                    if (coroutineContext.isActive) {
                        delay(intermediateDuration)
                        visibility[i].value = true
                        jobs.add(
                            async {
                                delay(animationDuration)
                                current = i
                                delay(100)
                                visibility[i].value = false

                                if (current == visibility.size - 1) {
                                    _stopped.update { true }
                                }
                            }
                        )
                    }
                }
            }
            _paused.update { false }
        }
    }

    fun pause() {
        if (!paused.value) {
            job?.cancel()
            job = null
            jobs.forEach { it.cancel() }
            jobs.clear()
            _paused.update { true }
        }
    }
}


/**
 * Creates a [AnimatedTextState] that is remembered across compositions.
 */
@Composable
fun rememberAnimatedTextState() = remember { AnimatedTextState() }
