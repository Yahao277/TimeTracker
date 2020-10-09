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
  public Clock(int freq_seconds) {
    this.freq = freq_seconds;
    this.last_tick = LocalDateTime.now();
    this.timer = new Timer();
  }

  public void start(){
    System.out.println("clock starts");
    TimerTask task = new TimerTask(){
      public void run(){
        update();
      }
    };
    timer.scheduleAtFixedRate(task,0,1000*this.freq);
  }

  public void stop(){
    System.out.println("clock stops");
    timer.cancel();
  }

  public void update() {
    this.last_tick = LocalDateTime.now();
    //System.out.println(this.freq + "; " + this.last_tick);
    this.setChanged();
    this.notifyObservers(this.last_tick);
  }

}
