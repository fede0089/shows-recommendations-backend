package com.moviesrecommendations.domain.usecases

import com.moviesrecommendations.domain.entities.Movie
import com.moviesrecommendations.domain.ports.MovieRepository

class GetMovie(private val movieRepository: MovieRepository) {

    operator fun invoke(id:String): Movie {
        return movieRepository.getMovie(id)
    }

}