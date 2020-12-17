package app;

public class IdGenerator {
  private static volatile IdGenerator uniqueInstance = null;
  private int id;

  public static synchronized IdGenerator getInstance() {
    if(uniqueInstance == null) {
      synchronized (IdGenerator.class) {
        uniqueInstance = new IdGenerator(-1);
      }
    }
    return uniqueInstance;
  }

  private IdGenerator(int number) {
    this.id = number;
  }
  public int generateId() {
    this.id = this.id + 1;
    return this.id;
  }
}

