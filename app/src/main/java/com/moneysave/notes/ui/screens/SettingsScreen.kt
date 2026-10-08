package com.moneysave.notes.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(
    paddingValues: PaddingValues
) {
    var darkMode by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Dark Mode",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "Use a dark appearance"
                )
            }

            Switch(
                checked = darkMode,
                onCheckedChange = { darkMode = it }
            )
        }

        Text(
            text = "Currency: ₹ INR",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "Notifications",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "Data Backup & Export",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "Import Data",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "About MoneySave Notes",
            style = MaterialTheme.typography.titleMedium
        )
    }
}
