package com.example.lab3.repository

import com.example.lab3.model.MoviesResponse
import io.reactivex.Observable
import io.reactivex.Scheduler
import kotlinx.coroutines.flow.Flow
import retrofit2.Call

internal class MoviesRepository(
    private val remoteDataSource: RemoteDataSourceI,
    private val ioThread: Scheduler,
    private val mainThread: Scheduler
) : MoviesRepositoryI {
    override fun getNowPlyingMoviesRemote(page: Int): Observable<MoviesResponse> {
        return remoteDataSource.getNowPlayingMovies(page)
            .subscribeOn(ioThread)
            .observeOn(mainThread)
    }

    override fun getPopularMoviesRemote(page: Int): Observable<MoviesResponse> {
        return remoteDataSource.getPopularMovies(page)
            .subscribeOn(ioThread)
            .observeOn(mainThread)
    }
}

interface MoviesRepositoryI {
    fun getNowPlyingMoviesRemote(page: Int): Observable<MoviesResponse>
    fun getPopularMoviesRemote(page: Int): Observable<MoviesResponse>
}