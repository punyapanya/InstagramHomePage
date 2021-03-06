package com.example.instagrampage.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagrampage.data.local.db.entities.Post
import com.example.instagrampage.data.local.db.entities.Story
import com.example.instagrampage.data.repository.Repository
import com.example.instagrampage.ui.adapters.PostAdapter
import com.example.instagrampage.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: Repository
) : ViewModel() {

  private val TAG = "HomeViewModel"

  private val _items = MutableLiveData<List<PostAdapter.Item>>()
  val items: LiveData<List<PostAdapter.Item>>
    get() = _items
  private var itemsJob: Job? = null

  private var currentPage: Int = 0

  init {
    collectItems()
  }

  fun collectItems() {
    currentPage++
    itemsJob?.cancel()
    itemsJob = viewModelScope.launch {
      val storiesFlow = repository.getStories().transform { stories ->
        when (stories.status) {
          Result.Status.LOADING -> Log.d(TAG, "collectItems: stories loading")
          Result.Status.SUCCESS -> {
            Log.d(TAG, "collectItems: stories success")
            emit(PostAdapter.Item.Header(stories.data!!))
          }
          Result.Status.ERROR -> Log.d(TAG, "collectItems: stories error")
        }
      }
      val postsFlow = repository.getPosts(currentPage * POSTS_PER_PAGE).transform { posts ->
        when (posts.status) {
          Result.Status.LOADING -> Log.d(TAG, "collectItems: posts loading")
          Result.Status.SUCCESS -> {
            Log.d(TAG, "collectItems: posts success")
            emit(posts.data!!.map { post -> PostAdapter.Item.Post(post) })
          }
          Result.Status.ERROR -> Log.d(TAG, "collectItems: posts error")
        }
      }
      val itemsFlow = combine(storiesFlow, postsFlow) { stories, posts ->
        Log.d(TAG, "collectItems: stories and posts combining")
        val items: List<PostAdapter.Item> = mutableListOf<PostAdapter.Item>().apply {
          add(stories)
          addAll(posts)
          add(PostAdapter.Item.Footer)
        }
        items
      }
      itemsFlow.collect {
        _items.value = it
      }
    }
  }

  fun insertStory(story: Story) {
    viewModelScope.launch(Dispatchers.IO) {
      repository.insertStory(story)
    }
  }

  fun insertPost(post: Post) {
    viewModelScope.launch(Dispatchers.IO) {
      repository.insertPost(post)
    }
  }

  companion object {
    const val POSTS_PER_PAGE = 10
  }
}