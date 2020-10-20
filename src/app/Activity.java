/**
 * File: Activity.java
 * Description: this file contains an abstract class part
 *    of the Composite pattern. It plays the role of the
 *    general component in the pattern. Both the leaf class and
 *    the compositor class will inherit from this class.
 */

package app;

import org.json.JSONObject;
import org.json.JSONTokener;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
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

  protected Activity(Activity parent, String name, LocalDateTime start,LocalDateTime end,Long duration){
    this.parent = parent;
    this.name = name;
    this.duration = Duration.ofSeconds(duration);
    this.start_time = start;
    this.end_time = end;

    // Add myself to my parent list
    if (this.parent != null) {
      this.parent.addActivity(this);
    }
  }


  // Composite functions
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

  /**
   * Propagate upwards the structure an increment of time.
   * @param lapse The amount of time to increment
   * @param i The interval that comes from
   */
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
