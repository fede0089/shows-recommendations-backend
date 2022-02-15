package com.moviesrecommendations.domain.ports

import com.moviesrecommendations.domain.entities.Movie

interface MovieRepository {
    fun getMovie(id: String): Movie
}