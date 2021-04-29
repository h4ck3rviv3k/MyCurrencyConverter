package com.example.mycurrencyconverter.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mycurrencyconverter.R
import com.example.mycurrencyconverter.domain.model.CurrencyData
import com.example.mycurrencyconverter.domain.model.CurrencyTransfer
import com.example.mycurrencyconverter.ui.adapter.CurrencyAdapter
import com.example.mycurrencyconverter.ui.model.CurrencyViewModel
import com.example.mycurrencyconverter.util.bothNonNull
import com.example.mycurrencyconverter.util.viewUtil.GridSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_currency.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author: Vivek Singh
 * @property CurrencyFragment : This class is responsible for rendering the UI
 */
class CurrencyFragment : Fragment() {

    private val currencyViewModel: CurrencyViewModel by viewModel()
    private var inputPrice: EditText? = null
    private var currencySpinner: AppCompatSpinner? = null
    private var rvCurrencyList: RecyclerView? = null
    private var currencyAdapter: CurrencyAdapter? = null
    private var scanHandler: Handler? = null
    private var emptyState: TextView? = null

    // Sync exchange rates in every 30-minute
    private val syncExchangeRateTask = object : Runnable {
        override fun run() {
            currencyViewModel.fetchData()
            scanHandler?.postDelayed(this, SYNC_TIME_IN_MILLIS)
        }
    }

    companion object {
        const val SPAN_COUNT = 3
        const val SYNC_TIME_IN_MILLIS = 1810000L

        fun newInstance() = CurrencyFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scanHandler = Handler(Looper.getMainLooper())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_currency, container, false)
        inputPrice = root.tv_price
        currencySpinner = root.currency_menu
        emptyState = root.empty_state

        rvCurrencyList = root.rv_currency_list
        rvCurrencyList?.layoutManager = GridLayoutManager(context, SPAN_COUNT)
        rvCurrencyList?.addItemDecoration(GridSpacingItemDecoration(SPAN_COUNT))


        inputPrice?.doOnTextChanged { text, _, _, _ ->
            val getString = text?.toString() ?: "1"
            val getNumber: Double = parseInputPrice(getString)
            currencyViewModel.updatePrice(getNumber)
        }
        currencyViewModel.readyData().observe(viewLifecycleOwner, currencyRatesObserver)
        currencyViewModel.getTargetPrice().observe(viewLifecycleOwner, inputPriceObserver)
        currencyViewModel.getStandRate().observe(viewLifecycleOwner, selectCurrencyObserver)
        return root
    }

    override fun onResume() {
        super.onResume()
        scanHandler?.post(syncExchangeRateTask)
    }

    override fun onPause() {
        super.onPause()
        scanHandler?.removeCallbacks(syncExchangeRateTask)
    }

    //Observer for any update in EditText
    private val inputPriceObserver = Observer<Double> {
        currencyAdapter?.updateTargetPrice(it)
    }

    //Observer for any update in Currency Spinner
    private val selectCurrencyObserver = Observer<Double> {
        currencyAdapter?.updateSelectRate(it)
    }

    private val currencyRatesObserver = Observer<CurrencyTransfer> {
        setupCurrencySpinner(it)
    }

    private fun setupCurrencySpinner(currencyTransfer: CurrencyTransfer) {
        bothNonNull(context, currencySpinner)?.let {
            val currencyList = currencyTransfer.data

            val currencyWithCountryNameList = currencyTransfer.getFormattedCountryWithCurrencyName()
            val adapter = ArrayAdapter(
                it.first, R.layout.support_simple_spinner_dropdown_item,
                currencyWithCountryNameList
            )
            it.second.adapter = adapter
            it.second.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    pos: Int,
                    id: Long
                ) {
                    currencyViewModel.updateRate(currencyList[pos].rate)
                }
            }
            val dataSize = currencyList.size
            emptyState?.text = if (dataSize > 0) "" else getText(R.string.placeholder_msg)
            val selectIndex = it.second.selectedItemPosition

            val selectedRate = if (currencyList.size > 0) currencyList[selectIndex].rate else 0.0
            val targetPriceText = inputPrice?.toString() ?: "1"

            val targetPrice = parseInputPrice(targetPriceText)

            //Update Exchange Rates in RecyclerView
            setupRecyclerView(currencyList, targetPrice, selectedRate)
        }
    }

    private fun setupRecyclerView(
        currencyList: MutableList<CurrencyData>,
        targetPrice: Double,
        selectedRate: Double
    ) {
        rvCurrencyList?.let { recyclerView ->

            if (recyclerView.adapter == null) {
                currencyAdapter = CurrencyAdapter(currencyList, targetPrice, selectedRate)
                recyclerView.adapter = currencyAdapter
            } else {
                currencyAdapter?.updateCurrencyList(currencyList, targetPrice, selectedRate)
            }

        }
    }

    private fun parseInputPrice(text: String): Double {
        val parseVal: Double = if (text.isEmpty()) 1.0 else text.toDoubleOrNull() ?: 1.0
        return if (parseVal > 0.0) parseVal else 1.0
    }
}