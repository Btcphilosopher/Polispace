package com.example.data

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

// Serialization Models for Gemini API using standard serialization/Moshi compatibility
data class Part(val text: String)

data class Content(val parts: List<Part>)

data class GenerationConfig(val temperature: Float = 0.2f)

data class GenerateContentRequest(
    val contents: List<Content>,
    val generationConfig: GenerationConfig = GenerationConfig()
)

data class Candidate(val content: Content)

data class GenerateContentResponse(val candidates: List<Candidate>)

interface GeminiApiService {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GenerateContentRequest
    ): GenerateContentResponse
}

object RetrofitClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    // Since converter-moshi is imported, we can configure a basic Retrofit with Moshi
    private val moshi = com.squareup.moshi.Moshi.Builder()
        .addLast(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory())
        .build()

    val service: GeminiApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GeminiApiService::class.java)
    }
}

object GeminiValuationEngine {

    suspend fun fetchAiValuation(
        city: String,
        district: String,
        areaSqm: Double,
        rooms: Int,
        floor: Int,
        buildYear: Int,
        propertyType: String,
        isEnglish: Boolean
    ): String = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY" || apiKey == "null") {
            // Graceful fallback to rich local rule engine
            return@withContext getRuleBasedValuation(city, district, areaSqm, rooms, floor, buildYear, propertyType, isEnglish)
        }

        val prompt = """
            You are POLISPACE AI, the next-generation real estate intelligence engine for Poland.
            Provide a professional, analytical market valuation and intelligence report for a property in Poland.
            Property details:
            - City: $city
            - District/Neighborhood: $district
            - Area: $areaSqm m²
            - Rooms: $rooms
            - Floor: $floor
            - Build Year: $buildYear
            - Type: $propertyType
            
            Format your response in exactly 4 concise, high-impact bullet points.
            Write the response in ${if (isEnglish) "ENGLISH" else "POLISH"}.
            Focus on:
            1. Estimated price range in PLN (base price and price/sqm) based on realistic 2026 Polish market trends.
            2. District investment potential (Mokotów, Śródmieście, Old Town, etc.).
            3. Building efficiency and age considerations (e.g. vintage tenement vs brand new building).
            4. Future valuation trend (e.g., development zones, transit lines like metro expansion).
            Do not include general greeting words. Keep it strictly professional, architectural, and data-dense.
        """.trimIndent()

        val request = GenerateContentRequest(
            contents = listOf(Content(parts = listOf(Part(text = prompt))))
        )

        try {
            val response = RetrofitClient.service.generateContent(apiKey, request)
            response.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text 
                ?: if (isEnglish) "Error parsing AI response. Please try again." else "Błąd przetwarzania odpowiedzi AI. Spróbuj ponownie."
        } catch (e: Exception) {
            val errMsg = e.localizedMessage ?: "Network error"
            if (isEnglish) {
                "POLISPACE live AI connection failed ($errMsg).\nFalling back to Local Analytics Engine:\n\n${getRuleBasedValuation(city, district, areaSqm, rooms, floor, buildYear, propertyType, isEnglish = true)}"
            } else {
                "Połączenie z serwerem live AI nie powiodło się ($errMsg).\nUruchomiono Lokalny Silnik Analityczny:\n\n${getRuleBasedValuation(city, district, areaSqm, rooms, floor, buildYear, propertyType, isEnglish = false)}"
            }
        }
    }

    private fun getRuleBasedValuation(
        city: String,
        district: String,
        areaSqm: Double,
        rooms: Int,
        floor: Int,
        buildYear: Int,
        propertyType: String,
        isEnglish: Boolean
    ): String {
        val basePricePerSqm = when (city.lowercase()) {
            "warszawa", "warsaw" -> 18500.0
            "kraków", "krakow" -> 16000.0
            "gdańsk", "gdansk" -> 14500.0
            "wrocław", "wroclaw" -> 13800.0
            "poznan", "poznań" -> 12000.0
            else -> 9500.0
        }

        // Apply some modifiers
        val ageModifier = if (buildYear > 2020) 1.15 else if (buildYear < 1950) 0.95 else 1.0
        val sizeModifier = if (areaSqm < 40.0) 1.10 else if (areaSqm > 100.0) 0.90 else 1.0
        val estimatedPerSqm = basePricePerSqm * ageModifier * sizeModifier
        val estimatedTotal = estimatedPerSqm * areaSqm

        val formattedSqm = String.format("%,.0f PLN", estimatedPerSqm)
        val formattedTotal = String.format("%,.0f PLN", estimatedTotal)
        val lowerBound = String.format("%,.0f PLN", estimatedTotal * 0.95)
        val upperBound = String.format("%,.0f PLN", estimatedTotal * 1.05)

        val demandScore = ((10.0 - (estimatedPerSqm / 5000.0)) + (rooms * 0.5) + (if (buildYear > 2020) 2.0 else 0.5)).coerceIn(1.0, 10.0)
        val formattedDemand = String.format("%.1f/10", demandScore)

        if (isEnglish) {
            return """
                • **Estimated Valuation**: $lowerBound - $upperBound (Average: $formattedSqm/m²)
                • **District Demand**: High score ($formattedDemand). $district in $city is currently a high-performance market with consistent annual gains.
                • **Structure Assessment**: Built in $buildYear. Efficiency rating is highly competitive. Passive heating modifications recommended.
                • **Future Outlook**: Upward pressure. Underpinned by Polish mortgage subsidy schemes and regional infrastructure developments.
            """.trimIndent()
        } else {
            return """
                • **Szacowana Wycena**: $lowerBound - $upperBound (Średnio: $formattedSqm/m²)
                • **Popyt Dzielnicowy**: Wysoki wskaźnik ($formattedDemand). $district w mieście $city to obecnie bardzo chłonny rynek o stabilnym rocznym wzroście.
                • **Ocena Budynku**: Rok budowy $buildYear. Efektywność energetyczna spełnia nowoczesne normy, zalecany montaż fotowoltaiki.
                • **Prognoza Cenowa**: Trend wzrostowy. Rynek stymulowany programami rządowymi oraz rozbudową infrastruktury tramwajowo-metralnej.
            """.trimIndent()
        }
    }
}
