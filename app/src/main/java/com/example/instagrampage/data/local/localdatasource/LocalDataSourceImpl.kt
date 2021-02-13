package com.example.instagrampage.data.local.localdatasource

import com.example.instagrampage.data.local.db.daos.ContentDao
import com.example.instagrampage.data.local.db.entities.Post
import com.example.instagrampage.data.local.db.entities.Story
import com.example.instagrampage.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class LocalDataSourceImpl(
    private val contentDao: ContentDao
) : LocalDataSource {

    override fun getPosts(amount: Int): Flow<Result<List<Post>>> {
        return contentDao.getPosts(amount).map { Result.success<List<Post>>(it) }
    }

    override fun getStories(): Flow<Result<List<Story>>> {
        return contentDao.getStories().map { Result.success<List<Story>>(it) }
    }

    override suspend fun insertPosts(posts: List<Post>) {
        withContext(Dispatchers.IO) {
            contentDao.insertPosts(posts)
        }
    }

    override suspend fun insertStories(stories: List<Story>) {
        withContext(Dispatchers.IO) {
            contentDao.insertStories(stories)
        }
    }

    override suspend fun insertPost(post: Post) {
        withContext(Dispatchers.IO) {
            contentDao.insertPost(post)
        }
    }

    override suspend fun insertStory(story: Story) {
        withContext(Dispatchers.IO) {
            contentDao.insertStory(story)
        }
    }

    override suspend fun deletePosts() {
        withContext(Dispatchers.IO) {
            contentDao.deletePosts()
        }
    }

    override suspend fun deleteStories() {
        withContext(Dispatchers.IO) {
            contentDao.deleteStories()
        }
    }
}