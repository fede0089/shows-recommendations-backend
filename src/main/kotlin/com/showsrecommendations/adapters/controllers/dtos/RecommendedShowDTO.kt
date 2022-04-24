package com.showsrecommendations.adapters.controllers.dtos

import com.showsrecommendations.domain.entities.Recommendation
import com.showsrecommendations.domain.entities.Show

data class RecommendedShowDTO (val title:String,
                               val genres: List<String>,
                               val year:String,
                               val cover:String,
                               val type:String,
                               val positiveReviewsQty:Int,
                               val negativeReviewsQty: Int){

    companion object {
        fun from(recommendation: Recommendation, show: Show): RecommendedShowDTO =
            RecommendedShowDTO(
                title = show.title,
                genres = show.genres,
                year = show.year,
                cover = show.cover,
                type = show.type,
                positiveReviewsQty = recommendation.positiveReviewsQty,
                negativeReviewsQty = recommendation.negativeReviewsQty
                )
    }

}

