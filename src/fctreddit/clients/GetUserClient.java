package fctreddit.clients;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Logger;

import fctreddit.Discovery;
import fctreddit.api.User;
import fctreddit.api.java.Result;
import fctreddit.clients.grpc.GrpcUsersClient;
import fctreddit.clients.java.UsersClient;
import fctreddit.clients.rest.RestUsersClient;

public class GetUserClient {
	
	private static Logger Log = Logger.getLogger(GetUserClient.class.getName());


	public static void main(String[] args) throws IOException {



		if( args.length != 2) {
			System.err.println( "Use: java " + GetUserClient.class.getCanonicalName() + " userId password");
			return;
		}
		Discovery discovery = new Discovery(Discovery.DISCOVERY_ADDR);
		discovery.start();
		URI[] uris = discovery.knownUrisOf("Users",1);

		URI serverUrl = uris[0];
		String userId = args[1];
		String password = args[2];
		
		UsersClient client = null;
		
		if(serverUrl.toString().endsWith("rest"))
			client = new RestUsersClient( serverUrl  );
		else
			client = new GrpcUsersClient(  serverUrl );
			
		Result<User> result = client.getUser(userId, password);
		if( result.isOK()  )
			Log.info("Get user:" + result.value() );
		else
			Log.info("Get user failed with error: " + result.error());
		
	}
	
}
