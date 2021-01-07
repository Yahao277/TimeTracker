import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:front/tree.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'dart:io';

class CreateEditScreen extends StatefulWidget {
  CreateEditScreen({Key key}) : super(key: key);

  @override
  _CreateEditScreenState createState() => _CreateEditScreenState();
}

class _CreateEditScreenState extends State<CreateEditScreen> {
  bool editMode = false;
  bool task;
  TextEditingController _nameController;
  TextEditingController _tagController;
  //Map<String,String> act = {"parent_id":"0","duration":"0","EndTime":"null","name":"null","StartTime":"null","active":"false","type":"null"};
  //Map<String,dynamic> activity = {"parent_id":0,"duration":0,"intervals":[],"EndTime":"null","name":"null","StartTime":"null","active":false,"type":"null"};
  Map<String,String> activity = {"name":"null","type":"task","parent_id":"0","tag":""};

  @override
  void initState() {
    super.initState();
    this.task = true;
    this._nameController = TextEditingController();
    this._tagController = TextEditingController();
  }

  Widget _generateCrear() {

    return Padding(
      padding: EdgeInsets.symmetric(
        horizontal: 64,
        vertical: 32,
      ),
      child: SingleChildScrollView(
        child: Column(
          children: [
            TextField(
              maxLength: 128,
              decoration:
              InputDecoration(labelText: 'Name for the new activity:'),
                controller: _nameController,
            ),
            SizedBox(
              height: 16,
            ),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text('Project'),
                CupertinoSwitch(
                  value: this.task,
                  onChanged: (bool newstate) {
                    print('state change');
                    setState(() {
                      this.task = newstate;
                    });
                  },
                ),
                Text('Task')
              ],
            ),
            SizedBox(
              height: 64,
            ),
            /*TextField(
              minLines: 3,
              maxLines: 5,
              maxLength: 120,
              keyboardType: TextInputType.multiline,
              decoration: InputDecoration(
                  labelText: 'Description',
                  hintText: 'Enter here a desccription for the activity...'),
            ),*/
            SizedBox(
              height: 64,
            ),
            Row(
              children: [
                Text('Add tags:'),
                Flexible(child: TextField(
                  controller: _tagController
                )),
                /*IconButton(
                  icon: Icon(Icons.add),
                  color: Colors.blue,
                  onPressed: () {
                  },
                ),*/
              ],
            ),
          ],
        ),
      ),
    );
  }

  bool _validateForm(String name){
    if (name != null && name.length > 0){
      return true;
    }
    return false;
  }

  @override
  Widget build(BuildContext context) {
    int parentId = ModalRoute.of(context).settings.arguments;
    bool editMode = false;
    this._nameController.text = '';

    return Scaffold(
      appBar: AppBar(
        automaticallyImplyLeading: true,
        title: Text('Create activity'),
        centerTitle: true,
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () async {

          bool validateResult = _validateForm(_nameController.text);
          activity['name'] = _nameController.text;
          activity['type'] = this.task ? "Task" : "Project";
          activity["parent_id"] = parentId.toString();
          activity["tag"] = _tagController.text;

          String url = "10.0.2.2:8080";
          print(_nameController.text);

          //print(Uri.http("localhost:8080", "/add", activity));
          String uri = Uri.http(url,"/add",activity).toString();

          Map<String,String> headers = {
            'Content-type' : 'application/json',
            'Accept': 'application/json',
          };

          print(Uri.http(url,"/add",activity));
         final response = await http.get(Uri.http(url,"/add",activity),headers:headers);
         print(response.statusCode);

          await showDialog(
            context:context,
            builder:(context){
              return AlertDialog(
                content: validateResult ? Text("Activity created") : Text("Failed")
              );
            }
          );


          if(validateResult){
            Navigator.pushReplacementNamed(context,"/");
          }else{
            return;
          }

        },
        child: Icon(Icons.check_circle_outline),
      ),
      floatingActionButtonLocation: FloatingActionButtonLocation.endFloat,
      floatingActionButtonAnimator: FloatingActionButtonAnimator.scaling,
      body: _generateCrear(),
    );
  }
}

/*
  Widget _generateEditar(Activity act) {
    return Padding(
      padding: EdgeInsets.symmetric(
        horizontal: 64,
        vertical: 32,
      ),
      child: SingleChildScrollView(
        child: Column(
          children: [
            TextField(
              maxLength: 128,
              decoration: InputDecoration(
                labelText: 'Name for the activity:',
              ),
            ),
            SizedBox(
              height: 16,
            ),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text('Project'),
                CupertinoSwitch(
                  value: this.task,
                  onChanged: (bool newstate) {
                    print('state change');
                    setState(() {
                      this.task = newstate;
                    });
                  },
                ),
                Text('Task')
              ],
            ),
            SizedBox(
              height: 64,
            ),
            TextField(
              minLines: 3,
              maxLines: 5,
              maxLength: 120,
              keyboardType: TextInputType.multiline,
              decoration: InputDecoration(
                  labelText: 'Description',
                  hintText: 'Enter here a desccription for the activity...'),
            ),
            SizedBox(
              height: 64,
            ),
            Row(
              children: [
                Text('Add tags:'),
                Flexible(child: TextField()),
                IconButton(
                  icon: Icon(Icons.add),
                  color: Colors.blue,
                  onPressed: () {
                    // TODO::
                  },
                )
              ],
            ),
          ],
        ),
      ),
    );
  }
*/
