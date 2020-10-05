package com.example.lab3.API

import com.example.lab3.model.MoviesResponse
import io.reactivex.Observable
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("movie/popular")
    fun getPopularMovies(
        @Query("page") page: Int
    ): Observable<MoviesResponse>

    @GET("movie/now_playing")
    fun getNowPlayingMovies(
        @Query("page") page: Int
    ): Observable<MoviesResponse>
}