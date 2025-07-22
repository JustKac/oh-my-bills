import 'package:flutter/material.dart';

import '../views_model/transaction_view_model.dart';



class MonthlySummary extends StatelessWidget {
  final TransactionViewModel viewModel;

  const MonthlySummary({Key? key, required this.viewModel}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Card(
      margin: EdgeInsets.all(16.0),
      child: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text('Resumo Mensal', style: Theme.of(context).textTheme.headlineLarge),
            SizedBox(height: 10),
            Text('Gastos: R\$ ${viewModel.totalExpenses.toStringAsFixed(2)}'),
            Text(
              'Dívida Cartão: R\$ ${viewModel.totalCreditCard.toStringAsFixed(2)}',
            ),
          ],
        ),
      ),
    );
  }
}
