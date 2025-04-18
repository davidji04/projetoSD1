package fctreddit;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceRegistry {

    private static ServiceRegistry instance;

    private final Map<String, URI> latestUris;


    private ServiceRegistry() {
        latestUris = new ConcurrentHashMap<>();

    }

    public void addService(String serviceName, URI serviceUri) {
        latestUris.put(serviceName, serviceUri);
    }

    public URI getLatestUri(String serviceName) {
        return latestUris.get(serviceName);
    }

    synchronized public static ServiceRegistry getInstance() {
        if (instance == null) {
            instance = new ServiceRegistry();
        }
        return instance;
    }

}
