import 'package:flutter/material.dart';
import 'package:flutter_spinkit/flutter_spinkit.dart';
import '../services/api_service.dart';

class TranslateScreen extends StatefulWidget {
  const TranslateScreen({super.key});

  @override
  State<TranslateScreen> createState() => _TranslateScreenState();
}

class _TranslateScreenState extends State<TranslateScreen> {
  final TextEditingController _textController = TextEditingController();
  String _translatedText = '';
  bool _isLoading = false;
  
  String _sourceLang = 'ar';
  String _targetLang = 'en';

  final Map<String, String> _languages = {
    'ar': 'العربية',
    'en': 'الإنجليزية',
    'es': 'الإسبانية',
    'fr': 'الفرنسية',
    'de': 'الألمانية',
    'it': 'الإيطالية',
    'pt': 'البرتغالية',
    'ru': 'الروسية',
    'zh': 'الصينية',
    'ja': 'اليابانية',
    'tr': 'التركية',
  };

  Future<void> _translate() async {
    if (_textController.text.isEmpty) return;

    setState(() {
      _isLoading = true;
    });

    try {
      final result = await ApiService.translate(
        _textController.text,
        _sourceLang,
        _targetLang,
      );
      setState(() {
        _translatedText = result;
        _isLoading = false;
      });
    } catch (e) {
      setState(() {
        _isLoading = false;
      });
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('فشل الترجمة')),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(16.0),
      child: Column(
        children: [
          Row(
            children: [
              Expanded(
                child: DropdownButtonFormField<String>(
                  value: _sourceLang,
                  decoration: const InputDecoration(
                    labelText: 'من',
                    border: OutlineInputBorder(),
                  ),
                  items: _languages.entries.map((entry) {
                    return DropdownMenuItem(
                      value: entry.key,
                      child: Text(entry.value),
                    );
                  }).toList(),
                  onChanged: (value) {
                    setState(() {
                      _sourceLang = value!;
                    });
                  },
                ),
              ),
              const SizedBox(width: 16),
              IconButton(
                icon: const Icon(Icons.swap_horiz),
                onPressed: () {
                  setState(() {
                    final temp = _sourceLang;
                    _sourceLang = _targetLang;
                    _targetLang = temp;
                  });
                },
              ),
              const SizedBox(width: 16),
              Expanded(
                child: DropdownButtonFormField<String>(
                  value: _targetLang,
                  decoration: const InputDecoration(
                    labelText: 'إلى',
                    border: OutlineInputBorder(),
                  ),
                  items: _languages.entries.map((entry) {
                    return DropdownMenuItem(
                      value: entry.key,
                      child: Text(entry.value),
                    );
                  }).toList(),
                  onChanged: (value) {
                    setState(() {
                      _targetLang = value!;
                    });
                  },
                ),
              ),
            ],
          ),
          const SizedBox(height: 16),
          TextField(
            controller: _textController,
            maxLines: 4,
            decoration: const InputDecoration(
              hintText: 'أدخل النص للترجمة',
              border: OutlineInputBorder(),
            ),
          ),
          const SizedBox(height: 16),
          SizedBox(
            width: double.infinity,
            child: ElevatedButton(
              onPressed: _isLoading ? null : _translate,
              child: _isLoading
                  ? const SpinKitThreeBounce(color: Colors.white, size: 20)
                  : const Text('ترجم'),
            ),
          ),
          const SizedBox(height: 24),
          if (_translatedText.isNotEmpty)
            Card(
              color: Colors.grey[100],
              child: Padding(
                padding: const EdgeInsets.all(16.0),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    const Text(
                      'الترجمة:',
                      style: TextStyle(
                        fontWeight: FontWeight.bold,
                        color: Colors.grey,
                      ),
                    ),
                    const SizedBox(height: 8),
                    Text(
                      _translatedText,
                      style: const TextStyle(fontSize: 18),
                    ),
                  ],
                ),
              ),
            ),
        ],
      ),
    );
  }
}
