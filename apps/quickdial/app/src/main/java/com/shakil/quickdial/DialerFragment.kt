package com.shakil.quickdial

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.shakil.quickdial.databinding.FragmentDialerBinding

class DialerFragment : Fragment() {

    private var _binding: FragmentDialerBinding? = null
    private val binding get() = _binding!!
    private var enteredNumber: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupDialPad()
        setupQuickCall()
        renderNumber()
    }

    private fun setupQuickCall() {
        binding.quickCallButton.setOnClickListener { call(QuickContact.number) }
    }

    private fun setupDialPad() {
        val keys = listOf(
            "1", "2", "3",
            "4", "5", "6",
            "7", "8", "9",
            "*", "0", "#"
        )
        keys.forEach { key ->
            val button = Button(requireContext()).apply {
                text = key
                textSize = 24f
                isAllCaps = false
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
        binding.callButton.setOnClickListener {
            if (enteredNumber.isNotEmpty()) call(enteredNumber)
        }
    }

    private fun renderNumber() {
        val display = enteredNumber.ifEmpty { getString(R.string.enter_number_hint) }
        binding.numberDisplay.text = display
        binding.callButton.isEnabled = enteredNumber.isNotEmpty()
        binding.callButton.alpha = if (enteredNumber.isNotEmpty()) 1f else 0.4f
    }

    private fun call(number: String) {
        val granted = ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED

        val intent = if (granted) {
            Intent(Intent.ACTION_CALL, Uri.parse("tel:$number"))
        } else {
            Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
