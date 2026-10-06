package com.example.tugasprojekuts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tugasprojekuts.data.Book
import com.example.tugasprojekuts.ui.BookDetailScreen
import com.example.tugasprojekuts.ui.BookHomeScreen
import com.example.tugasprojekuts.ui.theme.TugasProjekUtsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TugasProjekUtsTheme {
                BookApp()
            }
        }
    }
}

@Composable
private fun BookApp(bookViewModel: BookViewModel = viewModel()) {
    val navController = rememberNavController()
    val uiState by bookViewModel.uiState.collectAsState()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            BookHomeScreen(
                uiState = uiState,
                onSearch = bookViewModel::search,
                onBookClick = { book ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("book", book)
                    navController.navigate("detail")
                }
            )
        }
        composable("detail") {
            val book = navController.previousBackStackEntry
                ?.savedStateHandle?.get<Book>("book")
            BookDetailScreen(book = book, onBack = { navController.popBackStack() })
        }
    }
}