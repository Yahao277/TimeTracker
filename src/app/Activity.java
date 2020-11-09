/**
 * File: Activity.java
 * Description: this file contains an abstract class part
 *    of the Composite pattern. It plays the role of the
 *    general component in the pattern. Both the leaf class and
 *    the compositor class will inherit from this class.
 */

package app;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;


abstract class Activity {

  private static Logger logger = (Logger) LoggerFactory.getLogger("milestone1.activity");

  private final Activity parent;
  private String name;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private Duration duration;

  protected Activity(Activity parent, String name) {

    this.logger.trace("New Activity");
    this.logger.debug("Normal constructor");

    this.parent = parent;
    this.name = name;
    this.duration = null;

    // Add myself to my parent list
    if (this.parent != null) {
      this.parent.addActivity(this);
    }

  }

  protected Activity(Activity parent, String name,
                     LocalDateTime start, LocalDateTime end, Long duration) {

    this.logger.trace("New Activity");
    this.logger.debug("JSON constructor");

    this.parent = parent;
    this.name = name;
    this.duration = Duration.ofSeconds(duration);
    this.startTime = start;
    this.endTime = end;

    // Add myself to my parent list
    if (this.parent != null) {
      this.parent.addActivity(this);
    }
  }


  // Composite functions
  public abstract Duration calc_duration();

  public abstract String toString();

  public abstract void addActivity(Activity a);

  public abstract void rmActivity(Activity a);

  public abstract Activity getChild(int nthChild);

  // Visitor pattern
  public abstract void accept(Printer printer);

  // getters and setters
  public Activity getParent() {
    return parent;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LocalDateTime getStartTime() {
    return startTime;
  }

  protected void setStartTime(LocalDateTime startTime) {
    this.startTime = startTime;
  }

  public LocalDateTime getEndTime() {
    return endTime;
  }

  protected void setEndTime(LocalDateTime endTime) {
    this.endTime = endTime;
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

  /*
   * Propagate upwards the structure an increment of time.
   * @param lapse The amount of time to increment
   * @param i The interval that comes from
   */
  public void propagateTime(int lapse, Interval i) {

    this.logger.debug("PropagateTime:"+this.name);

    // Check if it has started before
    if (this.startTime == null) {
      this.startTime = i.getStartTime();
    }

    // Set duration if it hasn't been set before
    if (this.duration == null) {
      this.duration = Duration.ZERO;
    }

    // Update duration and end timestamp
    this.endTime = i.getEndTime();
    this.duration = this.duration.plusSeconds(lapse);

    // Tell parents
    if (this.parent != null) {
      this.parent.propagateTime(lapse, i);
    }
  }

}
