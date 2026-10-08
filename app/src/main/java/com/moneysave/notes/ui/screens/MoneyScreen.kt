
package com.moneysave.notes.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.moneysave.notes.MoneySaveApplication
import com.moneysave.notes.data.Transaction
import com.moneysave.notes.viewmodel.TransactionViewModel
import com.moneysave.notes.viewmodel.TransactionViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MoneyScreen(paddingValues: PaddingValues) {
    val context = LocalContext.current
    val app = context.applicationContext as MoneySaveApplication

    val vm: TransactionViewModel = viewModel(
        factory = TransactionViewModelFactory(app.transactionRepository)
    )
    val transactions by vm.transactions.collectAsState()

    var showForm by remember { mutableStateOf(false) }
    var type by remember { mutableStateOf("expense") }
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Food") }
    var payment by remember { mutableStateOf("Cash") }
    var note by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    val categories = if (type == "income") {
        listOf("Salary", "Pocket Money", "Business", "Gift", "Other")
    } else {
        listOf("Food", "Shopping", "Travel", "Bills", "Education",
            "Entertainment", "Health", "Other")
    }

    Scaffold(
        modifier = Modifier.padding(paddingValues),
        floatingActionButton = {
            FloatingActionButton(onClick = {
                amount = ""
                note = ""
                error = ""
                showForm = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add transaction")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding).padding(16.dp)
        ) {
            Text("Money Tracker", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(6.dp))
            Text("Track your income and expenses")
            Spacer(Modifier.height(16.dp))

            if (transactions.isEmpty()) {
                Text("Abhi koi transaction nahi hai. + dabakar add karo.")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(transactions, key = { it.id }) { item ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(item.category,
                                        style = MaterialTheme.typography.titleMedium)
                                    Text(item.note.ifBlank { item.paymentMethod })
                                    Text(
                                        SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                                            .format(Date(item.date)),
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        (if (item.type == "income") "+" else "-") +
                                            "₹${"%.2f".format(item.amount)}"
                                    )
                                    TextButton(onClick = { vm.deleteTransaction(item) }) {
                                        Text("Delete")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showForm) {
        AlertDialog(
            onDismissRequest = { showForm = false },
            title = { Text("Add Transaction") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = type == "income",
                            onClick = {
                                type = "income"
                                category = "Salary"
                            },
                            label = { Text("Income") }
                        )
                        FilterChip(
                            selected = type == "expense",
                            onClick = {
                                type = "expense"
                                category = "Food"
                            },
                            label = { Text("Expense") }
                        )
                    }

                    OutlinedTextField(
                        value = amount,
                        onValueChange = { amount = it },
                        label = { Text("Amount (₹)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("Category")
                    categories.forEach { option ->
                        if (option == category) {
                            AssistChip(
                                onClick = { category = option },
                                label = { Text("✓ $option") }
                            )
                        } else {
                            SuggestionChip(
                                onClick = { category = option },
                                label = { Text(option) }
                            )
                        }
                    }

                    Text("Payment method")
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("Cash", "UPI", "Card").forEach { method ->
                            FilterChip(
                                selected = payment == method,
                                onClick = { payment = method },
                                label = { Text(method) }
                            )
                        }
                    }

                    OutlinedTextField(
                        value = note,
                        onValueChange = { note = it },
                        label = { Text("Note (optional)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (error.isNotBlank()) {
                        Text(error, color = MaterialTheme.colorScheme.error)
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    val value = amount.toDoubleOrNull()
                    if (value == null || !value.isFinite() || value <= 0) {
                        error = "Sahi amount enter karo."
                    } else {
                        vm.addTransaction(
                            Transaction(
                                amount = value,
                                type = type,
                                category = category,
                                date = System.currentTimeMillis(),
                                paymentMethod = payment,
                                note = note.trim()
                            )
                        )
                        showForm = false
                    }
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showForm = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
