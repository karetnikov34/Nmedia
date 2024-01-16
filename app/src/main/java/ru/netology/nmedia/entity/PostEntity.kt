package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post

@Entity
data class PostEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val content: String,
    val publishedDate: String,
    val likedByMe: Boolean = false,
    val likesCount: Long = 0,
    val shareCount: Long = 0,
    val viewCount: Long = 0,
    val video: String = ""
) {
    fun toDto(): Post = Post(
        id = id,
        author = author,
        content = content,
        publishedDate = publishedDate,
        likedByMe = likedByMe,
        likesCount = likesCount,
        shareCount = shareCount,
        viewCount = viewCount,
        video = video
    )

    companion object {
        fun fromDto(dto: Post): PostEntity = with(dto) {
            PostEntity(
                id = id,
                author = author,
                content = content,
                publishedDate = publishedDate,
                likedByMe = likedByMe,
                likesCount = likesCount,
                shareCount = shareCount,
                viewCount = viewCount,
                video = video
            )
        }
    }
}