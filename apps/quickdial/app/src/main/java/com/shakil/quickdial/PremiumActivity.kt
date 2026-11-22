package com.shakil.quickdial

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.shakil.quickdial.databinding.ActivityPremiumBinding

class PremiumActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPremiumBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPremiumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.trxInput.setText(
            getSharedPreferences("quickdial_prefs", MODE_PRIVATE)
                .getString("premium_trxid", "")
        )

        binding.activateButton.setOnClickListener {
            val trxId = binding.trxInput.text?.toString().orEmpty()
            if (AppPrefs.activatePremium(this, trxId)) {
                Toast.makeText(this, R.string.premium_activated, Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, R.string.premium_invalid_trx, Toast.LENGTH_LONG).show()
            }
        }
    }
}
