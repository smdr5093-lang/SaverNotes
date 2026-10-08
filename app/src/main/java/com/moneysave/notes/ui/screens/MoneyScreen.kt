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
fun MoneyScreen(
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
            text = "Money",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Track your income and expenses",
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = "No transactions yet",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "Your income and expense transactions will appear here.",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
