package com.moviesrecommendations.application

import com.moviesrecommendations.adapters.controllers.MovieController
import com.moviesrecommendations.adapters.repositories.MovieRepositoryMocked
import com.moviesrecommendations.domain.usecases.GetMovie
import io.ktor.application.call
import io.ktor.response.*
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty



fun main() {

    //config
    val getMovie = GetMovie(movieRepository = MovieRepositoryMocked())
    val movieController = MovieController(getMovieUseCase = getMovie)

    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        routing {
            get("/") {
                val movie = movieController.getMovieById("fakeId")
                call.respondText(movie.toString())
            }
        }
    }.start(wait = true)
}