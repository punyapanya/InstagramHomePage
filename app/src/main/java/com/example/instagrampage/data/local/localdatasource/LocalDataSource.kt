package com.example.instagrampage.data.local.localdatasource

import com.example.instagrampage.data.local.db.entities.Post
import com.example.instagrampage.data.local.db.entities.Story
import com.example.instagrampage.util.Result
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    fun getPosts(amount: Int): Flow<Result<List<Post>>>

    fun getStories(): Flow<Result<List<Story>>>

    suspend fun insertPosts(posts: List<Post>)

    suspend fun insertStories(stories: List<Story>)

    suspend fun insertPost(post: Post)

    suspend fun insertStory(story: Story)

    suspend fun deletePosts()

    suspend fun deleteStories()
}