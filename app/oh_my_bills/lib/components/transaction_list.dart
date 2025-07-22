import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:oh_my_bills/views_model/transaction_view_model.dart';

class TransactionList extends StatelessWidget {
  final TransactionViewModel viewModel;

  const TransactionList({Key? key, required this.viewModel}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Padding(
          padding: const EdgeInsets.all(16.0),
          child: Text(
            'Transações',
            style: Theme.of(context).textTheme.headlineLarge,
          ),
        ),
        ListView.builder(
          shrinkWrap: true,
          physics: NeverScrollableScrollPhysics(),
          itemCount: viewModel.transactions.length,
          itemBuilder: (context, index) {
            final transaction = viewModel.transactions[index];
            return ListTile(
              title: Text(transaction.description),
              subtitle: Text(
                '${DateFormat('dd/MM/yyyy').format(transaction.date)} - R\$ ${transaction.amount.toStringAsFixed(2)}',
              ),
              trailing:
                  transaction.isCreditCard ? Icon(Icons.credit_card) : null,
            );
          },
        ),
      ],
    );
  }
}
