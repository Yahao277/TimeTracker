package app;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
* static class for loading json files, then creates a new Activity tree.
 * methods:
 * -createTreeFromJSONfile: this method reads the json file
 * -newTreeFromJSON: recursive method to create the tree.
 * */

public class JSONLoader {

  public static Activity createTreeFromJSONFile(String s) {

    File json_file = new File(s);
    InputStream in = null;

    try {
      in = new FileInputStream(json_file);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      System.exit(1);
    }

    JSONObject obj = new JSONObject(new JSONTokener(in));

    return JSONLoader.newTreeFromJSON(null, obj);
  }

  private static Activity newTreeFromJSON(Activity parent, JSONObject obj) {

    Activity newone;
    JSONArray arr;

    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    LocalDateTime start_time = obj.getString("StartTime").equals("null") ? null : LocalDateTime.from(formatter.parse(obj.getString("StartTime")));
    LocalDateTime end_time = obj.getString("EndTime").equals("null") ? null : LocalDateTime.from(formatter.parse(obj.getString("EndTime")));
    Long duration = obj.getLong("duration");

    // Create and add childs
    switch (obj.getString("type")) {
      case "Project":
        newone = new Project(
            parent,
            obj.getString("name"),
            start_time,
            end_time,
            duration
        );

        arr = obj.getJSONArray("activities");
        for(int i=0;i < arr.length();i++){
          JSONObject objecte = arr.getJSONObject(i);
          Activity child =  newTreeFromJSON(newone, objecte);
      }
        break;
      case "Task":
        newone = new Task(
            parent,
            obj.getString("name"),
            obj.getBoolean("active"),
            start_time,
            end_time,
            duration
        );

        arr = obj.getJSONArray("intervals");

        for(int i=0;i < arr.length();i++){
          JSONObject objecte = arr.getJSONObject(i);

          LocalDateTime start = objecte.getString("StartTime").equals("null") ? null : LocalDateTime.from(formatter.parse(objecte.getString("StartTime")));
          LocalDateTime end = objecte.getString("EndTime").equals("null") ? null : LocalDateTime.from(formatter.parse(objecte.getString("EndTime")));

          Interval interval =  new Interval((Task) newone,start, end,objecte.getLong("duration"));
          ((Task) newone).addInterval(interval);
        }
        break;
      default:
        throw new NotImplementedException();
    }

    return newone;
  }


}
