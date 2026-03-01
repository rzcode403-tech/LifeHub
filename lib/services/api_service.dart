import 'dart:convert';
import 'package:http/http.dart' as http;
import '../models/models.dart';

class ApiService {
  static const String weatherBaseUrl = 'https://api.openweathermap.org/data/2.5';
  static const String newsBaseUrl = 'https://newsapi.org/v2';
  static const String cryptoBaseUrl = 'https://api.coingecko.com/api/v3';
  static const String countriesBaseUrl = 'https://restcountries.com/v3.1';
  static const String nasaBaseUrl = 'https://api.nasa.gov/planetary';
  static const String jokeBaseUrl = 'https://v2.jokeapi.dev/joke';
  static const String factBaseUrl = 'https://uselessfacts.jsph.pl/api/v2/facts';
  static const String quoteBaseUrl = 'https://api.quotable.io';
  static const String translateBaseUrl = 'https://libretranslate.de';
  static const String currencyBaseUrl = 'https://api.exchangerate-api.com/v4/latest';

  // API Keys - يمكنك تغييرها لاحقاً
  static const String weatherApiKey = 'YOUR_API_KEY';
  static const String newsApiKey = 'YOUR_API_KEY';
  static const String nasaApiKey = 'DEMO_KEY';

  // ========== WEATHER ==========
  static Future<Weather> getWeather(String city) async {
    final response = await http.get(
      Uri.parse('$weatherBaseUrl/weather?q=$city&appid=$weatherApiKey&units=metric'),
    );
    
    if (response.statusCode == 200) {
      return Weather.fromJson(jsonDecode(response.body));
    } else {
      throw Exception('Failed to load weather');
    }
  }

  // ========== NEWS ==========
  static Future<List<NewsArticle>> getNews() async {
    final response = await http.get(
      Uri.parse('$newsBaseUrl/top-headlines?country=us&apiKey=$newsApiKey'),
    );
    
    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      final List articles = data['articles'];
      return articles.map((e) => NewsArticle.fromJson(e)).toList();
    } else {
      throw Exception('Failed to load news');
    }
  }

  // ========== CRYPTO ==========
  static Future<List<Crypto>> getCrypto() async {
    final response = await http.get(
      Uri.parse('$cryptoBaseUrl/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=20&page=1'),
    );
    
    if (response.statusCode == 200) {
      final List data = jsonDecode(response.body);
      return data.map((e) => Crypto.fromJson(e)).toList();
    } else {
      throw Exception('Failed to load crypto');
    }
  }

  // ========== COUNTRIES ==========
  static Future<List<Country>> getCountries() async {
    final response = await http.get(
      Uri.parse('$countriesBaseUrl/all'),
    );
    
    if (response.statusCode == 200) {
      final List data = jsonDecode(response.body);
      return data.map((e) => Country.fromJson(e)).toList();
    } else {
      throw Exception('Failed to load countries');
    }
  }

  // ========== NASA ==========
  static Future<NasaApod> getNasaApod() async {
    final response = await http.get(
      Uri.parse('$nasaBaseUrl/apod?api_key=$nasaApiKey'),
    );
    
    if (response.statusCode == 200) {
      return NasaApod.fromJson(jsonDecode(response.body));
    } else {
      throw Exception('Failed to load NASA APOD');
    }
  }

  // ========== JOKE ==========
  static Future<Joke> getJoke() async {
    final response = await http.get(
      Uri.parse('$jokeBaseUrl/Any'),
    );
    
    if (response.statusCode == 200) {
      return Joke.fromJson(jsonDecode(response.body));
    } else {
      throw Exception('Failed to load joke');
    }
  }

  // ========== FACT ==========
  static Future<Fact> getFact() async {
    final response = await http.get(
      Uri.parse('$factBaseUrl/random?language=en'),
    );
    
    if (response.statusCode == 200) {
      return Fact.fromJson(jsonDecode(response.body));
    } else {
      throw Exception('Failed to load fact');
    }
  }

  // ========== QUOTE ==========
  static Future<Quote> getQuote() async {
    final response = await http.get(
      Uri.parse('$quoteBaseUrl/random'),
    );
    
    if (response.statusCode == 200) {
      return Quote.fromJson(jsonDecode(response.body));
    } else {
      throw Exception('Failed to load quote');
    }
  }

  // ========== TRANSLATE ==========
  static Future<String> translate(String text, String source, String target) async {
    final response = await http.post(
      Uri.parse('$translateBaseUrl/translate'),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode({
        'q': text,
        'source': source,
        'target': target,
      }),
    );
    
    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      return data['translatedText'] ?? 'Translation error';
    } else {
      throw Exception('Failed to translate');
    }
  }

  // ========== CURRENCY ==========
  static Future<Map<String, dynamic>> getExchangeRates(String base) async {
    final response = await http.get(
      Uri.parse('$currencyBaseUrl/$base'),
    );
    
    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      return data['rates'];
    } else {
      throw Exception('Failed to load exchange rates');
    }
  }
}
