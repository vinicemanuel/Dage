package com.if1001.cin.dage

import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream

/**
 * Definições básicas reutilizáveis por diversas classes
 */

fun Double.format(digits: Int) = java.lang.String.format("%.${digits}f", this)

val HOME_FRAGMENT_TAG = "homeTag"
val PAST_WORKOUTS_FRAGMENT_TAG = "pastWorkoutsTag"
val MAP_PLAY_LIST_FRAGMENT_TAG = "mapPlayListTag"
val PLAYING_SONG_FRAGMENT_TAG = "playingSongTag"

val SPOTIFY_JSON_KEY_DISPLAY_NAME = "display_name"
val SPOTIFY_JSON_KEY_EMAIL = "email"
val SPOTIFY_JSON_KEY_IMAGES = "images"
val SPOTIFY_JSON_KEY_IMAGE_URL = "url"
val SPOTIFY_JSON_KEY_ID = "id"

val REQUEST_ID_MULTIPLE_PERMISSIONS = 1

/**
 * Transfoma Bitmap em String Base64
 */
fun BitMapToString(bitmap: Bitmap): String {
    val baos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
    val b = baos.toByteArray()
    return Base64.encodeToString(b, Base64.DEFAULT)
}