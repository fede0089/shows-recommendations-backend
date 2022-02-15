package com.moviesrecommendations.adapters.controllers.dtos

import com.moviesrecommendations.domain.entities.Movie

data class MovieDTO (val title:String){

    companion object {
        fun from(movie: Movie): MovieDTO{
            return MovieDTO(title = movie.title)
        }
    }

}

