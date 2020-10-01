package app;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Project extends Activity {

  private List<Activity> childs;

  public Project(Activity parent, String name) {
    super(parent, name);

    this.childs = new ArrayList<>();
    this.setDuration(Duration.ofSeconds(0));
  }

  @Override
  public Duration calc_duration() {

    // Accumulative var
    Duration acc = Duration.ofSeconds(0);

    // Iterate between children and accumulate
    for (Activity a: this.childs) {
       acc = acc.plus(a.calc_duration());
    }

    this.setDuration(acc);
    return acc;
  }

  @Override
  public String toString() {
    return "Project: "+ this.getName() + " Started: "
    + this.getStart_time().toString() + " Ended: " + this.getEnd_time().toString()
        + " Duration: " + this.getDuration();
  }

  @Override
  public void addActivity(Activity a) {
    this.childs.add(a);
  }

  @Override
  public void rmActivity(Activity a) {
    this.childs.remove(a);
  }

  @Override
  public Activity getChild(int nth_child) {
    return this.childs.get(nth_child);
  }
}
