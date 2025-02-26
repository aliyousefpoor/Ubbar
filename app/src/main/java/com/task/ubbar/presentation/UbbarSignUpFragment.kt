package com.task.ubbar.presentation

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputLayout
import com.task.ubbar.MainActivity
import com.task.ubbar.R
import com.task.ubbar.Utils
import com.task.ubbar.data.model.AddressRequestModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UbbarSignUpFragment : Fragment() {
    private val viewModel: UbbarViewModel by viewModels()

    private lateinit var name: EditText
    private lateinit var lastName: EditText
    private lateinit var phone: EditText
    private lateinit var landLine: EditText
    private lateinit var address: EditText
    private lateinit var maleTextView: TextView
    private lateinit var femaleTextView: TextView
    private lateinit var nameInput: TextInputLayout
    private lateinit var lastNameInput: TextInputLayout
    private lateinit var phoneInput: TextInputLayout
    private lateinit var landLineInput: TextInputLayout
    private lateinit var addressInput: TextInputLayout
    private lateinit var nextButton: Button
    private var isMale = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = inflater.inflate(R.layout.signup_fragment, container, false)
        name = rootView.findViewById(R.id.nameEditText)
        lastName = rootView.findViewById(R.id.lastNameEditText)
        phone = rootView.findViewById(R.id.phoneEditText)
        landLine = rootView.findViewById(R.id.landLineEditText)
        address = rootView.findViewById(R.id.addressEditText)
        maleTextView = rootView.findViewById(R.id.male)
        femaleTextView = rootView.findViewById(R.id.female)
        nameInput = rootView.findViewById(R.id.et_first_name)
        lastNameInput = rootView.findViewById(R.id.et_last_name)
        phoneInput = rootView.findViewById(R.id.et_phone)
        landLineInput = rootView.findViewById(R.id.et_landline)
        addressInput = rootView.findViewById(R.id.et_address)
        nextButton = rootView.findViewById(R.id.btn_next)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.updateToolbarTitle("ثبت نام")

        setOnClicks()
        setupObservers()
    }

    private fun setOnClicks() {
        name.doOnTextChanged { text, _, _, _ ->
            nameInput.setStartIconDrawable(if (text?.length != 0) R.drawable.check_mark_circle_icon else R.drawable.invalid_background)
            nameInput.setStartIconTintList(
                ColorStateList.valueOf(
                    if (text?.length != 0) requireContext().getColor(
                        R.color.green
                    ) else requireContext().getColor(R.color.invalid_input_background)
                )
            )
            lifecycleScope.launch {
                delay(500)
                viewModel.nameValue.value = text.toString()
            }
        }

        lastName.doOnTextChanged { text, _, _, _ ->
            lastNameInput.setStartIconDrawable(if (text?.length != 0) R.drawable.check_mark_circle_icon else R.drawable.invalid_background)
            lastNameInput.setStartIconTintList(
                ColorStateList.valueOf(
                    if (text?.length != 0) requireContext().getColor(
                        R.color.green
                    ) else requireContext().getColor(R.color.invalid_input_background)
                )
            )

            lifecycleScope.launch {
                viewModel.lastNameValue.value = text.toString()
            }
        }

        phone.doOnTextChanged { text, _, _, _ ->
            phoneInput.setStartIconDrawable(if (Utils.isValidIranianPhoneNumber(text.toString())) R.drawable.check_mark_circle_icon else R.drawable.invalid_background)
            phoneInput.setStartIconTintList(
                ColorStateList.valueOf(
                    if (Utils.isValidIranianPhoneNumber(text.toString())) requireContext().getColor(
                        R.color.green
                    ) else requireContext().getColor(R.color.invalid_input_background)
                )
            )

            lifecycleScope.launch {
                viewModel.phoneValue.value = text.toString()
            }
        }

        landLine.doOnTextChanged { text, _, _, _ ->
            landLineInput.setStartIconDrawable(if (Utils.isValidIranLandline(text.toString())) R.drawable.check_mark_circle_icon else R.drawable.invalid_background)
            landLineInput.setStartIconTintList(
                ColorStateList.valueOf(
                    if (Utils.isValidIranLandline(text.toString())) requireContext().getColor(
                        R.color.green
                    ) else requireContext().getColor(R.color.invalid_input_background)
                )
            )
            lifecycleScope.launch {
                viewModel.landLineValue.value = text.toString()
            }
        }

        address.doOnTextChanged { text, _, _, _ ->
            addressInput.setStartIconDrawable(if (text?.length != 0) R.drawable.check_mark_circle_icon else R.drawable.invalid_background)
            addressInput.setStartIconTintList(
                ColorStateList.valueOf(
                    if (text?.length != 0) requireContext().getColor(
                        R.color.green
                    ) else requireContext().getColor(R.color.invalid_input_background)
                )
            )

            lifecycleScope.launch {
                viewModel.addressValue.value = text.toString()
            }
        }

        maleTextView.setOnClickListener {
            handleGenderSwitchView(true)
        }

        femaleTextView.setOnClickListener {
            handleGenderSwitchView(false)
        }

        nextButton.setOnClickListener {
            val addressRequestModel = AddressRequestModel(
                address = address.text.toString(),
                lat = 0,
                lng = 0,
                coordiante_mobile = phone.text.toString(),
                coordiante_phone_number = landLine.text.toString(),
                first_name = name.text.toString(),
                last_name = lastName.text.toString(),
                gender = if (isMale) "Male" else "Female"
            )
            val fragment = MapFragment.newInstance(addressRequestModel)

            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    private fun setupObservers() {
        viewModel.isFormValid.observe(viewLifecycleOwner) { isValid ->
            nextButton.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    if (isValid) R.color.button_background else R.color.disable_button_background
                )
            )
            nextButton.isEnabled = isValid
        }
    }

    private fun handleGenderSwitchView(isMaleFlag: Boolean) {
        isMale = isMaleFlag
        maleTextView.setTextColor(requireContext().getColor(if (isMale) R.color.white else R.color.select_border))
        femaleTextView.setTextColor(requireContext().getColor(if (isMale) R.color.select_border else R.color.white))
        maleTextView.setBackgroundResource(if (isMale) R.drawable.select_male_gender_switch_bg else R.drawable.unselect_male_gender_switch_bg)
        femaleTextView.setBackgroundResource(if (isMale) R.drawable.unselect_female_gender_switch_bg else R.drawable.select_female_gender_switch_bg)
    }

}