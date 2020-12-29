import 'package:flutter/material.dart';
import 'package:front/objectes/formatDatesDialog.dart';
import 'package:front/objectes/horariDialog.dart';
import 'package:front/objectes/idiomesDialog.dart';

class SettingsScreen extends StatefulWidget {
  SettingsScreen({Key key}) : super(key: key);

  @override
  _SettingsScreenState createState() => _SettingsScreenState();
}

class _SettingsScreenState extends State<SettingsScreen> {
  @override
  Widget build(BuildContext context) {
    Configuracio conf = ModalRoute.of(context).settings.arguments;

    return Scaffold(
      appBar: AppBar(
        automaticallyImplyLeading: true,
        centerTitle: true,
        title: Text('Settings'),
      ),
      body: Padding(
        padding: EdgeInsets.symmetric(horizontal: 64.0, vertical: 120.0),
        child: Column(
          children: [
            ElementConfigurable(
              label: 'Language:',
              value: conf.idioma,
              onPressed: _IdiomesDialog,
            ),
            ElementConfigurable(
              label: 'Date format:',
              value: conf.formatData,
              onPressed: _DataDialog,
            ),
            ElementConfigurable(
              label: 'Time zone:',
              value: conf.horari,
              onPressed: _HorariDialog,
            ),
          ],
        ),
      ),
    );
  }

  Future<void> _IdiomesDialog() async {
    switch (await showDialog<Idiomes>(
      context: context,
      builder: (BuildContext context) {
        return IdiomesDialog();
      },
    )) {
      case Idiomes.catala:
        print('Canviat a catala');
        break;
      case Idiomes.angles:
        print('Canviat a angles');
        break;
      case Idiomes.espanol:
        print('Canviat a espanyol');
        break;
    }
  }

  Future<void> _DataDialog() async {
    switch (await showDialog<Formats>(
        context: context,
        builder: (BuildContext context) {
          return FormatDataDialog();
        })) {
      case Formats.format1:
        print('Format 1');
        break;
      case Formats.format2:
        print('Format 2');
        break;
      case Formats.format3:
        print('Format 3');
        break;
    }
  }

  Future<void> _HorariDialog() async {
    switch (await showDialog<Horaris>(
        context: context,
        builder: (BuildContext context) {
          return HorariDialog();
        })) {
      case Horaris.madrid:
        print('horari madrid');
        break;
      case Horaris.vienna:
        print('horari vienna');
        break;
      default:
    }
  }
}

class Configuracio {
  String idioma, formatData, horari;

  Configuracio(this.idioma, this.formatData, this.horari);
}

class ElementConfigurable extends StatelessWidget {
  String label;
  String value;
  Function() onPressed;

  ElementConfigurable({Key key, this.label, this.onPressed, this.value})
      : super(key: key) {
    assert(label != null);
    assert(value != null);
    assert(onPressed != null);
  }

  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        Text(
          this.label,
          style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
        ),
        TextButton(
          child: Text(this.value),
          onPressed: this.onPressed,
        ),
      ],
    );
  }
}
