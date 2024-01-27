package ru.netology.nmedia.dto.repository

import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun getAllAsync(callback: GetCallback<List<Post>>)
    fun likeByIdAsync(id: Long, callback: GetCallback <Post>)
    fun unlikeByIdAsync(id: Long, callback: GetCallback <Post>)
    fun removeByIdAsync(id: Long, callback: GetCallback<Unit>)
    fun saveAsync(post: Post, callback: GetCallback<Unit>)
    fun getByIdAsync(id: Long, callback: GetCallback <Post>)

    interface GetCallback <T> {
        fun onSuccess(posts: T)
        fun onError(e: Exception)
    }
}