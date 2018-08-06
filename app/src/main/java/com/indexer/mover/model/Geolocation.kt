package com.indexer.mover.model

import java.io.Serializable

data class Geolocation(
    val latitude: Double,
    val longitude: Double
) : Serializable