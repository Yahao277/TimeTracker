// see Serializing JSON inside model classes in
// https://flutter.dev/docs/development/data-and-backend/json

import 'package:intl/intl.dart';
import 'dart:convert' as convert;

final DateFormat _dateFormatter = DateFormat("yyyy-MM-dd HH:mm:ss");

abstract class Activity {
  int id;
  String name;
  DateTime initialDate;
  DateTime finalDate;
  int duration;
  List<dynamic> tags = List<dynamic>();
  List<dynamic> children = List<dynamic>();

  Activity.fromJson(Map<String, dynamic> json):
    id = json['id'],
    name = json['name'],
    duration = json['duration'],
    tags = json['tags'],
    initialDate = json['StartTime']=='null' ? null : _dateFormatter.parse(json['StartTime'].replaceAll(new RegExp(r'T'),' ')),
    finalDate = json['EndTime']=='null' ? null : _dateFormatter.parse(json['EndTime'].replaceAll(new RegExp(r'T'),' '));

}


class Project extends Activity {
  Project.fromJson(Map<String, dynamic> json) : super.fromJson(json) {
    if (json.containsKey('activities') && json['activities'].length > 0 ) {
      print(json['activities']);
      // json has only 1 level because depth=1 or 0 in time_tracker
      for (Map<String, dynamic> jsonChild in json['activities']) {
        print(jsonChild);
        if (jsonChild['type'] == "Project") {
          children.add(Project.fromJson(jsonChild));
          // condition on key avoids infinite recursion
        } else if (jsonChild['type'] == "Task") {
          children.add(Task.fromJson(jsonChild));
        } else {
          assert(false);
        }
      }
    }
  }
}


class Task extends Activity {
  bool active;
  Task.fromJson(Map<String, dynamic> json) : super.fromJson(json) {
    active = json['active'];
    for (Map<String, dynamic> jsonChild in json['intervals']) {
      children.add(Interval.fromJson(jsonChild));
    }
  }
}


class Interval {
  int id;
  DateTime initialDate;
  DateTime finalDate;
  int duration;
  bool active;

  Interval.fromJson(Map<String, dynamic> json)
      : id = json['id'],
        initialDate = json['StartTime']== 'null' ? null : _dateFormatter.parse(json['StartTime'].replaceAll(new RegExp(r'T'),' ')),
        finalDate = json['EndTime']== 'null' ? null : _dateFormatter.parse(json['EndTime'].replaceAll(new RegExp(r'T'),' ')),
        duration = json['duration'],
        active = json['active'];
}


class Tree {
  Activity root;

  Tree(Map<String, dynamic> dec) {
    // 1 level tree, root and children only, root is either Project or Task. If Project
    // children are Project or Task, that is, Activity. If root is Task, children are Instance.

    if (dec['type'] == "Project") {
      root = Project.fromJson(dec);
    } else if (dec['type'] == "Task") {
      root = Task.fromJson(dec);
    } else {
      assert(false);
    }
  }
}


Tree getTree() {
  String strJson = "{"
      "\"name\":\"root\", \"class\":\"project\", \"id\":0, \"initialDate\":\"2020-09-22 16:04:56\", \"finalDate\":\"2020-09-22 16:05:22\", \"duration\":26,"
      "\"activities\": [ "
      "{ \"name\":\"software design\", \"class\":\"project\", \"id\":1, \"initialDate\":\"2020-09-22 16:05:04\", \"finalDate\":\"2020-09-22 16:05:16\", \"duration\":16 },"
      "{ \"name\":\"software testing\", \"class\":\"project\", \"id\":2, \"initialDate\": null, \"finalDate\":null, \"duration\":0 },"
      "{ \"name\":\"databases\", \"class\":\"project\", \"id\":3,  \"finalDate\":null, \"initialDate\":null, \"duration\":0 },"
      "{ \"name\":\"transportation\", \"class\":\"task\", \"id\":6, \"active\":false, \"initialDate\":\"2020-09-22 16:04:56\", \"finalDate\":\"2020-09-22 16:05:22\", \"duration\":10, \"intervals\":[] }"
      "] "
      "}";
  Map<String, dynamic> decoded = convert.jsonDecode(strJson);
  Tree tree = Tree(decoded);
  return tree;
}

Tree getTreeTask() {
  String strJson = "{"
      "\"name\":\"transportation\",\"class\":\"task\", \"initialDate\":\"2020-09-22 13:36:08\", \"finalDate\":\"2020-09-22 13:36:34\", \"duration\":10,"
      "\"intervals\":["
      "{\"class\":\"interval\", \"initialDate\":\"2020-09-22 13:36:08\", \"finalDate\":\"2020-09-22 13:36:14\", \"duration\":6 },"
      "{\"class\":\"interval\", \"initialDate\":\"2020-09-22 13:36:30\", \"finalDate\":\"2020-09-22 13:36:34\", \"duration\":4}"
      "]}";
  Map<String, dynamic> decoded = convert.jsonDecode(strJson);
  Tree tree = Tree(decoded);
  return tree;
}


testLoadTree() {
  Tree tree = getTree();
  print("root name ${tree.root.name}, duration ${tree.root.duration}");
  for (Activity act in tree.root.children) {
    print("child name ${act.name}, duration ${act.duration}");
  }
}


void main() {
  testLoadTree();
}