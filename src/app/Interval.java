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

  private Task parent;

  public LocalDateTime getEndTime() {
    return last_tick;
  }

  private LocalDateTime started_at;
  private LocalDateTime last_tick;
  private Duration duration;

  public Interval(Task parent) { // Constructor
    this.parent = parent;
    this.last_tick = null;
    this.duration = Duration.ZERO;
    this.started_at = LocalDateTime.now();
  }

  public Interval(Task parent, LocalDateTime start, LocalDateTime last_tick, Long duration){
    this.parent = parent;
    this.last_tick = last_tick;
    this.duration = Duration.ofSeconds(duration);
    this.started_at = start;
  }

  public void begin() {
    this.started_at = LocalDateTime.now();
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

  @Override
  public void update(Observable o, Object arg) { // Updates itself and propagates
                                                 // the changes up the tree
        Clock c = (Clock) o;
        this.last_tick = c.getLatTick();
        this.duration = this.duration.plusSeconds(c.getFreq());
        this.parent.propagateTime(c.getFreq(), this);

  }

  public void accept(Printer printer){ // Visitor implementation
    printer.addInterval(started_at,last_tick,getDuration().getSeconds(), this.parent.getName());
  }

  public LocalDateTime getStartTime() {
    return this.started_at;
  }
}
