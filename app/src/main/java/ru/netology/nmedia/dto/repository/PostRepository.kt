package ru.netology.nmedia.dto.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun getAll(): List<Post>
    fun likeById(id: Long)
    fun unlikeById(id: Long)
    fun removeById(id: Long)
    fun save(post: Post)
    fun getById(id: Long): Post

}