package app;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Observable;


public class JSONPrinter extends Printer {
  // Class that turns JSON objects into strings and the other way around
  private JSONObject obj;
  private FileWriter file;
  private String path;

  public JSONPrinter(String path){ // Constructor
    this.path = path;
    this.obj = new JSONObject();
  }

  @Override
  public void write() { // We write the JSON object into a file
    try {
      file = new FileWriter(path);
      file.write(obj.toString());
      file.flush();
      file.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  @Override
  public void addInterval(LocalDateTime start, LocalDateTime end, long duration, String parent){
    if(start == null){
      obj.put("StartTime","null");
    }else{
      obj.put("StartTime",start);
    }

    if(end == null){
      obj.put("EndTime","null");
    }else{
      obj.put("EndTime",end);
    }

    obj.put("duration",duration);
  }

  @Override
  public void printActivity(Activity root) { // Visitor implementation
    root.accept(this);

  }

  @Override
  public void printInterval(Interval interval) {
    interval.accept(this);
  } // Visitor implementation

  @Override
  public void addProject(String name, LocalDateTime start, LocalDateTime end, long duration, List<Activity> childs, String parent) {
  // We add a project into our JSON array
    JSONObject aux = this.obj;
    JSONArray array = new JSONArray();

    for(Activity child: childs){
      this.obj = new JSONObject();
      this.printActivity(child);
      array.put(this.obj);
    }
    obj = aux;
    obj.put("name",name);
    obj.put("type","Project");

    if(start == null){
      obj.put("StartTime","null");
    }else{
      obj.put("StartTime",start);
    }

    if(end == null){
      obj.put("EndTime","null");
    }else{
      obj.put("EndTime",end);
    }

    obj.put("duration",duration);
    obj.put("activities",array);
  }

  @Override
  public void addTask(String name, LocalDateTime start, LocalDateTime end, long duration, boolean active,List<Interval> intervals, String parent) {
    // We add a task into our JSON array
    obj.put("name",name);
    obj.put("type","Task");

    if(start == null){
      obj.put("StartTime","null");
    }else{
      obj.put("StartTime",start);
    }

    if(end == null){
      obj.put("EndTime","null");
    }else{
      obj.put("EndTime",end);
    }
    obj.put("active",active);
    obj.put("duration",duration);

    JSONObject aux = this.obj;
    JSONArray array = new JSONArray();

    for(Interval interval: intervals){
      this.obj = new JSONObject();
      this.printInterval(interval);
      array.put(this.obj);
    }
    obj = aux;
    obj.put("intervals",array);
  }

  @Override
  public void update(Observable o, Object arg) {

  }
}
