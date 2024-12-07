package com.example.bookshelf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.bookshelf.ui.theme.BookshelfTheme
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.bookshelf.data.Book
import com.example.bookshelf.ui.BookshelfUiState
import com.example.bookshelf.ui.BookshelfViewModel
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BookshelfTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BookshelfApp()
                }
            }
        }
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BookshelfApp(viewModel: BookshelfViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    var isAnimating by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = isAnimating) {
        delay(3000)
        isAnimating = !isAnimating
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1A237E),
                        Color(0xFF3949AB)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                "Bookshelf",
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            SearchBar(viewModel)

            Spacer(modifier = Modifier.height(16.dp))

            AnimatedContent(
                targetState = uiState,
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) + slideInVertically(animationSpec = tween(300)) togetherWith
                    fadeOut(animationSpec = tween(300)) + slideOutVertically(animationSpec = tween(300))
                }
            ) { state ->
                when (state) {
                    is BookshelfUiState.Initial -> IdleAnimation(isAnimating)
                    is BookshelfUiState.Loading -> LoadingAnimation()
                    is BookshelfUiState.Success -> BookList(books = state.books)
                    is BookshelfUiState.Empty -> EmptyStateAnimation()
                    is BookshelfUiState.Error -> ErrorAnimation(state.message)
                }
            }
            
            // Remove this Spacer and the second AnimatedContent block
            // Spacer(modifier = Modifier.height(16.dp))
            // 
            // AnimatedContent(
            //     targetState = uiState,
            //     transitionSpec = {
            //         fadeIn(animationSpec = tween(300)) with
            //         fadeOut(animationSpec = tween(300))
            //     }
            // ) { state ->
            //     ...
            // }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(viewModel: BookshelfViewModel) {
    OutlinedTextField(
        value = viewModel.searchQuery,
        onValueChange = { viewModel.onSearchQueryChange(it) },
        label = { Text("Search books", color = Color.White) },
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp)),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.White.copy(alpha = 0.7f),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
        trailingIcon = {
            IconButton(onClick = { viewModel.searchBooks() }) {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White)
            }
        }
    )
}

@Composable
fun IdleAnimation(isAnimating: Boolean) {
    val scale by animateFloatAsState(
        targetValue = if (isAnimating) 1.2f else 1f,
        animationSpec = tween(durationMillis = 1000)
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "Enter a search query",
            style = MaterialTheme.typography.headlineMedium.copy(color = Color.White),
            modifier = Modifier.scale(scale)
        )
    }
}

@Composable
fun LoadingAnimation() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Color.White)
    }
}

@Composable
fun EmptyStateAnimation() {
    var isAnimating by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = isAnimating) {
        delay(1000)
        isAnimating = !isAnimating
    }

    val alpha by animateFloatAsState(
        targetValue = if (isAnimating) 1f else 0.5f,
        animationSpec = tween(durationMillis = 1000)
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "No books found",
            style = MaterialTheme.typography.headlineMedium.copy(color = Color.White),
            modifier = Modifier.alpha(alpha)
        )
    }
}

@Composable
fun ErrorAnimation(message: String) {
    var isAnimating by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = isAnimating) {
        delay(500)
        isAnimating = !isAnimating
    }

    val offset by animateDpAsState(
        targetValue = if (isAnimating) 0.dp else 10.dp,
        animationSpec = tween(durationMillis = 300)
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "Error: $message",
            style = MaterialTheme.typography.headlineMedium.copy(color = Color.Red),
            modifier = Modifier.offset(x = offset)
        )
    }
}

// Update your existing BookList and BookItem composables to match the new style
@Composable
fun BookList(books: List<Book>) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(books) { book ->
            BookItem(book)
        }
    }
}

@Composable
fun BookItem(book: Book) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = book.volumeInfo.imageLinks?.thumbnail,
                contentDescription = book.volumeInfo.title,
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(4.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = book.volumeInfo.title,
                maxLines = 2,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
        }
    }
}