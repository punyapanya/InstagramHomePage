package com.example.instagrampage.data.local.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Post(
    val authorName: String,
    val title: String,
    val likes: Int,
    val comments: Int,
    val avatar: String,
    val picture: String,

    @PrimaryKey(autoGenerate = true)
    var postId: Long = 0L
)
