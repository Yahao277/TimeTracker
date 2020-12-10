import 'package:flutter/material.dart';
import 'package:front/page_activities.dart';
import 'package:front/pageIntervals.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'TimeTracker',
      theme: ThemeData(
        primarySwatch: Colors.blue,
        textTheme: TextTheme(
            subhead: TextStyle(fontSize:20.0),
            body1:TextStyle(fontSize:20.0)),
      ),
      home: PageActivities(),
    );
  }
}