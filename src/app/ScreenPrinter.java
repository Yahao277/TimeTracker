package app;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Observable;


//  Child class of printer. Responsible for screen printing the entire tree from root.To make it print periodically,
//  in the update() method the of Observer interface, it runs: "this.printActivity (root)".
//  And then in the Clock the instance of ScreenPrinter is added to the list of observers.

public class ScreenPrinter extends Printer {

  private Activity root;
  private String lastScreen;

  //Method visiting Activity (visitor).
  public void printActivity(Activity root) {
    if(this.root == root) {
      lastScreen = "";
    }
    root.accept(this);
  }

  //Method visiting Interval (visitor).
  public void printInterval(Interval interval){
    interval.accept(this);
  }

  //Sets the root.
  public ScreenPrinter(Activity root) {
    this.root = root;
  }


  //Takes the project information for screen printing and then runs a printActivity() of  children.
  @Override
  public void addProject(String name, LocalDateTime start, LocalDateTime end, long duration, List<Activity> childs, Activity parent) {
    if( start != null) {
      String holder = String.format(
          "%-8s: %-25s\tChild of: %-25s\tStarted at: %-25s\tEnded at: %-25s\tDuration: %ds",
          "Project",
          name,
          (parent == null) ? "null" : parent.getName() ,
          start,
          end,
          duration
      );

      System.out.println(holder);
      this.lastScreen += holder + '\n';
    }

    for(Activity child: childs) {
      this.printActivity(child);
    }
  }


  //Responsible for printing the job and also printInterval () from its list of intervals.
  @Override
  public void addTask(String name, LocalDateTime start, LocalDateTime end, long duration, boolean active ,List<Interval> intervals, String parent) {
    if(start != null) {
      String holder = String.format(
          "%-8s: %-25s\tChild of: %-25s\tStarted at: %-25s\tEnded at: %-25s\tDuration: %ds",
          "Task",
          name,
          parent,
          start,
          end,
          duration
      );

      System.out.println(holder);
      this.lastScreen += holder + '\n';
    }
    for(Interval interval : intervals){
      this.printInterval(interval);
    }
  }


  //prints start_time, end_time, and interval duration.
  @Override
  public void addInterval(LocalDateTime start, LocalDateTime end, long duration, String parent) {

    if(start == null){
      return;
    }

    String holder =  String.format(
        "%-8s: %-25s\tChild of: %-25s\tStarted at: %-25s\tEnded at: %-25s\tDuration: %ds",
        "Interval",
        "",
        parent,
        start,
        end,
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
