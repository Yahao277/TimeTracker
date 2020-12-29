import 'package:flutter/material.dart';

enum Idiomes { catala, espanol, angles }

class IdiomesDialog extends StatelessWidget {
  const IdiomesDialog({Key key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return SimpleDialog(
      title: Text('Choose a language:'),
      children: [
        SimpleDialogOption(child: const Text('Català'), onPressed: () {
          Navigator.pop(context, Idiomes.catala);
        },),
        SimpleDialogOption(child: const Text('Español'), onPressed: () {
          Navigator.pop(context, Idiomes.espanol);
        },),
        SimpleDialogOption(child: const Text('English'), onPressed: () {
          Navigator.pop(context, Idiomes.angles);
        },)
      ],
    );
  }
}