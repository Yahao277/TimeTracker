package app;

import java.util.ArrayList;
import java.util.List;

public class SearchTag  implements SearchVisitor {

  private String tag2search;
  private List<Activity> activities_found;

  SearchTag(String tag) {
    super();
    this.tag2search = tag;
    this.activities_found = new ArrayList<>();
  }

  public void setTag2search(String tag2search) {
    this.tag2search = tag2search;
    this.activities_found.clear();
  }

  public List<Activity> getResults() {
    return this.activities_found;
  }

  @Override
  public void visitActivity(Activity a) {
    a.accept(this);
  }

  @Override
  public void checkInProject(Project p) {
    if (p.hasTag(this.tag2search)) {
      this.activities_found.add(p);
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
      this.activities_found.add(t);
    }
  }
}
