import 'package:flutter/material.dart';
import 'weather_screen.dart';
import 'news_screen.dart';
import 'currency_screen.dart';
import 'crypto_screen.dart';
import 'translate_screen.dart';
import 'countries_screen.dart';
import 'nasa_screen.dart';
import 'fun_screen.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  int _selectedIndex = 0;

  final List<Widget> _screens = [
    const WeatherScreen(),
    const NewsScreen(),
    const CurrencyScreen(),
    const CryptoScreen(),
    const TranslateScreen(),
    const CountriesScreen(),
    const NasaScreen(),
    const FunScreen(),
  ];

  final List<String> _titles = [
    'الطقس',
    'الأخبار',
    'العملات',
    'العملات الرقمية',
    'الترجمة',
    'الدول',
    'الفضاء',
    'ترفيه',
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(_titles[_selectedIndex]),
        centerTitle: true,
        elevation: 0,
      ),
      body: _screens[_selectedIndex],
      bottomNavigationBar: NavigationBar(
        selectedIndex: _selectedIndex,
        onDestinationSelected: (index) {
          setState(() {
            _selectedIndex = index;
          });
        },
        destinations: const [
          NavigationDestination(
            icon: Icon(Icons.wb_sunny_outlined),
            selectedIcon: Icon(Icons.wb_sunny),
            label: 'طقس',
          ),
          NavigationDestination(
            icon: Icon(Icons.newspaper_outlined),
            selectedIcon: Icon(Icons.newspaper),
            label: 'أخبار',
          ),
          NavigationDestination(
            icon: Icon(Icons.currency_exchange_outlined),
            selectedIcon: Icon(Icons.currency_exchange),
            label: 'عملات',
          ),
          NavigationDestination(
            icon: Icon(Icons.currency_bitcoin_outlined),
            selectedIcon: Icon(Icons.currency_bitcoin),
            label: 'كريبتو',
          ),
          NavigationDestination(
            icon: Icon(Icons.translate_outlined),
            selectedIcon: Icon(Icons.translate),
            label: 'ترجمة',
          ),
          NavigationDestination(
            icon: Icon(Icons.public_outlined),
            selectedIcon: Icon(Icons.public),
            label: 'دول',
          ),
          NavigationDestination(
            icon: Icon(Icons.rocket_outlined),
            selectedIcon: Icon(Icons.rocket),
            label: 'فضاء',
          ),
          NavigationDestination(
            icon: Icon(Icons.emoji_emotions_outlined),
            selectedIcon: Icon(Icons.emoji_emotions),
            label: 'ترفيه',
          ),
        ],
      ),
    );
  }
}
