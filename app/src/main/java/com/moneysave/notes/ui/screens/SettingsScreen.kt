
package com.moneysave.notes.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(paddingValues: PaddingValues) {
    var darkMode by rememberSaveable { mutableStateOf(false) }
    var currency by rememberSaveable { mutableStateOf("INR (₹)") }
    var showAbout by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text("Settings", style = MaterialTheme.typography.headlineMedium)

        Card(Modifier.fillMaxWidth()) {
            Row(
                Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    Text("Dark Mode", style = MaterialTheme.typography.titleMedium)
                    Text("Dark appearance preference")
                }
                Switch(
                    checked = darkMode,
                    onCheckedChange = { darkMode = it }
                )
            }
        }

        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Currency", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))

                listOf("INR (₹)", "USD ($)", "EUR (€)", "GBP (£)").forEach { option ->
                    Row(
                        Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = currency == option,
                            onClick = { currency = option }
                        )
                        Text(option)
                    }
                }

                Text(
                    "Selected: $currency",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Notifications", style = MaterialTheme.typography.titleMedium)
                Text("Reminder settings will be added in a future update.")
            }
        }

        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Data Backup & Export", style = MaterialTheme.typography.titleMedium)
                Text("Backup and export options will be added in a future update.")
            }
        }

        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Import Data", style = MaterialTheme.typography.titleMedium)
                Text("Import options will be added in a future update.")
            }
        }

        OutlinedButton(
            onClick = { showAbout = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("About MoneySave Notes")
        }
    }

    if (showAbout) {
        AlertDialog(
            onDismissRequest = { showAbout = false },
            title = { Text("MoneySave Notes") },
            text = {
                Text(
                    "Track Money. Save Better. Stay Organized.\n\n" +
                    "An offline-first app for tracking transactions, " +
                    "savings goals and personal notes."
                )
            },
            confirmButton = {
                TextButton(onClick = { showAbout = false }) {
                    Text("Close")
                }
            }
        )
    }
}
