package app;

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

  public Clock(int freq_seconds) {
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
    this.notifyObservers(this.last_tick);
  }

}
