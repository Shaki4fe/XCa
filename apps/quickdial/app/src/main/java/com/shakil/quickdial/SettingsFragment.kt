package com.shakil.quickdial

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.shakil.quickdial.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        renderPremiumState()
        renderAnnouncementSwitch()

        binding.announcementSwitch.setOnCheckedChangeListener { _, checked ->
            AppPrefs.setAnnouncementEnabled(requireContext(), checked)
        }
        binding.premiumActivateButton.setOnClickListener {
            startActivity(Intent(requireContext(), PremiumActivity::class.java))
        }
        binding.updateCheckButton.setOnClickListener {
            UpdateChecker.checkForUpdate(requireActivity(), manual = true)
        }
        binding.bkashCard.setOnClickListener {
            val copy = getString(R.string.settings_bkash_value, QuickContact.bkashNumber)
            Toast.makeText(requireContext(), copy, Toast.LENGTH_LONG).show()
        }
    }

    private fun renderPremiumState() {
        val active = AppPrefs.isPremium(requireContext())
        binding.premiumStatus.text = if (active) {
            getString(R.string.settings_premium_active)
        } else {
            getString(R.string.settings_premium_inactive)
        }
        binding.premiumActivateButton.text = if (active) {
            getString(R.string.settings_premium_manage)
        } else {
            getString(R.string.settings_premium_activate)
        }
    }

    private fun renderAnnouncementSwitch() {
        val premium = AppPrefs.isPremium(requireContext())
        val enabled = AppPrefs.isAnnouncementEnabled(requireContext())
        binding.announcementSwitch.isEnabled = premium
        binding.announcementSwitch.isChecked = premium && enabled
        binding.announcementSummary.text = if (premium) {
            getString(R.string.settings_announcement_summary)
        } else {
            getString(R.string.settings_announcement_premium_only)
        }
    }

    override fun onResume() {
        super.onResume()
        renderPremiumState()
        renderAnnouncementSwitch()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
