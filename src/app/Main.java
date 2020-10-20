package app;

import java.io.IOException;
import java.time.LocalDateTime;

public class Main {

    public static void printTree(Activity root_node, int level) {

        String indent = new String(new char[level]).replace('\0','\t');
        System.out.println(indent + root_node.getName());

        int i = 1;
        Activity aux = root_node.getChild(0);
        while(aux != null) {
            Main.printTree(aux, level+1);
            aux = root_node.getChild(i++);
        }
    }
    public static void testB() throws InterruptedException {

        // Create clock
        Clock clock = Clock.getInstance(2);

        // To hold everything together
        Project root_node = new Project(null, "ROOT_NODE");

         // First level
        Project sw_desing = new Project(root_node, "Software design");
        Project sw_testing = new Project(root_node, "Software testing");
        Project databases = new Project(root_node, "Databases");
        Task transport = new Task(root_node, "transportation");
        // SW design
        Project problems = new Project(sw_desing, "Problems");
        Project time_tracker = new Project(sw_desing, "project time tracker");

        // Problems
        Task list1 = new Task(problems, "first list");
        Task list2 = new Task(problems, "second list");

        // Time tracker
        Task handout = new Task(time_tracker,"Read handout");
        Task milestone1 = new Task(time_tracker, "first milestone");

        System.out.print("=================================================================\n");
        System.out.print("Created Tree\n");
        System.out.print("=================================================================\n");
        Main.printTree(root_node, 0);
        System.out.print("=================================================================\n");
        System.out.print("=================================================================\n");
        System.out.print("Initial stuff\n");
        System.out.print("=================================================================\n");
        System.out.print(root_node.toString());
        System.out.print("=================================================================\n");

        // Create a Screen Printer to see output
        Printer printer = new ScreenPrinter(root_node);
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
    }
    public static void testLoadJSON() throws InterruptedException {
        // Create clock
        Clock clock = Clock.getInstance(2);

        // To hold everything together
        Activity root_node = new Project(null, "ROOT_NODE");

        // First level
        Project sw_desing = new Project(root_node, "Software design");
        Project sw_testing = new Project(root_node, "Software testing");
        Project databases = new Project(root_node, "Databases");
        Task transport = new Task(root_node, "transportation");
        // SW design
        Project problems = new Project(sw_desing, "Problems");
        Project time_tracker = new Project(sw_desing, "project time tracker");

        // Problems
        Task list1 = new Task(problems, "first list");
        Task list2 = new Task(problems, "second list");

        // Time tracker
        Task handout = new Task(time_tracker,"Read handout");
        Task milestone1 = new Task(time_tracker, "first milestone");



        System.out.print("=================================================================\n");
        System.out.print("Created Tree\n");
        System.out.print("=================================================================\n");
        Main.printTree(root_node, 0);
        System.out.print("=================================================================\n");
        System.out.print("=================================================================\n");
        System.out.print("Initial stuff\n");
        System.out.print("=================================================================\n");
        System.out.print(root_node.toString());
        System.out.print("=================================================================\n");

        // Create a Screen Printer to see output
        Printer printer = new ScreenPrinter(root_node);
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
        json.printActivity(root_node);
        json.write();

        Activity root = JSONLoader.createTreeFromJSONFile("testA.json");
        System.out.println("-----------------------------\n\n");
        System.out.println(root.toString());
        System.out.println("-----------------------------\n\n");
        Main.printTree(root,0);


    }

    public static void testJSON() {
        // To hold everything together
        Activity root_node = new Project(null, "ROOT_NODE");

        // First level
        Project sw_desing = new Project(root_node, "Software design");
        Project sw_testing = new Project(root_node, "Software testing");
        Project databases = new Project(root_node, "Databases");
        Task transport = new Task(root_node, "transportation");
        // SW design
        Project problems = new Project(sw_desing, "Problems");
        Project time_tracker = new Project(sw_desing, "project time tracker");

        // Problems
        Task list1 = new Task(problems, "first list");
        Task list2 = new Task(problems, "second list");

        // Time tracker
        Task handout = new Task(time_tracker,"Read handout");
        Task milestone1 = new Task(time_tracker, "first milestone");

        System.out.print("=================================================================\n");
        System.out.print("Created Tree\n");
        System.out.print("=================================================================\n");
        Main.printTree(root_node, 0);
        System.out.print("=================================================================\n");

      System.out.print("=================================================================\n");
      System.out.print("Starting test!\n");
      System.out.print("=================================================================\n");
      System.out.println("Creating JSON file");

      Printer json = new JSONPrinter("test.json");
      json.printActivity(root_node);
      json.write();

      System.out.println("Destroying tree");
      root_node = null;
      sw_desing = null;
      sw_testing = null;
      databases = null;
      transport = null;
      problems = null;
      time_tracker = null;
      list1 = null;
      list2 = null;
      handout = null;
      milestone1 = null;

      System.out.println("Loading tree from json");
      root_node = JSONLoader.createTreeFromJSONFile("test.json");

    }
    public static void main(String[] args) {
        // Main.testJSON();
        try {
            //Main.testB();
            Main.testLoadJSON();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
