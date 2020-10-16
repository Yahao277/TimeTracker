/**
 * File: Clock.java
 * Description: this file contains a Singleton class used to keep track
 *    the time for running intervals. Also fulfills the role of the
 *    observable element in the Observer pattern letting know, using
 *    the pattern, of the changing time to the intervals that are observing
 *    it.
 */

package app;

import java.time.LocalDateTime;
import java.util.*;
import java.util.Timer;
import java.util.TimerTask;

public class Clock extends Observable {

  private static final int DEFAULT_FREQ = 2;
  private static volatile Clock uniqueInstance = null;

  private LocalDateTime last_tick;
  private final int freq;
  private Timer timer;


  /**
   * Get an instance, adn if it is the first one to be created
   * set the clock time between notifications send to the observers
   * @param freq time between notifications
   * @return
   */
  public static synchronized Clock getInstance(int freq){
    if(uniqueInstance == null){
      synchronized ( Clock.class ) {
        uniqueInstance = new Clock(freq);
      }
    }
    return uniqueInstance;
  }

  /**
   * Get an instance, adn if it is the first one to be created
   * set the clock time between notifications sent to the
   * observers to the default 2 seconds
   * @return
   */
  public static synchronized Clock getInstance() {
    if(uniqueInstance == null){
      synchronized ( Clock.class ) {
        uniqueInstance = new Clock(Clock.DEFAULT_FREQ);
      }
    }
    return uniqueInstance;
  }

  /**
   * Private constructor as in the Singleton pattern
   * @param freq_seconds
   */
  private Clock(int freq_seconds) {
    this.freq = freq_seconds;
    this.last_tick = LocalDateTime.now();
    this.timer = new Timer();
  }

  /**
   * Start the clock
   */
  public void start(){
    TimerTask task = new TimerTask(){
      public void run(){
        update();
      }
    };
    timer.scheduleAtFixedRate(task,0,1000*this.freq);
  }

  /**
   * Stop the clock
   */
  public void stop(){
    timer.cancel();
  }

  /**
   * Observer pattern function used to notify all the observers.
   * Update time and let everyone know.
   */
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
