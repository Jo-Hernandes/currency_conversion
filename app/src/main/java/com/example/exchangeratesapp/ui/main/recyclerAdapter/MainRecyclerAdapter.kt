package com.example.exchangeratesapp.ui.main.recyclerAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.exchangeratesapp.common.onClicked
import com.example.exchangeratesapp.databinding.CurrencyListItemBinding
import com.example.exchangeratesapp.models.ExchangeCurrency

class MainRecyclerAdapter(
    private val onClickListener: (code: ExchangeCurrency) -> Unit
) : RecyclerView.Adapter<MainRecyclerViewHolder>() {

    var currencyItemList: List<ExchangeCurrency> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var selectedCurrency: ExchangeCurrency? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

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

    override fun getItemCount(): Int = currencyItemList.count()

    override fun getItemId(position: Int): Long = currencyItemList[position].code
        .toCharArray()
        .map { it.code }
        .reduce { acc, char -> acc + char }
        .toLong()

    override fun onBindViewHolder(holder: MainRecyclerViewHolder, position: Int) {
        val exchangeCurrency = currencyItemList[position]
        holder.bindItem(exchangeCurrency, selectedCurrency?.code == exchangeCurrency.code)
        holder.itemView.onClicked {
            onClickListener(exchangeCurrency)
        }
    }
}
