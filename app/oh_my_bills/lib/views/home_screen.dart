import 'package:flutter/material.dart';
import 'package:oh_my_bills/components/transaction_form.dart';
import 'package:oh_my_bills/components/transaction_list.dart';

import '../components/monthly_summary.dart';
import '../views_model/transaction_view_model.dart' show TransactionViewModel;


class HomeScreen extends StatelessWidget {
  final TransactionViewModel viewModel;

  const HomeScreen({super.key, required this.viewModel});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Controle de Finanças')),
      body: AnimatedBuilder(
        animation: viewModel,
        builder: (context, _) {
          return viewModel.isLoading
              ? Center(child: CircularProgressIndicator())
              : SingleChildScrollView(
                child: Column(
                  children: [
                    MonthlySummary(viewModel: viewModel),
                    TransactionList(viewModel: viewModel),
                  ],
                ),
              );
        },
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          Navigator.push(
            context,
            MaterialPageRoute(
              builder: (context) => TransactionForm(viewModel: viewModel),
            ),
          );
        },
        child: Icon(Icons.add),
      ),
    );
  }
}
