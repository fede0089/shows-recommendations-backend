package com.moviesrecommendations.adapters.repositories

import com.moviesrecommendations.domain.entities.Movie
import com.moviesrecommendations.domain.ports.MovieRepository

class MovieRepositoryMocked: MovieRepository{
    override fun getMovie(id: String): Movie {
        return Movie(title = "Mocked movie")
    }
}