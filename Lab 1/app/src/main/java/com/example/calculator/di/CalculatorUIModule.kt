package com.example.calculator.di

import com.example.calculator.presenter.CalculatorContract
import com.example.calculator.presenter.CalculatorPresenter
import org.koin.dsl.module

val calculatorUIModule = module {
    factory<CalculatorContract.Presenter> { (view: CalculatorContract.View) ->
        CalculatorPresenter(
            view
        )
    }
}