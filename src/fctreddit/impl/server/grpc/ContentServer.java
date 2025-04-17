package fctreddit.impl.server.grpc;

import java.net.InetAddress;
import java.net.URI;
import java.util.logging.Logger;

import fctreddit.Discovery;
import fctreddit.clients.grpc.GrpcImagesClient;
import fctreddit.clients.grpc.GrpcUsersClient;

import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.Server;
import io.grpc.ServerCredentials;

public class ContentServer {
  public static final int PORT = 8080;

  private static final String GRPC_CTX = "/grpc";
  public static final String SERVICE = "ContentService";

  private static final String SERVER_BASE_URI = "grpc://%s:%s%s";

  private static Logger Log = Logger.getLogger(ContentServer.class.getName());

  public static void main(String[] args) throws Exception {

    String serverURI = String.format(SERVER_BASE_URI, InetAddress.getLocalHost().getHostAddress(), PORT, GRPC_CTX);
    Discovery discovery = new Discovery(Discovery.DISCOVERY_ADDR, SERVICE, serverURI);
    discovery.start();
    Log.info(String.format("Content gRPC Server ready @ %s\n", serverURI));

    URI[] usersUris = discovery.knownUrisOf("Users", 1);
    URI[] imageURIs = discovery.knownUrisOf("Images", 1);

    GrpcContentServerStub stub = new GrpcContentServerStub(new GrpcImagesClient(imageURIs[0]),
        new GrpcUsersClient(usersUris[0]));
    ServerCredentials cred = InsecureServerCredentials.create();
    Server server = Grpc.newServerBuilderForPort(PORT, cred).addService(stub).build();

    server.start().awaitTermination();
  }

}
