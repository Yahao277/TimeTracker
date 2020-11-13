/**
 * File: Interval.java
 * Description: Class that represents a time interval and observes
 * the clock to update itself.
 */

package app;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Observable;
import java.util.Observer;

public class Interval implements Observer {
  private static Logger logger = (Logger) LoggerFactory.getLogger("milestone1.Interval");
  private final Task parent;

  public LocalDateTime getEndTime() {
    return lastTick;
  }

  private LocalDateTime startedAt;
  private LocalDateTime lastTick;
  private Duration duration;

  public Interval(Task parent) { // Constructor
    this.logger.trace("New Interval of: "+parent.getName());
    this.parent = parent;
    this.lastTick = null;
    this.duration = Duration.ZERO;
    this.startedAt = LocalDateTime.now();
  }

  public Interval(Task parent, LocalDateTime start, LocalDateTime lastTick, Long duration) {
    this.logger.trace("New Interval of: "+parent.getName()+" from JSON");
    this.parent = parent;
    this.lastTick = lastTick;
    this.duration = Duration.ofSeconds(duration);
    this.startedAt = start;
  }

  public void begin() {
    this.logger.debug("Starting the interval");
    this.startedAt = LocalDateTime.now();
    // Observe to clock
    Clock.getInstance().addObserver(this);
  }

  public void end() {
    this.logger.debug("Stoping the interval");
    // Stop Observing the clock
    Clock.getInstance().deleteObserver(this);
  }

  public Duration getDuration() {
    return this.duration;
  }

  // Updates itself and propagates the changes up the tree
  @Override
  public void update(Observable o, Object arg) {
    this.logger.debug("Updating interval");
    Clock c = (Clock) o;
    this.lastTick = c.getLatTick();
    this.duration = this.duration.plusSeconds(c.getFreq());
    this.parent.propagateTime(c.getFreq(), this);

  }

  public void accept(Printer printer) { // Visitor implementation
    this.logger.debug("Accepting visitor");
    printer.addInterval(startedAt, lastTick, getDuration().getSeconds(), this.parent.getName());
  }

  public LocalDateTime getStartTime() {
    return this.startedAt;
  }

}
