package app;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/*
 * Project class extends from the abstract class Activity,
 * we add a new private attribute (a list of activities childs).
 * comparing to the Activity class, the two main changes are:
 * - Possibility of Adding new subtasks or subprojects, or remove it.
 * - The calculation of duration is based on the sum of
 * durations of his childs activities
 */

public class Project extends Activity {

  private final List<Activity> childs;

  public Project(Activity parent, String name) {
    super(parent, name);

    this.childs = new ArrayList<>();
    this.setDuration(Duration.ofSeconds(0));
  }

  public Project(Activity parent, String name, LocalDateTime start,
                 LocalDateTime end, Long duration) {
    super(parent, name, start, end, duration);

    this.childs = new ArrayList<>();
  }

  /**
   To calculate the duration we should get the duration
   of his childs activities
   */
  @Override
  public Duration calc_duration() {

    // Accumulative var
    Duration acc = Duration.ofSeconds(0);

    // Iterate between children and accumulate
    for (Activity a : this.childs) {
      acc = acc.plus(a.calc_duration());
    }

    this.setDuration(acc);
    return acc;
  }

  @Override
  public String toString() {

    String holder;

    if (this.getParent() == null) {
      // Don't print anything for the root_node
      holder = "";
    } else {
      holder = String.format(
          "%-8s: %-25s\tStarted at: %-25s\tEnded at: %-25s\tDuration: %ds\n",
          "Project",
          this.getName(),
          (this.getStartTime() != null) ? this.getStartTime() : "null",
          (this.getEndTime() != null) ? this.getEndTime() : "null",
          this.getDuration()
      );
    }

    for (Activity a :
        this.childs) {
      holder += a.toString();
    }
    return holder;
  }

  /**
   accepts the Printer visitor. printer can gets the information what it needs,
   to print on screen or write in a json file
  */
  @Override
  public void accept(Printer printer) {
    printer.addProject(getName(), getStartTime(), getEndTime(),
          getDuration(), childs, this.getParent());

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
  public Activity getChild(int nthChild) {
    if (nthChild > (this.childs.size() - 1) || nthChild < 0) {
      return null;
    }
    return this.childs.get(nthChild);
  }
}
