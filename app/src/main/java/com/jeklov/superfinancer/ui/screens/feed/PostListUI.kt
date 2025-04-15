package com.jeklov.superfinancer.ui.screens.feed

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.jeklov.post.data.db.PostDB
import com.jeklov.post.data.models.Post
import com.jeklov.post.data.view.model.PostViewModel
import com.jeklov.post.data.view.model.PostViewModelProviderFactory
import com.jeklov.post.ui.screens.PostScreens
import com.jeklov.superfinancer.MainActivity
import com.jeklov.superfinancer.ui.screens.Screens

@Composable
fun PostListScreenUI(
    paddingValues: PaddingValues,
    navigationController: NavHostController,
    context: MainActivity
) {
    val database = PostDB.getInstance(context = context)
    val viewModel: PostViewModel = viewModel(factory = PostViewModelProviderFactory(database))

    val posts by viewModel.posts.observeAsState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = paddingValues.calculateTopPadding(), bottom = paddingValues.calculateBottomPadding())
    ) {
        Column {
            Button(onClick = { navigationController.navigate(PostScreens.PostCreate) }) {
                Text("Создать пост")
            }
            if (posts != null) {
                LazyColumn {
                    items(posts!!) { post ->
                        PostItem(post = post, viewModel, navigationController)
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostItem(post: Post, viewModel: PostViewModel, navigationController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .combinedClickable(
                onClick = { navigationController.navigate(Screens.WebNews.screen + "/" + post.url) },
                onLongClick = { viewModel.toggleFavorite(post) }
            )
    ) {
        Text(text = post.text, maxLines = 3, overflow = TextOverflow.Ellipsis)

        if (post.photos.isNotEmpty()) {
            LazyRow {
                items(post.photos) { photoUri ->
                    Image(
                        painter = rememberAsyncImagePainter(photoUri),
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .padding(4.dp)
                    )
                }
            }
        }

        // Отображение тегов
        Row {
            post.tags.forEach { tag ->
                Text(text = "#$tag ", modifier = Modifier.padding(end = 4.dp))
            }
        }
    }
}