import 'package:flutter/material.dart';
import 'package:front/objectes/llistaActivitats.dart';
import 'package:front/objectes/SideBarMenu.dart';
import 'package:front/objectes/ordenarDialog.dart';
import 'package:front/interfaces/SettingsView.dart';

class RootScreen extends StatefulWidget {
  RootScreen({Key key}) : super(key: key);

  @override
  _RootScreenState createState() => _RootScreenState();
}

class _RootScreenState extends State<RootScreen> {
  // Default
  Configuracio conf;

  @override
  void initState() {
    super.initState();
    conf = Configuracio('català', 'DD-MM-YYYY', 'MAD');
  }

  Future<void> _preguntaOrdre() async {
    switch (await showDialog<OrdenarSegons>(
      context: context,
      builder: (BuildContext context) {
        return OrdenarDialog();
      },
    )) {
      case OrdenarSegons.nom:
        print('Ordenar per nom');
        break;
      case OrdenarSegons.duracio:
        print('Ordenar per duracio');
        break;
      case OrdenarSegons.inici:
        print('Ordenar per inici');
        break;
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('TimeTracker'),
        centerTitle: true,
        actions: [
          IconButton(
            icon: Icon(
              Icons.sort,
              color: Colors.white,
            ),
            onPressed: () {
              _preguntaOrdre();
            },
          )
        ],
      ),
      drawer: SideBarMenu(conf),
      body: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          SizedBox(
            height: 32.0,
          ),
          Padding(
            padding: EdgeInsets.fromLTRB(16.0, 0, 0, 0),
            child: Text(
              'Your Activites:',
              style: TextStyle(fontSize: 22),
            ),
          ),
          Divider(
            indent: 80.0,
            endIndent: 80.0,
          ),
          SizedBox(
            height: 16.0,
          ),
          LlistatActivitats()
        ],
      ),
      floatingActionButton: FloatingActionButton(
        tooltip: 'Create new Activity',
        onPressed: () {
          Navigator.pushNamed(context, '/createActivity');
        },
        child: const Icon(Icons.add),
      ),
    );
  }
}
