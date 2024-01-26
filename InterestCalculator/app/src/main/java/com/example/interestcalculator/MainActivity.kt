package com.example.interestcalculator
import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.interestcalculator.databinding.ActivityMainBinding

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.editTextAmount.doAfterTextChanged {
            calculateAndDisplay()
        }

        binding.editTextInterestRate.doAfterTextChanged {
            calculateAndDisplay()
        }

        binding.editTextYears.doAfterTextChanged {
            calculateAndDisplay()
        }
    }

    private fun calculateAndDisplay() {
        val amount = binding.editTextAmount.text.toString().toDoubleOrNull() ?: 0.0
        val interestRate = binding.editTextInterestRate.text.toString().toDoubleOrNull() ?: 0.0
        val years = binding.editTextYears.text.toString().toIntOrNull() ?: 0

        val interest = (amount * interestRate * years) / 100
        val totalAmount = amount + interest

        val summary = "Borrow Amount: $amount\nInterest Amount: $interest\nTotal Amount: $totalAmount"
        binding.textViewSummary.text = summary
    }
}

