package com.showsrecommendations.domain.ports

import com.showsrecommendations.domain.entities.FollowedUser
import com.showsrecommendations.domain.entities.User

interface UsersRepository {
    fun getUser(userId: String): User
}