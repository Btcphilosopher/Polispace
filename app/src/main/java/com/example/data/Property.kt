package com.example.data

enum class PropertyType {
    APARTMENT, HOUSE, STUDIO, COMMERCIAL
}

enum class MarketType {
    BUY, RENT
}

enum class EnergyClass {
    A, B, C, D, E, F
}

data class LocalizedText(
    val pl: String,
    val en: String
) {
    fun get(isEnglish: Boolean): String = if (isEnglish) en else pl
}

data class Property(
    val id: String,
    val title: LocalizedText,
    val description: LocalizedText,
    val address: LocalizedText,
    val city: String,
    val district: LocalizedText,
    val price: Double, // in PLN
    val monthlyRent: Double? = null, // for rental units, additional rent
    val areaSqm: Double,
    val rooms: Int,
    val floor: Int,
    val maxFloors: Int,
    val buildYear: Int,
    val propertyType: PropertyType,
    val marketType: MarketType,
    val energyClass: EnergyClass,
    val energyPerformance: Double, // kWh/m²/year
    val isFurnished: Boolean,
    val agentName: String,
    val agentPhone: String,
    val agentEmail: String,
    val latitude: Double,
    val longitude: Double,
    val crimeRateScore: Double, // 1.0 (very low) to 10.0 (high)
    val transportAccessScore: Double, // 1.0 to 10.0
    val schoolRatingScore: Double, // 1.0 to 10.0
    val infrastructureZone: LocalizedText,
    val imageResName: String? = null // local drawable reference or illustration type
)
