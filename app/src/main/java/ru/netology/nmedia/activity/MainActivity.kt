package ru.netology.nmedia.activity

import android.content.Intent
import android.content.pm.ResolveInfo
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel by viewModels<PostViewModel>()

        val newPostContract = registerForActivityResult(NewPostActivityContract()) { result ->
            if (result == null) {
                viewModel.editCancel()
                return@registerForActivityResult
            }
            viewModel.changeContent(result)
            viewModel.save()
        }

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
                newPostContract.launch(post.content)
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

                val activityThatShouldBeRun = intent.resolveActivity(packageManager)
                val listOfAvailableActivities: List<ResolveInfo> =
                    packageManager.queryIntentActivities(intent, 0)
                if ((activityThatShouldBeRun != null) || (!listOfAvailableActivities.isEmpty())) {
                    startActivity(onVideoClickIntent)
                } else {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video));
                    startActivity(browserIntent);
                }
            }

        }
        )

        binding.list.adapter = adapter

        viewModel.data.observe(this) { posts ->
            val newPost = adapter.currentList.size < posts.size
            adapter.submitList(posts) {
                if (newPost) {
                    binding.list.smoothScrollToPosition(0)
                }
            }
        }

        binding.newPostButton.setOnClickListener {
            newPostContract.launch("")
        }
    }
}