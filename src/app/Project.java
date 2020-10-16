package app;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
/**
 * Project class extends from the abstract class Activity,
 * we add a new private attribute (a list of activities childs)
 *
 * comparing to the Activity class, the two main changes are:
 * - Possibility of Adding new subtasks or subprojects, or remove it.
 * - The calculation of duration is based on the sum of
 * durations of his childs activities
 */

public class Project extends Activity {

  private List<Activity> childs;

  public Project(Activity parent, String name) {
    super(parent, name);

    this.childs = new ArrayList<>();
    this.setDuration(Duration.ofSeconds(0));
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
          (this.getStart_time() != null) ? this.getStart_time() : "null",
          (this.getEnd_time() != null) ? this.getEnd_time() : "null",
          this.getDuration()
      );
    }

    for (Activity a :
        this.childs) {
      holder += a.toString();
    }
    return holder;
  }

  @Override
  public String toJSON() {
    return null;
  }

  /**
   accepts the Printer visitor. printer can gets the information what it needs,
   to print on screen or write in a json file
  */
  @Override
  public void accept(Printer printer) {
    if(this.getParent() != null) {
      printer.addProject(getName(), getStart_time(), getEnd_time(), getDuration(), childs, this.getParent().getName());
    }
    else{
      printer.addProject(getName(), getStart_time(), getEnd_time(), getDuration(), childs, "null");
    }
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
    if (nth_child > (this.childs.size()-1) || nth_child < 0) {
      return null;
    }
    return this.childs.get(nth_child);
  }
}
