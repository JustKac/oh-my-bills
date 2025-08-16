import 'package:flutter/foundation.dart';
import 'package:oh_my_bills/models/bank_card.dart';

class BankCardViewModel extends ChangeNotifier {
  List<BankCard> _bankCards = [];
  bool _isLoading = false;

  BankCardViewModel(bankCardService);

  List<BankCard> get bankCards => _bankCards;
  bool get isLoading => _isLoading;

  void fetchBankCards() {
    _isLoading = true;
    notifyListeners();

    // Simulate fetching bank cards (replace with actual data source)
    Future.delayed(const Duration(seconds: 1), () {
      _bankCards = [
        BankCard(id: '1', bankName: 'Banco A', cardNumber: '1234567890123456'),
        BankCard(id: '2', bankName: 'Banco B', cardNumber: '9876543210987654'),
      ];
      _isLoading = false;
      notifyListeners();
    });
  }
}
