package app;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Task extends Activity{

  private List<Interval> intervals;
  private Interval curr_interval;

  protected Task(Activity parent, String name) {
    super(parent, name);

    this.intervals = new ArrayList<>();
    this.curr_interval = null;
  }

  private void starInterval() {
    this.curr_interval = new Interval();
    this.intervals.add(this.curr_interval);
  }

  private void endInterval() {
    this.curr_interval = null;
  }

  public void start() {
    this.starInterval();
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
    return "Task "+this.getName()+" Started: "
        + this.getStart_time().toString() + " Ended: " + this.getEnd_time().toString()
        + " Duration: " + this.getDuration();
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
