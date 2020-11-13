/**
 * File: main.java
 * Description: Main entry point of the program. Also contains some
 * tests and helper functions.
 */

package app;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class Main {
  public static void printTree(Activity rootNode, int level) {
    String indent = new String(new char[level]).replace('\0', '\t');
    System.out.println(indent + rootNode.getName());

    int i = 1;
    Activity aux = rootNode.getChild(0);
    while (aux != null) {
      Main.printTree(aux, level + 1);
      aux = rootNode.getChild(i++);
    }
  }

  public static Activity testB() throws InterruptedException {

    // Create clock
    Clock clock = Clock.getInstance(2);

    // To hold everything together
    Project rootNode = new Project(null, "ROOT_NODE");

    // First level
    Project swDesing = new Project(rootNode, "Software design");
    Project swTesting = new Project(rootNode, "Software testing");
    Project databases = new Project(rootNode, "Databases");
    Task transport = new Task(rootNode, "transportation");
    // SW design
    Project problems = new Project(swDesing, "Problems");
    Project timeTracker = new Project(swDesing, "project time tracker");

    // Problems
    Task list1 = new Task(problems, "first list");
    Task list2 = new Task(problems, "second list");

    // Time tracker
    Task handout = new Task(timeTracker, "Read handout");
    Task milestone1 = new Task(timeTracker, "first milestone");

    System.out.print("=================================================================\n");
    System.out.print("Created Tree\n");
    System.out.print("=================================================================\n");
    Main.printTree(rootNode, 0);
    System.out.print("=================================================================\n");
    System.out.print("=================================================================\n");
    System.out.print("Initial stuff\n");
    System.out.print("=================================================================\n");
    System.out.print(rootNode.toString());
    System.out.print("=================================================================\n");

    // Create a Screen Printer to see output
    Printer printer = new ScreenPrinter(rootNode);
    Clock.getInstance().addObserver(printer);

    // Start the test
    System.out.print("=================================================================\n");
    System.out.print("Starting test!\n");
    System.out.print("=================================================================\n");
    clock.start();

    transport.start();
    Thread.sleep(4000);
    transport.end();
    Thread.sleep(2000);
    list1.start();
    Thread.sleep(6000);
    list2.start();
    Thread.sleep(4000);
    list1.end();
    Thread.sleep(2000);
    list2.end();
    Thread.sleep(2000);
    transport.start();
    Thread.sleep(4000);
    transport.end();

    clock.stop();
    // End test
    Thread.sleep(1000);
    System.out.print("=================================================================\n");
    System.out.print("Test ended!\n");
    System.out.print("=================================================================\n");

    // Show results
    System.out.print("=================================================================\n");
    System.out.print("Results!\n");
    System.out.print("=================================================================\n");
    printer.write();

    return rootNode;
  }

  public static void testLoadJSON() throws InterruptedException {
    // Create clock
    Clock clock = Clock.getInstance(2);

    // To hold everything together
    Project rootNode = new Project(null, "ROOT_NODE");

    // First level
    Project swDesing = new Project(rootNode, "Software design");
    Project swTesting = new Project(rootNode, "Software testing");
    Project databases = new Project(rootNode, "Databases");
    Task transport = new Task(rootNode, "transportation");
    // SW design
    Project problems = new Project(swDesing, "Problems");
    Project timeTracker = new Project(swDesing, "project time tracker");

    // Problems
    Task list1 = new Task(problems, "first list");
    Task list2 = new Task(problems, "second list");

    // Time tracker
    Task handout = new Task(timeTracker, "Read handout");
    Task milestone1 = new Task(timeTracker, "first milestone");


    System.out.print("=================================================================\n");
    System.out.print("Created Tree\n");
    System.out.print("=================================================================\n");
    Main.printTree(rootNode, 0);
    System.out.print("=================================================================\n");
    System.out.print("=================================================================\n");
    System.out.print("Initial stuff\n");
    System.out.print("=================================================================\n");
    System.out.print(rootNode.toString());
    System.out.print("=================================================================\n");

    // Create a Screen Printer to see output
    Printer printer = new ScreenPrinter(rootNode);
    Clock.getInstance().addObserver(printer);

    // Start the test
    System.out.print("=================================================================\n");
    System.out.print("Starting test!\n");
    System.out.print("=================================================================\n");
    clock.start();

    transport.start();
    Thread.sleep(4000);
    transport.end();
    Thread.sleep(2000);
    list1.start();
    Thread.sleep(6000);
    list2.start();

    clock.stop();

    Printer json = new JSONPrinter("testA.json");
    json.printActivity(rootNode);
    json.write();

    Activity root = JSONLoader.createTreeFromJSONFile("testA.json");
    System.out.println("-----------------------------\n\n");
    System.out.println(root.toString());
    System.out.println("-----------------------------\n\n");
    Main.printTree(root, 0);
  }

  public static void milestone1() {
    Activity root;
    try {
      root = Main.testB();
    } catch (InterruptedException e) {
      e.printStackTrace();
      return;
    }

    Printer jsonPrinter = new JSONPrinter("fita1.json");

    System.out.print("=================================================================\n");
    System.out.print("Saving to fita1.json!\n");
    System.out.print("=================================================================\n");

    jsonPrinter.printActivity(root);
    jsonPrinter.write();

    System.out.print("=================================================================\n");
    System.out.print("Loading from fita1.json!\n");
    System.out.print("=================================================================\n");
    root = JSONLoader.createTreeFromJSONFile("fita1.json");

    System.out.print("=================================================================\n");
    System.out.print("Results!\n");
    System.out.print("=================================================================\n");
    Printer p = new ScreenPrinter(root);
    p.printActivity(root);

  }

  static void testLog() {
    // Creation
    Logger log = (Logger) LoggerFactory.getLogger("app.Main");

    // Setting level
    // log.setLevel(Level.ERROR);

    // Login
    log.error("err");
    log.warn("waaar");
    log.info("info");
    log.debug("debug");
    log.trace("trace");

  }

  public static void testSearchtag() {
    // To hold everything together
    Project rootNode = new Project(null, "ROOT_NODE");

    // First level
    Project swDesing = new Project(rootNode, "Software design");
    Project swTesting = new Project(rootNode, "Software testing");
    Project databases = new Project(rootNode, "Databases");
    Task transport = new Task(rootNode, "transportation");
    // SW design
    Project problems = new Project(swDesing, "Problems");
    Project timeTracker = new Project(swDesing, "project time tracker");

    problems.addTag("hey");
    databases.addTag("hey");

    SearchTag st = new SearchTag("hey");

    st.visitActivity(rootNode);

    for (Activity res :
        st.getResults()) {
      System.out.println(res.toString());
    }

  }

  public static void testSearchTime1() throws InterruptedException {
    // Create clock
    Clock clock = Clock.getInstance(2);

    // To hold everything together
    Project rootNode = new Project(null, "ROOT_NODE");

    // First level
    Project P0 = new Project(rootNode, "P0");
    Project P1 = new Project(rootNode, "P1");
    Project P3 = new Project(rootNode, "P3");
    Task T4 = new Task(rootNode, "T4");
    Task T5 = new Task(rootNode, "T5");

    // Second Level
    Task T0 = new Task(P0, "T0");
    Task T1 = new Task(P0, "T1");
    Task T2 = new Task(P0, "T2");
    Task T3 = new Task(P1, "T3");

    clock.start();

    Thread.sleep(1000);
    T0.start();
    Thread.sleep(1000);
    T4.start();
    Thread.sleep(1000);
    T0.end();
    T4.end();
    T1.start();
    T2.start();
    Thread.sleep(1000);
    T0.start();
    T5.start();
    Thread.sleep(1000);
    T0.end();
    T1.end();
    T4.start();
    Thread.sleep(2000);
    T1.start();
    T5.end();
    Thread.sleep(1000);
    T5.start();
    Thread.sleep(1000);
    T5.end();
    T2.end();
    Thread.sleep(1000);
    T5.start();
    Thread.sleep(1000);
    T5.end();
    T2.start();
    Thread.sleep(2000);
    T1.end();
    T2.end();
    T4.end();
    T3.start();
    Thread.sleep(1000);
    T0.start();
    T4.start();
    T3.end();
    Thread.sleep(1000);
    T0.end();
    Thread.sleep(1000);
    T4.end();

    clock.stop();

    JSONPrinter jsonp = new JSONPrinter("testSearchTime part1.json");
    jsonp.printActivity(rootNode);
    jsonp.write();

  }

  public static void testSearchTime2() {

    Activity root = JSONLoader.createTreeFromJSONFile("testSearchTime part1.json");

    SearchTime st = new SearchTime(root.getStartTime().plusSeconds(6), root.getStartTime().plusSeconds(12));

    st.visitActivity(root);

    st.getTotal();

  }

  public static void main(String[] args) {
    // Main.testJSON();
    //Main.milestone1();
    //Main.testLog();
    //Main.testSearchtag();

    // try {
    //   Main.testSearchTime1();
    // } catch (Exception e) {
    //
    // }

    Main.testSearchTime2();
  }
}
