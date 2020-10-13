package app;
import app.Activity;
import app.Interval;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class ScreenPrinter extends Printer {

  private Activity root;
  private String lastScreen;

  public void printActivity(Activity root) {
    if(this.root == root) {
      lastScreen = "";
    }
    root.accept(this);
  }
  public void printInterval(Interval interval){
    interval.accept(this);
  }

  public ScreenPrinter(Activity root) {
    this.root = root;
  }

  @Override
  public void addProject(String name, LocalDateTime start, LocalDateTime end, long duration, List<Activity> childs, String parent) {
    String holder = String.format(
        "%-8s: %-25s\tChild of: %-25s\tStarted at: %-25s\tEnded at: %-25s\tDuration: %ds",
        "Project",
        name,
        parent,
        (start != null) ? start : "null",
        (end != null) ? end : "null",
        duration
    );

    System.out.println(holder);
    this.lastScreen += holder+'\n';

    for(Activity child: childs) {
      this.printActivity(child);
    }
  }

  @Override
  public void addTask(String name, LocalDateTime start, LocalDateTime end, long duration, List<Interval> intervals, String parent) {

    String holder = String.format(
        "%-8s: %-25s\tChild of: %-25s\tStarted at: %-25s\tEnded at: %-25s\tDuration: %ds",
        "Task",
        name,
        parent,
        (start != null) ? start : "null",
        (end != null) ? end : "null",
        duration
    );

    System.out.println(holder);
    this.lastScreen += holder+'\n';

    for(Interval interval : intervals){
      this.printInterval(interval);
    }
  }

  @Override
  public void addInterval(LocalDateTime start, LocalDateTime end, long duration, String parent) {
    String holder =  String.format(
        "%-8s: %-25s\tChild of: %-25s\tStarted at: %-25s\tEnded at: %-25s\tDuration: %ds",
        "Interval",
        "",
        parent,
        (start != null) ? start : "null",
        (end != null) ? end : "null",
        duration
    );

    System.out.println(holder);
    this.lastScreen += holder+'\n';
  }

  @Override
  public void write(){
    System.out.println(this.lastScreen);
  }


  @Override
  public void update(Observable o, Object arg) {
    this.printActivity(this.root);
    System.out.println("===================================================================================");
  }
}
