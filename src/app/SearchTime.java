package app;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;

public class SearchTime implements SearchVisitor {

  private LocalDateTime start;
  private LocalDateTime end;

  private HashMap<String, Duration> parts;
  private Duration accumulated;

  public Duration getTotal() {
    return this.accumulated;
  }

  public Duration getSpecific(String identifier) {
    if (!this.parts.containsKey(identifier))
      return Duration.ZERO;
    return this.parts.get(identifier);
  }


  public SearchTime(LocalDateTime start, LocalDateTime end) {
    this.start = start;
    this.end = end;
    this.accumulated = Duration.ofSeconds(0);
  }

  public void resetTimeBoundaries(LocalDateTime start, LocalDateTime end) {
    this.start = start;
    this.end = end;
    this.accumulated = Duration.ofSeconds(0);
  }

  @Override
  public void visitActivity(Activity a) {
      a.accept(this);
  }

  @Override
  public void checkInProject(Project p) {
    Duration helper = this.accumulated;
    this.accumulated = Duration.ofSeconds(0);
    Activity aux = p.getChild(0);
    int i = 1;
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

      LocalDateTime i_start = aux.getStartTime();
      LocalDateTime i_end = aux.getEndTime();

      // Is it inside the time frame?
      if (i_start.isBefore(this.start) && i_end.isAfter(this.end)) {
        // Cut both sides ---|------|----
        this.accumulated = this.accumulated.plus(Duration.between(this.start, this.end));

      }  else if (i_start.isAfter(this.start) && i_end.isBefore(this.end)) {
        // All in | ---- |
        this.accumulated = this.accumulated.plus(Duration.between(i_start, i_end));

      } else if (i_end.isAfter(this.start) && i_end.isBefore(this.end)) {
        // Cut start ---|----   |
        this.accumulated = this.accumulated.plus(Duration.between(this.start, i_end));

      } else if (i_start.isAfter(this.start) && i_start.isBefore(this.end)) {
        // Cut end | ----|---
        this.accumulated = this.accumulated.plus(Duration.between(i_start, this.end));

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
