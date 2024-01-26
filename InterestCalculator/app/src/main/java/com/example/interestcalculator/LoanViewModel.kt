package com.example.interestcalculator
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoanViewModel : ViewModel() {

    val amount = MutableLiveData<String>()
    val interestRate = MutableLiveData<String>()
    val years = MutableLiveData<String>()

    init {
        amount.value = ""
        interestRate.value = ""
        years.value = ""
    }

    private val _summary = MutableLiveData<String>()
    val summary: LiveData<String>
        get() = _summary

    fun updateSummary() {
        val amountValue = amount.value?.toDoubleOrNull() ?: 0.0
        val interestRateValue = interestRate.value?.toDoubleOrNull() ?: 0.0
        val yearsValue = years.value?.toIntOrNull() ?: 0

        // Calculate interest
        val interest = amountValue * interestRateValue * yearsValue / 100

        // Calculate total amount
        val total = amountValue + interest

        // Create a summary string
        val summaryText = "Loan Summary:\nAmount: $amountValue\nInterest: $interest\nTotal: $total"
        _summary.value = summaryText
    }
}
