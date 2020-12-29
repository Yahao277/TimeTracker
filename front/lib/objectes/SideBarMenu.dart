import 'package:flutter/material.dart';
import 'package:front/interfaces/SettingsView.dart';

class TileLabel extends StatelessWidget {
  String text;

  TileLabel(this.text, {Key key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Text(
      this.text,
      style: TextStyle(fontSize: 16.0),
    );
  }
}

class SideBarMenu extends StatelessWidget {
  Configuracio conf;

  SideBarMenu(this.conf, {Key key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Drawer(
      child: Column(
        children: [
          DrawerHeader(
            child: Center(
              child: Text(
                '- Menu -',
                style: TextStyle(
                  color: Colors.white,
                  fontSize: 32.0,
                ),
              ),
            ),
            decoration: BoxDecoration(color: Colors.blueAccent),
          ),
          // Homebutton
          ListTile(
            leading: Icon(Icons.home),
            title: TileLabel('Home'),
            onTap: () {
              Navigator.popUntil(context, ModalRoute.withName('/'));
            },
          ),
          // Search Tag
          ListTile(
            leading: Icon(Icons.search),
            title: TileLabel('Search Tag'),
            onTap: () {
              Navigator.pushNamed(context, '/searchTag');
            },
          ),
          // Calculate time
          ListTile(
            leading: Icon(Icons.alarm),
            title: TileLabel('Calculate Time'),
            onTap: () {
              Navigator.pushNamed(context, '/calcTime');
            },
          ),
          Expanded(
            child: Container(),
          ),
          // Settings
          Divider(
            color: Colors.black54,
          ),
          ListTile(
            leading: Icon(Icons.settings),
            title: TileLabel('Settings'),
            onTap: () {
              this.conf = Navigator.pushNamed(context, '/settings',
                  arguments: this.conf) as Configuracio;
            },
          ),
          // For pretty porpuses
          SizedBox(
            height: 16.0,
          )
        ],
      ),
    );
  }
}
