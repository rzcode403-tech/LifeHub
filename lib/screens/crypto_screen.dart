import 'package:flutter/material.dart';
import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter_spinkit/flutter_spinkit.dart';
import '../models/models.dart';
import '../services/api_service.dart';

class CryptoScreen extends StatefulWidget {
  const CryptoScreen({super.key});

  @override
  State<CryptoScreen> createState() => _CryptoScreenState();
}

class _CryptoScreenState extends State<CryptoScreen> {
  List<Crypto> _cryptos = [];
  bool _isLoading = true;
  String? _error;

  @override
  void initState() {
    super.initState();
    _loadCrypto();
  }

  Future<void> _loadCrypto() async {
    try {
      final cryptos = await ApiService.getCrypto();
      setState(() {
        _cryptos = cryptos;
        _isLoading = false;
      });
    } catch (e) {
      setState(() {
        _error = 'فشل تحميل البيانات';
        _isLoading = false;
      });
    }
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

    return RefreshIndicator(
      onRefresh: _loadCrypto,
      child: ListView.builder(
        padding: const EdgeInsets.all(8),
        itemCount: _cryptos.length,
        itemBuilder: (context, index) {
          final crypto = _cryptos[index];
          final isPositive = crypto.priceChange24h >= 0;

          return Card(
            margin: const EdgeInsets.only(bottom: 8),
            child: ListTile(
              leading: CircleAvatar(
                backgroundColor: Colors.transparent,
                child: CachedNetworkImage(
                  imageUrl: crypto.imageUrl,
                  placeholder: (context, url) => const CircularProgressIndicator(),
                  errorWidget: (context, url, error) => const Icon(Icons.error),
                ),
              ),
              title: Text(
                crypto.name,
                style: const TextStyle(fontWeight: FontWeight.bold),
              ),
              subtitle: Text(
                '\$${crypto.currentPrice.toStringAsFixed(2)}',
                style: const TextStyle(fontSize: 16),
              ),
              trailing: Container(
                padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
                decoration: BoxDecoration(
                  color: isPositive ? Colors.green : Colors.red,
                  borderRadius: BorderRadius.circular(20),
                ),
                child: Text(
                  '${crypto.priceChange24h.toStringAsFixed(2)}%',
                  style: const TextStyle(
                    color: Colors.white,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ),
            ),
          );
        },
      ),
    );
  }
}
