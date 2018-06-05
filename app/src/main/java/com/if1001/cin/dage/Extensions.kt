package com.if1001.cin.dage

fun Double.format(digits: Int) = java.lang.String.format("%.${digits}f", this)

val HOME_FRAGMENT_TAG = "homeTag"
val PAST_WORKOUTS_TAG = "pastWorkoutsTag"
val MAP_PLAY_LIST_TAG = "mapPlayListTag"