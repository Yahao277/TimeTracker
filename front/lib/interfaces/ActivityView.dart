import 'package:flutter/material.dart';
import 'package:front/objectes/SideBarMenu.dart';
import 'package:front/pageIntervals.dart';
import 'package:front/tree.dart' as Tree hide getTree;
import 'package:front/requests.dart';
import 'package:front/objectes/llistaActivitats.dart';
import 'package:front/objectes/ordenarDialog.dart';
import 'package:front/interfaces/SettingsView.dart';
import 'package:front/interfaces/HomeScreen.dart';
import 'dart:async';

class ActivityScreen extends StatefulWidget{
  int id;
  ActivityScreen(this.id);

  @override
  _ActivityScreenState createState() => _ActivityScreenState();

}

class _ActivityScreenState extends State<ActivityScreen>{
  Tree.Tree tree;
  int id;
  Future<Tree.Tree> futureTree;
  Timer _timer;
  static const int periodeRefresh = 2;
  bool desplegat;
  Widget info;

  @override
  void initState(){
    super.initState();
    id = widget.id; // of PageActivities
    futureTree = getTree(id);
    _activateTimer();
    this.desplegat = false;
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

  void _refresh() async {
    futureTree = getTree(id); // to be used in build()
    setState(() {});
  }

  Future<void> showSortOptions() async{
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
      builder: (context) => ActivityScreen(childId),
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

  @override
  Widget build(BuildContext context) {

    return FutureBuilder<Tree.Tree>(
      future:futureTree,
      builder:(context,snapshot){
        if(snapshot.hasData){
          this.info = Info(
            activity: snapshot.data.root,
          );
          return Scaffold(
            appBar: AppBar(
              centerTitle: true,
              title: Text(snapshot.data.root.name),
              actions:[
                IconButton(
                  icon: Icon(
                    Icons.sort,
                    color: Colors.white,
                  ),
                  onPressed: () {
                    showSortOptions();
                  },
                ),
              ],
            ),
            // TODO:: Pasar info de configuracio a traves de totes les pantalles
              drawer: SideBarMenu(Configuracio('catala', '????', 'MADRID')),
            body: Column(
              children:[
                Padding(
                  padding: EdgeInsets.only(top: 32),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                    children: [
                      Text((snapshot.data.root is Tree.Project) ? 'Project: ${snapshot.data.root.name}' : 'Task: ${snapshot.data.root.name}'),
                      Text('Duration: ${snapshot.data.root.duration} seconds')
                    ],
                  ),
                ),
                Padding(
                  padding: EdgeInsets.symmetric(horizontal: 84),
                  child: Row(
                    children: [
                      Expanded(
                        child: Container(
                          child: Divider(
                            color: Colors.black,
                            height: 36,
                          ),
                        ),
                      ),
                      SizedBox(
                        width: 8,
                      ),
                      Text('Detalls'),
                      IconButton(
                        icon: Icon((this.desplegat)
                            ? Icons.arrow_drop_up
                            : Icons.arrow_drop_down),
                        onPressed: () {
                          setState(() {
                            this.desplegat = !this.desplegat;
                          });
                        },
                      )
                    ],
                  ),
                ),
                _buildDesplagable(snapshot.data.root),
                Text((snapshot.data.root is Tree.Project) ? "Sub Activities" : "Intervals"),
                Divider(),
                ListView.separated(
                  shrinkWrap: true,
                  padding: EdgeInsets.symmetric(horizontal: 16.0),
                  itemCount: snapshot.data.root.children.length,
                  itemBuilder: (BuildContext context, int index) => (snapshot.data.root) is Tree.Project ?
                      _buildRow(snapshot.data.root.children[index], index) :
                      _buildIntervalRow(snapshot.data.root.children[index],index),
                  separatorBuilder: (BuildContext context, int index) =>
                  const Divider(),
        ),
              ],
            ),
            floatingActionButton: FloatingActionButton(
              onPressed: (){
                if(snapshot.data.root is Tree.Project) {
                  Navigator.pushNamed(context, '/createActivity',arguments:snapshot.data.root.id);
                }else{
                  if((snapshot.data.root as Tree.Task).active){
                    stop(snapshot.data.root.id);
                    _refresh();
                  }else{
                    start(snapshot.data.root.id);
                    _refresh();
                  }
                }

              },
              child: _decideIcon(snapshot.data.root),
              backgroundColor: Colors.green,
            ),
            floatingActionButtonLocation: FloatingActionButtonLocation.centerFloat,
          );
        } else if(snapshot.hasError){
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

  Widget _buildIntervalRow(Tree.Interval interval, int index){
    String strDuration = Duration(seconds:interval.duration).toString().split('.').first;
    String strInitialDate = interval.initialDate.toString().split('.')[0];
    String strFinalDate = interval.finalDate.toString().split('.')[0];

    return ListTile(
      title: Text('from ${strInitialDate} to ${strFinalDate}'),
      trailing: Text('$strDuration'),
    );
  }

  Widget _decideIcon(Tree.Activity act){
    if((act) is Tree.Project) {
      return Icon(Icons.add);
    }else{
      Tree.Task tasca = act as Tree.Task;
      return tasca.active ? Icon(Icons.stop) : Icon(Icons.play_arrow);
    }
  }

  @override
  Widget _buildRow(Tree.Activity activity, int index) {
    String strDuration = Duration(seconds: activity.duration).toString().split('.').first;
    // split by '.' and taking first element of resulting list
    // removes the microseconds part
    if (activity is Tree.Project) {
      return ListTile(
        title: Text('${activity.name}'),
        trailing: Text('$strDuration'),
        onTap: () => _navigateDownActivities(activity.id),
      );
    } else if (activity is Tree.Task) {
      Tree.Task task = activity as Tree.Task;
      Widget trailing;
      trailing = IconButton(icon:(activity.active) ? Icon(Icons.stop) : Icon(Icons.play_arrow),
        onPressed: (){
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
        trailing: trailing,
        onTap: () =>  _navigateDownActivities(activity.id),
        onLongPress: () {
        },
      );
    }
  }

  Widget _buildDesplagable(Tree.Activity act) {
    return AnimatedContainer(
      duration: Duration(seconds: 10),
      child: (this.desplegat)
          ? this.info
          : SizedBox(
        height: 32,
      ),
    );
  }
}

class Info extends StatelessWidget {
  Tree.Activity activity;

  Info({Key key, @required this.activity}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: EdgeInsets.symmetric(horizontal: 32),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            'Description:',
            style: TextStyle(fontSize: 18),
          ),
          Row(
            children: [
              Expanded(
                child: Text(
                    'Lorem ipsum dolor sit amet consectetur adipiscing, elit imperdiet vehicula facilisis leo taciti dapibus,'),
              )
            ],
          ),
          SizedBox(
            height: 16,
          ),
          Text('Started At:${activity.initialDate}'),
          Text('Ended at:${activity.finalDate}'),
          SizedBox(
            height: 16,
          ),
          _printTags(activity.tags),
          SizedBox(
            height: 32,
          )
        ],
      ),
    );
  }

  Widget _printTags(List<dynamic> tags){
    String acc = 'Tags: ';
    for (String tag in tags){
      acc += tag;
      acc += ', ';
    }
    return Text(acc);
  }
}
