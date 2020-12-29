import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:front/pageIntervals.dart';
import 'package:front/page_activities.dart';
import 'package:front/objectes/llistaActivitats.dart';
import 'package:front/objectes/SideBarMenu.dart';
import 'package:front/objectes/ordenarDialog.dart';
import 'package:front/interfaces/SettingsView.dart';
import 'package:front/tree.dart' hide getTree;

// the old getTree()
import 'package:front/requests.dart';
// has the new getTree() that sends an http request to the server
import 'dart:async';

class HomeScreen extends StatefulWidget {
  int id;
  HomeScreen(this.id);

  @override
  _HomeScreenState createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  Tree tree;
  int id;
  Future<Tree> futureTree;
  Timer _timer;
  Configuracio conf;

  static const int periodeRefresh = 2;

  @override
  void initState() {
    super.initState();
    conf = Configuracio('català', 'DD-MM-YYYY', 'MAD');
    id = widget.id; // of PageActivities
    futureTree = getTree(id);
    _activateTimer();
  }

  @override
  void dispose() {
    // "The framework calls this method when this State object will never build again"
    // therefore when going up
    _timer.cancel();
    super.dispose();
  }

  void _activateTimer() {
    _timer = Timer.periodic(Duration(seconds: periodeRefresh), (Timer t) {
      futureTree = getTree(id);
      setState(() {});
    });
  }

  void cancel(){
    if(_timer.isActive) {
      _timer.cancel();
    }
  }

  Future<void> _showSortOptions() async{
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

  void _navigateDownActivities(int childId) {
    _timer.cancel();
    Navigator.of(context)
        .push(MaterialPageRoute<void>(
      builder: (context) => PageActivities(childId),
    )).then((var value) {
      _activateTimer();
      _refresh();
    }
    );
  }

  void _navigateDownIntervals(int childId) {
    _timer.cancel();
    Navigator.of(context)
        .push(MaterialPageRoute<void>(
      builder: (context) => PageIntervals(childId),
    )).then( (var value) {
      _activateTimer();
      _refresh();
    }
    );
  }

  void _refresh() async {
    futureTree = getTree(id); // to be used in build()
    setState(() {});
  }

  @override
  Widget _buildRow(Activity activity, int index) {
    String strDuration = Duration(seconds: activity.duration).toString().split('.').first;
    // split by '.' and taking first element of resulting list
    // removes the microseconds part
    if (activity is Project) {
      return ListTile(
        title: Text('${activity.name}'),
        subtitle: Text('fsdfsdfsd'), // TODO: activity tags list
        leading: Icon(Icons.folder),
        trailing: Text('$strDuration'),
        onTap: () => _navigateDownActivities(activity.id),
        // TODO, navigate down to show children tasks and projects
      );
    } else if (activity is Task) {
      Task task = activity as Task;
      Widget trailing;

      trailing = IconButton(icon:(activity.active) ? Icon(Icons.stop) : Icon(Icons.play_arrow),
      onPressed: (){
        //TODO: start or stop
        if(activity.active){
          stop(activity.id);
          _refresh();
        }else{
          start(activity.id);
          _refresh();
        }
      },);
      return ListTile(
        title: Text('${activity.name}'),
        subtitle: Text('tasca'),
        leading: Icon(Icons.text_snippet),
        trailing: trailing,
        onTap: () =>  _navigateDownIntervals(activity.id),
        // TODO, navigate down to show intervals
        onLongPress: () {
        },
        // TODO start/stop counting the time for tis task
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return FutureBuilder<Tree>(
        future: futureTree,
        // this makes the tree of children, when available, go into snapshot.data
        builder: (context,snapshot){
          // anonymous function
          if(snapshot.hasData){
            return Scaffold(
              appBar: AppBar(
                  title: Text('TimeTracker'),
                  actions: <Widget>[
                    IconButton(icon: Icon(Icons.sort)
                        , onPressed: () {
                          _showSortOptions();
                          /*while(Navigator.of(context).canPop()) {
                            //print("pop");
                            // TODO: Screen Ordenar
                            Navigator.of(context).pop();
                          }*/
                          /* this works also:
                      Navigator.popUntil(context, ModalRoute.withName('/'));
                      */
                          PageActivities(0);
                        }),

                    //TODO More acitons
                  ]
              ),
              drawer: SideBarMenu(conf),
              body:Column(
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
                  ListView.separated(
                    // it's like ListView.builder() but better because it includes a separator between items
                    shrinkWrap: true,
                    padding: EdgeInsets.symmetric(horizontal: 16.0),
                    itemCount: snapshot.data.root.children.length,
                    itemBuilder: (BuildContext context, int index) =>
                        _buildRow(snapshot.data.root.children[index], index),
                    separatorBuilder: (BuildContext context, int index) =>
                    const Divider(),
                  ),
                ],
              ),
              floatingActionButton: FloatingActionButton(
                onPressed: (){
                  Navigator.pushNamed(context, '/createActivity');
                },
                child: Icon(Icons.add),
                backgroundColor: Colors.green,
              ),
              floatingActionButtonLocation: FloatingActionButtonLocation.endFloat,
            );
          }
          else if (snapshot.hasError){
            return Text("${snapshot.error}");
          }
          return Container(
              height: MediaQuery.of(context).size.height,
              color: Colors.white,
              child: Center(
                child: CircularProgressIndicator(),
              ));
        }
    );
  }
}