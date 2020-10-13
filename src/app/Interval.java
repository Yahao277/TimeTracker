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

  public Interval(Task parent) {
    this.parent = parent;
    this.last_tick = null;
    this.duration = Duration.ZERO;
    this.started_at = LocalDateTime.now();
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
  public void update(Observable o, Object arg) {
        Clock c = (Clock) o;
        this.last_tick = c.getLatTick();
        this.duration = this.duration.plusSeconds(c.getFreq());
        this.parent.propagateTime(c.getFreq(), this);

  }

  public void accept(Printer printer){
    printer.addInterval(started_at,last_tick,getDuration().getSeconds(), this.parent.getName());
  }

  public LocalDateTime getStartTime() {
    return this.started_at;
  }
}
