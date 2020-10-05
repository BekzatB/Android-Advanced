package com.example.lab3.model

import com.google.gson.annotations.SerializedName

data class MoviesResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val movies: List<MoviesData>,
    @SerializedName("total_pages") val totalPages: Int
)
