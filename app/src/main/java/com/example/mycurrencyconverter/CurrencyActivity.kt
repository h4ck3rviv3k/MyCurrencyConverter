package com.example.mycurrencyconverter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mycurrencyconverter.ui.fragment.CurrencyFragment

/**
 * @author: Vivek Singh
 * @property CurrencyActivity : This class is the main activity which holds the currencyFragment
 */
class CurrencyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            loadFragment()
        }
    }

    private fun loadFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, CurrencyFragment.newInstance())
            .commitNow()
    }
}