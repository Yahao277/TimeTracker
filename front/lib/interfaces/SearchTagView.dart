import 'package:flutter/material.dart';

class SearchTagScreen extends StatefulWidget {
  SearchTagScreen({Key key}) : super(key: key);

  @override
  _SearchTagScreenState createState() => _SearchTagScreenState();
}

class _SearchTagScreenState extends State<SearchTagScreen> {
  Widget _resultats;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        automaticallyImplyLeading: true,
        centerTitle: true,
        title: Text('Search Tag'),
      ),
      body: Column(
        children: [
          Padding(
            padding: EdgeInsets.symmetric(horizontal: 64, vertical: 64),
            child: Row(
              children: [
                Flexible(
                  child: TextField(
                    autofocus: true,
                    keyboardType: TextInputType.name,
                    maxLengthEnforced: true,
                    maxLength: 20,
                    decoration: InputDecoration(labelText: "Tag to search for"),
                  ),
                ),
                SizedBox(width: 16,),
                ColoredBox(
                  color: Colors.blue,
                  child: IconButton(
                    icon: Icon(Icons.search),
                    onPressed: () {
                      //TODO::
                    },
                  ),
                )
              ],
            ),
          ),
        ],
      ),
    );
  }
}
