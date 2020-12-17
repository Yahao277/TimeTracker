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

class SearchId implements SearchVisitor
{
  int id2search;
  Activity found;
  public SearchId(int id){
    this.id2search = id;
    this.found = null;
  }

  public Activity getFoundActivity(){
    return this.found;
  }

  @Override
  public void visitActivity(Activity a) {
    a.accept(this);
  }

  @Override
  public void checkInProject(Project p) {
    if(this.found == null) {
      if (p.getId() == this.id2search) {
        this.found = p;
      }else {
        Activity aux = p.getChild(0);
        int i = 1;
        while (aux != null) {
          this.visitActivity(aux);
          aux = p.getChild(i);
          ++i;
        }
      }
    }
  }

  @Override
  public void checkInTask(Task t) {
    if(t.getId() == this.id2search){
      this.found = t;
    }

  }
}
