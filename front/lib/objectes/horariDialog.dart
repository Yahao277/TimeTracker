import 'package:flutter/material.dart';

enum Horaris {madrid, vienna}

class HorariDialog extends StatelessWidget {
  const HorariDialog({Key key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return SimpleDialog(
      title: Text('Pick time zone'),
      children: [
        SimpleDialogOption(child: Text('Madrid'), onPressed: () {
          Navigator.pop(context, Horaris.madrid);
        },),
        SimpleDialogOption(child: Text('Vienna'), onPressed: () {
          Navigator.pop(context, Horaris.vienna);
        },),
      ],
    );
  }
}