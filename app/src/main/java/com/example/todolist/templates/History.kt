package com.example.todolist.templates

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todolist.components.ExpandableListItem
import com.example.todolist.components.Subtitle
import com.example.todolist.components.Title
import com.example.todolist.viewModel.DBViewModel

@Composable
fun TemplateHistory(dbViewModel: DBViewModel = viewModel()){
    val groupedItems by dbViewModel.getItemsGroupedByDay().collectAsState(initial = emptyMap())

    val expandedStates = remember { mutableStateListOf<Boolean>() }
    val listState = rememberLazyListState()

    LaunchedEffect(groupedItems) {
        if (expandedStates.size != groupedItems.size) {
            expandedStates.clear()
            expandedStates.addAll(List(groupedItems.size) { false })
        }
    }

    Scaffold  { paddingValues ->
        LazyColumn (
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp),
            state = listState
        ) {
            item{
                Title("History")
            }

            item{
                Subtitle("Past to do lists")
            }

            itemsIndexed(groupedItems.toList(), key = { index, _ -> index }) { index, (date, grouped) ->
                ExpandableListItem(
                    items = grouped,
                    title = date,
                    isExpanded = expandedStates.getOrElse(index) { false },
                    onExpandedChange = { expandedStates[index] = it }
                )
            }
        }
    }

}