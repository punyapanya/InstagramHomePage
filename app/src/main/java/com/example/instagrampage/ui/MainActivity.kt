package com.example.instagrampage.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.example.instagrampage.MyApplication
import com.example.instagrampage.R
import com.example.instagrampage.data.local.db.entities.Post
import com.example.instagrampage.data.local.db.entities.Story
import com.example.instagrampage.data.repository.Repository
import com.example.instagrampage.databinding.ActivityMainBinding
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var bnd: ActivityMainBinding

    @Inject lateinit var repository: Repository

    val toolbar: MaterialToolbar
        get() = bnd.toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bnd = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bnd.root)

        inject()
        setupTestDatabase()

        findNavController(R.id.fragment).addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> {
                    bnd.toolbar.menu.clear()
                    bnd.toolbar.navigationIcon = null
                    bnd.toolbar.setTitle(R.string.instagram)
                    bnd.toolbar.inflateMenu(R.menu.menu_home)
                }
                R.id.commentsFragment -> {
                    bnd.toolbar.menu.clear()
                    bnd.toolbar.setNavigationIcon(R.drawable.ic_back)
                    bnd.toolbar.setTitle(R.string.comments)
                    bnd.toolbar.inflateMenu(R.menu.menu_comments)
                    bnd.toolbar.setNavigationOnClickListener { onBackPressed() }
                }
            }
        }
    }

    private fun setupTestDatabase() {
        runBlocking {
            repository.deletePosts()
            repository.deleteStories()

            val post = Post("jennie_bp_kkkk", "I did a lot of things today", 65984, 1230, "", "")
            val posts = mutableListOf<Post>()
            for (i in 1..100) {
                posts.add(post.copy(authorName = "jennie_bp_kkkk$i"))
            }
            repository.insertPosts(posts)

            val story = Story("jennie", false, "")
            val stories = mutableListOf<Story>()
            for (i in 1..20) {
                stories.add(story.copy(authorName = "jennie$i"))
            }
            repository.insertStories(stories)
        }
    }

    private fun inject() {
        val app = application as MyApplication
        app.appComponent.inject(this)
    }
}