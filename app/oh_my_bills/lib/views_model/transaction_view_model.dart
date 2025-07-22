import 'package:flutter/foundation.dart';

import '../models/transaction.dart';
import '../services/transaction_service.dart';

class TransactionViewModel extends ChangeNotifier {
  final TransactionService _transactionService;
  List<Transaction> _transactions = [];
  bool _isLoading = false;

  TransactionViewModel(this._transactionService) {
    _loadTransactions();
  }

  List<Transaction> get transactions => _transactions;
  bool get isLoading => _isLoading;

  Future<void> _loadTransactions() async {
    _isLoading = true;
    notifyListeners();
    _transactions = await _transactionService.getTransactions();
    _isLoading = false;
    notifyListeners();
  }

  Future<void> addTransaction(Transaction transaction) async {
    await _transactionService.addTransaction(transaction);
    await _loadTransactions();
  }

  double get totalExpenses => _transactions
      .where((t) => !t.isCreditCard)
      .fold(0.0, (sum, t) => sum + t.amount);

  double get totalCreditCard => _transactions
      .where((t) => t.isCreditCard)
      .fold(0.0, (sum, t) => sum + t.amount);
}
