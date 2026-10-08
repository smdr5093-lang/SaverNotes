package com.moneysave.notes.data

class SavingsRepository(
    private val savingsGoalDao: SavingsGoalDao
) {
    val allGoals = savingsGoalDao.getAllGoals()

    suspend fun insert(goal: SavingsGoal) {
        savingsGoalDao.insertGoal(goal)
    }

    suspend fun update(goal: SavingsGoal) {
        savingsGoalDao.updateGoal(goal)
    }

    suspend fun delete(goal: SavingsGoal) {
        savingsGoalDao.deleteGoal(goal)
    }
}
