import 'package:flutter/material.dart';
import 'package:oh_my_bills/services/transaction_service.dart';
import 'package:oh_my_bills/views/home_screen.dart';
import 'package:oh_my_bills/views_model/bank_card_view_model.dart';
import 'package:oh_my_bills/views_model/transaction_view_model.dart';
import 'package:provider/provider.dart';

// Mock service for BankCardViewModel (replace with actual service)
class MockBankCardService {}

void main() {
  final transactionService = MockTransactionService();
  final bankCardService = MockBankCardService(); // Replace with actual service
  runApp(
    MultiProvider(
      providers: [
        ChangeNotifierProvider(
          create: (_) => TransactionViewModel(transactionService),
        ),
        ChangeNotifierProvider(
          create: (_) => BankCardViewModel(bankCardService),
        ),
      ],
      child: const FinanceApp(),
    ),
  );
}

class FinanceApp extends StatelessWidget {
  const FinanceApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Controle de Finanças',
      theme: ThemeData(
        primaryColor: Colors.greenAccent,
        scaffoldBackgroundColor: Colors.white,
        floatingActionButtonTheme: const FloatingActionButtonThemeData(
          backgroundColor: Colors.greenAccent,
        ),
        appBarTheme: const AppBarTheme(
          backgroundColor: Colors.greenAccent,
          titleTextStyle: TextStyle(color: Colors.black87, fontSize: 20),
        ),
        visualDensity: VisualDensity.adaptivePlatformDensity,
      ),
      home: const HomeScreen(),
    );
  }
}
