package com.shakil.quickdial

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.shakil.quickdial.databinding.FragmentContactsBinding
import kotlin.concurrent.thread

class ContactsFragment : Fragment() {

    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!

    private data class Contact(val name: String, val number: String) {
        override fun toString() = "$name  •  $number"
    }

    private val contactsPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) loadContacts()
        else Toast.makeText(
            requireContext(), R.string.contacts_permission_denied, Toast.LENGTH_LONG
        ).show()
    }

    private val callPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* caller proceeds with dialer fallback */ }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.contactsList.setOnItemClickListener { _, _, position, _ ->
            val contact = binding.contactsList.getItemAtPosition(position) as? Contact
                ?: return@setOnItemClickListener
            call(contact.number)
        }
        ensurePermissionAndLoad()
    }

    private fun ensurePermissionAndLoad() {
        val granted = ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED

        if (granted) loadContacts()
        else contactsPermission.launch(Manifest.permission.READ_CONTACTS)
    }

    private fun loadContacts() {
        val contextRef = requireContext()
        thread {
            val contacts = mutableListOf<Contact>()
            var cursor: Cursor? = null
            try {
                cursor = contextRef.contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    arrayOf(
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                    ),
                    null,
                    null,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
                )
                while (cursor != null && cursor.moveToNext()) {
                    val name = cursor.getString(0) ?: continue
                    val number = cursor.getString(1) ?: continue
                    contacts.add(Contact(name, number))
                }
            } finally {
                cursor?.close()
            }
            val result = contacts
            requireActivity().runOnUiThread {
                _binding?.contactsList?.adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_list_item_1,
                    result
                )
            }
        }
    }

    private fun call(number: String) {
        val granted = ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED

        if (!granted) {
            callPermission.launch(Manifest.permission.CALL_PHONE)
        }
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
