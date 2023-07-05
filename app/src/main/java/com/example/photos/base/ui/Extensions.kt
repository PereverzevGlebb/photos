package com.example.photos.base.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

inline fun <reified T> Fragment.collect(
    flow: Flow<T>,
    crossinline collector: suspend (T) -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest { data ->
                collector.invoke(data)
            }
        }
    }
}

fun Fragment.sharePhoto(imageUri: Uri) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "image/jpeg"
        putExtra(Intent.EXTRA_STREAM, imageUri)
    }
    startActivity(Intent.createChooser(shareIntent, "Share by"))
}

fun Fragment.shareLink(url: String) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra(
        Intent.EXTRA_TEXT, url
    )
    startActivity(Intent.createChooser(intent, "Share by"))
}

fun Toolbar.setNavigationOnClickListenerWithBackNavigation() {
    setNavigationOnClickListener {
        findNavController().popBackStack()
    }
}