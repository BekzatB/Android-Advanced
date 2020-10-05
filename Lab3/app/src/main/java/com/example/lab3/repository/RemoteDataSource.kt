package com.example.lab3.repository

import com.example.lab3.API.ApiService
import com.example.lab3.model.MoviesResponse
import io.reactivex.Observable
import kotlinx.coroutines.flow.Flow
import retrofit2.Call

internal class RemoteDataSource(private val api: ApiService) : RemoteDataSourceI {
    override fun getNowPlayingMovies(page: Int): Observable<MoviesResponse> {
        return api.getNowPlayingMovies(page)
    }

    override fun getPopularMovies(page: Int): Observable<MoviesResponse> {
        return api.getPopularMovies(page)
    }
}

interface RemoteDataSourceI {
    fun getPopularMovies(page: Int): Observable<MoviesResponse>
    fun getNowPlayingMovies(page: Int): Observable<MoviesResponse>
}