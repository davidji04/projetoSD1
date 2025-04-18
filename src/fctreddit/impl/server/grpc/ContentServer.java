package fctreddit.impl.server.grpc;

import java.net.InetAddress;
import java.net.URI;
import java.util.logging.Logger;

import fctreddit.Discovery;
import fctreddit.ServiceRegistry;
import fctreddit.clients.grpc.GrpcContentClient;
import fctreddit.clients.grpc.GrpcImagesClient;
import fctreddit.clients.grpc.GrpcUsersClient;

import fctreddit.impl.server.ServerInitializer;
import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.Server;
import io.grpc.ServerCredentials;

public class ContentServer {
  public static final int PORT = 8079;

  private static final String GRPC_CTX = "/grpc";
  public static final String SERVICE = "ContentService";

  private static final String SERVER_BASE_URI = "grpc://%s:%s%s";

  private static Logger Log = Logger.getLogger(ContentServer.class.getName());

  public static void main(String[] args) throws Exception {

//    String serverURI = String.format(SERVER_BASE_URI, InetAddress.getLocalHost().getHostAddress(), PORT, GRPC_CTX);
//    Discovery discovery = new Discovery(Discovery.DISCOVERY_ADDR, SERVICE, serverURI);
//    discovery.start();
    String ip = InetAddress.getLocalHost().getHostAddress();
    ServerInitializer s = new ServerInitializer(ip,PORT,SERVICE,SERVER_BASE_URI);
    s.startServerGrpc();
    Log.info(String.format("Content gRPC Server ready \n"));

//    ServiceRegistry sr = ServiceRegistry.getInstance();
//    URI usersUri = sr.getLatestUri("Users");
//    URI imageUri = sr.getLatestUri("Images");
//    GrpcUsersClient usersClient = null;
//    if(usersUri != null)
//      usersClient = new GrpcUsersClient(usersUri);
//    GrpcImagesClient imageClient = null;
//    if(imageUri != null)
//      imageClient = new GrpcImagesClient(imageUri);
//    GrpcContentServerStub stub = new GrpcContentServerStub( imageClient, usersClient);
//    ServerCredentials cred = InsecureServerCredentials.create();
//    Server server = Grpc.newServerBuilderForPort(PORT, cred).addService(stub).build();

//    server.start().awaitTermination();
  }

}
