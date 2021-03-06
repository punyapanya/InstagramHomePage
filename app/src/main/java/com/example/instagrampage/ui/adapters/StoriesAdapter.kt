package com.example.instagrampage.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.instagrampage.R
import com.example.instagrampage.data.local.db.entities.Story
import com.example.instagrampage.databinding.ItemStoriesBinding

class StoriesAdapter : ListAdapter<Story, StoriesAdapter.StoryViewHolder>(DIFF_CALLBACK) {

  //CALLBACKS
  var onStoryClickedCallback: (Story) -> Unit = { /* no-op */ }

  //ADAPTER BEHAVIOUR OVERRIDDEN
  override fun getItemId(position: Int) = getItem(position).storyId

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
    val storyViewHolder = StoryViewHolder.createFrom(parent)
    storyViewHolder.itemView.setOnClickListener {
      val story = getItem(storyViewHolder.adapterPosition)
      if (!story.isWatched) onStoryClickedCallback(story.copy(isWatched = true))
    }
    return storyViewHolder
  }

  override fun onBindViewHolder(holder: StoryViewHolder, position: Int) = holder.bind(getItem(position))

  //VIEW HOLDERS
  class StoryViewHolder(
      private val bnd: ItemStoriesBinding
  ) : RecyclerView.ViewHolder(bnd.root) {

    fun bind(story: Story) {
      bnd.apply {
        tvName.text = story.authorName
        val strokeColor = if (story.isWatched) R.color.gray_app else R.color.pink
        ivAvatar.setStrokeColorResource(strokeColor)
      }
    }

    companion object {
      fun createFrom(parent: ViewGroup) = StoryViewHolder(
          ItemStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      )
    }
  }

  //DIFF_CALLBACK
  companion object {
    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
      override fun areItemsTheSame(oldItem: Story, newItem: Story) = oldItem.storyId == newItem.storyId
      override fun areContentsTheSame(oldItem: Story, newItem: Story) = oldItem == newItem
    }
  }
}