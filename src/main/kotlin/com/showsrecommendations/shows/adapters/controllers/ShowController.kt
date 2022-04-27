package com.showsrecommendations.shows.adapters.controllers
import com.showsrecommendations.shows.domain.usecases.GetShowForLoggedUser
import com.showsrecommendations.shows.domain.usecases.GetShowForUnloggedUser

class ShowController(
    private val getShowForUnloggedUser: GetShowForUnloggedUser,
    private val getShowForLoggedUser: GetShowForLoggedUser
) {
    fun getShow(showId: String): GetShowForUnloggedUser.Response {
        val request = GetShowForUnloggedUser.Request(showId = showId)
        return getShowForUnloggedUser.invoke(getShowRequest = request)
    }

    fun getShow(userId:String, showId: String): GetShowForLoggedUser.Response {
        val request = GetShowForLoggedUser.Request(userId = userId, showId = showId)
        return getShowForLoggedUser.invoke(getShowRequest = request)
    }
}