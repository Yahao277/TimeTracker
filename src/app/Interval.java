package app;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Observable;
import java.util.Observer;

public class Interval implements Observer {

  private Task parent;

  public LocalDateTime getEndTime() {
    return last_tick;
  }

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
        System.out.println("interval:               " + this.started_at + "   "+ LocalDateTime.now() + "    " + getDuration());
        this.parent.propagateTime(this);

  }

  public void accept(Printer printer){
    printer.addInterval(started_at,last_tick,getDuration().getSeconds(), this.parent.getName());
  }

  public void setClock(Clock clock) {
    this.c = clock;
    clock.addObserver(this);
  }
}
