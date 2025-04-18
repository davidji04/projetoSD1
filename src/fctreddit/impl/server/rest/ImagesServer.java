package fctreddit.impl.server.rest;

import java.net.InetAddress;
import java.net.URI;
import java.util.logging.Logger;

import fctreddit.Discovery;
import fctreddit.ServiceRegistry;
import fctreddit.api.java.Image;
import fctreddit.clients.rest.RestContentClient;
import fctreddit.clients.rest.RestUsersClient;
import fctreddit.impl.server.ServerInitializer;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

public class ImagesServer {

  private static Logger Log = Logger.getLogger(ImagesServer.class.getName());

  static {
    System.setProperty("java.net.preferIPv4Stack", "true");
    System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s\n");
  }

  public static final int PORT = 8082;
  public static final String SERVICE = "Images"; // Changed from "Users" to "Images"
  private static final String SERVER_URI_FMT = "http://%s:%s/rest";

  public static void main(String[] args) {
    try {
      String ip = InetAddress.getLocalHost().getHostAddress();
      String serverURI = String.format(SERVER_URI_FMT, ip, PORT);

      // Initialize dependencies first
      URI contentUri = ServiceRegistry.getInstance().getLatestUri("Content");
      URI usersUri = ServiceRegistry.getInstance().getLatestUri("Users");

      RestContentClient contentClient = contentUri != null ? new RestContentClient(contentUri) : null;
      RestUsersClient userClient = usersUri != null ? new RestUsersClient(usersUri) : null;

      // Create resource with dependencies
      ImagesResource imagesResource = new ImagesResource(userClient, contentClient);

      // Configure server
      ResourceConfig config = new ResourceConfig();
      config.register(imagesResource);

      // Start server
      JdkHttpServerFactory.createHttpServer(URI.create(serverURI), config);

      // Start discovery
      Discovery discovery = new Discovery(Discovery.DISCOVERY_ADDR, SERVICE, serverURI);
      discovery.start();

      // Initialize server metrics/status
      ServerInitializer s = new ServerInitializer(ip, PORT, SERVICE, SERVER_URI_FMT);
      s.startServerRest();

      Log.info(String.format("%s Server ready @ %s\n", SERVICE, serverURI));
    } catch (Exception e) {
      Log.severe("Server failed: " + e.getMessage());
      e.printStackTrace();
    }
  }
}