package com.example.tugasprojekuts.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import com.example.tugasprojekuts.UiState
import com.example.tugasprojekuts.data.Book

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookHomeScreen(
    uiState: UiState,
    onSearch: (String) -> Unit,
    onBookClick: (Book) -> Unit
) {
    var keyword by rememberSaveable { mutableStateOf("indonesia") }

    Scaffold(topBar = { TopAppBar(title = { Text("Jelajah Buku") }) }) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = keyword,
                    onValueChange = { keyword = it },
                    label = { Text("Cari judul atau penulis") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { onSearch(keyword) }),
                    modifier = Modifier.weight(1f)
                )
                Button(onClick = { onSearch(keyword) }) { Text("Cari") }
            }
            when (uiState) {
                UiState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
                is UiState.Error -> Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Gagal memuat buku. Periksa koneksi internet.")
                    Button(onClick = { onSearch(uiState.query) }) { Text("Coba lagi") }
                }
                is UiState.Success -> if (uiState.books.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(if (uiState.query.isEmpty()) "Masukkan kata kunci pencarian." else "Buku tidak ditemukan.")
                    }
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(uiState.books) { book ->
                            BookCard(book = book, onClick = { onBookClick(book) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BookCard(book: Book, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(book.title.orEmpty().ifBlank { "Tanpa judul" }, style = MaterialTheme.typography.titleMedium)
            Text("Penulis: ${book.authorLabel()}", style = MaterialTheme.typography.bodyMedium)
            Text("Terbit pertama: ${book.firstPublishYear?.toString() ?: "Tidak diketahui"}",
                style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(book: Book?, onBack: () -> Unit) {
    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Detail Buku") },
            navigationIcon = { TextButton(onClick = onBack) { Text("Kembali") } }
        )
    }) { padding ->
        if (book == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Data buku tidak tersedia.")
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(book.title.orEmpty().ifBlank { "Tanpa judul" }, style = MaterialTheme.typography.headlineMedium)
                DetailField("Penulis", book.authorLabel())
                DetailField("Tahun terbit pertama", book.firstPublishYear?.toString() ?: "Tidak diketahui")
                DetailField("Jumlah edisi", book.editionCount?.toString() ?: "Tidak diketahui")
                DetailField("Bahasa", book.language.orEmpty().joinToString(", ").ifBlank { "Tidak diketahui" })
            }
        }
    }
}

@Composable
private fun DetailField(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(label, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
}

private fun Book.authorLabel(): String = authorName.orEmpty().joinToString(", ")
    .ifBlank { "Tidak diketahui" }
