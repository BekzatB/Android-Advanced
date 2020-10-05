package com.example.lab3.di

import com.example.lab3.ui.MoviesFragmentViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val moviesUIModule = module {
    viewModel { MoviesFragmentViewModel(get(), get()) }
}