package app;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Observer;

/**
   abstract class Printer,
    here we used two patterns: Visitor and Observer

    methods that visits Activity and Interval class:
    -printActivity(Activity root)
    -printInterval(Interval interval)
    methods that we use for printing the information:
    - addProject( ... )
    - addTask( ... )
    - addInterval ( ...)
    - write()

    Also we implements this class as a Observer, so our Clock can notify
    the changes of the Activity tree, and then print the new information of the tree.
 */
public abstract class Printer implements Observer {
  public abstract void printActivity(Activity root);
  public abstract void printInterval(Interval interval);
  public abstract void addProject(String name, LocalDateTime start, LocalDateTime end, long duration, List<Activity> childs, Activity parent);
  public abstract void addTask(String name, LocalDateTime start, LocalDateTime end, long duration,boolean active ,List<Interval> intervals, String parent);
  public abstract void addInterval(LocalDateTime start, LocalDateTime end, long duration, String parent);
  public abstract void write();
}
