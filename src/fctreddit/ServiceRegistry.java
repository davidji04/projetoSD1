package fctreddit;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class ServiceRegistry {
    private static Logger Log = Logger.getLogger(ServiceRegistry.class.getName());


    private static ServiceRegistry instance;

    private final Map<String, URI> latestUris;

    static final int DISCOVERY_RETRY_TIMEOUT = 1000;


    private ServiceRegistry() {
        latestUris = new ConcurrentHashMap<>();

    }

    public void addService(String serviceName, URI serviceUri) {
        latestUris.put(serviceName, serviceUri);
    }

    public URI getLatestUri(String serviceName) {
        int retries = 0;
        int maxRetries = 3;
        URI latestUri = null;

        while (retries < maxRetries) {
            latestUri = latestUris.get(serviceName);
            if (latestUri != null) {
                break;
            }
            try {
                Thread.sleep(DISCOVERY_RETRY_TIMEOUT);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
            retries++;
        }
        return latestUri;
    }

    synchronized public static ServiceRegistry getInstance() {
        if (instance == null) {
            instance = new ServiceRegistry();
        }
        return instance;
    }

}
