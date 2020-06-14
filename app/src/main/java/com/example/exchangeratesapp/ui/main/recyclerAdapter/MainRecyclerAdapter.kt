package com.example.exchangeratesapp.ui.main.recyclerAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.exchangeratesapp.common.onClicked
import com.example.exchangeratesapp.databinding.CurrencyListItemBinding
import com.example.exchangeratesapp.models.ExchangeCurrency

class MainRecyclerAdapter(
    private val onClickListener: (code: String) -> Unit
) : RecyclerView.Adapter<MainRecyclerViewHolder>() {

    var currencyItem: List<ExchangeCurrency> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var inputValue: Double = 0.0
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var selectedCode: String = ""

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MainRecyclerViewHolder =
        MainRecyclerViewHolder(
            CurrencyListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = currencyItem.count()

    override fun getItemId(position: Int): Long = currencyItem[position].code
        .toCharArray()
        .reduce { acc, char -> acc + char.toInt() }
        .toLong()

    override fun onBindViewHolder(holder: MainRecyclerViewHolder, position: Int) {
        val exchangeCurrency = currencyItem[position]
        holder.bindItem(exchangeCurrency, inputValue, selectedCode == exchangeCurrency.code)
        holder.itemView.onClicked {
            selectedCode = exchangeCurrency.code
            onClickListener(exchangeCurrency.code)
        }
    }
}