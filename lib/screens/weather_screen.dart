import 'package:flutter/material.dart';
import 'package:flutter_spinkit/flutter_spinkit.dart';
import '../models/models.dart';
import '../services/api_service.dart';

class WeatherScreen extends StatefulWidget {
  const WeatherScreen({super.key});

  @override
  State<WeatherScreen> createState() => _WeatherScreenState();
}

class _WeatherScreenState extends State<WeatherScreen> {
  final TextEditingController _cityController = TextEditingController();
  Weather? _weather;
  bool _isLoading = false;
  String? _error;

  Future<void> _searchWeather() async {
    if (_cityController.text.isEmpty) return;

    setState(() {
      _isLoading = true;
      _error = null;
    });

    try {
      final weather = await ApiService.getWeather(_cityController.text);
      setState(() {
        _weather = weather;
        _isLoading = false;
      });
    } catch (e) {
      setState(() {
        _error = 'فشل تحميل الطقس. تأكد من اسم المدينة.';
        _isLoading = false;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(16.0),
      child: Column(
        children: [
          TextField(
            controller: _cityController,
            decoration: InputDecoration(
              hintText: 'أدخل اسم المدينة',
              prefixIcon: const Icon(Icons.search),
              suffixIcon: IconButton(
                icon: const Icon(Icons.arrow_forward),
                onPressed: _searchWeather,
              ),
              border: OutlineInputBorder(
                borderRadius: BorderRadius.circular(12),
              ),
            ),
            onSubmitted: (_) => _searchWeather(),
          ),
          const SizedBox(height: 20),
          if (_isLoading)
            const SpinKitWave(
              color: Colors.blue,
              size: 50,
            )
          else if (_error != null)
            Text(
              _error!,
              style: const TextStyle(color: Colors.red),
            )
          else if (_weather != null)
            Expanded(
              child: Card(
                elevation: 4,
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(16),
                ),
                child: Padding(
                  padding: const EdgeInsets.all(20.0),
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Text(
                        _weather!.cityName,
                        style: const TextStyle(
                          fontSize: 28,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                      const SizedBox(height: 10),
                      Text(
                        '${_weather!.temperature.toStringAsFixed(1)}°C',
                        style: const TextStyle(
                          fontSize: 64,
                          fontWeight: FontWeight.bold,
                          color: Colors.blue,
                        ),
                      ),
                      Text(
                        _weather!.description,
                        style: const TextStyle(
                          fontSize: 20,
                          color: Colors.grey,
                        ),
                      ),
                      const SizedBox(height: 30),
                      Row(
                        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                        children: [
                          _buildWeatherInfo(
                            Icons.water_drop,
                            '${_weather!.humidity}%',
                            'الرطوبة',
                          ),
                          _buildWeatherInfo(
                            Icons.air,
                            '${_weather!.windSpeed} m/s',
                            'الرياح',
                          ),
                          _buildWeatherInfo(
                            Icons.speed,
                            '${_weather!.pressure} hPa',
                            'الضغط',
                          ),
                        ],
                      ),
                    ],
                  ),
                ),
              ),
            ),
        ],
      ),
    );
  }

  Widget _buildWeatherInfo(IconData icon, String value, String label) {
    return Column(
      children: [
        Icon(icon, color: Colors.blue, size: 30),
        const SizedBox(height: 5),
        Text(
          value,
          style: const TextStyle(
            fontSize: 16,
            fontWeight: FontWeight.bold,
          ),
        ),
        Text(
          label,
          style: const TextStyle(
            fontSize: 12,
            color: Colors.grey,
          ),
        ),
      ],
    );
  }
}
