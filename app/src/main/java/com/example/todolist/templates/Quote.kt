package com.example.todolist.templates

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todolist.components.Title
import com.example.todolist.viewModel.QuoteViewModel

@Composable
fun QuoteTemplate(viewModel: QuoteViewModel = viewModel ()){
    val quote by viewModel.quote.collectAsState()

    Scaffold { paddingValues ->
        Column (
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Title("Quote of the day")
            Spacer(modifier = Modifier.height(8.dp))
            Text(quote, style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)
        }
    }
}