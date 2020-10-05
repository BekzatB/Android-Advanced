package com.example.lab3.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lab3.model.MoviesData
import com.example.lab3.model.MoviesResponse
import com.example.lab3.repository.MoviesRepositoryI
import io.reactivex.disposables.CompositeDisposable

class MoviesFragmentViewModel(
    private val repository: MoviesRepositoryI,
    private val disposable: CompositeDisposable
) : ViewModel() {

    val liveData by lazy {
        MutableLiveData<State>()
    }

    fun getPopularMovies(page: Int = 1) {
        disposable.add(
            repository.getPopularMoviesRemote(page)
                .doOnSubscribe { liveData.value = State.ShowLoading }
                .doFinally { liveData.value = State.HideLoading }
                .subscribe({
                    liveData.value = State.PopularMoviesResult(it)
                }, {
                    it.printStackTrace()
                })
        )
    }

    fun getNowPlayingMovies(page: Int = 1) {
        disposable.add(
            repository.getNowPlyingMoviesRemote(page)
                .doOnSubscribe { liveData.value = State.ShowLoading }
                .doFinally { liveData.value = State.HideLoading }
                .subscribe({
                    liveData.value = State.NowPlayingMoviesResult(it)
                }, {
                    it.printStackTrace()
                })
        )
    }
}

sealed class State {
    data class PopularMoviesResult(val result: MoviesResponse) : State()
    data class NowPlayingMoviesResult(val result: MoviesResponse) : State()
    object ShowLoading : State()
    object HideLoading : State()
}