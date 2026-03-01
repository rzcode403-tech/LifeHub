import 'package:flutter/material.dart';
import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter_spinkit/flutter_spinkit.dart';
import '../models/models.dart';
import '../services/api_service.dart';

class CountriesScreen extends StatefulWidget {
  const CountriesScreen({super.key});

  @override
  State<CountriesScreen> createState() => _CountriesScreenState();
}

class _CountriesScreenState extends State<CountriesScreen> {
  List<Country> _countries = [];
  List<Country> _filteredCountries = [];
  bool _isLoading = true;
  String? _error;
  final TextEditingController _searchController = TextEditingController();

  @override
  void initState() {
    super.initState();
    _loadCountries();
  }

  Future<void> _loadCountries() async {
    try {
      final countries = await ApiService.getCountries();
      setState(() {
        _countries = countries;
        _filteredCountries = countries;
        _isLoading = false;
      });
    } catch (e) {
      setState(() {
        _error = 'فشل تحميل البيانات';
        _isLoading = false;
      });
    }
  }

  void _filterCountries(String query) {
    setState(() {
      _filteredCountries = _countries
          .where((country) =>
              country.name.toLowerCase().contains(query.toLowerCase()))
          .toList();
    });
  }

  @override
  Widget build(BuildContext context) {
    if (_isLoading) {
      return const Center(
        child: SpinKitWave(color: Colors.blue, size: 50),
      );
    }

    if (_error != null) {
      return Center(child: Text(_error!));
    }

    return Column(
      children: [
        Padding(
          padding: const EdgeInsets.all(8.0),
          child: TextField(
            controller: _searchController,
            decoration: const InputDecoration(
              hintText: 'ابحث عن دولة...',
              prefixIcon: Icon(Icons.search),
              border: OutlineInputBorder(),
            ),
            onChanged: _filterCountries,
          ),
        ),
        Expanded(
          child: ListView.builder(
            itemCount: _filteredCountries.length,
            itemBuilder: (context, index) {
              final country = _filteredCountries[index];
              return Card(
                margin: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                child: ListTile(
                  leading: CachedNetworkImage(
                    imageUrl: country.flagUrl,
                    width: 50,
                    placeholder: (context, url) => const CircularProgressIndicator(),
                    errorWidget: (context, url, error) => const Icon(Icons.error),
                  ),
                  title: Text(
                    country.name,
                    style: const TextStyle(fontWeight: FontWeight.bold),
                  ),
                  subtitle: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text('العاصمة: ${country.capital ?? 'غير معروفة'}'),
                      Text('السكان: ${_formatNumber(country.population)}'),
                      if (country.currency != null)
                        Text('العملة: ${country.currency}'),
                    ],
                  ),
                  trailing: Chip(
                    label: Text(country.region),
                    backgroundColor: Colors.blue[100],
                  ),
                ),
              );
            },
          ),
        ),
      ],
    );
  }

  String _formatNumber(int number) {
    if (number >= 1000000) {
      return '${(number / 1000000).toStringAsFixed(1)} مليون';
    } else if (number >= 1000) {
      return '${(number / 1000).toStringAsFixed(1)} ألف';
    }
    return number.toString();
  }
}
