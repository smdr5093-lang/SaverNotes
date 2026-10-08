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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.moneysave.notes.MoneySaveApplication
import com.moneysave.notes.viewmodel.NoteViewModel
import com.moneysave.notes.viewmodel.NoteViewModelFactory
import com.moneysave.notes.viewmodel.SavingsViewModel
import com.moneysave.notes.viewmodel.SavingsViewModelFactory
import com.moneysave.notes.viewmodel.TransactionViewModel
import com.moneysave.notes.viewmodel.TransactionViewModelFactory

@Composable
fun HomeScreen(
    paddingValues: PaddingValues
) {
    val context = LocalContext.current
    val application =
        context.applicationContext as MoneySaveApplication

    val transactionViewModel: TransactionViewModel = viewModel(
        factory = TransactionViewModelFactory(
            application.transactionRepository
        )
    )

    val savingsViewModel: SavingsViewModel = viewModel(
        factory = SavingsViewModelFactory(
            application.savingsRepository
        )
    )

    val noteViewModel: NoteViewModel = viewModel(
        factory = NoteViewModelFactory(
            application.noteRepository
        )
    )

    val transactions by transactionViewModel.transactions.collectAsState()
    val goals by savingsViewModel.goals.collectAsState()
    val notes by noteViewModel.notes.collectAsState()

    val income = transactions
        .filter { it.type.equals("income", ignoreCase = true) }
        .sumOf { it.amount }

    val expenses = transactions
        .filter { it.type.equals("expense", ignoreCase = true) }
        .sumOf { it.amount }

    val balance = income - expenses

    val saved = goals.sumOf { it.currentAmount }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "MoneySave Notes",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Track Money. Save Better. Stay Organized.",
            style = MaterialTheme.typography.bodyMedium
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Total Balance",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "₹${"%.2f".format(balance)}",
                    style = MaterialTheme.typography.headlineLarge
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SummaryCard(
                title = "Income",
                amount = "₹${"%.2f".format(income)}",
                modifier = Modifier.weight(1f)
            )

            SummaryCard(
                title = "Expenses",
                amount = "₹${"%.2f".format(expenses)}",
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SummaryCard(
                title = "Saved",
                amount = "₹${"%.2f".format(saved)}",
                modifier = Modifier.weight(1f)
            )

            SummaryCard(
                title = "Transactions",
                amount = transactions.size.toString(),
                modifier = Modifier.weight(1f)
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Recent Transactions",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (transactions.isEmpty()) {
                    Text(
                        text = "No transactions yet."
                    )
                } else {
                    transactions
                        .take(5)
                        .forEach { transaction ->

                            Text(
                                text = "${transaction.category} • ₹${"%.2f".format(transaction.amount)}",
                                style = MaterialTheme.typography.bodyLarge
                            )

                            Spacer(
                                modifier = Modifier.height(6.dp)
                            )
                        }
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Active Savings Goals",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (goals.isEmpty()) {
                    Text(
                        text = "No savings goals yet."
                    )
                } else {
                    goals
                        .take(5)
                        .forEach { goal ->

                            Text(
                                text = "${goal.name} • ₹${"%.2f".format(goal.currentAmount)} / ₹${"%.2f".format(goal.targetAmount)}",
                                style = MaterialTheme.typography.bodyLarge
                            )

                            Spacer(
                                modifier = Modifier.height(6.dp)
                            )
                        }
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Recent Notes",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (notes.isEmpty()) {
                    Text(
                        text = "No notes yet."
                    )
                } else {
                    notes
                        .take(5)
                        .forEach { note ->

                            Text(
                                text = note.title,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                text = note.content,
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Spacer(
                                modifier = Modifier.height(8.dp)
                            )
                        }
                }
            }
        }
    }
}

@Composable
private fun SummaryCard(
    title: String,
    amount: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = amount,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}
