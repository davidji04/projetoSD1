package fctreddit.clients.users;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Logger;

import fctreddit.Discovery;
import fctreddit.api.User;
import fctreddit.api.java.Result;
import fctreddit.clients.java.UsersClient;
import fctreddit.clients.rest.RestUsersClient;
import fctreddit.clients.grpc.GrpcUsersClient;

public class CreateUserClient {

	private static Logger Log = Logger.getLogger(CreateUserClient.class.getName());


	public static void main(String[] args) throws IOException {

		if( args.length != 4) {
			System.err.println( "Use: java " + CreateUserClient.class.getCanonicalName() + " userId fullName email password");
			return;
		}
		Discovery discovery = new Discovery(Discovery.DISCOVERY_ADDR);
		discovery.start();

		URI[] uris = discovery.knownUrisOf("Users",1);

		URI serverUrl = uris[0];
		String userId = args[0];
		String fullName = args[1];
		String email = args[2];
		String password = args[3];
		
		User usr = new User( userId, fullName, email, password);
		UsersClient client = null;
		
		if(serverUrl.toString().endsWith("rest"))
			client = new RestUsersClient( serverUrl );
		else
			client = new GrpcUsersClient( serverUrl );
		
		Result<String> result = client.createUser( usr );
		if( result.isOK()  )
			Log.info("Created user:" + result.value() + "\n" );
		else
			Log.info("Create user failed with error: " + result.error() + "\n");

		discovery.stop();
	}
	
}
