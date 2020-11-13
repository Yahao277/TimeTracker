package app;

public interface SearchVisitor {
  public void visitActivity(Activity a);
  public void checkInProject(Project p);
  public void checkInTask(Task t);
}
