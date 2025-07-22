class Transaction {
  final String id;
  final String description;
  final double amount;
  final DateTime date;
  final bool isCreditCard;

  Transaction({
    required this.id,
    required this.description,
    required this.amount,
    required this.date,
    required this.isCreditCard,
  });
}
