package ru.netology.nmedia.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
import ru.netology.nmedia.databinding.ActivityAppBinding

class AppActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.action != Intent.ACTION_SEND) return

        val text = intent.getStringExtra(Intent.EXTRA_TEXT)

        if (text.isNullOrBlank()) {
            Snackbar.make(binding.root, R.string.error_empty_content, Snackbar.LENGTH_SHORT).show()
            return
        }
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()

        val navhostfragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        val navController = navhostfragment.navController
        navController.navigate(R.id.action_feedFragment_to_newPostFragment, Bundle().apply {textArg = text})
    }
}