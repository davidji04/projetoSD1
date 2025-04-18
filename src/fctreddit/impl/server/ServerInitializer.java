package fctreddit.impl.server;

import fctreddit.Discovery;
import fctreddit.ServiceRegistry;
import fctreddit.clients.rest.RestContentClient;
import fctreddit.clients.rest.RestImageClient;
import fctreddit.clients.rest.RestUsersClient;
import fctreddit.impl.server.rest.ContentResource;
import fctreddit.impl.server.rest.ImagesResource;
import fctreddit.impl.server.rest.UsersResource;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;
import java.util.logging.Logger;

public class ServerInitializer {

    private static Logger Log = Logger.getLogger(ServerInitializer.class.getName());

    private static final String USERS = "Users";

    private static final String CONTENT = "Content";

    private static final String IMAGES = "Images";

    private static final ServiceRegistry sr = ServiceRegistry.getInstance() ;
    private final String ip;

    private final int port;

    private final String name;

    private final String format;

    static {
        System.setProperty("java.net.preferIPv4Stack", "true");
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s\n");
    }

    public ServerInitializer(String ip, int port, String name, String format) {
        this.ip = ip;
        this.port = port;
        this.name = name;
        this.format = format;
    }

    public void startServerRest(){
        try{
            ResourceConfig config = new ResourceConfig();
            String serverURI = String.format(format, ip, port);
            Discovery discovery = new Discovery(Discovery.DISCOVERY_ADDR,name,serverURI);
            discovery.start();
            setRestResource(config);
            JdkHttpServerFactory.createHttpServer( URI.create(serverURI), config);
        } catch (Exception e) {
            Log.severe(e.getMessage());
        }
    }
    private void initializeUsers(ResourceConfig config) {
        URI contentUri = sr.getLatestUri(CONTENT);
        URI imageUri = sr.getLatestUri(IMAGES);
        RestContentClient contentClient = null;
        RestImageClient imageClient = null;
        if (contentUri != null)
            contentClient = new RestContentClient(contentUri);
        if (imageUri != null)
            imageClient = new RestImageClient(imageUri);
        config.register(new UsersResource(contentClient, imageClient));
    }

    private void initializeImages(ResourceConfig config) {
        URI contentUri = sr.getLatestUri(CONTENT);
        URI usersUri = sr.getLatestUri(USERS);
        RestContentClient contentClient = null;
        RestUsersClient usersClient = null;
        if (contentUri != null)
            contentClient = new RestContentClient(contentUri);
        if (usersUri != null)
            usersClient = new RestUsersClient(usersUri);
        config.register(new ImagesResource(usersClient,contentClient));
    }

    private void initializeContents(ResourceConfig config) {
        URI usersUri = sr.getLatestUri(USERS);
        URI imageUri = sr.getLatestUri(IMAGES);
        RestUsersClient usersClient = null;
        RestImageClient imageClient = null;
        if(usersUri != null)
            usersClient = new RestUsersClient(usersUri);
        if(imageUri != null)
            imageClient = new RestImageClient(imageUri);
        config.register(new ContentResource(usersClient,imageClient));

    }

    private void setRestResource(ResourceConfig config) {
        if(name.equals(USERS)) initializeUsers(config);
        else if(name.equals(IMAGES))initializeImages(config);
        else if(name.equals(CONTENT)) initializeContents(config);
    }

    private void setGrpcResource(ResourceConfig config) {
        if(name.equals(USERS)) initializeUsers(config);
    }

}
