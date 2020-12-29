import 'package:flutter/material.dart';
import 'package:front/page_activities.dart';
import 'package:front/pageIntervals.dart';
import 'package:front/interfaces/HomeScreen.dart';
import 'package:front/rutes.dart';

void main() => Rutes();

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'TimeTracker',
      theme: ThemeData(
        primarySwatch: Colors.blue,
        textTheme: TextTheme(
            subhead: TextStyle(fontSize:15.0),
            body1:TextStyle(fontSize:15.0)),
      ),
      home: HomeScreen(0),
    );
  }
}