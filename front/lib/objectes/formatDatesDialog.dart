import 'package:flutter/material.dart';

enum Formats { format1, format2, format3 }

class FormatDataDialog extends StatelessWidget {
  const FormatDataDialog({Key key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return SimpleDialog(
      title: Text('Pick date format:'),
      children: [
        SimpleDialogOption(
          child: Text('DD-MM-YYYY'),
          onPressed: () {
            Navigator.pop(context, Formats.format1);
          },
        ),
        SimpleDialogOption(
          child: Text('YYYY-MM-DD'),
          onPressed: () {
            Navigator.pop(context, Formats.format2);
          },
        ),
        SimpleDialogOption(
          child: Text('?????'),
          onPressed: () {
            Navigator.pop(context, Formats.format2);
          },
        ),
      ],
    );
  }
}
