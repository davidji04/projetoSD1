package fctreddit.impl.server.rest;

import fctreddit.Discovery;
import fctreddit.ServiceRegistry;
import fctreddit.clients.rest.RestContentClient;
import fctreddit.clients.rest.RestImageClient;
import fctreddit.clients.rest.RestUsersClient;
import fctreddit.impl.server.ServerInitializer;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.InetAddress;
import java.net.URI;
import java.util.logging.Logger;

public class ContentServer {

    private static Logger Log = Logger.getLogger(ContentServer.class.getName());

    static {
        System.setProperty("java.net.preferIPv4Stack", "true");
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s\n");
    }

    public static final int PORT = 8081;
    public static final String SERVICE = "Content";
    private static final String SERVER_URI_FMT = "http://%s:%s/rest";

    public static void main(String[] args) {
        try {
//            ResourceConfig config = new ResourceConfig();
//
//            String ip = InetAddress.getLocalHost().getHostAddress();
//            String serverURI = String.format(SERVER_URI_FMT, ip, PORT);
//
//            Discovery discovery = new Discovery(Discovery.DISCOVERY_ADDR, SERVICE, serverURI);
//
//            discovery.start();
//            ServiceRegistry sr = ServiceRegistry.getInstance();
//            URI usersURI = sr.getLatestUri("Users");
//            URI imagesURI = sr.getLatestUri("Images");
//            RestUsersClient usersClient = null;
//            RestImageClient imageClient = null;
//            if(usersURI != null)
//                   usersClient = new RestUsersClient(usersURI);
//            if(imagesURI != null)
//                imageClient = new RestImageClient(imagesURI);
//            ContentResource contentResource = new ContentResource(usersClient, imageClient);
//            config.register(contentResource);
//
//            JdkHttpServerFactory.createHttpServer(URI.create(serverURI), config);

            String ip = InetAddress.getLocalHost().getHostAddress();
            ServerInitializer s = new ServerInitializer(ip,PORT,SERVICE,SERVER_URI_FMT);
            s.startServerRest();
            Log.info(String.format("%s Server ready \n", SERVICE));
        } catch (Exception e) {
            Log.severe(e.getMessage());
        }
    }


}
