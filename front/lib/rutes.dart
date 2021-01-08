import 'package:flutter/material.dart';
import 'package:front/interfaces/SearchTagView.dart';
import 'package:front/interfaces/SettingsView.dart';
import 'package:front/interfaces/HomeScreen.dart';
import 'package:front/interfaces/CreateActivity.dart';

/**
 * Import de las diferentes pantalles
 */
import 'package:front/interfaces/todo.dart';

class Rutes {
  var rutes = <String, WidgetBuilder>{
    '/' : (BuildContext) => HomeScreen(0),
    // Del menu lateral
    '/searchTag': (BuildContext) => SearchTagScreen(),
    '/calcTime': (BuildContext) => WorkInProgres(),
    '/settings': (BuildContext) => SettingsScreen(),
    '/createActivity': (BuildContext) => CreateEditScreen(),
    // De navegar l'arbre
    '/infoActivitat': (BuildContext) => WorkInProgres(),
  };

  Rutes() {
    runApp(
      MaterialApp(
        title: 'TimeTracker',
        initialRoute: '/',
        routes: rutes,
        debugShowCheckedModeBanner: false,
      ),
    );
  }
}
