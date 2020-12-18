package app;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;



/**
 * Project class extends from the abstract class Activity,
 * we add a new private attribute (a list of activities childs).
 * comparing to the Activity class, the two main changes are:
 * - Possibility of Adding new subtasks or subprojects, or remove it.
 * - The calculation of duration is based on the sum of
 * durations of his childs activities
 */

public class Project extends Activity {

  private static Logger logger = (Logger) LoggerFactory.getLogger("milestone1.activity.project");
  private final List<Activity> childs;
  private static Logger logger2 = (Logger) LoggerFactory.getLogger("milestone2.activity.project");

  /**
   * Class Invariant
   * @return boolean
   */
  private boolean invariant() {
    logger2.info("Checking invariant");
    return (this.childs.size() >= 0 && this.getName().length() > 0);
  }

  public Project(Activity parent, String name) {
    super(parent, name);

    // Pre conditions
    assert name != null : "Preconditions -Project()";
    assert name.length() > 0 : "Preconditions -Project()";

    this.logger.trace("New Project");

    this.childs = new ArrayList<>();
    this.setDuration(Duration.ofSeconds(0));

    assert this.invariant() : "Violated invariant - Project()";
  }

  public Project(Activity parent, String name, LocalDateTime start,
                 LocalDateTime end, Long duration) {
    super(parent, name, start, end, duration);

    assert name != null : "Preconditions -Project(json)";

    this.logger.trace("New Project from JSON");

    this.childs = new ArrayList<>();

    assert this.invariant() : "Violated invariant - Prjocet(json)";
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
    assert this.invariant();
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
    assert this.invariant();
    return holder;
  }

  /**
   accepts the Printer visitor. printer can gets the information what it needs,
   to print on screen or write in a json file
  */
  @Override
  public void accept(Printer printer) {

    assert printer != null : "Pre condition - accept()";

    this.logger.debug("Accepting visitor");
    printer.addProject(getName(), getStartTime(), getEndTime(),
          getDuration(), childs, this.getParent(),getId());

    assert this.invariant();

  }
  
  @Override
  public void accept(SearchVisitor s) {
    assert s != null : "Pre condition - accept()";
    this.logger2.debug("Accepting visitor search");
    s.checkInProject(this);
    assert this.invariant();
  }

  @Override
  public void accept(JSONfind p,int depth) {

    if (depth == 0) {
      p.addProject(getName(), getStartTime(), getEndTime(),
          getDuration(), new ArrayList<>(), this.getParent(), getId(), depth);
    } else if (depth > 0) {
      p.addProject(getName(), getStartTime(), getEndTime(),
          getDuration(), childs, this.getParent(), getId(), depth);
    } else if (depth < 0) {
      // do nothing
    }
  }

  @Override
  public void addActivity(Activity a) {
    assert a != null : "Pre condition - addActivity()";
    this.childs.add(a);
    assert this.invariant() : "Violated invariant - AddActivity()";
  }

  @Override
  public void rmActivity(Activity a) {
    assert a != null : "Pre condition - rmActivity()";
    this.childs.remove(a);
    assert this.invariant() : "Violated invariant - rmActivity()";
  }

  @Override
  public Activity getChild(int nthChild) {
    assert nthChild >= 0 : "Pre condition - getChild()";
    if (nthChild > (this.childs.size() - 1) || nthChild < 0) {
      return null;
    }
    assert this.invariant() : "Violated invariant - getChild()";
    return this.childs.get(nthChild);
  }
}
