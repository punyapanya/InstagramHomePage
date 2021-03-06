package com.example.instagrampage.data.repository

import com.example.instagrampage.data.local.db.entities.Post
import com.example.instagrampage.data.local.db.entities.Story
import com.example.instagrampage.data.local.localdatasource.LocalDataSource
import com.example.instagrampage.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class RepositoryImpl(
    private val localDataSource: LocalDataSource
) : Repository {

  override fun getPosts(amount: Int): Flow<Result<List<Post>>> = flow {
    emit(Result.loading<List<Post>>())
    emitAll(localDataSource.getPosts(amount))
  }

  override fun getStories(): Flow<Result<List<Story>>> = flow {
    emit(Result.loading<List<Story>>())
    emitAll(localDataSource.getStories())
  }

  override suspend fun insertPosts(posts: List<Post>) {
    localDataSource.insertPosts(posts)
  }

  override suspend fun insertStories(stories: List<Story>) {
    localDataSource.insertStories(stories)
  }

  override suspend fun insertPost(post: Post) {
    localDataSource.insertPost(post)
  }

  override suspend fun insertStory(story: Story) {
    localDataSource.insertStory(story)
  }

  override suspend fun deletePosts() {
    localDataSource.deletePosts()
  }

  override suspend fun deleteStories() {
    localDataSource.deleteStories()
  }
}