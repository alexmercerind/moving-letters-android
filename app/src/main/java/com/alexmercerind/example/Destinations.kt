package com.alexmercerind.example

interface Destinations {
    val value: String

    object Home: Destinations {
        override val value = "Home"
    }

    object Effect1: Destinations {
        override val value = "Effect1"
    }

    object Effect2: Destinations {
        override val value = "Effect2"
    }

    object Effect3: Destinations {
        override val value = "Effect3"
    }

    object Effect4: Destinations {
        override val value = "Effect4"
    }
}
