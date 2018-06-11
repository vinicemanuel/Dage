package com.if1001.cin.dage.model

import android.arch.persistence.room.Entity

@Entity
data class PlayList(var PlayListName: String, var id: String) {
}