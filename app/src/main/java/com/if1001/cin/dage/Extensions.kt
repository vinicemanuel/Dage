package com.if1001.cin.dage

fun Double.format(digits: Int) = java.lang.String.format("%.${digits}f", this)

val HOME_FRAGMENT_TAG = "homeTag"
val PAST_WORKOUTS_TAG = "pastWorkoutsTag"
val MAP_PLAY_LIST_TAG = "mapPlayListTag"

val SPOTIFY_JSON_KEY_DISPLAY_NAME = "display_name"
val SPOTIFY_JSON_KEY_EMAIL = "email"
val SPOTIFY_JSON_KEY_IMAGES = "images"
val SPOTIFY_JSON_KEY_IMAGE_URL = "url"