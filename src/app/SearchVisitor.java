package app;

/**
 * class: SearchVisitor
 * An interface to visit the Activity class, with this interface we implements
 * the SearchTime and SearchTag class
 */

public interface SearchVisitor {
  public void visitActivity(Activity a);

  public void checkInProject(Project p);

  public void checkInTask(Task t);
}
