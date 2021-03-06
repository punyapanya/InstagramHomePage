package com.example.instagrampage.data.local.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.instagrampage.data.local.db.entities.Post
import com.example.instagrampage.data.local.db.entities.Story
import kotlinx.coroutines.flow.Flow

@Dao
interface ContentDao {

  @Query("SELECT * FROM Post ORDER BY postId ASC LIMIT :amount")
  fun getPosts(amount: Int): Flow<List<Post>>

  @Query("SELECT * FROM Story")
  fun getStories(): Flow<List<Story>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertPosts(posts: List<Post>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertStories(stories: List<Story>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertPost(post: Post)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertStory(story: Story)

  @Query("DELETE FROM Post")
  suspend fun deletePosts()

  @Query("DELETE FROM Story")
  suspend fun deleteStories()
}