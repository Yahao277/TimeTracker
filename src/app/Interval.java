package app;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Observable;
import java.util.Observer;

public class Interval implements Observer {

  private Task parent;
  private LocalDateTime started_at;
  private LocalDateTime last_tick;

  // TEST DUMMY
  private Clock c;

  public Interval(Task parent) {
    this.parent = parent;
    this.last_tick = null;
    this.started_at = LocalDateTime.now();
    this.parent.propagateStartTime(this.started_at);
  }

  public void begin() {
    this.started_at = LocalDateTime.now();
    // Observe to clock
  }

  public void end() {
    // Stop Observing the clock
    this.c.deleteObserver(this);
  }

  public Duration getDuration() {
    return Duration.between(this.started_at, this.last_tick);
  }

  @Override
  public void update(Observable o, Object arg) {
        this.last_tick = (LocalDateTime) arg;
        this.parent.propagateTime(this.last_tick);

  }

  public void setClock(Clock clock) {
    this.c = clock;
    clock.addObserver(this);
  }
}
