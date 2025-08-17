import 'package:flutter/material.dart';
import 'package:oh_my_bills/models/bank_card.dart';
import 'package:oh_my_bills/views_model/bank_card_view_model.dart';

class BankCardList extends StatelessWidget {
  final BankCardViewModel viewModel;

  const BankCardList({super.key, required this.viewModel});

  @override
  Widget build(BuildContext context) {
    final cards = viewModel.bankCards;

    return cards.isEmpty
        ? const Padding(
          padding: EdgeInsets.all(16.0),
          child: Text(
            'Nenhum cartão de banco cadastrado.',
            style: TextStyle(fontSize: 16, color: Colors.grey),
          ),
        )
        : Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Padding(
              padding: EdgeInsets.all(16.0),
              child: Text(
                'Cartões Bancários',
                style: TextStyle(
                  fontSize: 20,
                  fontWeight: FontWeight.bold,
                  color: Colors.black87,
                ),
              ),
            ),
            SizedBox(
              height: 120,
              child: ListView.builder(
                scrollDirection: Axis.horizontal,
                padding: const EdgeInsets.symmetric(horizontal: 16.0),
                itemCount: cards.length,
                itemBuilder: (context, index) {
                  final card = cards[index];
                  return Padding(
                    padding: const EdgeInsets.only(right: 16.0),
                    child: _buildBankCard(card),
                  );
                },
              ),
            ),
          ],
        );
  }

  Widget _buildBankCard(BankCard card) {
    return Card(
      elevation: 4,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
      color: Colors.green[50], // Light green card background
      child: Container(
        width: 200,
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text(
              card.bankName,
              style: const TextStyle(
                fontSize: 18,
                fontWeight: FontWeight.bold,
                color: Colors.black87,
              ),
            ),
            const SizedBox(height: 8),
            Text(
              '**** **** **** ${card.cardNumber.substring(card.cardNumber.length - 4)}',
              style: const TextStyle(fontSize: 14, color: Colors.black54),
            ),
          ],
        ),
      ),
    );
  }
}
