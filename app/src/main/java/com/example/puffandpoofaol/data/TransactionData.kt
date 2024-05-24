package com.example.puffandpoofaol.data

data class Transaction(
    val transactionID: Int,
    val userID: Int,
    val dollID: Int,
    var transactionAmount: Int,
    val transactionDate: String,
    val dollName: String,
)