package com.example.exchangeratesapp.ui.main

import org.koin.androidx.viewmodel.ext.android.viewModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.exchangeratesapp.common.ConnectionException
import com.example.exchangeratesapp.common.observe
import com.example.exchangeratesapp.common.showSnackbar
import com.example.exchangeratesapp.databinding.MainFragmentBinding
import com.example.exchangeratesapp.ui.main.recyclerAdapter.MainRecyclerAdapter

class MainFragment : Fragment() {

    private val mainViewModel: MainViewModel by viewModel()
    private val adapter =
        MainRecyclerAdapter { mainViewModel.fetchRates(it) }.apply {
            setHasStableIds(true)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        MainFragmentBinding.inflate(inflater).apply {
            lifecycleOwner = viewLifecycleOwner
            handler = mainViewModel

            currencyListRecycler.adapter = adapter
            currencyListRecycler.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }.root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeData()
    }

    private fun observeData() = with(mainViewModel) {
        observe(displayCurrencies) { adapter.currencyItem = it }
        observe(displayException, ::displayExceptionSnackbar)
        observe(updateValue) { adapter.inputValue = if (it.isNotBlank()) it.toDouble() else 0.0 }
    }

    private fun displayExceptionSnackbar(exception: ConnectionException) {
        showSnackbar(
            text = exception.message,
            onRetry = exception.retry
        )
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}
