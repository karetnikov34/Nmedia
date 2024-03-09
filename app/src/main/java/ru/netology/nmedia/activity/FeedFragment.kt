package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel


class FeedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(layoutInflater, container, false)

        val viewModel by activityViewModels<PostViewModel>()

        val adapter = PostsAdapter(object : OnInteractionListener {
            override fun onLike(post: Post) {
                viewModel.likeById(post)
            }

            override fun onShare(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }

                val chooser = Intent.createChooser(intent, null)
                startActivity(chooser)
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)
                findNavController().navigate(R.id.action_feedFragment_to_newPostFragment, Bundle().apply{textArg = post.content})
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }
        }
        )

        binding.list.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { state ->
            adapter.submitList(state.posts)
            binding.progress.isVisible = state.loading
            binding.errorGroup.isVisible = state.error
            binding.emptyText.isVisible = state.empty
        }

        viewModel.requestCode.observe(viewLifecycleOwner) { requestCode ->
            responseAnswer(requestCode)
        }

        binding.retryButton.setOnClickListener {
            viewModel.loadPosts()
        }

        binding.newPostButton.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadPosts()
            binding.swipeRefresh.isRefreshing = false
        }

        return binding.root
    }

    fun responseAnswer(code: Int) = when (code) {
        in 100..199 -> Toast.makeText(
            activity,
            "Informational response $code",
            Toast.LENGTH_LONG
        ).show()

        in 300..399 -> Toast.makeText(
            activity,
            "Redirection message $code",
            Toast.LENGTH_LONG
        ).show()

        in 400..499 -> Toast.makeText(
            activity,
            "Client error response $code",
            Toast.LENGTH_LONG
        ).show()

        in 500..599 -> Toast.makeText(
            activity,
            "Server error response $code",
            Toast.LENGTH_LONG
        ).show()

        else -> Toast.makeText(activity, "Non-standard response $code", Toast.LENGTH_LONG)
            .show()
    }
}
