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
  private JSONObject obj;
  private FileWriter file;
  private String path;

  public JSONPrinter(String path){
    this.path = path;
    this.obj = new JSONObject();
  }

  @Override
  public void write() throws IOException {
    file = new FileWriter(path);
    file.write(obj.toString());

    file.flush();
    file.close();

  }

  @Override
  public void addInterval(LocalDateTime start, LocalDateTime end, long duration, String parent){
    obj.put("StartTime",start);
    obj.put("EndTime",end);
    obj.put("duration",duration);
  }

  @Override
  public void printActivity(Activity root) {
    root.accept(this);

  }

  @Override
  public void printInterval(Interval interval) {
    interval.accept(this);
  }

  @Override
  public void addProject(String name, LocalDateTime start, LocalDateTime end, long duration, List<Activity> childs, String parent) {

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
    obj.put("StartTime",start);
    obj.put("EndTime",end);
    obj.put("duration",duration);
    obj.put("activities",array);
  }

  @Override
  public void addTask(String name, LocalDateTime start, LocalDateTime end, long duration, List<Interval> intervals, String parent) {
    obj.put("name",name);
    obj.put("type","Task");
    obj.put("StartTime",start);
    obj.put("EndTime",end);
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
