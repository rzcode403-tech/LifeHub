// Weather Model
class Weather {
  final String cityName;
  final double temperature;
  final String description;
  final int humidity;
  final double windSpeed;
  final int pressure;

  Weather({
    required this.cityName,
    required this.temperature,
    required this.description,
    required this.humidity,
    required this.windSpeed,
    required this.pressure,
  });

  factory Weather.fromJson(Map<String, dynamic> json) {
    return Weather(
      cityName: json['name'] ?? '',
      temperature: (json['main']['temp'] as num).toDouble(),
      description: json['weather'][0]['description'] ?? '',
      humidity: json['main']['humidity'] ?? 0,
      windSpeed: (json['wind']['speed'] as num).toDouble(),
      pressure: json['main']['pressure'] ?? 0,
    );
  }
}

// News Model
class NewsArticle {
  final String title;
  final String? description;
  final String url;
  final String? imageUrl;
  final String publishedAt;

  NewsArticle({
    required this.title,
    this.description,
    required this.url,
    this.imageUrl,
    required this.publishedAt,
  });

  factory NewsArticle.fromJson(Map<String, dynamic> json) {
    return NewsArticle(
      title: json['title'] ?? '',
      description: json['description'],
      url: json['url'] ?? '',
      imageUrl: json['urlToImage'],
      publishedAt: json['publishedAt'] ?? '',
    );
  }
}

// Crypto Model
class Crypto {
  final String id;
  final String name;
  final double currentPrice;
  final double priceChange24h;
  final String imageUrl;

  Crypto({
    required this.id,
    required this.name,
    required this.currentPrice,
    required this.priceChange24h,
    required this.imageUrl,
  });

  factory Crypto.fromJson(Map<String, dynamic> json) {
    return Crypto(
      id: json['id'] ?? '',
      name: json['name'] ?? '',
      currentPrice: (json['current_price'] as num).toDouble(),
      priceChange24h: (json['price_change_percentage_24h'] as num?)?.toDouble() ?? 0.0,
      imageUrl: json['image'] ?? '',
    );
  }
}

// Country Model
class Country {
  final String name;
  final String? capital;
  final int population;
  final String region;
  final String flagUrl;
  final String? currency;

  Country({
    required this.name,
    this.capital,
    required this.population,
    required this.region,
    required this.flagUrl,
    this.currency,
  });

  factory Country.fromJson(Map<String, dynamic> json) {
    final currencies = json['currencies'] as Map<String, dynamic>?;
    String? currencyName;
    if (currencies != null && currencies.isNotEmpty) {
      currencyName = currencies.values.first['name'];
    }

    return Country(
      name: json['name']['common'] ?? '',
      capital: (json['capital'] as List?)?.isNotEmpty == true 
          ? json['capital'][0] 
          : null,
      population: json['population'] ?? 0,
      region: json['region'] ?? '',
      flagUrl: json['flags']['png'] ?? '',
      currency: currencyName,
    );
  }
}

// NASA Model
class NasaApod {
  final String title;
  final String explanation;
  final String url;
  final String date;
  final String mediaType;

  NasaApod({
    required this.title,
    required this.explanation,
    required this.url,
    required this.date,
    required this.mediaType,
  });

  factory NasaApod.fromJson(Map<String, dynamic> json) {
    return NasaApod(
      title: json['title'] ?? '',
      explanation: json['explanation'] ?? '',
      url: json['url'] ?? '',
      date: json['date'] ?? '',
      mediaType: json['media_type'] ?? '',
    );
  }
}

// Fun Models
class Joke {
  final String text;
  final String type;

  Joke({required this.text, required this.type});

  factory Joke.fromJson(Map<String, dynamic> json) {
    String jokeText;
    if (json['type'] == 'single') {
      jokeText = json['joke'] ?? '';
    } else {
      jokeText = '${json['setup']}\n\n${json['delivery']}';
    }
    return Joke(text: jokeText, type: json['type'] ?? '');
  }
}

class Fact {
  final String text;

  Fact({required this.text});

  factory Fact.fromJson(Map<String, dynamic> json) {
    return Fact(text: json['text'] ?? '');
  }
}

class Quote {
  final String content;
  final String author;

  Quote({required this.content, required this.author});

  factory Quote.fromJson(Map<String, dynamic> json) {
    return Quote(
      content: json['content'] ?? '',
      author: json['author'] ?? '',
    );
  }
}
