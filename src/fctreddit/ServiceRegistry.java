package fctreddit;

import fctreddit.clients.grpc.GrpcContentClient;
import fctreddit.clients.grpc.GrpcImagesClient;
import fctreddit.clients.grpc.GrpcUsersClient;
import fctreddit.clients.java.ContentClient;
import fctreddit.clients.java.ImagesClient;
import fctreddit.clients.java.UsersClient;
import fctreddit.clients.rest.RestContentClient;
import fctreddit.clients.rest.RestImageClient;
import fctreddit.clients.rest.RestUsersClient;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class ServiceRegistry {
    private static Logger Log = Logger.getLogger(ServiceRegistry.class.getName());


    private static ServiceRegistry instance;

    private final Map<String, URI> latestUris;

    static final int DISCOVERY_RETRY_TIMEOUT = 200;


    private ServiceRegistry() {
        latestUris = new ConcurrentHashMap<>();

    }

    public void addService(String serviceName, URI serviceUri) {
        latestUris.put(serviceName, serviceUri);
    }

    public URI getLatestUri(String serviceName) {
        return latestUris.get(serviceName);
    }
    public UsersClient getUsersClient() {
        URI userUri = ServiceRegistry.getInstance().getLatestUri("Users");
        if (userUri != null) {
            if (userUri.toString().endsWith("rest"))
                return new RestUsersClient(userUri);
            else
                return new GrpcUsersClient(userUri);
        }
        return null;
    }

    public ContentClient getContentClient() {
        URI contentUri = ServiceRegistry.getInstance().getLatestUri("Content");
        if (contentUri != null) {
            if(contentUri.toString().endsWith("rest"))
                return new RestContentClient(contentUri);
            else
                return new GrpcContentClient(contentUri);
        }
        return null;
    }

    public ImagesClient getImagesClient() {
        URI imagesUri = ServiceRegistry.getInstance().getLatestUri("Images");
        if (imagesUri != null) {
            if(imagesUri.toString().endsWith("rest"))
                return new RestImageClient(imagesUri);
            else
                return new GrpcImagesClient(imagesUri);
        }
        return null;
    }
    synchronized public static ServiceRegistry getInstance() {
        if (instance == null) {
            instance = new ServiceRegistry();
        }
        return instance;
    }

}
