import 'package:flutter/material.dart';
import 'package:oh_my_bills/components/bank_card_list.dart';
import 'package:oh_my_bills/components/monthly_summary.dart';
import 'package:oh_my_bills/components/transaction_form.dart';
import 'package:oh_my_bills/components/transaction_list.dart';
import 'package:oh_my_bills/views_model/bank_card_view_model.dart';
import 'package:oh_my_bills/views_model/transaction_view_model.dart';
import 'package:provider/provider.dart';

class HomeScreen extends StatelessWidget {
  const HomeScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final transactionViewModel = Provider.of<TransactionViewModel?>(context);
    final bankCardViewModel = Provider.of<BankCardViewModel?>(context);

    // Check if view models are provided
    if (transactionViewModel == null || bankCardViewModel == null) {
      return Scaffold(
        appBar: AppBar(
          title: const Text('Controle de Finanças'),
          backgroundColor: Colors.green[100],
        ),
        body: const Center(
          child: Text(
            'Error: View models not found. Ensure providers are set up correctly.',
            style: TextStyle(color: Colors.red, fontSize: 16),
            textAlign: TextAlign.center,
          ),
        ),
      );
    }

    return Scaffold(
      appBar: AppBar(
        title: const Text('Controle de Finanças'),
        backgroundColor: Colors.green[100], // Light green app bar
        leading: Builder(
          builder:
              (context) => IconButton(
                icon: const Icon(Icons.menu),
                onPressed: () => Scaffold.of(context).openDrawer(),
              ),
        ),
      ),
      drawer: Drawer(
        child: ListView(
          padding: EdgeInsets.zero,
          children: [
            DrawerHeader(
              decoration: BoxDecoration(color: Colors.green[100]),
              child: const Text(
                'Menu',
                style: TextStyle(color: Colors.black87, fontSize: 24),
              ),
            ),
            ListTile(
              leading: const Icon(Icons.home),
              title: const Text('Home'),
              onTap: () {
                Navigator.pop(context); // Close the drawer
                // Already on HomeScreen, no action needed
              },
            ),
            ListTile(
              leading: const Icon(Icons.settings),
              title: const Text('Settings'),
              onTap: () {
                Navigator.pop(context); // Close the drawer
                // Navigate to Settings screen (replace with actual route)
                // Navigator.push(context, MaterialPageRoute(builder: (context) => SettingsScreen()));
              },
            ),
            ListTile(
              leading: const Icon(Icons.logout),
              title: const Text('Logout'),
              onTap: () {
                Navigator.pop(context); // Close the drawer
                // Implement logout logic here
              },
            ),
          ],
        ),
      ),
      body: AnimatedBuilder(
        animation: Listenable.merge([transactionViewModel, bankCardViewModel]),
        builder: (context, _) {
          return transactionViewModel.isLoading || bankCardViewModel.isLoading
              ? const Center(child: CircularProgressIndicator())
              : SingleChildScrollView(
                child: Column(
                  children: [
                    MonthlySummary(viewModel: transactionViewModel),
                    BankCardList(viewModel: bankCardViewModel),
                    TransactionList(viewModel: transactionViewModel),
                  ],
                ),
              );
        },
      ),
      floatingActionButton: FloatingActionButton(
        backgroundColor: Colors.green[100], // Light green FAB
        onPressed: () {
          Navigator.push(
            context,
            MaterialPageRoute(
              builder:
                  (context) => TransactionForm(viewModel: transactionViewModel),
            ),
          );
        },
        child: const Icon(Icons.add),
      ),
    );
  }
}
