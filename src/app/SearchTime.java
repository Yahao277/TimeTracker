package app;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;



public class SearchTime implements SearchVisitor {

  private LocalDateTime start;
  private LocalDateTime end;

  private HashMap<String, Duration> parts;
  private Duration accumulated;
  private static Logger logger = (Logger) LoggerFactory.getLogger("milestone2.SearchTime");

  public Duration getTotal() {
    return this.accumulated;
  }

  public double getSpecific(String identifier) {
    if (!this.parts.containsKey(identifier)) {
      return 0;
    }
    long millis = this.parts.get(identifier).toMillis();
    return Math.ceil(millis / 1000.0);
  }

  public SearchTime(LocalDateTime start, LocalDateTime end) {
    this.start = start;
    this.end = end;
    this.accumulated = Duration.ofSeconds(0);
    this.parts = new HashMap<>();
    logger.trace("New SearchTime between: "+  start + " - " + end);
  }

  public void resetTimeBoundaries(LocalDateTime start, LocalDateTime end) {
    this.start = start;
    this.end = end;
    this.accumulated = Duration.ofSeconds(0);
  }

  @Override
  public void visitActivity(Activity a) {
    logger.debug("Visiting ativity");
    a.accept(this);
  }

  @Override
  public void checkInProject(Project p) {
    Duration helper = this.accumulated;
    this.accumulated = Duration.ofSeconds(0);
    Activity aux = p.getChild(0);
    int i = 1;
    logger.debug("Checking project: " + p.getName());
    while (aux != null) {
      this.visitActivity(aux);
      aux = p.getChild(i);
      ++i;
    }
    this.parts.put(p.getName(), this.accumulated);
    this.accumulated = helper.plus(this.accumulated);
  }

  @Override
  public void checkInTask(Task t) {
    Duration helper = this.accumulated;
    this.accumulated = Duration.ofSeconds(0);
    Interval aux = t.getInterval(0);
    int i = 1;
    while (aux != null) {
      logger.debug("Checking task: " + t.getName());
      LocalDateTime auxStartTime = aux.getStartTime();
      LocalDateTime auxEndTime = aux.getEndTime();

      // Is it inside the time frame?
      if (auxStartTime.isBefore(this.start) && auxEndTime.isAfter(this.end)) {
        // Cut both sides ---|------|----
        this.accumulated = this.accumulated.plus(Duration.between(this.start, this.end));

      }  else if (auxStartTime.isAfter(this.start) && auxEndTime.isBefore(this.end)) {
        // All in | ---- |
        this.accumulated = this.accumulated.plus(Duration.between(auxStartTime, auxEndTime));

      } else if (auxEndTime.isAfter(this.start) && auxEndTime.isBefore(this.end)) {
        // Cut start ---|----   |
        this.accumulated = this.accumulated.plus(Duration.between(this.start, auxEndTime));

      } else if (auxStartTime.isAfter(this.start) && auxStartTime.isBefore(this.end)) {
        // Cut end | ----|---
        this.accumulated = this.accumulated.plus(Duration.between(auxStartTime, this.end));

      } else {
        // It is outside the margin do nothing
      }
      aux = t.getInterval(i);
      ++i;
    }
    this.parts.put(t.getName(), this.accumulated);
    this.accumulated = helper.plus(this.accumulated);
  }
}
