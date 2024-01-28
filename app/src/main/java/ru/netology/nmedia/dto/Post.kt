package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    val authorAvatar: String = "",
    val content: String,
    val published: String,
    val likedByMe: Boolean = false,
    val likes: Long = 0,
    var attachment: Attachment? = null
)

enum class AttachmentType {
    IMAGE
}

data class Attachment(
    val url: String,
    val description: String?,
    val type: AttachmentType
)