package com.example.instagrampage.ui.viewmodels

import androidx.lifecycle.*
import com.example.instagrampage.data.local.db.entities.Story
import com.example.instagrampage.data.repository.Repository
import com.example.instagrampage.ui.adapters.PostAdapter
import com.example.instagrampage.util.Result
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
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
        collectData()
    }

    fun collectData() {
        currentPage++
        itemsJob?.cancel()

        itemsJob = viewModelScope.launch {
            combine(repository.getStories(), repository.getPosts(currentPage * POSTS_PER_PAGE)) { stories, posts ->
                val storiesToShow: PostAdapter.Item.HeaderItem = when (stories.status) {
                    Result.Status.LOADING -> PostAdapter.Item.HeaderItem(listOf())
                    Result.Status.SUCCESS -> PostAdapter.Item.HeaderItem(stories.data!!)
                    Result.Status.ERROR -> PostAdapter.Item.HeaderItem(listOf())
                }
                val postsToShow: List<PostAdapter.Item.PostItem> = when (posts.status) {
                    Result.Status.LOADING -> listOf()
                    Result.Status.SUCCESS -> posts.data!!.map { PostAdapter.Item.PostItem(it) }
                    Result.Status.ERROR -> listOf()
                }
                val itemsToShow: List<PostAdapter.Item> = mutableListOf<PostAdapter.Item>().apply {
                    add(storiesToShow)
                    addAll(postsToShow)
                    if (postsToShow.isNotEmpty()) add(PostAdapter.Item.FooterItem)
                }
                itemsToShow
            }.filter {
                it.size > 1
            }.collect {
                _items.value = it
            }
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