package com.jeklov.superfinancer.ui.screens.feed

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jeklov.post.data.db.PostDB
import com.jeklov.post.data.models.Post
import com.jeklov.post.data.view.model.PostViewModel
import com.jeklov.post.data.view.model.PostViewModelProviderFactory
import com.jeklov.superfinancer.MainActivity

@SuppressLint("MutableCollectionMutableState")
@Composable
fun PostCreateScreen(
    availableTags: List<String>,
    context: MainActivity
) {
    val database = PostDB.getInstance(context = context)
    val viewModel: PostViewModel = viewModel(factory = PostViewModelProviderFactory(database))

    var postText by remember { mutableStateOf("") }
    var selectedTags by remember { mutableStateOf(mutableSetOf<String>()) }
    var attachedPhotos by remember { mutableStateOf<List<Uri>>(emptyList()) }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = postText,
            onValueChange = { postText = it },
            label = { Text("Введите текст поста") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Кнопка для прикрепления фото
        Button(onClick = { /* Логика выбора фото */ }) {
            Text("Прикрепить фото")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Выбор тегов
        TagSelector(
            availableTags = availableTags,
            selectedTags = selectedTags,
            onTagSelected = { tag ->
                if (selectedTags.contains(tag)) {
                    selectedTags.remove(tag)
                } else {
                    selectedTags.add(tag)
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (postText.isNotBlank() && selectedTags.isNotEmpty()) {
                    viewModel.addPost(Post(text = postText, tags = selectedTags.toList(), photos = attachedPhotos))
                }
            },
            enabled = postText.isNotBlank() && selectedTags.isNotEmpty()
        ) {
            Text("Создать пост")
        }
    }
}

@Composable
fun TagSelector(
    availableTags: List<String>,
    selectedTags: Set<String>,
    onTagSelected: (String) -> Unit
) {
    LazyColumn {
        items(availableTags) { tag ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onTagSelected(tag) }
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = selectedTags.contains(tag),
                    onCheckedChange = { onTagSelected(tag) }
                )
                Text(text = tag, modifier = Modifier.padding(start = 8.dp))
            }
        }
    }
}