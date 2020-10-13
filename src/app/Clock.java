package app;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Timer;
import java.util.TimerTask;

// TODO: decide if clock is timer or timertask
//timer task
public class Clock extends Observable {

  private final int freq;
  private LocalDateTime last_tick;
  private Timer timer;
  private static final int DEFAULT_FREQ = 2;
  private static volatile Clock uniqueInstance = null;

  public static synchronized Clock getInstance(int freq){
    if(uniqueInstance == null){
      synchronized ( Clock.class ) {
        // therefore, only synchronize the first time if (uniqueInstance == null) { // check again
        uniqueInstance = new Clock(freq);
      }
    }
    return uniqueInstance;
  }

  public static synchronized Clock getInstance() {
    if(uniqueInstance == null){
      synchronized ( Clock.class ) {
        // therefore, only synchronize the first time if (uniqueInstance == null) { // check again
        uniqueInstance = new Clock(Clock.DEFAULT_FREQ);
      }
    }
    return uniqueInstance;
  }
  private Clock(int freq_seconds) {
    this.freq = freq_seconds;
    this.last_tick = LocalDateTime.now();
    this.timer = new Timer();
  }

  public void start(){
    TimerTask task = new TimerTask(){
      public void run(){
        update();
      }
    };
    timer.scheduleAtFixedRate(task,0,1000*this.freq);
  }

  public void stop(){
    timer.cancel();
  }

  public void update() {
    this.last_tick = LocalDateTime.now();
    this.setChanged();
    this.notifyObservers(this);
  }

  public LocalDateTime getLatTick() {
    return this.last_tick;
  }

  public int getFreq() {
    return this.freq;
  }
}
