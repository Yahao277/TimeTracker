package webserver;

import app.Activity;
import app.Main;
import app.Clock;

public class MainWebServer {
  public static void main(String[] args) {
    webServer();
  }

  public static void webServer() {
    final Activity root = Main.makeTreeCourses();

    Clock clock = Clock.getInstance(2);
    clock.start();

    new WebServer(root);
  }

}
