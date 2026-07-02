package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.util.UUID

class PolispaceRepository(private val dao: PolispaceDao) {

    // Retrieve all active properties: static samples + custom properties created by the user
    val allPropertiesFlow: Flow<List<Property>> = dao.getCustomPropertiesFlow().map { customList ->
        val mappedCustom = customList.map { entity ->
            Property(
                id = entity.id,
                title = LocalizedText(pl = entity.titlePl, en = entity.titleEn),
                description = LocalizedText(pl = entity.descPl, en = entity.descEn),
                address = LocalizedText(pl = entity.addressPl, en = entity.addressEn),
                city = entity.city,
                district = LocalizedText(pl = entity.districtPl, en = entity.districtEn),
                price = entity.price,
                monthlyRent = if (entity.marketType == "RENT") entity.price * 0.15 else null, // estimate rent utilities
                areaSqm = entity.areaSqm,
                rooms = entity.rooms,
                floor = entity.floor,
                maxFloors = entity.maxFloors,
                buildYear = entity.buildYear,
                propertyType = try { PropertyType.valueOf(entity.propertyType) } catch (e: Exception) { PropertyType.APARTMENT },
                marketType = try { MarketType.valueOf(entity.marketType) } catch (e: Exception) { MarketType.BUY },
                energyClass = try { EnergyClass.valueOf(entity.energyClass) } catch (e: Exception) { EnergyClass.B },
                energyPerformance = entity.energyPerformance,
                isFurnished = entity.isFurnished == 1,
                agentName = entity.agentName,
                agentPhone = entity.agentPhone,
                agentEmail = entity.agentEmail,
                latitude = entity.latitude,
                longitude = entity.longitude,
                crimeRateScore = entity.crimeRateScore,
                transportAccessScore = entity.transportAccessScore,
                schoolRatingScore = entity.schoolRatingScore,
                infrastructureZone = LocalizedText(pl = entity.infraZonePl, en = entity.infraZoneEn),
                imageResName = null
            )
        }
        SampleDataset.properties + mappedCustom
    }

    // Get favorite IDs from the Room database
    val favoriteIdsFlow: Flow<List<String>> = dao.getFavoriteIdsFlow()

    // Add list to favorites
    suspend fun toggleFavorite(propertyId: String) {
        val isFav = dao.isFavorite(propertyId)
        if (isFav) {
            dao.deleteFavorite(propertyId)
        } else {
            dao.insertFavorite(FavoriteEntity(propertyId))
        }
    }

    // Create a new property listing from the Seller's tab
    suspend fun listProperty(
        titlePl: String,
        titleEn: String,
        descPl: String,
        descEn: String,
        addressPl: String,
        addressEn: String,
        city: String,
        districtPl: String,
        districtEn: String,
        price: Double,
        areaSqm: Double,
        rooms: Int,
        floor: Int,
        maxFloors: Int,
        buildYear: Int,
        propertyType: PropertyType,
        marketType: MarketType,
        energyClass: EnergyClass,
        energyPerformance: Double,
        isFurnished: Boolean,
        agentName: String,
        agentPhone: String,
        agentEmail: String,
        latitude: Double = 52.2297, // default Warsaw
        longitude: Double = 21.0122,
        crimeRateScore: Double = 2.0,
        transportAccessScore: Double = 8.0,
        schoolRatingScore: Double = 7.5,
        infraPl: String = "Strefa Standardowa",
        infraEn: String = "Standard Utility Zone"
    ) {
        val uniqueId = "custom_" + UUID.randomUUID().toString().take(8)
        val entity = CustomPropertyEntity(
            id = uniqueId,
            titlePl = titlePl,
            titleEn = titleEn,
            descPl = descPl,
            descEn = descEn,
            addressPl = addressPl,
            addressEn = addressEn,
            city = city,
            districtPl = districtPl,
            districtEn = districtEn,
            price = price,
            areaSqm = areaSqm,
            rooms = rooms,
            floor = floor,
            maxFloors = maxFloors,
            buildYear = buildYear,
            propertyType = propertyType.name,
            marketType = marketType.name,
            energyClass = energyClass.name,
            energyPerformance = energyPerformance,
            isFurnished = if (isFurnished) 1 else 0,
            agentName = agentName,
            agentPhone = agentPhone,
            agentEmail = agentEmail,
            latitude = latitude,
            longitude = longitude,
            crimeRateScore = crimeRateScore,
            transportAccessScore = transportAccessScore,
            schoolRatingScore = schoolRatingScore,
            infraZonePl = infraPl,
            infraZoneEn = infraEn
        )
        dao.insertCustomProperty(entity)
    }

