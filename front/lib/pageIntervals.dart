import 'package:flutter/material.dart';
import 'package:front/tree.dart' as Tree hide getTree;
import 'package:front/page_activities.dart';
// to avoid collision with an Interval class in another library
import 'package:front/requests.dart';

class PageIntervals extends StatefulWidget {
  int id;
  PageIntervals(this.id);

  @override
  _PageIntervalsState createState() => _PageIntervalsState();
}

class _PageIntervalsState extends State<PageIntervals> {
  int id;
  Future<Tree.Tree> futureTree;

  @override
  void initState() {
    super.initState();
    id = widget.id;
    futureTree = getTree(id);
  }

  @override
  Widget build(BuildContext context) {
    return FutureBuilder<Tree.Tree>(
      future: futureTree,
      // this makes the tree of children, when available, go into snapshot.data
      builder: (context, snapshot) {
        // anonymous function
        if (snapshot.hasData) {
          int numChildren = snapshot.data.root.children.length;
          return Scaffold(
            appBar: AppBar(
              title: Text(snapshot.data.root.name),
              actions: <Widget>[
                IconButton(icon: Icon(Icons.home),
                  onPressed: () {
                    while(Navigator.of(context).canPop()) {
                      print("pop");
                      Navigator.of(context).pop();
                    }
                    /* this works also:
                      Navigator.popUntil(context, ModalRoute.withName('/'));
                      */
                    PageActivities(0);
                } // TODO
                )
              ],
            ),
            body: ListView.separated(
              // it's like ListView.builder() but better because it includes a separator between items
              padding: const EdgeInsets.all(16.0),
              itemCount: numChildren,
              itemBuilder: (BuildContext context, int index) =>
                  _buildRow(snapshot.data.root.children[index], index),
              separatorBuilder: (BuildContext context, int index) =>
              const Divider(),
            ),
          );
        } else if (snapshot.hasError) {
          return Text("${snapshot.error}");
        }
        // By default, show a progress indicator
        return Container(
            height: MediaQuery.of(context).size.height,
            color: Colors.white,
            child: Center(
              child: CircularProgressIndicator(),
            ));
      },
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