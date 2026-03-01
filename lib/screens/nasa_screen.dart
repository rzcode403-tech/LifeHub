import 'package:flutter/material.dart';
import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter_spinkit/flutter_spinkit.dart';
import '../models/models.dart';
import '../services/api_service.dart';

class NasaScreen extends StatefulWidget {
  const NasaScreen({super.key});

  @override
  State<NasaScreen> createState() => _NasaScreenState();
}

class _NasaScreenState extends State<NasaScreen> {
  NasaApod? _apod;
  bool _isLoading = true;
  String? _error;

  @override
  void initState() {
    super.initState();
    _loadApod();
  }

  Future<void> _loadApod() async {
    try {
      final apod = await ApiService.getNasaApod();
      setState(() {
        _apod = apod;
        _isLoading = false;
      });
    } catch (e) {
      setState(() {
        _error = 'فشل تحميل الصورة';
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

    return SingleChildScrollView(
      padding: const EdgeInsets.all(16),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          if (_apod != null) ...[
            Card(
              clipBehavior: Clip.antiAlias,
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(16),
              ),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  if (_apod!.mediaType == 'image')
                    CachedNetworkImage(
                      imageUrl: _apod!.url,
                      placeholder: (context, url) => Container(
                        height: 250,
                        color: Colors.grey[300],
                        child: const Center(child: CircularProgressIndicator()),
                      ),
                      errorWidget: (context, url, error) => Container(
                        height: 250,
                        color: Colors.grey[300],
                        child: const Icon(Icons.error),
                      ),
                    )
                  else
                    Container(
                      height: 250,
                      color: Colors.grey[300],
                      child: const Center(
                        child: Icon(Icons.videocam, size: 50),
                      ),
                    ),
                  Padding(
                    padding: const EdgeInsets.all(16),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          _apod!.title,
                          style: const TextStyle(
                            fontSize: 22,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                        const SizedBox(height: 8),
                        Text(
                          'التاريخ: ${_apod!.date}',
                          style: TextStyle(
                            color: Colors.grey[600],
                            fontSize: 14,
                          ),
                        ),
                        const SizedBox(height: 16),
                        const Text(
                          'الشرح:',
                          style: TextStyle(
                            fontSize: 16,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                        const SizedBox(height: 8),
                        Text(
                          _apod!.explanation,
                          style: const TextStyle(
                            fontSize: 14,
                            height: 1.5,
                          ),
                        ),
                      ],
                    ),
                  ),
                ],
              ),
            ),
          ],
        ],
      ),
    );
  }
}
