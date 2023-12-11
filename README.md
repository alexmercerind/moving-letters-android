# [moving-letters-android](https://github.com/alexmercerind/moving-letters-android)

#### Text animation library for Android (Jetpack Compose).

## Guide

Following text animations are available:

#### ScaleInAnimatedText

<img width="300" src="https://github.com/alexmercerind/moving-letters-android/assets/28951144/4cbc4245-bc86-4279-8c50-f63017196c1d"></img>

#### ScaleOutAnimatedText

<img width="300" src="https://github.com/alexmercerind/moving-letters-android/assets/28951144/eaacb4b2-a826-4d11-b709-6dcb40a9f2bd"></img>

#### FadeAnimatedText

<img width="300" src="https://github.com/alexmercerind/moving-letters-android/assets/28951144/00123137-bc65-4ddc-aca3-425e5a3fce7e"></img>

#### JumpAnimatedText

<img width="300" src="https://github.com/alexmercerind/moving-letters-android/assets/28951144/05ba4ce8-6f79-4378-8701-a6ce48f59531"></img>

#### RotateAnimatedText

<img width="300" src="https://github.com/alexmercerind/moving-letters-android/assets/28951144/79f807c7-b9fa-4f1a-8fee-13fc538cda74"></img>

#### Programmatic API

You may use `AnimatedTextState` as follows to control the animation programmatically:

```kt
val state = rememberAnimatedTextState()

XYZAnimatedText(
    state = state,
    text = "I like Jetpack Compose"
)

state.stop()
state.pause()
state.start()
state.resume()
```

#### Example Application

<img width="300" src="https://github.com/alexmercerind/moving-letters-android/assets/28951144/b9ad1f5a-0546-47e3-89c6-40159dfefc88"></img>

You may download the [APK](https://github.com/alexmercerind/moving-letters-android/files/13639901/app-release.zip) quick trial.

The [source-code of the example application](https://github.com/alexmercerind/moving-letters-android/tree/main/app/src/main/java/com/alexmercerind/example) provides more details.

## Inspiration

[Moving Letters for Web / JavaScript](https://tobiasahlin.com/moving-letters/) by [@tobiasahlin](https://twitter.com/tobiasahlin).

I wanted to implement it in Jetpack Compose!

## License

Copyright © 2023 & onwards, Hitesh Kumar Saini.

This project & the work under this repository is governed by MIT license that can be found in the [LICENSE](./LICENSE) file.
