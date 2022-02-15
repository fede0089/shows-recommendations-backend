package com.moviesrecommendations.adapters.controllers

import com.moviesrecommendations.domain.entities.Movie
import com.moviesrecommendations.domain.usecases.GetMovie

class MovieController(private val getMovieUseCase: GetMovie) {

    fun getMovieById(id:String): Movie{
        return getMovieUseCase(id = "fakeId")
    }

}