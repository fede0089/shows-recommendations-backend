package com.showsrecommendations.domain.entities

data class User(val id: String,
                val name: String,
                val lastname: String,
                val birthday: String,
                val externalAccountId: String)
