package fctreddit.impl.server.grpc;

import java.net.InetAddress;
import java.net.URI;
import java.util.logging.Logger;

import fctreddit.Discovery;
import fctreddit.clients.grpc.GrpcContentClient;
import fctreddit.clients.grpc.GrpcUsersClient;
import fctreddit.impl.server.ServerInitializer;
import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.Server;
import io.grpc.ServerCredentials;

public class ImagesServer {
  public static final int PORT = 8078;

  public static final String SERVICE = "Images";

  private static final String SERVER_BASE_URI = "grpc://%s:%s%s";

  private static Logger Log = Logger.getLogger(ImagesServer.class.getName());

  public static void main(String[] args) throws Exception {
    // String serverURI = String.format(SERVER_BASE_URI,
    // InetAddress.getLocalHost().getHostAddress(), PORT, GRPC_CTX);
    //
    // Discovery discovery = new Discovery(Discovery.DISCOVERY_ADDR, SERVICE,
    // serverURI);
    // discovery.start();
    String ip = InetAddress.getLocalHost().getHostAddress();
    ServerInitializer s = new ServerInitializer(ip, PORT, SERVICE, SERVER_BASE_URI);
    s.startServerGrpc();
    Log.info(String.format("Images gRPC Server ready \n"));

    // URI[] usersUris = discovery.knownUrisOf("Users", 1);
    // URI[] contentUris = discovery.knownUrisOf("Content", 1);
    // GrpcImagesServerStub stub = new GrpcImagesServerStub(new
    // GrpcUsersClient(usersUris[0]), new GrpcContentClient(contentUris[0]));
    //
    // ServerCredentials cred = InsecureServerCredentials.create();
    // Server server = Grpc.newServerBuilderForPort(PORT,
    // cred).addService(stub).build();
    // server.start().awaitTermination();
  }
}
