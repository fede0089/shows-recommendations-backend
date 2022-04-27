package com.showsrecommendations.shows.domain.entities

data class Show(val id:String,
                val title: String,
                val genres: List<String>,
                val year:String,
                val cover:String,
                val type:String,
                val overview:String,
                val imdbId: String,
                val externalId:String)
