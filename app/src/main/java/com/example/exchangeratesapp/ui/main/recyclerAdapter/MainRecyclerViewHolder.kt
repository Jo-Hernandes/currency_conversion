package com.example.exchangeratesapp.ui.main.recyclerAdapter

import androidx.recyclerview.widget.RecyclerView
import com.example.exchangeratesapp.databinding.CurrencyListItemBinding
import com.example.exchangeratesapp.models.ExchangeCurrency

class MainRecyclerViewHolder(private val holderBinding: CurrencyListItemBinding) :
    RecyclerView.ViewHolder(holderBinding.root) {

    fun bindItem(currency: ExchangeCurrency, inputValue: Double, isSelected: Boolean) =
        with(holderBinding) {
            this.currency = currency
            this.inputValue = inputValue
            this.isSelected = isSelected
        }
}