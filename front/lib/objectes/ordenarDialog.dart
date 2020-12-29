import 'package:flutter/material.dart';

enum OrdenarSegons { nom, inici, duracio }

class OrdenarDialog extends StatelessWidget {
  const OrdenarDialog({Key key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return SimpleDialog(
      title: Text('Order list of Activities by:'),
      children: [
        SimpleDialogOption(
          onPressed: () {
            Navigator.pop(context, OrdenarSegons.inici);
          },
          child: const Text('Starting Date'),
        ),
        SimpleDialogOption(
          onPressed: () {
            Navigator.pop(context, OrdenarSegons.duracio);
          },
          child: const Text('Total duration'),
        ),
        SimpleDialogOption(
          onPressed: () {
            Navigator.pop(context, OrdenarSegons.nom);
          },
          child: const Text('Name'),
        ),
      ],
    );
  }
}
