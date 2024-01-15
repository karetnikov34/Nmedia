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
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostViewHolder
import ru.netology.nmedia.databinding.FragmentSinglePostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.LongArg
import ru.netology.nmedia.viewmodel.PostViewModel

class SinglePostFragment: Fragment() {
    companion object {
        var Bundle.longArg: Long by LongArg
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel by activityViewModels<PostViewModel>()
        val binding = FragmentSinglePostBinding.inflate(layoutInflater, container, false)

        val id = requireArguments().longArg

        viewModel.data.observe(viewLifecycleOwner) {
            val post: Post? = it.find { it.id == id }
            if (post == null) {
                findNavController().navigateUp()
                return@observe
            }
            PostViewHolder(
                binding.includeAlonePost,
                object : OnInteractionListener {
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
                        findNavController().navigate(R.id.action_singlePostFragment_to_newPostFragment, Bundle().apply{textArg = post.content})
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
                        null
                    }
                }
            ).bind(post)
        }
        return binding.root
    }
}