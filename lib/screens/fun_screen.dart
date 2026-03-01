import 'package:flutter/material.dart';
import 'package:flutter_spinkit/flutter_spinkit.dart';
import '../models/models.dart';
import '../services/api_service.dart';

class FunScreen extends StatefulWidget {
  const FunScreen({super.key});

  @override
  State<FunScreen> createState() => _FunScreenState();
}

class _FunScreenState extends State<FunScreen> {
  Joke? _joke;
  Fact? _fact;
  Quote? _quote;
  bool _isLoadingJoke = false;
  bool _isLoadingFact = false;
  bool _isLoadingQuote = false;

  @override
  void initState() {
    super.initState();
    _loadJoke();
    _loadFact();
    _loadQuote();
  }

  Future<void> _loadJoke() async {
    setState(() => _isLoadingJoke = true);
    try {
      final joke = await ApiService.getJoke();
      setState(() {
        _joke = joke;
        _isLoadingJoke = false;
      });
    } catch (e) {
      setState(() => _isLoadingJoke = false);
    }
  }

  Future<void> _loadFact() async {
    setState(() => _isLoadingFact = true);
    try {
      final fact = await ApiService.getFact();
      setState(() {
        _fact = fact;
        _isLoadingFact = false;
      });
    } catch (e) {
      setState(() => _isLoadingFact = false);
    }
  }

  Future<void> _loadQuote() async {
    setState(() => _isLoadingQuote = true);
    try {
      final quote = await ApiService.getQuote();
      setState(() {
        _quote = quote;
        _isLoadingQuote = false;
      });
    } catch (e) {
      setState(() => _isLoadingQuote = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
      padding: const EdgeInsets.all(16),
      child: Column(
        children: [
          // Joke Card
          _buildCard(
            title: 'نكتة عشوائية',
            icon: Icons.emoji_emotions,
            content: _joke?.text ?? 'جاري التحميل...',
            isLoading: _isLoadingJoke,
            onRefresh: _loadJoke,
            color: Colors.orange,
          ),
          const SizedBox(height: 16),
          // Fact Card
          _buildCard(
            title: 'حقيقة عشوائية',
            icon: Icons.lightbulb,
            content: _fact?.text ?? 'جاري التحميل...',
            isLoading: _isLoadingFact,
            onRefresh: _loadFact,
            color: Colors.green,
          ),
          const SizedBox(height: 16),
          // Quote Card
          _buildQuoteCard(),
        ],
      ),
    );
  }

  Widget _buildCard({
    required String title,
    required IconData icon,
    required String content,
    required bool isLoading,
    required VoidCallback onRefresh,
    required Color color,
  }) {
    return Card(
      elevation: 4,
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              children: [
                Icon(icon, color: color),
                const SizedBox(width: 8),
                Text(
                  title,
                  style: TextStyle(
                    fontSize: 18,
                    fontWeight: FontWeight.bold,
                    color: color,
                  ),
                ),
                const Spacer(),
                if (isLoading)
                  const SpinKitThreeBounce(color: Colors.blue, size: 20)
                else
                  IconButton(
                    icon: const Icon(Icons.refresh),
                    onPressed: onRefresh,
                  ),
              ],
            ),
            const Divider(),
            const SizedBox(height: 8),
            Text(
              content,
              style: const TextStyle(fontSize: 16),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildQuoteCard() {
    return Card(
      elevation: 4,
      color: Colors.purple[50],
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              children: [
                const Icon(Icons.format_quote, color: Colors.purple),
                const SizedBox(width: 8),
                const Text(
                  'اقتباس ملهم',
                  style: TextStyle(
                    fontSize: 18,
                    fontWeight: FontWeight.bold,
                    color: Colors.purple,
                  ),
                ),
                const Spacer(),
                if (_isLoadingQuote)
                  const SpinKitThreeBounce(color: Colors.blue, size: 20)
                else
                  IconButton(
                    icon: const Icon(Icons.refresh),
                    onPressed: _loadQuote,
                  ),
              ],
            ),
            const Divider(),
            const SizedBox(height: 8),
            if (_quote != null) ...[
              Text(
                '"${_quote!.content}"',
                style: const TextStyle(
                  fontSize: 16,
                  fontStyle: FontStyle.italic,
                ),
              ),
              const SizedBox(height: 8),
              Align(
                alignment: Alignment.centerRight,
                child: Text(
                  '- ${_quote!.author}',
                  style: TextStyle(
                    color: Colors.grey[600],
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ),
            ] else
              const Text('جاري التحميل...'),
          ],
        ),
      ),
    );
  }
}
