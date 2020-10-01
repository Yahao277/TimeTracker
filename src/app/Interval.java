package app;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Observable;
import java.util.Observer;

public class Interval implements Observer {

  private LocalDateTime started_at;
  private LocalDateTime last_tick;

  public Interval() {
    this.last_tick = null;
    this.started_at = null;
  }

  public void begin() {
    this.started_at = LocalDateTime.now();
    // Observe to clock
  }

  public void end() {
    // Stop Observing the clock
  }

  public Duration getDuration() {
    return Duration.between(this.started_at, this.last_tick);
  }

  @Override
  public void update(Observable o, Object arg) {
        this.last_tick = (LocalDateTime) arg;
  }
}
