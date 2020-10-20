package app;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Child class of Activity. Each task has a duration, start time and end time, represented with the interval class. Unlike project, a task has no child class.
public class Task extends Activity{

  private List<Interval> intervals;
  private Interval curr_interval;
  private boolean active;

  protected Task(Activity parent, String name) {
    super(parent, name);

    this.intervals = new ArrayList<>();
    this.curr_interval = null;
    this.active = false;
  }

  public Task(Activity parent, String name, boolean active,LocalDateTime start, LocalDateTime end, Long duration){
    super(parent,name,start,end,duration);

    this.intervals = new ArrayList<>();
    this.curr_interval = null;

    this.active = active;
    if(active){
      this.start();
    }
  }


  private void startInterval() {
    this.curr_interval = new Interval(this);
    this.active = true;
    this.intervals.add(this.curr_interval);
  }

  private void endInterval() {

    this.curr_interval = null;
    this.active = false;
  }

  public boolean getActive(){return this.active;}

  public void start() {
    this.startInterval();
    this.curr_interval.begin();
  }

  public void end() {
    this.curr_interval.end();
    this.endInterval();
  }

  @Override
  public Duration calc_duration() {
    Duration acc = Duration.ofSeconds(0);

    for (Interval i :
        this.intervals) {
      acc = acc.plus(i.getDuration());
    }
    this.setDuration(acc);
    return acc;
  }

  @Override
  public String toString() {
    return String.format(
        "%-8s: %-25s\tStarted at: %-25s\tEnded at: %-25s\tDuration: %ds\n",
        "Task",
        this.getName(),
        (this.getStart_time() != null) ? this.getStart_time() : "null",
        (this.getEnd_time() != null) ? this.getEnd_time() : "null",
        this.getDuration()
    );
  }

  @Override
  public String toJSON() {
    return null;
  }


  //so that the Printer can paste the information necessary.
  @Override
  public void accept(Printer printer) {
    printer.addTask(getName(),getStart_time(),getEnd_time(),getDuration(),getActive(),intervals, this.getParent().getName());
  }

  public void addInterval(Interval interval){
    intervals.add(interval);
  }

  @Override
  public void addActivity(Activity a) {

  }

  @Override
  public void rmActivity(Activity a) {

  }

  @Override
  public Activity getChild(int nth_child) {
    return null;
  }

}