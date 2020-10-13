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

    public static void test_observer() throws InterruptedException {
        Clock clock = Clock.getInstance(2);
        Project root_node = new Project(null, "ROOT_NODE");
        Project p = new Project(root_node,"testeo");
        Task test = new Task(p, "testing");
        System.out.println(root_node.toString());
        clock.start();
        test.start(clock);
        //System.out.println(LocalDateTime.now());
        Thread.sleep(3000);

        test.end();
        clock.stop();
        System.out.println(root_node.toString());
    }

    public static void test_singleton() throws InterruptedException {
        Clock clock = Clock.getInstance(2);
        Clock clock2 = Clock.getInstance(5);

        clock.start();
        clock2.start();
        Thread.sleep(6000);
        clock2.stop();
        clock.stop();

    }

    public static void main(String[] args) throws InterruptedException {

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
        System.out.print(root_node.toString());
        System.out.print("=================================================================\n");
        System.out.print("=================================================================\n");
        Main.printTree(root_node, 0);
        System.out.print("=================================================================\n");

        /*Printer printer = new JSONPrinter("prova.json");
        printer.printActivity(root_node);
        try{
            printer.write();
        }catch(IOException e){
            e.printStackTrace();
        }*/

        Printer printer = new ScreenPrinter();
        printer.printActivity(root_node);


        /*
        clock.start();

        transport.start(clock);
        Thread.sleep(4000);
        transport.end();
        Thread.sleep(2000);
        list1.start(clock);
        Thread.sleep(6000);
        list2.start(clock);
        Thread.sleep(4000);
        list1.end();
        Thread.sleep(2000);
        list2.end();
        Thread.sleep(2000);
        transport.start(clock);
        Thread.sleep(4000);
        transport.end();

        clock.stop();

         */
    }
}
