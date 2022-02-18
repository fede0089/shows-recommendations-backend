package com.showsrecommendations.domain.entities

data class Review(val userId: String,
                  val showId: String,
                  val rating:Float,
                  val createdDate:String,
                  val lastUpdated:String)