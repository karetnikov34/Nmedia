package ru.netology.nmedia.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import ru.netology.nmedia.dto.Post
import java.text.SimpleDateFormat
import java.util.Calendar

class PostDaoImpl(private val db: SQLiteDatabase) : PostDao {
    companion object {
        val DDL = """
        CREATE TABLE ${PostColumns.TABLE} (
            ${PostColumns.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${PostColumns.COLUMN_AUTHOR} TEXT NOT NULL,
            ${PostColumns.COLUMN_CONTENT} TEXT NOT NULL,
            ${PostColumns.COLUMN_PUBLISHED_DATE} TEXT NOT NULL,
            ${PostColumns.COLUMN_LIKED_BY_ME} BOOLEAN NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_LIKES_COUNT} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_SHARE_COUNT} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_VIEW_COUNT} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_VIDEO} TEXT
        );
        """.trimIndent()
    }

    object PostColumns {
        const val TABLE = "posts"
        const val COLUMN_ID = "id"
        const val COLUMN_AUTHOR = "author"
        const val COLUMN_CONTENT = "content"
        const val COLUMN_PUBLISHED_DATE = "publishedDate"
        const val COLUMN_LIKED_BY_ME = "likedByMe"
        const val COLUMN_LIKES_COUNT = "likesCount"
        const val COLUMN_SHARE_COUNT = "shareCount"
        const val COLUMN_VIEW_COUNT = "viewCount"
        const val COLUMN_VIDEO = "video"
        val ALL_COLUMNS = arrayOf(
            COLUMN_ID,
            COLUMN_AUTHOR,
            COLUMN_CONTENT,
            COLUMN_PUBLISHED_DATE,
            COLUMN_LIKED_BY_ME,
            COLUMN_LIKES_COUNT,
            COLUMN_SHARE_COUNT,
            COLUMN_VIEW_COUNT,
            COLUMN_VIDEO
        )
    }

    override fun getAll(): List<Post> {
        val posts = mutableListOf<Post>()
        db.query(
            PostColumns.TABLE,
            PostColumns.ALL_COLUMNS,
            null,
            null,
            null,
            null,
            "${PostColumns.COLUMN_ID} DESC"
        ).use {
            while (it.moveToNext()) {
                posts.add(map(it))
            }
        }
        return posts
    }

    override fun save(post: Post): Post {
        val values = ContentValues().apply {
            // TODO: remove hardcoded values
            put(PostColumns.COLUMN_AUTHOR, "Me")
            put(PostColumns.COLUMN_CONTENT, post.content)
            put(
                PostColumns.COLUMN_PUBLISHED_DATE,
                SimpleDateFormat("EEE, d MMM yyyy, HH:mm").format(
                    Calendar.getInstance().time
                )
            )
        }
        val id = if (post.id != 0L) {
            db.update(
                PostColumns.TABLE,
                values,
                "${PostColumns.COLUMN_ID} = ?",
                arrayOf(post.id.toString()),
            )
            post.id
        } else {
            db.insert(PostColumns.TABLE, null, values)
        }
        db.query(
            PostColumns.TABLE,
            PostColumns.ALL_COLUMNS,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null,
        ).use {
            it.moveToNext()
            return map(it)
        }
    }

    override fun likeById(id: Long) {
        db.execSQL(
            """
           UPDATE posts SET
               likesCount = likesCount + CASE WHEN likedByMe THEN -1 ELSE 1 END,
               likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
           WHERE id = ?;
        """.trimIndent(), arrayOf(id)
        )
    }

    override fun shareById(id: Long) {
        db.execSQL(
            """
                UPDATE posts SET
                shareCount = shareCount + 1
                 WHERE id = ?;
                 """.trimIndent(), arrayOf(id)
        )
    }

    override fun removeById(id: Long) {
        db.delete(
            PostColumns.TABLE,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }

    private fun map(cursor: Cursor): Post {
        with(cursor) {
            return Post(
                id = getLong(getColumnIndexOrThrow(PostColumns.COLUMN_ID)),
                author = getString(getColumnIndexOrThrow(PostColumns.COLUMN_AUTHOR)),
                content = getString(getColumnIndexOrThrow(PostColumns.COLUMN_CONTENT)),
                publishedDate = getString(getColumnIndexOrThrow(PostColumns.COLUMN_PUBLISHED_DATE)),
                likedByMe = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_LIKED_BY_ME)) != 0,
                likesCount = getLong(getColumnIndexOrThrow(PostColumns.COLUMN_LIKES_COUNT)),
                shareCount = getLong(getColumnIndexOrThrow(PostColumns.COLUMN_SHARE_COUNT)),
                viewCount = getLong(getColumnIndexOrThrow(PostColumns.COLUMN_VIEW_COUNT)),
            )
        }
    }
}