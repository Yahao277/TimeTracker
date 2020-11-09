/**
 * File: Clock.java
 * Description: this file contains a Singleton class used to keep track
 *    the time for running intervals. Also fulfills the role of the
 *    observable element in the Observer pattern letting know, using
 *    the pattern, of the changing time to the intervals that are observing
 *    it.
 */

package app;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

public class Clock extends Observable {

  private static Logger logger = (Logger) LoggerFactory.getLogger("milestone1.clock");

  private static final int DEFAULT_FREQ = 2;
  private static volatile Clock uniqueInstance = null;

  private LocalDateTime lastTick;
  private final int freq;
  private final Timer timer;


  /*
   * Get an instance, adn if it is the first one to be created
   * set the clock time between notifications send to the observers
   * @param freq time between notifications
   * @return
   */
  public static synchronized Clock getInstance(int freq) {
    logger.debug("Asking for clock with freq");
    if (uniqueInstance == null) {
      synchronized (Clock.class) {
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
    logger.debug("Asking for clock");
    if (uniqueInstance == null) {
      synchronized (Clock.class) {
        uniqueInstance = new Clock(Clock.DEFAULT_FREQ);
      }
    }
    return uniqueInstance;
  }

  /**
   * Private constructor as in the Singleton pattern.
   * @param freqSeconds time frequence
   */
  private Clock(int freqSeconds) {
    this.logger.trace("New and unique clock");
    this.freq = freqSeconds;
    this.lastTick = LocalDateTime.now();
    this.timer = new Timer();
  }

  /**
   * Start the clock
   */
  public void start() {
    this.logger.debug("Starting clock");
    TimerTask task = new TimerTask() {
      public void run() {
        update();
      }
    };
    timer.scheduleAtFixedRate(task, 0, 1000 * this.freq);
  }

  /**
   * Stop the clock
   */
  public void stop() {
    this.logger.debug("Stoping clock");
    timer.cancel();
  }

  /**
   * Observer pattern function used to notify all the observers.
   * Update time and let everyone know.
   */
  public void update() {
    this.logger.debug("Notifying everyone");
    this.lastTick = LocalDateTime.now();
    this.setChanged();
    this.notifyObservers(this);
  }

  public LocalDateTime getLatTick() {
    return this.lastTick;
  }

  public int getFreq() {
    return this.freq;
  }
}
