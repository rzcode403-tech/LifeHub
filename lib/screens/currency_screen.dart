import 'package:flutter/material.dart';
import 'package:flutter_spinkit/flutter_spinkit.dart';
import '../services/api_service.dart';

class CurrencyScreen extends StatefulWidget {
  const CurrencyScreen({super.key});

  @override
  State<CurrencyScreen> createState() => _CurrencyScreenState();
}

class _CurrencyScreenState extends State<CurrencyScreen> {
  final TextEditingController _amountController = TextEditingController();
  String _fromCurrency = 'USD';
  String _toCurrency = 'EUR';
  double? _result;
  bool _isLoading = false;
  Map<String, dynamic>? _rates;

  final List<String> _currencies = [
    'USD', 'EUR', 'GBP', 'JPY', 'SAR', 'AED', 'EGP', 
    'KWD', 'QAR', 'BHD', 'OMR', 'JOD'
  ];

  Future<void> _convert() async {
    if (_amountController.text.isEmpty) return;

    setState(() {
      _isLoading = true;
    });

    try {
      _rates = await ApiService.getExchangeRates(_fromCurrency);
      final amount = double.parse(_amountController.text);
      final rate = _rates![_toCurrency] ?? 0.0;
      
      setState(() {
        _result = amount * rate;
        _isLoading = false;
      });
    } catch (e) {
      setState(() {
        _isLoading = false;
      });
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('فشل التحويل')),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(16.0),
      child: Column(
        children: [
          Card(
            child: Padding(
              padding: const EdgeInsets.all(16.0),
              child: Column(
                children: [
                  TextField(
                    controller: _amountController,
                    keyboardType: TextInputType.number,
                    decoration: const InputDecoration(
                      labelText: 'المبلغ',
                      border: OutlineInputBorder(),
                    ),
                  ),
                  const SizedBox(height: 16),
                  Row(
                    children: [
                      Expanded(
                        child: DropdownButtonFormField<String>(
                          value: _fromCurrency,
                          decoration: const InputDecoration(
                            labelText: 'من',
                            border: OutlineInputBorder(),
                          ),
                          items: _currencies.map((currency) {
                            return DropdownMenuItem(
                              value: currency,
                              child: Text(currency),
                            );
                          }).toList(),
                          onChanged: (value) {
                            setState(() {
                              _fromCurrency = value!;
                            });
                          },
                        ),
                      ),
                      const SizedBox(width: 16),
                      const Icon(Icons.arrow_forward),
                      const SizedBox(width: 16),
                      Expanded(
                        child: DropdownButtonFormField<String>(
                          value: _toCurrency,
                          decoration: const InputDecoration(
                            labelText: 'إلى',
                            border: OutlineInputBorder(),
                          ),
                          items: _currencies.map((currency) {
                            return DropdownMenuItem(
                              value: currency,
                              child: Text(currency),
                            );
                          }).toList(),
                          onChanged: (value) {
                            setState(() {
                              _toCurrency = value!;
                            });
                          },
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(height: 16),
                  SizedBox(
                    width: double.infinity,
                    child: ElevatedButton(
                      onPressed: _isLoading ? null : _convert,
                      child: _isLoading
                          ? const SpinKitThreeBounce(
                              color: Colors.white,
                              size: 20,
                            )
                          : const Text('تحويل'),
                    ),
                  ),
                ],
              ),
            ),
          ),
          const SizedBox(height: 24),
          if (_result != null)
            Card(
              color: Theme.of(context).primaryColor,
              child: Padding(
                padding: const EdgeInsets.all(24.0),
                child: Column(
                  children: [
                    const Text(
                      'النتيجة',
                      style: TextStyle(
                        color: Colors.white70,
                        fontSize: 16,
                      ),
                    ),
                    const SizedBox(height: 8),
                    Text(
                      '${_result!.toStringAsFixed(2)} $_toCurrency',
                      style: const TextStyle(
                        color: Colors.white,
                        fontSize: 32,
                        fontWeight: FontWeight.bold,
                      ),
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
