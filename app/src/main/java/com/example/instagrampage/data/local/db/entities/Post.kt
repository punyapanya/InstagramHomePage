package com.example.instagrampage.data.local.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Post(
    val authorName: String,
    val title: String,
    var likes: Int,
    val comments: Int,
    val avatar: String,
    val picture: String,
    var isLiked: Boolean,

    @PrimaryKey(autoGenerate = true)
    var postId: Long = 0L
)
