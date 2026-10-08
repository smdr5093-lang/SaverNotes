package com.moneysave.notes.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val amount: Double,
    val type: String,
    val category: String,
    val date: Long,
    val paymentMethod: String,
    val note: String = ""
)
