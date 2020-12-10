import 'package:flutter/material.dart';
import 'package:front/tree.dart' as Tree;

class PageIntervals extends StatefulWidget {
  @override
  _PageIntervalsState createState() => _PageIntervalsState();
}

class _PageIntervalsState extends State<PageIntervals> {
  Tree.Tree tree;

  @override
  void initState(){
    super.initState();
    tree = Tree.getTreeTask();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title:Text(tree.root.name),
        actions: <Widget>[
          IconButton(icon: Icon(Icons.home), onPressed: () {}, // TODO go home page = root)
          ),
          // TODO: other actions
        ],
      ),
      body: ListView.separated(
        padding: const EdgeInsets.all(16.0),
        itemCount: tree.root.children.length,
        itemBuilder: (BuildContext context,int index) => _buildRow(tree.root.children[index],index),
        separatorBuilder: (BuildContext context,int index) => const Divider(),
      )
    );
  }

  @override
  Widget _buildRow(Tree.Interval interval, int index){
    String strDuration = Duration(seconds:interval.duration).toString().split('.').first;
    String strInitialDate = interval.initialDate.toString().split('.')[0];
    String strFinalDate = interval.finalDate.toString().split('.')[0];

    return ListTile(
      title: Text('from ${strInitialDate} to ${strFinalDate}'),
      trailing: Text('$strDuration'),
    );
  }

}