package app;

import java.time.Duration;
import java.time.LocalDateTime;

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

    // DUMMY FOR NOW
    this.start_time = LocalDateTime.MIN;
    this.end_time = LocalDateTime.MAX;
    this.duration = Duration.ofSeconds(0);
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
    return duration.getSeconds();
  }

  protected void setDuration(Duration duration) {
    this.duration = duration;
  }

  public void propagateTime(Interval last_tick) {
    this.end_time = last_tick.getEndTime();
    
    this.duration = this.duration.plus(last_tick.getDuration());
    System.out.println("activity: " + getName() + "   " + getStart_time() + "   " + LocalDateTime.now() + "   " + getDuration());
    if (this.parent != null) {
      this.parent.propagateTime(last_tick);
    }
  }

  public void propagateStartTime(LocalDateTime last_tick) {
    if(this.parent != null) {
      if (this.parent.getStart_time() == LocalDateTime.MIN) {
        this.start_time = last_tick;
        this.parent.propagateStartTime(last_tick);
      }
    }
  }
}
