package fctreddit.impl.server.grpc;

import java.net.InetAddress;
import java.net.URI;
import java.util.logging.Logger;

import fctreddit.Discovery;
import fctreddit.ServiceRegistry;
import fctreddit.clients.grpc.GrpcContentClient;
import fctreddit.clients.grpc.GrpcImagesClient;
import fctreddit.impl.server.ServerInitializer;
import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.Server;
import io.grpc.ServerCredentials;

public class UsersServer {
public static final int PORT = 9000;

//	private static final String GRPC_CTX = "/grpc";
	public static final String SERVICE = "Users";

	private static final String SERVER_BASE_URI = "grpc://%s:%s%s";
	
	private static Logger Log = Logger.getLogger(UsersServer.class.getName());
	
	public static void main(String[] args) throws Exception {
//		String serverURI = String.format(SERVER_BASE_URI, InetAddress.getLocalHost().getHostAddress(), PORT, GRPC_CTX);
//
//
//		Discovery discovery = new Discovery(Discovery.DISCOVERY_ADDR,SERVICE,serverURI);
//		discovery.start();
		String ip = InetAddress.getLocalHost().getHostAddress();
		ServerInitializer s = new ServerInitializer(ip,PORT,SERVICE,SERVER_BASE_URI);
		s.startServerGrpc();
		Log.info(String.format("Users gRPC Server ready \n"));
//		ServiceRegistry sr = ServiceRegistry.getInstance();
//		URI contentUri = sr.getLatestUri("Content");
//		URI imageUri = sr.getLatestUri("Images");
//		GrpcContentClient contentClient = null;
//		if(contentUri != null)
//			contentClient = new GrpcContentClient(contentUri);
//		GrpcImagesClient imageClient = null;
//		if(imageUri != null)
//			imageClient = new GrpcImagesClient(imageUri);
//		GrpcUsersServerStub stub = new GrpcUsersServerStub( contentClient, imageClient);
//		ServerCredentials cred = InsecureServerCredentials.create();
//		Server server = Grpc.newServerBuilderForPort(PORT, cred) .addService(stub).build();
//		server.start().awaitTermination();
	}
}

