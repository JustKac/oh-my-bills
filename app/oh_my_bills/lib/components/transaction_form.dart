import 'package:flutter/material.dart';
import 'package:oh_my_bills/views_model/transaction_view_model.dart';
import '../models/transaction.dart';


class TransactionForm extends StatefulWidget {
  final TransactionViewModel viewModel;

  const TransactionForm({Key? key, required this.viewModel}) : super(key: key);

  @override
  _TransactionFormState createState() => _TransactionFormState();
}

class _TransactionFormState extends State<TransactionForm> {
  final _formKey = GlobalKey<FormState>();
  String _description = '';
  double _amount = 0.0;
  bool _isCreditCard = false;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Adicionar Transação'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Form(
          key: _formKey,
          child: Column(
            children: [
              TextFormField(
                decoration: InputDecoration(labelText: 'Descrição'),
                onChanged: (value) => _description = value,
                validator: (value) => value!.isEmpty ? 'Insira uma descrição' : null,
              ),
              TextFormField(
                decoration: InputDecoration(labelText: 'Valor'),
                keyboardType: TextInputType.number,
                onChanged: (value) => _amount = double.tryParse(value) ?? 0.0,
                validator: (value) => (double.tryParse(value ?? '') ?? 0) <= 0 ? 'Insira um valor válido' : null,
              ),
              SwitchListTile(
                title: Text('Cartão de Crédito'),
                value: _isCreditCard,
                onChanged: (value) => setState(() => _isCreditCard = value),
              ),
              SizedBox(height: 20),
              ElevatedButton(
                onPressed: () {
                  if (_formKey.currentState!.validate()) {
                    widget.viewModel.addTransaction(
                      Transaction(
                        id: DateTime.now().toString(),
                        description: _description,
                        amount: _amount,
                        date: DateTime.now(),
                        isCreditCard: _isCreditCard,
                      ),
                    );
                    Navigator.pop(context);
                  }
                },
                child: Text('Adicionar'),
              ),
            ],
          ),
        ),
      ),
    );
  }
}