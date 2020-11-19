package app;

import ch.qos.logback.classic.Logger;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.LoggerFactory;



/**
 * File: SearchTag.java
 * Description: This class implements a visitor against the tree of
 * projects and activities. The goal of this class is to search tags
 * associated with each activity.
 */

public class SearchTag  implements SearchVisitor {

  private String tag2search;
  private List<Activity> activitiesFound;
  private static Logger logger = (Logger) LoggerFactory.getLogger("milestone2.SearchTag");

  SearchTag(String tag) {
    super();
    this.tag2search = tag;
    this.activitiesFound = new ArrayList<>();
    logger.trace("New Search by tag, tag name: " + tag);
  }

  public void setTag2search(String tag2search) {
    this.tag2search = tag2search;
    this.activitiesFound.clear();
  }

  public List<Activity> getResults() {
    return this.activitiesFound;
  }

  /**
   * Pattern methods
   */
  @Override
  public void visitActivity(Activity a) {
    logger.debug("Visiting activity");
    a.accept(this);
  }

  /**
   * Specific methods for callback.
   */
  @Override
  public void checkInProject(Project p) {
    if (p.hasTag(this.tag2search)) {
      logger.debug("Found project: " + p.getName());
      this.activitiesFound.add(p);
    }

    Activity aux = p.getChild(0);
    int i = 1;
    while (aux != null) {
      this.visitActivity(aux);
      aux = p.getChild(i);
      ++i;
    }
  }

  @Override
  public void checkInTask(Task t) {
    if (t.hasTag(this.tag2search)) {
      logger.debug("Found task: " + t.getName());
      this.activitiesFound.add(t);
    }
  }
}
