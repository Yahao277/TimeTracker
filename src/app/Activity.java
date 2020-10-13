package app;

import org.json.JSONObject;
import org.json.JSONTokener;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

abstract class Activity {

  private Activity parent;
  private String name;
  private LocalDateTime start_time;
  private LocalDateTime end_time;
  private Duration duration;

  protected Activity(Activity parent, String name) {
    this.parent = parent;
    this.name = name;
    this.duration = null;

    // Add myself to my parent list
    if (this.parent != null) {
      this.parent.addActivity(this);
    }

  }

  public static Activity createTreeFromJSONFile(String s) {

    File json_file = new File(s);
    InputStream in = null;

    try {
      in = new FileInputStream(json_file);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      System.exit(1);
    }

    JSONObject obj = new JSONObject(new JSONTokener(in));

    return Activity.newTreeFromJSON(null, obj);
  }

  private static Activity newTreeFromJSON(Activity parent, JSONObject obj) {

    Activity newone;

    // Create and add childs
    switch (obj.getString("type")) {
      case "Project":
        newone = new Project(
            parent,
            obj.getString("name")
        );
        // TODO: Add childs
        break;
      case "Task":
        newone = new Task(
            parent,
            obj.getString("name")
        );
        // TODO: Add intervals
        break;
      default:
        throw new NotImplementedException();
    }

    // Set fields
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("");
    newone.start_time = LocalDateTime.from(formatter.parse(obj.getString("start")));




    return null;
  }

  abstract public Duration calc_duration();
  abstract public String toString();
  abstract public String toJSON();
  abstract public void addActivity(Activity a);
  abstract public void rmActivity(Activity a);
  abstract public Activity getChild(int nth_child);

  public abstract void accept(Printer printer);

  public Activity getParent() {
    return parent;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LocalDateTime getStart_time() {
    return start_time;
  }

  protected void setStart_time(LocalDateTime start_time) {
    this.start_time = start_time;
  }

  public LocalDateTime getEnd_time() {
    return end_time;
  }

  protected void setEnd_time(LocalDateTime end_time) {
    this.end_time = end_time;
  }

  public long getDuration() {
    long res = 0;

    if (this.duration != null) {
      res += duration.getSeconds();
    }
    return res;
  }

  protected void setDuration(Duration duration) {
    this.duration = duration;
  }

  public void propagateTime(int lapse, Interval i) {

    // Check if it has started before
    if (this.start_time == null) {
      this.start_time = i.getStartTime();
    }

    // Set duration if it hasn't been set before
    if (this.duration == null) {
      this.duration = Duration.ZERO;
    }

    // Update duration and end timestamp
    this.end_time = i.getEndTime();
    this.duration = this.duration.plusSeconds(lapse);

    // Tell parents
    if (this.parent != null) {
      this.parent.propagateTime(lapse, i);
    }
  }

}
