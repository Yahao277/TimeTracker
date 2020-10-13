package app;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Observer;

public abstract class Printer implements Observer {
  public abstract void printActivity(Activity root);
  public abstract void printInterval(Interval interval);
  public abstract void addProject(String name, LocalDateTime start, LocalDateTime end, long duration, List<Activity> childs, String parent);
  public abstract void addTask(String name, LocalDateTime start, LocalDateTime end, long duration, List<Interval> intervals, String parent);
  public abstract void addInterval(LocalDateTime start, LocalDateTime end, long duration, String parent);
  public abstract void write() throws IOException;
}
