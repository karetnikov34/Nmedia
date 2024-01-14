package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import java.text.DecimalFormat
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow


interface OnInteractionListener {
    fun onLike(post: Post)
    fun onShare(post: Post)
    fun onEdit(post: Post)
    fun onRemove(post: Post)
    fun onPlayVideo(post: Post)
    fun onViewPost(post: Post)
}

class PostsAdapter(
    private val onInteractionListener: OnInteractionListener
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(view, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }

}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        with(binding) {
            author.text = post.author
            content.text = post.content
            published.text = post.publishedDate
            likes.isChecked = post.likedByMe
            likes.text = compactDecimalFormat(post.likesCount)
            share.text = compactDecimalFormat(post.shareCount)
            viewCount.text = compactDecimalFormat(post.viewCount)
            likes.setOnClickListener {
                onInteractionListener.onLike(post)
            }
            share.setOnClickListener {
                onInteractionListener.onShare(post)
            }
            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.edit -> {
                                onInteractionListener.onEdit(post)
                                true
                            }

                            R.id.remove -> {
                                onInteractionListener.onRemove(post)
                                true
                            }

                            else -> false
                        }
                    }
                }.show()
            }

            if (post.video != "") {
                binding.video.setImageResource(R.drawable.blender)
                videoButton.setImageResource(R.drawable.ic_play_circle_outline_48)
            } else {
                binding.video.visibility = View.GONE
                binding.videoButton.visibility = View.GONE
                binding.video.setImageResource(0)
                videoButton.setImageResource(0)
            }
            video.setOnClickListener {
                onInteractionListener.onPlayVideo(post)
            }

            root.setOnClickListener {
                onInteractionListener.onViewPost(post)
            }
        }
    }
}

object PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Post, newItem: Post) = oldItem == newItem
}

private fun compactDecimalFormat(number: Long): String {
    val suffix = charArrayOf(' ', 'K', 'M', 'B', 'T', 'P', 'E')
    val value = log10(number.toDouble()).toInt()
    val base = value / 3
    return if (value == 4 || value == 5) {
        (number / 10.0.pow(base * 3).toLong()).toString() + suffix[base]
    } else if (value >= 3) {
        DecimalFormat("#.#").format(
            floor(number / (10.0.pow(base * 3)) * 10) / 10
        ) + suffix[base]
    } else {
        number.toString()
    }
}