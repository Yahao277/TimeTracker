package app;

import ch.qos.logback.classic.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.LoggerFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;



/**
 * static class for loading json files, then creates a new Activity tree.
 * methods:
 * -createTreeFromJSONfile: this method reads the json file
 * -newTreeFromJSON: recursive method to create the tree.
 * */

public class JSONLoader {

  private static Logger logger = (Logger) LoggerFactory.getLogger("milestone1.JSONLoarder");

  public static Activity createTreeFromJSONFile(String s) {

    logger.debug("Entry point");
    File jsonFile = new File(s);
    InputStream in = null;

    try {
      in = new FileInputStream(jsonFile);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      System.exit(1);
    }

    JSONObject obj = new JSONObject(new JSONTokener(in));

    return JSONLoader.newTreeFromJSON(null, obj);
  }

  public static Activity fromWebRequest(String request, Activity parent) {
    String json = new String(Base64.getDecoder().decode(request));

    JSONObject obj = new JSONObject(json);
    return JSONLoader.newTreeFromJSON(parent, obj);
  }

  private static Activity newTreeFromJSON(Activity parent, JSONObject obj) {
    logger.debug("recursive function: " + obj.getString("name"));
    Activity newone;
    JSONArray arr;

    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    LocalDateTime startTime = obj.getString("StartTime").equals("null")
        ? null : LocalDateTime.from(formatter.parse(obj.getString("StartTime")));
    LocalDateTime endTime = obj.getString("EndTime").equals("null")
        ? null : LocalDateTime.from(formatter.parse(obj.getString("EndTime")));
    Long duration = obj.getLong("duration");

    // Create and add childs
    switch (obj.getString("type")) {
      case "Project":
        newone = new Project(
            parent,
            obj.getString("name"),
            startTime,
            endTime,
            duration
        );

        arr = obj.getJSONArray("activities");
        for (int i = 0; i < arr.length(); i++) {
          JSONObject objecte = arr.getJSONObject(i);
          Activity child =  newTreeFromJSON(newone, objecte);
        }
        break;
      case "Task":
        newone = new Task(
            parent,
            obj.getString("name"),
            obj.getBoolean("active"),
            startTime,
            endTime,
            duration
        );

        arr = obj.getJSONArray("intervals");

        for (int i = 0; i < arr.length(); i++) {
          JSONObject objecte = arr.getJSONObject(i);

          LocalDateTime start = objecte.getString("StartTime").equals("null")
              ? null : LocalDateTime.from(formatter.parse(objecte.getString("StartTime")));
          LocalDateTime end = objecte.getString("EndTime").equals("null")
              ? null : LocalDateTime.from(formatter.parse(objecte.getString("EndTime")));

          Interval interval =  new Interval((Task) newone, start, end, objecte.getLong("duration"));
          ((Task) newone).addInterval(interval);
        }
        break;
      default:
        throw new NotImplementedException();
    }

    return newone;
  }


}
