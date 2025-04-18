package fctreddit.impl.server.rest;

import java.net.InetAddress;
import java.util.logging.Logger;

import fctreddit.impl.server.ServerInitializer;

public class ImagesServer {

  private static Logger Log = Logger.getLogger(ImagesServer.class.getName());

  static {
    System.setProperty("java.net.preferIPv4Stack", "true");
    System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s\n");
  }

  public static final int PORT = 8081;
  public static final String SERVICE = "Images";
  private static final String SERVER_URI_FMT = "http://%s:%s/rest";

  public static void main(String[] args) {
    try {
      String ip = InetAddress.getLocalHost().getHostAddress();
      ServerInitializer s = new ServerInitializer(ip, PORT, SERVICE, SERVER_URI_FMT);
      s.startServerRest();
      Log.info(String.format("%s Server ready \n", SERVICE));
    } catch (Exception e) {
      Log.severe("Server failed: " + e.getMessage());
      e.printStackTrace();
    }
  }
}