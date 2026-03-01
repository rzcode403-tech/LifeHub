package com.lifehub.app.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    
    // Weather API (OpenWeatherMap)
    val weatherApi: WeatherApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)
    }
    
    // News API
    val newsApi: NewsApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://newsapi.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApi::class.java)
    }
    
    // Currency API (ExchangeRate-API)
    val currencyApi: CurrencyApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://v6.exchangerate-api.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurrencyApi::class.java)
    }
    
    // Crypto API (CoinGecko)
    val cryptoApi: CryptoApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.coingecko.com/api/v3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CryptoApi::class.java)
    }
    
    // Translation API (LibreTranslate)
    val translationApi: TranslationApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://libretranslate.de/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TranslationApi::class.java)
    }
    
    // Countries API
    val countriesApi: CountriesApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://restcountries.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CountriesApi::class.java)
    }
    
    // NASA API
    val nasaApi: NasaApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.nasa.gov/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NasaApi::class.java)
    }
    
    // Fun APIs
    val jokeApi: JokeApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://v2.jokeapi.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(JokeApi::class.java)
    }
    
    val factApi: FactApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://uselessfacts.jsph.pl/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FactApi::class.java)
    }
    
    val quoteApi: QuoteApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.quotable.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuoteApi::class.java)
    }
}
