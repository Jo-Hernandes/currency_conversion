package com.example.exchangeratesapp.ui.main

import org.koin.androidx.viewmodel.ext.android.viewModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.exchangeratesapp.common.ConnectionException
import com.example.exchangeratesapp.common.observe
import com.example.exchangeratesapp.common.showSnackbar
import com.example.exchangeratesapp.databinding.MainFragmentBinding
import com.example.exchangeratesapp.ui.main.recyclerAdapter.MainRecyclerAdapter

class MainFragment : Fragment() {

    private val mainViewModel: MainViewModel by viewModel()
    private var binding: MainFragmentBinding? = null
    
    private var adapter: MainRecyclerAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        MainFragmentBinding.inflate(inflater).apply {
            lifecycleOwner = viewLifecycleOwner
            binding = this
            handler = mainViewModel

            adapter = MainRecyclerAdapter(mainViewModel::handleCurrencySelected).apply {
                setHasStableIds(true)
            }

            currencyListRecycler.adapter = adapter
        }.root


    override fun onStart() {
        super.onStart()
        observeData()
    }

    private fun observeData() = with(mainViewModel) {
        observe(displayCurrencies) { adapter?.currencyItemList = it }
        observe(displayException, ::displayExceptionSnackbar)
        observe(currentCurrency) { adapter?.selectedCurrency = it }
    }

    private fun displayExceptionSnackbar(exception: ConnectionException) {
        showSnackbar(
            text = exception.message,
            onRetry = exception.retry
        )
    }

    override fun onDestroy() {
        binding?.currencyListRecycler?.adapter = null
        binding = null
        super.onDestroy()
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}
