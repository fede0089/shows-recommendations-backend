package com.moviesrecommendations.adapters.controllers

import com.moviesrecommendations.adapters.controllers.dtos.MovieDTO
import com.moviesrecommendations.domain.usecases.GetMovie

class MovieController(private val getMovieUseCase: GetMovie) {

    fun getMovieById(id:String): MovieDTO{
        return MovieDTO.from(getMovieUseCase(id = id))
    }

}