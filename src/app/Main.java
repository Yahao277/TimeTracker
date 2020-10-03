package app;

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

    public static void test_observer() throws InterruptedException {
        Clock clock = new Clock(2);
        Project root_node = new Project(null, "ROOT_NODE");
        Project p = new Project(root_node,"testeo");
        Task test = new Task(p, "testing");
        test.start(clock);
        System.out.println(LocalDateTime.now());
        Thread.sleep(3000);

        clock.foo();
        test.end();

        System.out.println(root_node.toString());

    }

    public static void main(String[] args) {
        // // To hold everything together
        // Project root_node = new Project(null, "ROOT_NODE");
        //
        // // First level
        // Project sw_desing = new Project(root_node, "Software design");
        // Project sw_testing = new Project(root_node, "Software testing");
        // Project databases = new Project(root_node, "Databases");
        // Task transport = new Task(root_node, "transportation");
        //
        // // SW design
        // Project problems = new Project(sw_desing, "Problems");
        // Project time_tracker = new Project(sw_desing, "project time tracker");
        //
        // // Problems
        // Task list1 = new Task(problems, "first list");
        // Task list2 = new Task(problems, "second list");
        //
        // // Time tracker
        // Task handout = new Task(time_tracker,"Read handout");
        // Task milestone1 = new Task(time_tracker, "first milestone");
        //
        // System.out.print("=================================================================\n");
        // System.out.print(root_node.toString());
        // System.out.print("=================================================================\n");
        // System.out.print("=================================================================\n");
        // Main.printTree(root_node, 0);
        // System.out.print("=================================================================\n");

        try {
            Main.test_observer();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
