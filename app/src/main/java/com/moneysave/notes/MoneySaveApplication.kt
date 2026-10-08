package com.moneysave.notes

import android.app.Application
import com.moneysave.notes.data.SavingsDatabase
import com.moneysave.notes.data.SavingsRepository
import com.moneysave.notes.data.TransactionDatabase
import com.moneysave.notes.data.TransactionRepository

class MoneySaveApplication : Application() {

    val database by lazy {
        TransactionDatabase.getDatabase(this)
    }

    val transactionRepository by lazy {
        TransactionRepository(database.transactionDao())
    }

    val savingsDatabase by lazy {
        SavingsDatabase.getDatabase(this)
    }

    val savingsRepository by lazy {
        SavingsRepository(savingsDatabase.savingsGoalDao())
    }
}
