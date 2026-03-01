package com.lifehub.app.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// ==================== WEATHER API ====================
data class WeatherResponse(
    val main: MainWeather,
    val weather: List<Weather>,
    val wind: Wind,
    val name: String
)

data class MainWeather(
    val temp: Double,
    val humidity: Int,
    val pressure: Int
)

data class Weather(
    val main: String,
    val description: String,
    val icon: String
)

data class Wind(
    val speed: Double
)

interface WeatherApi {
    @GET("weather")
    suspend fun getWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String = "YOUR_API_KEY",
        @Query("units") units: String = "metric"
    ): Response<WeatherResponse>
}

// ==================== NEWS API ====================
data class NewsResponse(
    val articles: List<Article>
)

data class Article(
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String
)

interface NewsApi {
    @GET("v2/top-headlines")
    suspend fun getNews(
        @Query("country") country: String = "us",
        @Query("apiKey") apiKey: String = "YOUR_API_KEY"
    ): Response<NewsResponse>
}

// ==================== CURRENCY API ====================
data class CurrencyResponse(
    val rates: Map<String, Double>
)

interface CurrencyApi {
    @GET("v6/YOUR_API_KEY/latest/{base}")
    suspend fun getRates(
        @Path("base") base: String
    ): Response<CurrencyResponse>
}

// ==================== CRYPTO API ====================
data class CryptoResponse(
    val id: String,
    val name: String,
    val current_price: Double,
    val price_change_percentage_24h: Double,
    val image: String
)

interface CryptoApi {
    @GET("coins/markets")
    suspend fun getCrypto(
        @Query("vs_currency") currency: String = "usd",
        @Query("order") order: String = "market_cap_desc",
        @Query("per_page") perPage: Int = 20,
        @Query("page") page: Int = 1
    ): Response<List<CryptoResponse>>
}

// ==================== TRANSLATION API ====================
data class TranslationResponse(
    val translatedText: String
)

interface TranslationApi {
    @GET("translate")
    suspend fun translate(
        @Query("q") text: String,
        @Query("source") source: String,
        @Query("target") target: String
    ): Response<TranslationResponse>
}

// ==================== COUNTRIES API ====================
data class Country(
    val name: Name,
    val capital: List<String>?,
    val population: Long,
    val region: String,
    val flags: Flags,
    val currencies: Map<String, CurrencyInfo>?
)

data class Name(
    val common: String,
    val official: String
)

data class Flags(
    val png: String
)

data class CurrencyInfo(
    val name: String,
    val symbol: String?
)

interface CountriesApi {
    @GET("v3.1/all")
    suspend fun getAllCountries(): Response<List<Country>>
}

// ==================== NASA API ====================
data class NasaResponse(
    val title: String,
    val explanation: String,
    val url: String,
    val date: String,
    val media_type: String
)

interface NasaApi {
    @GET("planetary/apod")
    suspend fun getApod(
        @Query("api_key") apiKey: String = "DEMO_KEY"
    ): Response<NasaResponse>
}

// ==================== FUN APIs ====================
data class JokeResponse(
    val setup: String?,
    val delivery: String?,
    val joke: String?,
    val type: String
)

interface JokeApi {
    @GET("joke/Any")
    suspend fun getRandomJoke(): Response<JokeResponse>
}

data class FactResponse(
    val text: String
)

interface FactApi {
    @GET("random.json?language=en")
    suspend fun getRandomFact(): Response<FactResponse>
}

data class QuoteResponse(
    val content: String,
    val author: String
)

interface QuoteApi {
    @GET("random")
    suspend fun getRandomQuote(): Response<QuoteResponse>
}
