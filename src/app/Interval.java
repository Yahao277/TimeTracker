/**
 * File: Interval.java
 * Description: Class that represents a time interval and observes
 * the clock to update itself.
 */

package app;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Observable;
import java.util.Observer;

public class Interval implements Observer {
  private final Task parent;

  public LocalDateTime getEndTime() {
    return lastTick;
  }

  private LocalDateTime startedAt;
  private LocalDateTime lastTick;
  private Duration duration;

  public Interval(Task parent) { // Constructor
    this.parent = parent;
    this.lastTick = null;
    this.duration = Duration.ZERO;
    this.startedAt = LocalDateTime.now();
  }

  public Interval(Task parent, LocalDateTime start, LocalDateTime lastTick, Long duration) {
    this.parent = parent;
    this.lastTick = lastTick;
    this.duration = Duration.ofSeconds(duration);
    this.startedAt = start;
  }

  public void begin() {
    this.startedAt = LocalDateTime.now();
    // Observe to clock
    Clock.getInstance().addObserver(this);
  }

  public void end() {
    // Stop Observing the clock
    Clock.getInstance().deleteObserver(this);
  }

  public Duration getDuration() {
    return this.duration;
  }

  // Updates itself and propagates the changes up the tree
  @Override
  public void update(Observable o, Object arg) {
    Clock c = (Clock) o;
    this.lastTick = c.getLatTick();
    this.duration = this.duration.plusSeconds(c.getFreq());
    this.parent.propagateTime(c.getFreq(), this);

  }

  public void accept(Printer printer) { // Visitor implementation
    printer.addInterval(startedAt, lastTick, getDuration().getSeconds(), this.parent.getName());
  }

  public LocalDateTime getStartTime() {
    return this.startedAt;
  }
}
