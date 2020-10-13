package app;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public abstract class Printer {
  public abstract void printActivity(Activity root);
  public abstract void printInterval(Interval interval);
  public abstract void addProject(String name, LocalDateTime start, LocalDateTime end, long duration, List<Activity> childs);
  public abstract void addTask(String name, LocalDateTime start, LocalDateTime end, long duration, List<Interval> intervals);
  public abstract void addInterval(LocalDateTime start, LocalDateTime end, long duration);
  public abstract void write() throws IOException;
}
