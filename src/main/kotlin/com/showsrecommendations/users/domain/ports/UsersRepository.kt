package com.showsrecommendations.users.domain.ports

import com.showsrecommendations.users.domain.entities.User

interface UsersRepository {
    fun getUser(userId: String): User
}