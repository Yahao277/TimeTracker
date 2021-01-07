import 'package:flutter/material.dart';
import 'package:front/tree.dart';
import 'dart:async';

class ListActivities extends StatelessWidget {
  Tree tree;
  int id;

  ListActivities({Key key, this.tree,this.id}) : super(key: key) {
    if (this.tree == null) {
      // For debuggin
      this.tree = getTree();
    }
  }

  @override
  Widget build(BuildContext context) {
    return ListView.separated(
      shrinkWrap: true,
      padding: EdgeInsets.symmetric(horizontal: 16.0),
      itemCount: this.tree.root.children.length,
      itemBuilder: (BuildContext context, int i) {
        return ActivityTile(this.tree.root.children[i], i);
      },
      separatorBuilder: (BuildContext context, int i) => Divider(
        color: Colors.black54,
      ),
    );
  }
}

extension StringExtension on String {
  String capitalize() {
    return "${this[0].toUpperCase()}${this.substring(1)}";
  }
}

class ActivityTile extends StatelessWidget {
  Activity activity;
  Task aux;
  int number;

  ActivityTile(this.activity, this.number, {Key key}) : super(key: key) {
    this.aux = (this.activity is Task) ? this.activity : null;
  }

  @override
  Widget build(BuildContext context) {
    return ListTile(
      leading: Icon(
        (this.activity is Project) ? Icons.folder : Icons.text_snippet,
        color: Colors.black,
      ),
      title: Text(
        this.activity.name.capitalize(),
        style: TextStyle(
          fontSize: 20,
          fontWeight: FontWeight.bold,
          color: Colors.black87,
        ),
      ),
      trailing: (this.activity is Task)
          ? IconButton(
        icon: Icon((this.aux.active) ? Icons.stop : Icons.play_arrow),
        color: Colors.black,
        onPressed: () {
          // TODO
        },
      )
          : null,
      subtitle: Text('tag1, tag2,...'),
      onTap: () {
        // TODO navigation
      },
    );
  }
}
