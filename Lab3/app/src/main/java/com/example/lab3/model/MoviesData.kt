package com.example.lab3.model

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class MoviesData (
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String ? = null,
    @SerializedName("overview") val overview: String ? = null,
    @SerializedName("poster_path") val posterPath: String ? = null,
    @SerializedName("backdrop_path") val backdropPath: String ? = null,
    @SerializedName("vote_average") val rating: Float ? = null,
    @SerializedName("release_date") val releaseDate: String ? = null,
)