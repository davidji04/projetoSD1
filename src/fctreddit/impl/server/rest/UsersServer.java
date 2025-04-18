package fctreddit.impl.server.rest;

import java.net.InetAddress;
import java.util.logging.Logger;

import fctreddit.impl.server.ServerInitializer;

public class UsersServer {

	private static Logger Log = Logger.getLogger(UsersServer.class.getName());

	public static final int PORT = 8080;
	public static final String SERVICE = "Users";
	private static final String SERVER_URI_FMT = "http://%s:%s/rest";

	public static void main(String[] args) {
		try {
			// ResourceConfig config = new ResourceConfig();

			String ip = InetAddress.getLocalHost().getHostAddress();
			ServerInitializer s = new ServerInitializer(ip, PORT, SERVICE, SERVER_URI_FMT);
			s.startServerRest();
			// String serverURI = String.format(SERVER_URI_FMT, ip, PORT);
			//
			// Discovery discovery = new
			// Discovery(Discovery.DISCOVERY_ADDR,SERVICE,serverURI);
			//
			//
			// discovery.start();
			//
			// URI contentUri = ServiceRegistry.getInstance().getLatestUri("Content");
			// URI imageUri = ServiceRegistry.getInstance().getLatestUri("Images");
			// RestContentClient contentClient = null;
			// RestImageClient imageClient = null;
			// if (contentUri != null)
			// contentClient = new RestContentClient(contentUri);
			// if (imageUri != null)
			// imageClient = new RestImageClient(imageUri);
			// UsersResource resource = new UsersResource(contentClient, imageClient);
			// ; config.register(resource);
			//
			// JdkHttpServerFactory.createHttpServer( URI.create(serverURI), config);

			Log.info(String.format("%s Server ready \n", SERVICE));

		} catch (Exception e) {
			Log.severe(e.getMessage());
		}
	}
}
