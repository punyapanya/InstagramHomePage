package com.example.instagrampage.ui.viewmodels

import androidx.lifecycle.*
import com.example.instagrampage.data.local.db.entities.Post
import com.example.instagrampage.data.local.db.entities.Story
import com.example.instagrampage.data.repository.Repository
import com.example.instagrampage.util.Result
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: Repository
) : ViewModel() {

    private val TAG = "HomeViewModel"

    private val _posts = MutableLiveData<Result<List<Post>>>()
    val posts: LiveData<Result<List<Post>>>
        get() = _posts
    private var postsJob: Job? = null

    private val _stories = MutableLiveData<Result<List<Story>>>()
    val stories: LiveData<Result<List<Story>>>
        get() = _stories
    private var storiesJob: Job? = null

    private var currentPage: Int = 0

    init {
        collectStories()
        collectPosts()
    }

    fun collectStories() {
        storiesJob?.cancel()
        storiesJob = viewModelScope.launch {
            repository.getStories().collect { _stories.value = it }
        }
    }

    fun collectPosts() {
        currentPage++
        postsJob?.cancel()
        postsJob = viewModelScope.launch {
            delay(500)
            repository.getPosts(currentPage * POSTS_PER_PAGE).collect { _posts.value = it }
        }
    }

    fun insertStory(story: Story) {
        viewModelScope.launch {
            repository.insertStory(story)
        }
    }

    companion object {
        const val POSTS_PER_PAGE = 10
    }
}