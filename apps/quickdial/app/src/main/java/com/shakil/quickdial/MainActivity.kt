package com.shakil.quickdial

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import androidx.appcompat.app.AppCompatActivity
import com.shakil.quickdial.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var enteredNumber: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDialPad()
        setupQuickCall()

        binding.callButton.setOnClickListener {
            if (enteredNumber.isNotEmpty()) {
                dial(enteredNumber)
            }
        }
        binding.backspaceButton.setOnClickListener {
            if (enteredNumber.isNotEmpty()) {
                enteredNumber = enteredNumber.dropLast(1)
                renderNumber()
            }
        }
        binding.backspaceButton.setOnLongClickListener {
            enteredNumber = ""
            renderNumber()
            true
        }
    }

    private fun setupQuickCall() {
        binding.quickCallButton.setOnClickListener { dial(QuickContact.number) }
    }

    private fun setupDialPad() {
        val keys = listOf(
            "1", "2", "3",
            "4", "5", "6",
            "7", "8", "9",
            "*", "0", "#"
        )
        keys.forEach { key ->
            val button = Button(this).apply {
                text = key
                textSize = 24f
            }
            val params = GridLayout.LayoutParams().apply {
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                setMargins(8, 8, 8, 8)
            }
            button.layoutParams = params
            button.setOnClickListener {
                if (enteredNumber.length < QuickContact.maxDigits) {
                    enteredNumber += key
                    renderNumber()
                }
            }
            binding.dialPad.addView(button)
        }
    }

    private fun renderNumber() {
        val display = enteredNumber.ifEmpty { getString(R.string.enter_number_hint) }
        binding.numberDisplay.text = display
        binding.callButton.isEnabled = enteredNumber.isNotEmpty()
        binding.callButton.alpha = if (enteredNumber.isNotEmpty()) 1f else 0.4f
    }

    private fun dial(number: String) {
        startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number")))
    }
}
