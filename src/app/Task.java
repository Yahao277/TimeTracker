package app;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


// Child class of Activity. Each task has a duration,
// start time and end time, represented with the interval class.
// Unlike project, a task has no children.
public class Task extends Activity {
  private final List<Interval> intervals;
  private Interval currInterval;
  private boolean active;
  private static Logger logger;

  protected Task(Activity parent, String name) {
    super(parent, name);

    this.intervals = new ArrayList<>();
    this.currInterval = null;
    this.active = false;
    this.logger = LoggerFactory.getLogger(Task.class);
  }

  public Task(Activity parent, String name, boolean active,
              LocalDateTime start, LocalDateTime end, Long duration) {
    super(parent, name, start, end, duration);

    this.intervals = new ArrayList<>();
    this.currInterval = null;

    this.active = active;
    if (active) {
      this.start();
    }
  }


  private void startInterval() {
    this.currInterval = new Interval(this);
    this.active = true;
    this.intervals.add(this.currInterval);
  }

  private void endInterval() {

    this.currInterval = null;
    this.active = false;
  }

  public boolean getActive() {
    return this.active;
  }

  public void start() {
    this.logger.debug("starting interval");
    this.startInterval();
    this.currInterval.begin();
  }

  public void end() {
    this.currInterval.end();
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
        (this.getStartTime() != null) ? this.getStartTime() : "null",
        (this.getEndTime() != null) ? this.getEndTime() : "null",
        this.getDuration()
    );
  }


  //so that the Printer can paste the information necessary.
  @Override
  public void accept(Printer printer) {
    printer.addTask(getName(), getStartTime(), getEndTime(), getDuration(),
        getActive(), intervals, this.getParent().getName());
  }

  public void addInterval(Interval interval) {
    intervals.add(interval);
  }

  // We leave the following methods empty as this class as the
  // leaf in the composite won't have any children
  @Override
  public void addActivity(Activity a) {

  }

  @Override
  public void rmActivity(Activity a) {

  }

  @Override
  public Activity getChild(int nthChild) {
    return null;
  }

}