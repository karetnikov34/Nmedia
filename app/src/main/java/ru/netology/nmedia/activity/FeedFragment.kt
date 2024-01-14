package ru.netology.nmedia.activity

import android.content.Intent
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
import ru.netology.nmedia.activity.SinglePostFragment.Companion.longArg
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
                viewModel.likeById(post.id)
            }

            override fun onShare(post: Post) {
                viewModel.shareById(post.id)

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

            override fun onPlayVideo(post: Post) {
                val parsedUrl = Uri.parse(post.video)
                val intent = Intent().apply {
                    action = Intent.ACTION_VIEW;
                    putExtra(Intent.ACTION_VIEW, parsedUrl)
                    type = "text/plain"
                }
                val onVideoClickIntent = Intent.createChooser(intent, "Play")

                val activityThatShouldBeRun =
                    intent.resolveActivity(requireActivity().packageManager)
                val listOfAvailableActivities: List<ResolveInfo> =
                    requireActivity().packageManager.queryIntentActivities(intent, 0)
                if ((activityThatShouldBeRun != null) || (!listOfAvailableActivities.isEmpty())) {
                    startActivity(onVideoClickIntent)
                } else {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video));
                    startActivity(browserIntent);
                }
            }

            override fun onViewPost(post: Post) {
                viewModel.selectPost(post)
                findNavController().navigate(R.id.action_feedFragment_to_singlePostFragment, Bundle().apply{longArg = post.id})
            }

        }
        )

        binding.list.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val newPost = adapter.currentList.size < posts.size
            adapter.submitList(posts) {
                if (newPost) {
                    binding.list.smoothScrollToPosition(0)
                }
            }
        }

        binding.newPostButton.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }
        return binding.root
    }
}