package webserver;

import app.Activity;
import app.Clock;

public class MainWebServer {
  public static void main(String[] args) {
    webServer();
  }

  public static void webServer() {
    final Activity root = makeTreeCourses();

    new WebServer(root);
  }
}
