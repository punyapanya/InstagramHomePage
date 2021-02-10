package com.example.instagrampage.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instagrampage.MyApplication
import com.example.instagrampage.R
import com.example.instagrampage.data.repository.Repository
import com.example.instagrampage.databinding.FragmentHomeBinding
import com.example.instagrampage.ui.MainActivity
import com.example.instagrampage.ui.adapters.PostAdapter
import com.example.instagrampage.ui.viewmodels.HomeViewModel
import com.example.instagrampage.ui.viewmodels.HomeViewModelFactory
import com.example.instagrampage.util.Result
import javax.inject.Inject

class HomeFragment : Fragment() {

    private val TAG = "HomeFragment"

    private var _bnd: FragmentHomeBinding? = null
    private val bnd: FragmentHomeBinding
        get() = _bnd!!

    @Inject lateinit var repository: Repository
    private lateinit var viewModel: HomeViewModel
    private lateinit var rvAdapter: PostAdapter
    private var mStories = PostAdapter.Item.HeaderItem(listOf())
    private var mPosts = listOf<PostAdapter.Item.PostItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bnd = FragmentHomeBinding.inflate(inflater, container, false)
        return bnd.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inject()
        setupViewModel()
        setupAdapters()
        setupClickListeners()
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.stories.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> Log.d(TAG, "setupObservers: Loading state of stories")
                is Result.Success -> {
                    Log.d(TAG, "setupObservers: Success state of stories ${result.data.size}")
                    mStories = PostAdapter.Item.HeaderItem(result.data)
                    val items = when (rvAdapter.currentList.size) {
                        0, 1 -> createListOfItems(mStories, listOf(), false)
                        else -> createListOfItems(mStories, mPosts, true)
                    }
                    rvAdapter.submitList(items)
                }
                is Result.Error -> Log.d(TAG, "setupObservers: Error state of stories - ${result.message}")
            }
        }
        viewModel.posts.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> Log.d(TAG, "setupObservers: Loading state of posts")
                is Result.Success -> {
                    Log.d(TAG, "setupObservers: Success state of posts ${result.data.size}")
                    mPosts = result.data.map { PostAdapter.Item.PostItem(it) }
                    val items = when (rvAdapter.currentList.size) {
                        0 -> createListOfItems(PostAdapter.Item.HeaderItem(listOf()), mPosts, true)
                        else -> createListOfItems(mStories, mPosts, true)
                    }
                    rvAdapter.submitList(items) {
                        if (bnd.rvHome.adapter == null) bnd.rvHome.adapter = rvAdapter
                    }
                }
                is Result.Error -> Log.d(TAG, "setupObservers: Error state of posts - ${result.message}")
            }
        }
    }

    private fun setupClickListeners() {
        (requireActivity() as MainActivity).toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.itemDirect -> {
                    findNavController().navigate(R.id.action_homeFragment_to_commentsFragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun setupAdapters() {
        bnd.rvHome.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        bnd.rvHome.itemAnimator = object : DefaultItemAnimator() {
            override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder): Boolean =
                if (viewHolder is PostAdapter.HeaderViewHolder) true
                else super.canReuseUpdatedViewHolder(viewHolder)
        }
        rvAdapter = PostAdapter()
        rvAdapter.setHasStableIds(true)
        rvAdapter.onFooterAttachedCallback = { viewModel.collectPosts() }
        rvAdapter.onStoryClickedCallback = { viewModel.insertStory(it) }
    }

    private fun setupViewModel() {
        val viewModelFactory = HomeViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
    }

    private fun inject() {
        val app = requireActivity().application as MyApplication
        app.appComponent.inject(this)
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: ")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "onSaveInstanceState: ")
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        bnd.rvHome.adapter = null
        _bnd = null
        Log.d(TAG, "onDestroyView: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
    }

    private fun createListOfItems(
        storiesHeader: PostAdapter.Item.HeaderItem,
        posts: List<PostAdapter.Item.PostItem>,
        isFooter: Boolean
    ): List<PostAdapter.Item> = mutableListOf<PostAdapter.Item>().apply {
        add(storiesHeader)
        addAll(posts)
        if (isFooter) add(PostAdapter.Item.FooterItem)
    }
}