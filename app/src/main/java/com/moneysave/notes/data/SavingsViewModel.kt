package com.moneysave.notes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moneysave.notes.data.SavingsGoal
import com.moneysave.notes.data.SavingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SavingsViewModel(
    private val repository: SavingsRepository
) : ViewModel() {

    val goals: StateFlow<List<SavingsGoal>> =
        repository.allGoals.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addGoal(goal: SavingsGoal) {
        viewModelScope.launch {
            repository.insert(goal)
        }
    }

    fun updateGoal(goal: SavingsGoal) {
        viewModelScope.launch {
            repository.update(goal)
        }
    }

    fun deleteGoal(goal: SavingsGoal) {
        viewModelScope.launch {
            repository.delete(goal)
        }
    }
}
