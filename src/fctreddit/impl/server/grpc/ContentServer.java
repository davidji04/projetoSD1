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
  public static final String SERVICE = "Content";

  private static final String SERVER_BASE_URI = "grpc://%s:%s%s";

  private static Logger Log = Logger.getLogger(ContentServer.class.getName());

  public static void main(String[] args) throws Exception {

    String ip = InetAddress.getLocalHost().getHostAddress();
    ServerInitializer s = new ServerInitializer(ip,PORT,SERVICE,SERVER_BASE_URI);
    s.startServerGrpc();
    Log.info(String.format("Content gRPC Server ready \n"));


  }

}
