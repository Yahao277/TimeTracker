package app;
import app.Activity;
import app.Interval;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class ScreenPrinter extends Printer{
  public void printActivity(Activity root) {
      root.accept(this);
  }
  public void printInterval(Interval interval){
    interval.accept(this);
  }

  @Override
  public void addProject(String name, LocalDateTime start, LocalDateTime end, long duration, List<Activity> childs, String parent) {
    String holder;
    holder = "Project: " + name + "   Child of: " + parent + "   Started: " + start + "   Ended: " + end + "   Duration: " + duration;
    System.out.println(holder);
    for(Activity child: childs){
      this.printActivity(child);
    }
  }

  @Override
  public void addTask(String name, LocalDateTime start, LocalDateTime end, long duration, List<Interval> intervals, String parent) {
    String holder;
    holder = "Task: " + name + "   Child of: " + parent + "   Started: " + start + "   Ended: " + end + "   Duration: " + duration;
    System.out.println(holder);
    for(Interval interval : intervals){
      this.printInterval(interval);
    }
  }

  @Override
  public void addInterval(LocalDateTime start, LocalDateTime end, long duration, String parent) {
    String holder;
    holder = "Interval " + "   Child of: " + parent + "   Started: " + start + "   Ended: " + end + "   Duration: " + duration;
    System.out.println(holder);
  }

  @Override
  public void write() throws IOException {

  }


}
