/**
 * File: JSONPrinter.java
 * Description: Class that turns JSON objects into strings and
 * the other way around
 */

package app;

import ch.qos.logback.classic.Logger;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Observable;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;

public class JSONPrinter extends Printer {

  private static Logger logger = (Logger) LoggerFactory.getLogger("milestone1.printer.JSONprinter");

  private JSONObject obj;
  private final String path;

  public JSONPrinter(String path) { // Constructor
    this.logger.trace("New JSONPrinter");
    this.path = path;
    this.obj = new JSONObject();
  }

  @Override
  public void write() { // We write the JSON object into a file
    this.logger.debug("Writing JSON into file");
    try {
      FileWriter file = new FileWriter(path);
      file.write(obj.toString());
      file.flush();
      file.close();
    } catch (IOException e) {
      this.logger.error("Can not creat and write a new file.");
      e.printStackTrace();
    }

  }

  @Override
  public void addInterval(LocalDateTime start, LocalDateTime end, long duration, String parent) {

    this.logger.debug("Adding Interval from" + parent);

    if (start == null) {
      obj.put("StartTime", "null");
    } else {
      obj.put("StartTime", start);
    }

    if (end == null) {
      obj.put("EndTime", "null");
    } else {
      obj.put("EndTime", end);
    }

    obj.put("duration", duration);
  }

  @Override
  public void printActivity(Activity root) { // Visitor implementation
    this.logger.debug("Visiting Activity");
    root.accept(this);

  }

  @Override
  public void printInterval(Interval interval) {
    interval.accept(this);
  } // Visitor implementation

  @Override
  public void addProject(String name, LocalDateTime start, LocalDateTime end,
                         long duration, List<Activity> childs, Activity parent,int id) {
    this.logger.debug("Adding activity: " + name);
    // We add a project into our JSON array
    JSONObject aux = this.obj;
    JSONArray array = new JSONArray();

    for (Activity child : childs) {
      this.obj = new JSONObject();
      this.printActivity(child);
      array.put(this.obj);
    }

    obj = aux;
    obj.put("name", name);
    obj.put("type", "Project");
    obj.put("id",id);

    if (start == null) {
      obj.put("StartTime", "null");
    } else {
      obj.put("StartTime", start);
    }

    if (end == null) {
      obj.put("EndTime", "null");
    } else {
      obj.put("EndTime", end);
    }

    obj.put("duration", duration);
    obj.put("activities", array);
  }

  @Override
  public void addTask(String name, LocalDateTime start, LocalDateTime end,
                      long duration, boolean active, List<Interval> intervals, String parent,int id) {
    this.logger.debug("Adding task: " + name);
    // We add a task into our JSON array
    obj.put("name", name);
    obj.put("type", "Task");
    obj.put("id",id);

    if (start == null) {
      obj.put("StartTime", "null");
    } else {
      obj.put("StartTime", start);
    }

    if (end == null) {
      obj.put("EndTime", "null");
    } else {
      obj.put("EndTime", end);
    }
    obj.put("active", active);
    obj.put("duration", duration);

    JSONObject aux = this.obj;
    JSONArray array = new JSONArray();

    for (Interval interval : intervals) {
      this.obj = new JSONObject();
      this.printInterval(interval);
      array.put(this.obj);
    }
    obj = aux;
    obj.put("intervals", array);
  }

  @Override
  public void update(Observable o, Object arg) {
    this.logger.warn("Update should not be reached");
    // We leave it empty as we dont want this implementation to
    // do anything if it gets ever set as an observer
  }
}


class JSONfind  {

  private JSONObject obj;

  public JSONfind() {
    this.obj = new JSONObject();
  }

  public void printActivity(Activity root,int depth) {
    root.accept(this,depth);
  }

  public void printInterval(Interval interval) {
    interval.accept(this);
  }

  public void addProject(String name, LocalDateTime start, LocalDateTime end, long duration, List<Activity> childs,
                         Activity parent,int id,List<String>tags,int depth) {
    // We add a project into our JSON array
    JSONObject aux = this.obj;
    JSONArray array = new JSONArray();

    for (Activity child : childs) {
      this.obj = new JSONObject();
      this.printActivity(child, depth - 1);
      array.put(this.obj);
    }
    obj = aux;
    obj.put("name", name);
    obj.put("type", "Project");
    obj.put("id",id);
    obj.put("tags",tags);

    if (start == null) {
      obj.put("StartTime", "null");
    } else {
      obj.put("StartTime", start);
    }

    if (end == null) {
      obj.put("EndTime", "null");
    } else {
      obj.put("EndTime", end);
    }

    obj.put("duration", duration);
    obj.put("activities", array);
  }


  public void addTask(String name, LocalDateTime start, LocalDateTime end, long duration, boolean active,
                      List<Interval> intervals, String parent,int id, List<String> tags, int depth) {
    // We add a task into our JSON array
    obj.put("name", name);
    obj.put("type", "Task");
    obj.put("id",id);

    obj.put("tags",tags);

    if (start == null) {
      obj.put("StartTime", "null");
    } else {
      obj.put("StartTime", start);
    }

    if (end == null) {
      obj.put("EndTime", "null");
    } else {
      obj.put("EndTime", end);
    }
    obj.put("active", active);
    obj.put("duration", duration);

    JSONObject aux = this.obj;
    JSONArray array = new JSONArray();

    for (Interval interval : intervals) {
      this.obj = new JSONObject();
      this.printInterval(interval);
      array.put(this.obj);
    }
    obj = aux;
    obj.put("intervals", array);
  }


  public void addInterval(LocalDateTime start, LocalDateTime end, long duration, String parent) {
    if (start == null) {
      obj.put("StartTime", "null");
    } else {
      obj.put("StartTime", start);
    }

    if (end == null) {
      obj.put("EndTime", "null");
    } else {
      obj.put("EndTime", end);
    }

    obj.put("duration", duration);
  }


  public void write() {

  }

  public JSONObject getJson(){
    return this.obj;
  }


  public void update(Observable o, Object arg) {

  }
}