    // Clean custom listings
    suspend fun deleteCustomProperty(id: String) {
        dao.deleteCustomProperty(id)
    }

    // --- Mortgage Calculations ---
    fun calculateMortgage(
        propertyPrice: Double,
        depositAmount: Double,
        loanTermYears: Int,
        interestRatePercent: Double
    ): MortgageResult {
        val principal = propertyPrice - depositAmount
        if (principal <= 0) return MortgageResult(0.0, 0.0, 100.0, "EXCELLENT", true)

        val r = (interestRatePercent / 100.0) / 12.0
        val n = loanTermYears * 12
        val monthlyPayment = if (r == 0.0) {
            principal / n
        } else {
            principal * (r * Math.pow(1 + r, n.toDouble())) / (Math.pow(1 + r, n.toDouble()) - 1)
        }

        val totalRepayment = monthlyPayment * n
        val eligibilityScore = when {
            depositAmount / propertyPrice >= 0.20 -> 85.0
            depositAmount / propertyPrice >= 0.10 -> 65.0
            else -> 40.0
        }

        val rating = when {
            eligibilityScore >= 80.0 -> "EXCELLENT (AAA)"
            eligibilityScore >= 60.0 -> "STABLE (BBB)"
            else -> "HIGH RISK (C)"
        }

        return MortgageResult(
            monthlyPayment = monthlyPayment,
            totalRepayment = totalRepayment,
            eligibilityScore = eligibilityScore,
            riskRating = rating,
            isApproved = depositAmount / propertyPrice >= 0.10 // Polish banks require minimum 10% downpayment
        )
    }

    // --- Simulated Land Registry Query ---
    fun queryPolishLandRegistry(propertyId: String): LandRegistryInfo {
        val hashValue = (propertyId.hashCode().coerceAtLeast(0) % 89999) + 10000
        val kwNumber = "WA1M/$hashValue/8" // Standard Polish Księga Wieczysta Format

        return when (propertyId) {
            "prop_wawa_center" -> LandRegistryInfo(
                kwNumber = kwNumber,
                owner = "POLISPACE Development SA",
                mortgagesSection3 = "Brak wpisów / Clear of Claims",
                rightsSection4 = "Służebność przesyłu na rzecz innogy / Utility Servitude",
                isVerified = true
            )
            "prop_krak_old" -> LandRegistryInfo(
                kwNumber = "KR1P/20485/3",
                owner = "Helena Maria Nowak",
                mortgagesSection3 = "Bsz. Hipoteka przymusowa Skarbu Państwa - Wykreślona",
                rightsSection4 = "Dożywocie na rzecz lokatora - Zgłoszono Wygaśnięcie",
                isVerified = true
            )
            else -> LandRegistryInfo(
                kwNumber = "PL3D/$hashValue/1",
                owner = "Sprawdzono Cyfrowo / Digitally Verified Owner",
                mortgagesSection3 = "Czysty wpis hipoteczny / No Active Mortgages",
                rightsSection4 = "Brak roszczeń osób trzecich / Clear of Claims",
                isVerified = true
            )
        }
    }
}

data class MortgageResult(
    val monthlyPayment: Double,
    val totalRepayment: Double,
    val eligibilityScore: Double,
    val riskRating: String,
    val isApproved: Boolean
)

data class LandRegistryInfo(
    val kwNumber: String,
    val owner: String,
    val mortgagesSection3: String,
    val rightsSection4: String,
    val isVerified: Boolean
)
