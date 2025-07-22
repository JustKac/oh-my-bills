import 'package:flutter/material.dart';
import 'package:oh_my_bills/views_model/transaction_view_model.dart';

import 'services/transaction_service.dart';

import 'views/home_screen.dart';

void main() {
  final transactionService = MockTransactionService();
  final transactionViewModel = TransactionViewModel(transactionService);

  runApp(FinanceApp(transactionViewModel: transactionViewModel));
}

class FinanceApp extends StatelessWidget {
  final TransactionViewModel transactionViewModel;

  const FinanceApp({Key? key, required this.transactionViewModel})
    : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Controle de Finanças',
      theme: ThemeData(
        primarySwatch: Colors.teal,
        visualDensity: VisualDensity.adaptivePlatformDensity,
      ),
      home: HomeScreen(viewModel: transactionViewModel),
    );
  }
}
