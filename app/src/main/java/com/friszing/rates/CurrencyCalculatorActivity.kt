package com.friszing.rates

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.friszing.rates.di.module.CurrencyCalculatorModule
import com.friszing.rates.module.currencycalculator.fragment.CurrencyCalculatorFragmentFactory
import javax.inject.Inject

class CurrencyCalculatorActivity : AppCompatActivity() {

    @Inject
    lateinit var currencyCalculatorFragmentFactory: CurrencyCalculatorFragmentFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        supportFragmentManager.fragmentFactory = currencyCalculatorFragmentFactory
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeToolbar()
    }

    private fun injectDependencies() {
        (application as CurrencyCalculatorApp)
            .applicationComponent
            .currencyCalculatorComponentBuilder()
            .currencyCalculatorModule(CurrencyCalculatorModule())
            .build()
            .inject(this)
    }

    private fun initializeToolbar() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration =
            AppBarConfiguration(navController.graph)
        findViewById<Toolbar>(R.id.toolbar).setupWithNavController(
            navController,
            appBarConfiguration
        )
    }
}