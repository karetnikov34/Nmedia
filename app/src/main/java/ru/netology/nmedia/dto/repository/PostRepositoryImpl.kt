package ru.netology.nmedia.dto.repository

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.netology.nmedia.api.PostsApi
import ru.netology.nmedia.dto.Post

class PostRepositoryImpl : PostRepository {

    override fun getAllAsync(callback: PostRepository.GetCallback<List<Post>>) {
        PostsApi.retrofitService.getAll().enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException(response.message()))
                    return
                }
                callback.onSuccess(response.body() ?: throw RuntimeException("Body is null"))
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                callback.onError(RuntimeException(t))
            }

        })
    }

    override fun likeByIdAsync(id: Long, callback: PostRepository.GetCallback<Post>) {
        PostsApi.retrofitService.likeById(id).enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException(response.message()))
                    return
                }
                callback.onSuccess(response.body() ?: throw RuntimeException("Body is null"))
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                callback.onError(RuntimeException(t))
            }

        })
    }

    override fun unlikeByIdAsync(id: Long, callback: PostRepository.GetCallback<Post>) {
        PostsApi.retrofitService.unlikeById(id).enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException(response.message()))
                    return
                }
                callback.onSuccess(response.body() ?: throw RuntimeException("Body is null"))
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                callback.onError(RuntimeException(t))
            }

        })
    }

    override fun removeByIdAsync(id: Long, callback: PostRepository.GetCallback<Unit>) {
        PostsApi.retrofitService.removeById(id).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException(response.message()))
                    return
                }
                callback.onSuccess(response.body() ?: throw RuntimeException("Body is null"))
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                callback.onError(RuntimeException(t))
            }

        })
    }

    override fun saveAsync(post: Post, callback: PostRepository.GetCallback<Unit>) {
        PostsApi.retrofitService.save(post).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException(response.message()))
                    return
                }
                callback.onSuccess(response.body() ?: throw RuntimeException("Body is null"))
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                callback.onError(RuntimeException(t))
            }

        })
    }

    override fun getByIdAsync(id: Long, callback: PostRepository.GetCallback<Post>) {
        PostsApi.retrofitService.getById(id).enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException(response.message()))
                    return
                }
                callback.onSuccess(response.body() ?: throw RuntimeException("Body is null"))
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                callback.onError(RuntimeException(t))
            }

        })
    }
}