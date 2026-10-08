package com.moneysave.notes.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NotesScreen(
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Notes",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Keep your personal notes organized",
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = "No notes yet",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "Tap + to create your first note.",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
