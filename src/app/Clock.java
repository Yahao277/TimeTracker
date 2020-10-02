package app;

import javafx.beans.InvalidationListener;

import java.time.LocalDateTime;
import java.util.*;

// TODO: decide if clock is timer or timertask
public class Clock extends Observable{

  private final int freq;
  private LocalDateTime last_tick;

  public Clock(int freq_seconds) {
    this.freq = freq_seconds;
    this.last_tick = LocalDateTime.now();
  }

  public void foo() {
    this.last_tick = LocalDateTime.now();
    this.setChanged();
    this.notifyObservers(this.last_tick);
  }

}
