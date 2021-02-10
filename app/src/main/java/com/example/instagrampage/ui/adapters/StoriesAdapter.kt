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
        val viewHolder = StoryViewHolder.from(parent, onStoryClickedCallback)
        viewHolder.itemView.setOnClickListener {
            viewHolder.onStoryClicked(getItem(viewHolder.adapterPosition))
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) = holder.bind(getItem(position))

    //VIEW HOLDERS
    class StoryViewHolder(
        private val bnd: ItemStoriesBinding,
        private val onStoryClickedCallback: (story: Story) -> Unit
    ) : RecyclerView.ViewHolder(bnd.root) {

        fun bind(story: Story) {
            bnd.apply {
                tvName.text = story.authorName
                val strokeColor = if (story.isWatched) R.color.gray_app else R.color.pink
                ivAvatar.setStrokeColorResource(strokeColor)
            }
        }

        fun onStoryClicked(story: Story) {
            onStoryClickedCallback(story.copy(isWatched = true))
        }

        companion object {
            fun from(parent: ViewGroup, onStoryClickedCallback: (Story) -> Unit) = StoryViewHolder(
                ItemStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onStoryClickedCallback
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