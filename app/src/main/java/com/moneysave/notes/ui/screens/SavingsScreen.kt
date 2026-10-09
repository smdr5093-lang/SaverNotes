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
import com.moneysave.notes.data.SavingsGoal
import com.moneysave.notes.viewmodel.SavingsViewModel
import com.moneysave.notes.viewmodel.SavingsViewModelFactory

@Composable
fun SavingsScreen(paddingValues: PaddingValues) {
    val context = LocalContext.current
    val app = context.applicationContext as MoneySaveApplication

    val vm: SavingsViewModel = viewModel(
        factory = SavingsViewModelFactory(app.savingsRepository)
    )
    val goals by vm.goals.collectAsState()

    var showGoalDialog by remember { mutableStateOf(false) }
    var selectedGoal by remember { mutableStateOf<SavingsGoal?>(null) }
    var moneyText by remember { mutableStateOf("") }
    var moneyError by remember { mutableStateOf("") }
    var isWithdraw by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.padding(paddingValues),
        floatingActionButton = {
            FloatingActionButton(onClick = { showGoalDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add goal")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding).padding(16.dp)
        ) {
            Text("My Savings", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(6.dp))
            Text("Your goals, your progress")
            Spacer(Modifier.height(16.dp))

            if (goals.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Abhi goal nahi hai. + dabakar banao.")
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(goals, key = { it.id }) { goal ->
                        val progress = if (goal.targetAmount > 0) {
                            (goal.currentAmount / goal.targetAmount)
                                .coerceIn(0.0, 1.0).toFloat()
                        } else 0f

                        Card(Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(16.dp)) {
                                Row(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(goal.name, style = MaterialTheme.typography.titleLarge)
                                    Text("${(progress * 100).toInt()}%")
                                }

                                Spacer(Modifier.height(10.dp))
                                LinearProgressIndicator(
                                    progress = progress,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(Modifier.height(8.dp))
                                Text("Saved: ₹${"%.2f".format(goal.currentAmount)}")
                                Text("Target: ₹${"%.2f".format(goal.targetAmount)}")
                                Text("Remaining: ₹${"%.2f".format((goal.targetAmount - goal.currentAmount).coerceAtLeast(0.0))}")

                                Spacer(Modifier.height(10.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Button(onClick = {
                                        selectedGoal = goal
                                        isWithdraw = false
                                        moneyText = ""
                                        moneyError = ""
                                    }) {
                                        Text("Add money")
                                    }
                                    OutlinedButton(onClick = {
                                        selectedGoal = goal
                                        isWithdraw = true
                                        moneyText = ""
                                        moneyError = ""
                                    }) {
                                        Text("Withdraw")
                                    }
                                    TextButton(onClick = { vm.deleteGoal(goal) }) {
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

    if (showGoalDialog) {
        AddGoalDialog(
            onDismiss = { showGoalDialog = false },
            onAdd = { name, target ->
                vm.addGoal(SavingsGoal(name = name, targetAmount = target))
                showGoalDialog = false
            }
        )
    }

    val goal = selectedGoal
    if (goal != null) {
        AlertDialog(
            onDismissRequest = { selectedGoal = null },
            title = { Text(if (isWithdraw) "Withdraw money" else "Add savings") },
            text = {
                Column {
                    Text("Goal: ${goal.name}")
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = moneyText,
                        onValueChange = { moneyText = it },
                        label = { Text("Amount (₹)") },
                        singleLine = true
                    )
                    if (moneyError.isNotBlank()) {
                        Text(moneyError, color = MaterialTheme.colorScheme.error)
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    val amount = moneyText.toDoubleOrNull()
                    if (amount == null || !amount.isFinite() || amount <= 0) {
                        moneyError = "Sahi amount enter karo."
                    } else if (isWithdraw && amount > goal.currentAmount) {
                        moneyError = "Itna paisa saved nahi hai."
                    } else {
                        val updated = goal.copy(
                            currentAmount = if (isWithdraw) {
                                goal.currentAmount - amount
                            } else {
                                goal.currentAmount + amount
                            }
                        )
                        vm.updateGoal(updated)
                        selectedGoal = null
                    }
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedGoal = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun AddGoalDialog(
    onDismiss: () -> Unit,
    onAdd: (String, Double) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var target by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Savings Goal") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Goal name") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = target,
                    onValueChange = { target = it },
                    label = { Text("Target amount (₹)") },
                    singleLine = true
                )
                if (error.isNotBlank()) {
                    Text(error, color = MaterialTheme.colorScheme.error)
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val amount = target.toDoubleOrNull()
                if (name.isBlank() || amount == null ||
                    !amount.isFinite() || amount <= 0
                ) {
                    error = "Goal name aur sahi target amount daalo."
                } else {
                    onAdd(name.trim(), amount)
                }
            }) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
