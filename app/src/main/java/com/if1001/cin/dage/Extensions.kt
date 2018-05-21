package com.if1001.cin.dage

fun Double.format(digits: Int) = java.lang.String.format("%.${digits}f", this)