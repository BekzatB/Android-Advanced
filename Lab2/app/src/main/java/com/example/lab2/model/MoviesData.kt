package com.example.lab2.model

import com.google.gson.annotations.SerializedName

data class MoviesData (
        @SerializedName("id") val id: Int? = null,
        @SerializedName("title") val title: String? = null,
        @SerializedName("overview") val overview: String? = null,
        @SerializedName("poster_path") val posterPath: String? = null,
        @SerializedName("backdrop_path") val backdropPath: String? = null,
        @SerializedName("vote_average") val rating: Float? = null,
        @SerializedName("release_date") val releaseDate: String? = null,
)