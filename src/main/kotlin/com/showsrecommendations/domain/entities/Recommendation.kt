package com.showsrecommendations.domain.entities

data class Recommendation(val showId: String,
                          val positiveReviewsQty:Int,
                          val negativeReviewsQty:Int)