package com.if1001.cin.dage.model

import android.graphics.PointF

data class Workout(var locationName: String) {
    public lateinit var routes: List<PointF>
}