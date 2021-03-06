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
import javax.inject.Inject

class HomeFragment : Fragment() {

  private val TAG = "HomeFragment"

  private var _bnd: FragmentHomeBinding? = null
  private val bnd: FragmentHomeBinding
    get() = _bnd!!

  @Inject
  lateinit var repository: Repository
  private lateinit var viewModel: HomeViewModel
  private lateinit var rvAdapter: PostAdapter

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
    viewModel.items.observe(viewLifecycleOwner) { items ->
      Log.d(TAG, "setupObservers: items submitting ${items.size}")
      rvAdapter.submitList(items) {
        if (bnd.rvHome.adapter == null) bnd.rvHome.adapter = rvAdapter
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
    rvAdapter.onFooterAttachedCallback = { viewModel.collectItems() }
    rvAdapter.onStoryClickedCallback = { viewModel.insertStory(it) }
    rvAdapter.onPostLikedCallback = { viewModel.insertPost(it) }
  }

  private fun setupViewModel() {
    val viewModelFactory = HomeViewModelFactory(repository)
    viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
  }

  private fun inject() {
    val app = requireActivity().application as MyApplication
    app.appComponent.inject(this)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    bnd.rvHome.adapter = null
    _bnd = null
  }
}