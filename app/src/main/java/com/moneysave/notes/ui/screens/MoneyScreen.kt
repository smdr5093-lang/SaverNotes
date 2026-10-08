package com.moneysave.notes.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.moneysave.notes.data.Transaction
import com.moneysave.notes.viewmodel.TransactionViewModel
import com.moneysave.notes.viewmodel.TransactionViewModelFactory
import androidx.compose.ui.platform.LocalContext

@Composable
fun MoneyScreen(
    paddingValues: PaddingValues
) {
    val context = LocalContext.current
    val application = context.applicationContext as com.moneysave.notes.MoneySaveApplication

    val viewModel: TransactionViewModel = viewModel(
        factory = TransactionViewModelFactory(
            application.transactionRepository
        )
    )

    val transactions by viewModel.transactions.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.padding(paddingValues),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.addTransaction(
                        Transaction(
                            amount = 100.0,
                            type = "income",
                            category = "Other",
                            date = System.currentTimeMillis(),
                            paymentMethod = "Cash",
                            note = "Test transaction"
                        )
                    )
                }
            ) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add transaction"
                )
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {

            Text(
                text = "Money",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Track your income and expenses",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (transactions.isEmpty()) {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No transactions yet",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Tap + to add a test transaction."
                    )
                }

            } else {

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(
                        items = transactions,
                        key = { it.id }
                    ) { transaction ->

                        TransactionCard(
                            transaction = transaction,
                            onDelete = {
                                viewModel.deleteTransaction(transaction)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TransactionCard(
    transaction: Transaction,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = transaction.category,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = transaction.note,
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = transaction.paymentMethod,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "₹${transaction.amount}",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Button(
                    onClick = onDelete
                ) {
                    Text("Delete")
                }
            }
        }
    }
}
