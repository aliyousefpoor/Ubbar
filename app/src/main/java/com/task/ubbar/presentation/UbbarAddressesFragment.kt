package com.task.ubbar.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.task.ubbar.MainActivity
import com.task.ubbar.R
import com.task.ubbar.data.model.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UbbarAddressesFragment : Fragment() {
    private val viewModel: UbbarViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private val addressAdapter: AddressAdapter by lazy {
        AddressAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.addresses_fragment, container, false)
        recyclerView = rootView.findViewById(R.id.addressRecyclerView)
        progressBar = rootView.findViewById(R.id.progressBar)
        return rootView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.updateToolbarTitle("آدرس ها")

        viewModel.getAddress()
        recyclerView.apply {
            adapter = addressAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }


        viewModel.getAddressResponse.observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResult.Success -> {
                    result.data?.let {
                        progressBar.isVisible = false
                        recyclerView.isVisible = true
                        addressAdapter.setAddressList(it)
                    }
                }

                is NetworkResult.NetworkError -> {
                    progressBar.isVisible = false
                }

                is NetworkResult.Loading -> {
                    progressBar.isVisible = true
                }

                is NetworkResult.HttpException -> {
                    progressBar.isVisible = false
                }
            }
        }
    }
}