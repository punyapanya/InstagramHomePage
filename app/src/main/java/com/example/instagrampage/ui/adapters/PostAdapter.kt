package com.example.instagrampage.ui.adapters

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.instagrampage.R
import com.example.instagrampage.data.local.db.entities.Post
import com.example.instagrampage.data.local.db.entities.Story
import com.example.instagrampage.databinding.ItemFooterPostBinding
import com.example.instagrampage.databinding.ItemHeaderPostBinding
import com.example.instagrampage.databinding.ItemPostBinding

class PostAdapter : ListAdapter<PostAdapter.Item, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    //CALLBACKS
    var onFooterAttachedCallback: () -> Unit = { /* no-op */ }
    var onStoryClickedCallback: (Story) -> Unit = { /* no-op */ }

    //ADAPTER BEHAVIOUR OVERRIDDEN
    override fun getItemId(position: Int) = getItem(position).id

    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is Item.PostItem -> ItemType.POST.ordinal
            is Item.HeaderItem -> ItemType.HEADER.ordinal
            is Item.FooterItem -> ItemType.FOOTER.ordinal
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (ItemType.values()[viewType]) {
            ItemType.POST -> PostViewHolder.createFrom(parent)
            ItemType.HEADER -> HeaderViewHolder.createFrom(parent, onStoryClickedCallback)
            ItemType.FOOTER -> FooterViewHolder.createFrom(parent)
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int): Unit =
        when (val item = getItem(position)) {
            is Item.PostItem -> (holder as PostViewHolder).bind(item.post)
            is Item.HeaderItem -> (holder as HeaderViewHolder).bind(item.stories)
            is Item.FooterItem -> (holder as FooterViewHolder).bind()
        }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        if (holder is FooterViewHolder) onFooterAttachedCallback()
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        if (holder is HeaderViewHolder) holder.saveState()
    }

    //VIEW HOLDERS
    class PostViewHolder(
        private val bnd: ItemPostBinding
    ) : RecyclerView.ViewHolder(bnd.root) {

        fun bind(post: Post) {
            bnd.apply {
                tvName.text = post.authorName
                tvLikes.text = root.resources.getString(R.string.template_likes, post.likes)
                tvTitle.text = HtmlCompat.fromHtml(
                    root.resources.getString(R.string.template_title, post.authorName, post.title),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
                tvShowAllComments.text =
                    root.resources.getString(R.string.template_show_all_comments, post.comments)
            }
        }

        companion object {
            fun createFrom(parent: ViewGroup) = PostViewHolder(
                ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    class HeaderViewHolder(
        private val bnd: ItemHeaderPostBinding
    ) : RecyclerView.ViewHolder(bnd.root) {

        fun bind(stories: List<Story>) {
            val adapter = (bnd.rvStories.adapter as StoriesAdapter?)!!
            adapter.submitList(stories) {
                if (stories.isNotEmpty())
                    savedState?.let {
                        bnd.rvStories.layoutManager!!.onRestoreInstanceState(it)
                        savedState = null
                    }
            }
        }

        fun saveState() {
            savedState = bnd.rvStories.layoutManager!!.onSaveInstanceState()
        }

        companion object {
            fun createFrom(parent: ViewGroup, onStoryClickedCallback: (Story) -> Unit): HeaderViewHolder {
                val bnd = ItemHeaderPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                bnd.rvStories.layoutManager =
                    LinearLayoutManager(bnd.root.context, LinearLayoutManager.HORIZONTAL, false)
                val adapter = StoriesAdapter()
                adapter.setHasStableIds(true)
                adapter.onStoryClickedCallback = onStoryClickedCallback
                bnd.rvStories.adapter = adapter
                return HeaderViewHolder(bnd)
            }

            private var savedState: Parcelable? = null
        }
    }

    class FooterViewHolder(
        private val bnd: ItemFooterPostBinding
    ) : RecyclerView.ViewHolder(bnd.root) {

        fun bind() {
        }

        companion object {
            fun createFrom(parent: ViewGroup) = FooterViewHolder(
                ItemFooterPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    //DIFF_CALLBACK
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item) = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean =
                when (oldItem) {
                    is Item.PostItem -> newItem is Item.PostItem && oldItem.post == newItem.post
                    is Item.HeaderItem -> newItem is Item.HeaderItem && oldItem.stories == newItem.stories
                    is Item.FooterItem -> true
                }
        }
    }

    //SEALED AND ENUM CLASSES
    sealed class Item(val id: Long) {
        class PostItem(val post: Post) : Item(post.postId)
        class HeaderItem(val stories: List<Story>) : Item(Long.MIN_VALUE)
        object FooterItem : Item(Long.MIN_VALUE + 1)
    }

    enum class ItemType { HEADER, POST, FOOTER }
}