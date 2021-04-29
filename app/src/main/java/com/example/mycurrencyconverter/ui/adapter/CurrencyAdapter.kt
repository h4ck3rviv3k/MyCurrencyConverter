package com.example.mycurrencyconverter.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mycurrencyconverter.R
import com.example.mycurrencyconverter.domain.model.CurrencyData
import kotlinx.android.synthetic.main.item_currency.view.*
import java.text.DecimalFormat

/**
 * @author: Vivek Singh
 * @property CurrencyAdapter : This class is responsible for binding currency data with View
 * @param currencyList: List of currency data which holds the currencyName, currencyCode, and currencyRate
 * @param targetPrice: Price entered by User for which currency conversion needs to be done
 * @param selectedRate: Selected source currency code
 */
class CurrencyAdapter(
    var currencyList: List<CurrencyData>, var targetPrice: Double,
    var selectedRate: Double
) :
    RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CurrencyViewHolder(layoutInflater.inflate(R.layout.item_currency, parent, false))
    }

    override fun getItemCount(): Int = currencyList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val currencyData = currencyList[position]
        holder.code.text = currencyData.code
        val dec = DecimalFormat("#,###.#")
        holder.currency.text = "${dec.format(calculatePrice(currencyData))} ${currencyData.code}"
    }

    fun updateCurrencyList(
        pointsTableDataList: List<CurrencyData>,
        targetPrice: Double,
        selectedRate: Double
    ) {
        this.currencyList = pointsTableDataList
        this.targetPrice = targetPrice
        this.selectedRate = selectedRate
        notifyDataSetChanged()
    }

    fun updateSelectRate(newRate: Double) {
        this.selectedRate = newRate
        notifyDataSetChanged()
    }

    fun updateTargetPrice(newPrice: Double) {
        this.targetPrice = newPrice
        notifyDataSetChanged()
    }

    private fun calculatePrice(currencyData: CurrencyData): Double {
        return currencyData.rate * targetPrice / selectedRate
    }

    class CurrencyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val code: TextView = itemView.code
        val currency: TextView = itemView.currency
    }
}