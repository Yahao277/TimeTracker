package app;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;


// Child class of Activity. Each task has a duration,
// start time and end time, represented with the interval class.
// Unlike project, a task has no children.
public class Task extends Activity {
  private static Logger logger = (Logger) LoggerFactory.getLogger("milestone1.activity.task");
  private static Logger logger2 = (Logger) LoggerFactory.getLogger("milestone2.activity.task");
  private final List<Interval> intervals;
  private Interval currInterval;
  private boolean active;


  /**
   * Class invariant
   * @return boolean is okay
   */
  private boolean invariant() {
    return (
            this.getParent() != null
                &&
            this.intervals.size() >= 0
                &&
            this.getName().length() > 0
        );

  }

  protected Task(Activity parent, String name) {
    super(parent, name);

    // Pre conditions
    assert parent != null && name != null : "Preconditions -Task()";
    assert name.length() > 0 : "Preconditions -Task()";

    this.logger.trace("New Task");
    this.intervals = new ArrayList<>();
    this.currInterval = null;
    this.active = false;

    assert this.invariant() : "Violated invariant - Task()";

  }

  public Task(Activity parent, String name, boolean active,
              LocalDateTime start, LocalDateTime end, Long duration) {
    super(parent, name, start, end, duration);

    assert parent != null && name != null : "Preconditions -Task(json)";

    this.logger.trace("New Task from JSON");
    this.intervals = new ArrayList<>();
    this.currInterval = null;

    this.active = active;
    if (active) {
      this.start();
    }

    assert this.invariant() : "Violated invariant - Task(json)";
  }


  private void startInterval() {

    assert this.currInterval == null && this.active == false : "Pre condition - startInterval()";

    this.currInterval = new Interval(this);
    this.active = true;
    this.intervals.add(this.currInterval);

    assert this.currInterval != null && this.active == true : "Post condition - startInterval()";
  }

  private void endInterval() {
    assert (this.currInterval != null && this.active == true) :
        "Pre condition - endInterval() - Can't stop interval if there is no interval working";
    this.currInterval = null;
    this.active = false;

    assert this.currInterval == null && this.active == false : "Post condition - endtInterval()";
  }

  public boolean getActive() {
    return this.active;
  }

  public void start() {
    assert this.currInterval == null && this.active == false : "Pre condition - start()";
    this.logger.debug("Creating and starting interval");
    this.logger.debug("starting interval");
    this.startInterval();
    this.currInterval.begin();
    assert this.currInterval != null && this.active == true : "Post condition - start()";
  }

  public void end() {
    this.logger.debug("Stoping interval");
    assert (this.currInterval != null && this.active == true) : "Pre condition - end()";
    this.currInterval.end();
    this.endInterval();
    assert this.currInterval == null && this.active == false : "Post condition - end()";
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

    assert printer != null : "Pre condition - accept()";

    this.logger.debug("Accepting visitor");
    printer.addTask(getName(), getStartTime(), getEndTime(), getDuration(),
        getActive(), intervals, this.getParent().getName());
  }

  @Override
  public void accept(SearchVisitor s) {
    assert s != null : "Pre condition - accept()";
    this.logger2.debug("Accepting visitor");
    s.checkInTask(this);
  }

  public void addInterval(Interval interval) {
    assert interval != null : "Pre condition - addInterval()";
    intervals.add(interval);
    assert this.invariant() : "Violated invariant - addInterval()";
  }

  public Interval getInterval(int nthInterval) {
    assert nthInterval >= 0 : "Pre condition - getInterval()";
    if (nthInterval > (this.intervals.size() - 1) || nthInterval < 0) {
      return null;
    }
    assert this.invariant() : "Violated invariant - getInterval()";
    return this.intervals.get(nthInterval);
  }

  // We leave the following methods empty as this class as the
  // leaf in the composite won't have any children
  @Override
  public void addActivity(Activity a) {
    assert 1 == 0 : "We shouldn't get here - addActivity()";
  }

  @Override
  public void rmActivity(Activity a) {
    assert 1 == 0 : "We shouldn't get here - rmActivity()";
  }

  @Override
  public Activity getChild(int nthChild) {
    return null;
  }

}