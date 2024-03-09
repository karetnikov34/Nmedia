package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.repository.PostRepository
import ru.netology.nmedia.dto.repository.PostRepositoryImpl
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.util.SingleLiveEvent

private val empty = Post(
    0,
    "",
    "",
    "",
    "",
    false,
    0,
    null
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryImpl()
    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data
    val edited = MutableLiveData(empty)

    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    private val _requestCode = MutableLiveData<Int>()
    val requestCode: LiveData<Int> = _requestCode

    init {
        loadPosts()
    }

    fun loadPosts() {
        _data.value = FeedModel(loading = true)
        repository.getAllAsync(object : PostRepository.GetCallback<List<Post>> {
            override fun onSuccess(posts: List<Post>) {
                _data.value = FeedModel(posts = posts, empty = posts.isEmpty())
            }

            override fun onError(e: Exception, code: Int) {
                _data.value = FeedModel(error = true)
                if (code != 0) _requestCode.value = code
            }
        })
    }

    fun save() {
        edited.value?.let {
            repository.saveAsync(it, object : PostRepository.GetCallback<Unit> {
                override fun onSuccess(posts: Unit) {
                    _postCreated.value = Unit
                }

                override fun onError(e: Exception, code: Int) {
                    _data.value = FeedModel(error = true)
                    if (code != 0) _requestCode.value = code
                }
            }
            )
            edited.value = empty
        }
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String) {
        edited.value?.let { post ->
            val text = content.trim()
            if (text != post.content) {
                edited.value = post.copy(content = text)
            }
        }
    }

    fun likeById(post: Post) {
        if (post.likedByMe) {
            repository.unlikeByIdAsync(post.id, object : PostRepository.GetCallback<Post> {
                override fun onSuccess(posts: Post) {
                    _data.value =
                        FeedModel(
                            posts = _data.value?.posts.orEmpty()
                                .map {
                                    if (it.id == post.id) it.copy(
                                        likedByMe = posts.likedByMe,
                                        likes = posts.likes
                                    ) else it
                                })
                }

                override fun onError(e: Exception, code: Int) {
                    _data.value = FeedModel(error = true)
                    if (code != 0) _requestCode.value = code
                }

            })
        } else {
            repository.likeByIdAsync(post.id, object : PostRepository.GetCallback<Post> {
                override fun onSuccess(posts: Post) {
                    _data.value =
                        FeedModel(
                            posts = _data.value?.posts.orEmpty()
                                .map {
                                    if (it.id == post.id) it.copy(
                                        likedByMe = posts.likedByMe,
                                        likes = posts.likes
                                    ) else it
                                })
                }

                override fun onError(e: Exception, code: Int) {
                    _data.value = FeedModel(error = true)
                    if (code != 0) _requestCode.value = code
                }

            })
        }
    }

    fun removeById(id: Long) {
        repository.removeByIdAsync(id, object : PostRepository.GetCallback<Unit> {
            override fun onSuccess(posts: Unit) {
                _data.value =
                    _data.value?.copy(posts = _data.value?.posts.orEmpty()
                        .filter { it.id != id }
                    )
            }

            override fun onError(e: Exception, code: Int) {
                _data.value = FeedModel(error = true)
                if (code != 0) _requestCode.value = code
            }
        })
    }
}