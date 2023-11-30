package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val publishedDate: String,
    val likedByMe: Boolean = false,
    val likesCount: Long = 0,
    val shareCount: Long = 0,
    val viewCount: Long = 0,
    val video: String = ""
)
