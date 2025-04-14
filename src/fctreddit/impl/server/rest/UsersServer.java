package fctreddit.impl.server.rest;

import java.net.InetAddress;
import java.net.URI;
import java.util.logging.Logger;

import fctreddit.Discovery;
import fctreddit.clients.rest.RestContentClient;
import fctreddit.clients.rest.RestImageClient;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

public class UsersServer {

	private static Logger Log = Logger.getLogger(UsersServer.class.getName());

	static {
		System.setProperty("java.net.preferIPv4Stack", "true");
		System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s\n");
	}
	
	public static final int PORT = 8080;
	public static final String SERVICE = "Users";
	private static final String SERVER_URI_FMT = "http://%s:%s/rest";
	
	public static void main(String[] args) {
		try {
		ResourceConfig config = new ResourceConfig();

		String ip = InetAddress.getLocalHost().getHostAddress();
		String serverURI = String.format(SERVER_URI_FMT, ip, PORT);

		Discovery discovery = new Discovery(Discovery.DISCOVERY_ADDR,SERVICE,serverURI);


		discovery.start();
		URI[] contentUris = discovery.knownUrisOf("Content",1);
		URI[] imageURIs = discovery.knownUrisOf("Images",1);
		UsersResource resource = new UsersResource(new RestContentClient(contentUris[0]), new RestImageClient(imageURIs[0]));
;		config.register(resource);

		JdkHttpServerFactory.createHttpServer( URI.create(serverURI), config);

		Log.info(String.format("%s Server ready @ %s\n",  SERVICE, serverURI));

			//More code can be executed here...
		} catch( Exception e) {
			Log.severe(e.getMessage());
		}
	}	
}
