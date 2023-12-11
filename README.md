# [moving-letters-android](https://github.com/alexmercerind/moving-letters-android)

#### Text animation library for Android (Jetpack Compose).

## Guide

Following text animations are available:

### ScaleInAnimatedText

<img width="300" src="https://github.com/alexmercerind/moving-letters-android/assets/28951144/ab06f6c0-ceed-4e42-a0ae-cb1b0885a3dd"></img>

### ScaleOutAnimatedText

<img width="300" src="https://github.com/alexmercerind/moving-letters-android/assets/28951144/524aca06-16c1-4e8b-8396-9a6445e7be27"></img>

<br></br>
TODO:
<br></br>

### Programmatic API

You may use `AnimatedTextState` as follows to control the animation programmatically:

```kt
val state = rememberAnimatedTextState()

XYZAnimatedText(
    state = state,
    text = "I like Jetpack Compose"
)

// Start
state.start()
// Stop
state.stop()
// Resume
state.resume()
// Pause
state.pause()
```

### Example Application

You may refer to the [source code of example application](https://github.com/alexmercerind/moving-letters-android/tree/main/app/src/main/java/com/alexmercerind/example) for further details.

## Inspiration

[Moving Letters for Web / JavaScript](https://tobiasahlin.com/moving-letters/) by [@tobiasahlin](https://twitter.com/tobiasahlin).

I wanted to implement it in Jetpack Compose!

## License

Copyright © 2023 & onwards, Hitesh Kumar Saini.

This project & the work under this repository is governed by MIT license that can be found in the [LICENSE](./LICENSE) file.
