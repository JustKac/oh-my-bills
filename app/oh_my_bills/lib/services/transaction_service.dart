import '../models/transaction.dart';

abstract class TransactionService {
  Future<List<Transaction>> getTransactions();
  Future<void> addTransaction(Transaction transaction);
}

class MockTransactionService implements TransactionService {
  final List<Transaction> _transactions = [
    Transaction(
      id: '1',
      description: 'Supermercado',
      amount: 150.0,
      date: DateTime.now(),
      isCreditCard: false,
    ),
    Transaction(
      id: '2',
      description: 'Fatura Cartão',
      amount: 1200.0,
      date: DateTime.now(),
      isCreditCard: true,
    ),
  ];

  @override
  Future<List<Transaction>> getTransactions() async => _transactions;

  @override
  Future<void> addTransaction(Transaction transaction) async =>
      _transactions.add(transaction);
}
