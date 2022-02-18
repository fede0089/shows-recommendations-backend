package com.showsrecommendations.domain.entities

data class Recommendation(val showId: String,
                          val recommendationsQty:Int,
                          val unrecommendationsQty:Int)