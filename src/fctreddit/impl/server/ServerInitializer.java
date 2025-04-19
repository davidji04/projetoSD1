package fctreddit.impl.server;

import fctreddit.Discovery;
import fctreddit.ServiceRegistry;
import fctreddit.clients.grpc.GrpcContentClient;
import fctreddit.clients.grpc.GrpcImagesClient;
import fctreddit.clients.grpc.GrpcUsersClient;
import fctreddit.clients.rest.RestContentClient;
import fctreddit.clients.rest.RestImageClient;
import fctreddit.clients.rest.RestUsersClient;
import fctreddit.impl.server.grpc.GrpcContentServerStub;
import fctreddit.impl.server.grpc.GrpcImagesServerStub;
import fctreddit.impl.server.grpc.GrpcUsersServerStub;
import fctreddit.impl.server.rest.ContentResource;
import fctreddit.impl.server.rest.ImagesResource;
import fctreddit.impl.server.rest.UsersResource;
import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.Server;
import io.grpc.ServerCredentials;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;
import java.util.logging.Logger;


public class ServerInitializer {

    private static Logger Log = Logger.getLogger(ServerInitializer.class.getName());
    private static final String GRPC_CTX = "/grpc";


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

    public void startServerGrpc(){
        try {
            String serverURI = String.format(format, ip, port, GRPC_CTX);
            Discovery discovery = new Discovery(Discovery.DISCOVERY_ADDR, name, serverURI);
            discovery.start();
            Server s = setGrpcResource();
            s.start().awaitTermination();
        }catch (Exception e) {
            Log.severe(e.getMessage());
        }
    }



    private Server initializeGrpcUsers() {
        GrpcUsersServerStub stub = new GrpcUsersServerStub();
        ServerCredentials cred = InsecureServerCredentials.create();
        Server server = Grpc.newServerBuilderForPort(port, cred) .addService(stub).build();
        return server;
    }

    private Server initializeGrpcImages() {
        GrpcImagesServerStub stub = new GrpcImagesServerStub();
        ServerCredentials cred = InsecureServerCredentials.create();
        Server server = Grpc.newServerBuilderForPort(port, cred) .addService(stub).build();
        return server;

    }

    private Server initializeGrpcContent() {
        GrpcContentServerStub stub = new GrpcContentServerStub();
        ServerCredentials cred = InsecureServerCredentials.create();
        Server server = Grpc.newServerBuilderForPort(port, cred) .addService(stub).build();
        return server;
    }



    private void setRestResource(ResourceConfig config) {
        switch (name) {
            case USERS -> config.register(UsersResource.class);
            case IMAGES -> config.register(ImagesResource.class);
            case CONTENT -> config.register(ContentResource.class);
        }
    }

    private Server setGrpcResource() {
        switch (name) {
            case USERS -> {
                return initializeGrpcUsers();
            }
            case IMAGES -> {
                return initializeGrpcImages();
            }
            case CONTENT -> {
                return initializeGrpcContent();
            }
        }
        return null;

    }

}
