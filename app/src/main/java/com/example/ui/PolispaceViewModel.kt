package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PolispaceViewModel(application: Application) : AndroidViewModel(application) {

    // Room Database Initialisation
    private val database: PolispaceDatabase by lazy {
        Room.databaseBuilder(
            application.applicationContext,
            PolispaceDatabase::class.java,
            "polispace_database"
        ).fallbackToDestructiveMigration().build()
    }

    private val repository: PolispaceRepository by lazy {
        PolispaceRepository(database.polispaceDao())
    }

    // --- Dynamic UI State ---
    val isEnglish = MutableStateFlow(false) // Toggle between PL and EN
    val currentRole = MutableStateFlow("BUYER") // BUYER, SELLER, AGENT_CRM, DEV_CONSOLE

    // --- Search & Advanced Filters ---
    val searchQuery = MutableStateFlow("")
    val filterPropertyType = MutableStateFlow<PropertyType?>(null)
    val filterMarketType = MutableStateFlow<MarketType?>(null)
    val filterMinPrice = MutableStateFlow<Double?>(null)
    val filterMaxPrice = MutableStateFlow<Double?>(null)
    val filterMinArea = MutableStateFlow<Double?>(null)
    val filterEnergyClass = MutableStateFlow<EnergyClass?>(null)

    // Favorite listings IDs
    val favoriteIds: StateFlow<List<String>> = repository.favoriteIdsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered listings Flow
    @Suppress("UNCHECKED_CAST")
    val properties: StateFlow<List<Property>> = combine(
        listOf(
            repository.allPropertiesFlow,
            searchQuery,
            filterPropertyType,
            filterMarketType,
            filterMinPrice,
            filterMaxPrice,
            filterMinArea,
            filterEnergyClass
        )
    ) { array ->
        val allProps = array[0] as List<Property>
        val query = array[1] as String
        val propType = array[2] as PropertyType?
        val marketType = array[3] as MarketType?
        val minPrice = array[4] as Double?
        val maxPrice = array[5] as Double?
        val minArea = array[6] as Double?
        val energy = array[7] as EnergyClass?

        allProps.filter { prop ->
            val matchQuery = query.isEmpty() ||
                    prop.city.contains(query, ignoreCase = true) ||
                    prop.district.get(true).contains(query, ignoreCase = true) ||
                    prop.district.get(false).contains(query, ignoreCase = true) ||
                    prop.title.get(true).contains(query, ignoreCase = true) ||
                    prop.title.get(false).contains(query, ignoreCase = true)

            val matchType = propType == null || prop.propertyType == propType
            val matchMarket = marketType == null || prop.marketType == marketType
            val matchMinPrice = minPrice == null || prop.price >= minPrice
            val matchMaxPrice = maxPrice == null || prop.price <= maxPrice
            val matchMinArea = minArea == null || prop.areaSqm >= minArea
            val matchEnergy = energy == null || prop.energyClass == energy

            matchQuery && matchType && matchMarket && matchMinPrice && matchMaxPrice && matchMinArea && matchEnergy
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Selected Property Detail State ---
    val selectedProperty = MutableStateFlow<Property?>(null)
    val view3dMode = MutableStateFlow(false)
    val arSimulationActive = MutableStateFlow(false)

    // --- Real-time Bidding & Offer States (Simulated live WebSockets stream) ---
    val activeBids = MutableStateFlow<Map<String, List<BidEntry>>>(emptyMap())

    // --- Mortgage State ---
    val mortgagePrice = MutableStateFlow(1000000.0)
    val mortgageDeposit = MutableStateFlow(200000.0)
    val mortgageYears = MutableStateFlow(25)
    val mortgageRate = MutableStateFlow(6.8)

    val mortgageResult: StateFlow<MortgageResult> = combine(
        mortgagePrice, mortgageDeposit, mortgageYears, mortgageRate
    ) { price, deposit, years, rate ->
        repository.calculateMortgage(price, deposit, years, rate)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MortgageResult(0.0, 0.0, 100.0, "AAA", true))

    // --- AI pricing recommendation engine (Sellers/Buyers) ---
    val aiValuationResult = MutableStateFlow("")
    val isAiValuationLoading = MutableStateFlow(false)

    // --- Seller Form Fields ---
    val sellerTitlePl = MutableStateFlow("")
    val sellerTitleEn = MutableStateFlow("")
    val sellerDescPl = MutableStateFlow("")
    val sellerDescEn = MutableStateFlow("")
    val sellerAddressPl = MutableStateFlow("")
    val sellerAddressEn = MutableStateFlow("")
    val sellerCity = MutableStateFlow("Warszawa")
    val sellerDistrictPl = MutableStateFlow("")
    val sellerDistrictEn = MutableStateFlow("")
    val sellerPrice = MutableStateFlow("")
    val sellerArea = MutableStateFlow("")
    val sellerRooms = MutableStateFlow("3")
    val sellerFloor = MutableStateFlow("2")
    val sellerMaxFloor = MutableStateFlow("5")
    val sellerBuildYear = MutableStateFlow("2024")
    val sellerType = MutableStateFlow(PropertyType.APARTMENT)
    val sellerMarketType = MutableStateFlow(MarketType.BUY)
    val sellerEnergyClass = MutableStateFlow(EnergyClass.A)
    val sellerEnergyPerformance = MutableStateFlow("45.0")
    val sellerIsFurnished = MutableStateFlow(true)

    // --- Agent / CRM State ---
    val selectedCrmTab = MutableStateFlow("PIPELINE") // PIPELINE, LEADS, ANALYTICS
    val crmLeads = MutableStateFlow(
        listOf(
            CrmLead("Aneta Dąbrowska", "Złota 44 Penthouse", "3,500,000 PLN Offer", "NEGOTIATION", "+48 501 112 233"),
            CrmLead("John Smith", "Krakow Studio", "Rental Application", "APPROVED_SCREENING", "+44 7911 123456"),
            CrmLead("Piotr Zieliński", "Passive House Wroclaw", "Booked viewing (July 4th)", "SCHEDULED", "+48 602 888 999"),
            CrmLead("Monika Wiśniewska", "Baltic Sea View", "Finance Verification", "PRE_APPROVAL", "+48 733 444 555")
        )
    )

    init {
        // Hydrate live bids simulation
        viewModelScope.launch {
            activeBids.value = mapOf(
                "prop_wawa_center" to listOf(
                    BidEntry("Investor_PL", 4900000.0, "2 min ago"),
                    BidEntry("BerlinCapital", 4850000.0, "1 hour ago")
                ),
                "prop_gdansk_sea" to listOf(
                    BidEntry("Sopot_Lover", 1950000.0, "15 min ago")
                )
            )
        }
    }

    // --- Actions ---

    fun toggleLanguage() {
        isEnglish.value = !isEnglish.value
    }

    fun setRole(role: String) {
        currentRole.value = role
        // Clean filtered parameters on swap
        resetFilters()
    }

    fun resetFilters() {
        filterPropertyType.value = null
        filterMarketType.value = null
        filterMinPrice.value = null
        filterMaxPrice.value = null
        filterMinArea.value = null
        filterEnergyClass.value = null
        searchQuery.value = ""
    }

    fun toggleFavorite(propertyId: String) {
        viewModelScope.launch {
            repository.toggleFavorite(propertyId)
        }
    }

    fun selectProperty(property: Property?) {
        selectedProperty.value = property
        view3dMode.value = false
        arSimulationActive.value = false
        if (property != null) {
            // Preset mortgage values
            mortgagePrice.value = property.price
            mortgageDeposit.value = property.price * 0.20 // 20% downpayment
            // Trigger AI valuation summary
            triggerAiValuation(property)
        }
    }

    fun submitNewOffer(propertyId: String, bidder: String, amount: Double) {
        val current = activeBids.value.toMutableMap()
        val bids = current[propertyId]?.toMutableList() ?: mutableListOf()
        bids.add(0, BidEntry(bidder, amount, "Just now"))
        current[propertyId] = bids
        activeBids.value = current
    }

    fun triggerAiValuation(property: Property) {
        isAiValuationLoading.value = true
        viewModelScope.launch {
            aiValuationResult.value = GeminiValuationEngine.fetchAiValuation(
                city = property.city,
                district = property.district.get(isEnglish.value),
                areaSqm = property.areaSqm,
                rooms = property.rooms,
                floor = property.floor,
                buildYear = property.buildYear,
                propertyType = property.propertyType.name,
                isEnglish = isEnglish.value
            )
            isAiValuationLoading.value = false
        }
    }

    fun triggerCustomAiValuation() {
        isAiValuationLoading.value = true
        viewModelScope.launch {
            aiValuationResult.value = GeminiValuationEngine.fetchAiValuation(
                city = sellerCity.value,
                district = sellerDistrictPl.value.ifEmpty { "Śródmieście" },
                areaSqm = sellerArea.value.toDoubleOrNull() ?: 50.0,
                rooms = sellerRooms.value.toIntOrNull() ?: 2,
                floor = sellerFloor.value.toIntOrNull() ?: 1,
                buildYear = sellerBuildYear.value.toIntOrNull() ?: 2022,
                propertyType = sellerType.value.name,
                isEnglish = isEnglish.value
            )
            isAiValuationLoading.value = false
        }
    }

    fun submitSellerListing(onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.listProperty(
                titlePl = sellerTitlePl.value.ifEmpty { "Nowe Mieszkanie" },
                titleEn = sellerTitleEn.value.ifEmpty { "New Modern Listing" },
                descPl = sellerDescPl.value.ifEmpty { "Brak opisu" },
                descEn = sellerDescEn.value.ifEmpty { "No description available" },
                addressPl = sellerAddressPl.value.ifEmpty { "Ul. Nowa, Warszawa" },
                addressEn = sellerAddressEn.value.ifEmpty { "Nowa Street, Warsaw" },
                city = sellerCity.value,
                districtPl = sellerDistrictPl.value.ifEmpty { "Śródmieście" },
                districtEn = sellerDistrictEn.value.ifEmpty { "City Center" },
                price = sellerPrice.value.toDoubleOrNull() ?: 600000.0,
                areaSqm = sellerArea.value.toDoubleOrNull() ?: 55.0,
                rooms = sellerRooms.value.toIntOrNull() ?: 2,
                floor = sellerFloor.value.toIntOrNull() ?: 1,
                maxFloors = sellerMaxFloor.value.toIntOrNull() ?: 5,
                buildYear = sellerBuildYear.value.toIntOrNull() ?: 2024,
                propertyType = sellerType.value,
                marketType = sellerMarketType.value,
                energyClass = sellerEnergyClass.value,
                energyPerformance = sellerEnergyPerformance.value.toDoubleOrNull() ?: 45.0,
                isFurnished = sellerIsFurnished.value,
                agentName = "Polispace Agent",
                agentPhone = "+48 555 111 222",
                agentEmail = "my.agent@polispace.pl"
            )

            // Clear Seller inputs
            sellerTitlePl.value = ""
            sellerTitleEn.value = ""
            sellerDescPl.value = ""
            sellerDescEn.value = ""
            sellerAddressPl.value = ""
            sellerAddressEn.value = ""
            sellerPrice.value = ""
            sellerArea.value = ""

            onSuccess()
        }
    }

    fun getLandRegistry(propertyId: String): LandRegistryInfo {
        return repository.queryPolishLandRegistry(propertyId)
    }
}

data class BidEntry(
    val bidder: String,
    val amount: Double,
    val timeAgo: String
)

data class CrmLead(
    val name: String,
    val propertyTitle: String,
    val detail: String,
    val stage: String, // NEGOTIATION, APPROVED_SCREENING, SCHEDULED, PRE_APPROVAL
    val contact: String
)
