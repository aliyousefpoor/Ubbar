package com.task.ubbar.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.task.ubbar.R

class HomeFragment : Fragment() {
    private lateinit var addAddress: Button
    private lateinit var getAddress: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.home_fragment, container, false)
        getAddress = rootView.findViewById(R.id.getAddress)
        addAddress = rootView.findViewById(R.id.addAddress)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addAddress.setOnClickListener {
            val fragment = UbbarSignUpFragment()

            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        getAddress.setOnClickListener {
            val fragment = UbbarAddressesFragment()

            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
}