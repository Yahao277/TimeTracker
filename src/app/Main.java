/**
 * File: main.java
 * Description: Main entry point of the program. Also contains some
 * tests and helper functions.
 */

package app;


import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;


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
    Project swDesign = new Project(rootNode, "Software design");
    swDesign.addTag("java");
    swDesign.addTag("flutter");

    Project swTesting = new Project(rootNode, "Software testing");
    swTesting.addTag("c++");
    swTesting.addTag("Java");
    swTesting.addTag("python");

    Project databases = new Project(rootNode, "Databases");
    databases.addTag("SQL");
    databases.addTag("C++");
    databases.addTag("python");

    Task transport = new Task(rootNode, "transportation");

    // SW design
    Project problems = new Project(swDesign, "Problems");
    Project timeTracker = new Project(swDesign, "project time tracker");

    //problems
    Task firstList = new Task(problems,"first_list");
    firstList.addTag("java");

    Task secondList = new Task(problems,"second_list");
    secondList.addTag("Dart");

    //timeTracker
    Task readHandout = new Task(timeTracker,"read_handout");

    Task firstMilestone = new Task(timeTracker,"first_milestone");
    firstMilestone.addTag("Java");
    firstMilestone.addTag("IntelliJ");


    SearchTag st = new SearchTag("flutter");

    st.visitActivity(rootNode);

    System.out.println("Get results:");
    for (Activity res :
        st.getResults()) {
      System.out.println(res.getName());
    }

  }

  public static void testSearchTime1() throws InterruptedException {
    // Create clock
    Clock clock = Clock.getInstance(1);

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
    Thread.sleep(1000); // 2
    T4.start();
    Thread.sleep(1000); // 3
    T0.end();
    T4.end();
    T1.start();
    T2.start();
    Thread.sleep(1000); // 4
    T0.start();
    T5.start();
    Thread.sleep(1000); // 5
    T0.end();
    T1.end();
    T4.start();
    Thread.sleep(2000); // 7
    T1.start();
    T5.end();
    Thread.sleep(1000); // 8
    T5.start();
    Thread.sleep(1000); // 9
    T5.end();
    T2.end();
    Thread.sleep(1000); // 10
    T5.start();
    Thread.sleep(1000); // 11
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

    SearchTime st = new SearchTime(root.getStartTime().plusSeconds(5),
        root.getStartTime().plusSeconds(11));

    st.visitActivity(root);



    System.out.println("Total: " + st.getTotal().toMillis()/1000.0);
    System.out.println("P0: " + st.getSpecific("P0"));
    System.out.println("P1: " + st.getSpecific("P1"));
    System.out.println("T0: " + st.getSpecific("T0"));
    System.out.println("T1: " + st.getSpecific("T1"));
    System.out.println("T2: " + st.getSpecific("T2"));
    System.out.println("T3: " + st.getSpecific("T3"));
    System.out.println("T4: " + st.getSpecific("T4"));
    System.out.println("T5: " + st.getSpecific("T5"));

  }

  public static void main(String[] args) {
    // Main.testJSON();
    //Main.milestone1();
    //Main.testLog();
    //Main.testSearchtag();
/*
    try {
      Main.testSearchTime1();
    } catch (Exception e) {

    }
*/
    Main.testSearchTime2();
  }
}
