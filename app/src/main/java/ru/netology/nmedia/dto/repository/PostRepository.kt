package ru.netology.nmedia.dto.repository

import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun getAll(): List<Post>
    fun likeById(id: Long): Post
    fun unlikeById(id: Long): Post
    fun removeById(id: Long)
    fun save(post: Post)
    fun getById(id: Long): Post

}