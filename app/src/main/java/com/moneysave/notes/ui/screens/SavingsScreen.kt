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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.moneysave.notes.MoneySaveApplication
import com.moneysave.notes.data.SavingsGoal
import com.moneysave.notes.viewmodel.SavingsViewModel
import com.moneysave.notes.viewmodel.SavingsViewModelFactory

@Composable
fun SavingsScreen(
    paddingValues: PaddingValues
) {
    val context = LocalContext.current
    val application =
        context.applicationContext as MoneySaveApplication

    val viewModel: SavingsViewModel = viewModel(
        factory = SavingsViewModelFactory(
            application.savingsRepository
        )
    )

    val goals by viewModel.goals.collectAsState()

    var showDialog by remember {
        mutableStateOf(false)
    }

    Scaffold(
        modifier = Modifier.padding(paddingValues),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showDialog = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add savings goal"
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
                text = "Savings",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Set goals and track your savings progress",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (goals.isEmpty()) {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No savings goals yet",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Tap + to create your first goal."
                    )
                }

            } else {

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = goals,
                        key = { it.id }
                    ) { goal ->

                        SavingsGoalCard(
                            goal = goal,
                            onDelete = {
                                viewModel.deleteGoal(goal)
                            }
                        )
                    }
                }
            }
        }
    }

    if (showDialog) {
        AddSavingsGoalDialog(
            onDismiss = {
                showDialog = false
            },
            onAdd = { name, target ->
                viewModel.addGoal(
                    SavingsGoal(
                        name = name,
                        targetAmount = target
                    )
                )

                showDialog = false
            }
        )
    }
}

@Composable
private fun SavingsGoalCard(
    goal: SavingsGoal,
    onDelete: () -> Unit
) {
    val progress = if (goal.targetAmount > 0) {
        (goal.currentAmount / goal.targetAmount)
            .coerceIn(0.0, 1.0)
            .toFloat()
    } else {
        0f
    }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = goal.name,
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = "${(progress * 100).toInt()}%"
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Saved: ₹${goal.currentAmount}"
            )

            Text(
                text = "Target: ₹${goal.targetAmount}"
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = onDelete
            ) {
                Text("Delete")
            }
        }
    }
}

@Composable
private fun AddSavingsGoalDialog(
    onDismiss: () -> Unit,
    onAdd: (String, Double) -> Unit
) {
    var name by remember {
        mutableStateOf("")
    }

    var target by remember {
        mutableStateOf("")
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("New Savings Goal")
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                    },
                    label = {
                        Text("Goal name")
                    },
                    singleLine = true
                )

                OutlinedTextField(
                    value = target,
                    onValueChange = {
                        target = it
                    },
                    label = {
                        Text("Target amount")
                    },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amount = target.toDoubleOrNull()

                    if (name.isNotBlank() && amount != null && amount > 0) {
                        onAdd(name.trim(), amount)
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    )
}
