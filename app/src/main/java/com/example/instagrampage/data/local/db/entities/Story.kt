package com.example.instagrampage.data.local.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Story(
    val authorName: String,
    var isWatched: Boolean,
    val avatar: String,

    @PrimaryKey(autoGenerate = true)
    var storyId: Long = 0L
)
