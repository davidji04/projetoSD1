package fctreddit.clients;

import fctreddit.Discovery;
import fctreddit.api.User;
import fctreddit.api.java.Result;
import fctreddit.clients.grpc.GrpcUsersClient;
import fctreddit.clients.java.UsersClient;
import fctreddit.clients.rest.RestUsersClient;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Logger;

public class DeleteUserClient {
    private static Logger Log = Logger.getLogger(DeleteUserClient.class.getName());


    public static void main(String[] args) throws IOException {

        if( args.length != 2) {
            System.err.println( "Use: java " + DeleteUserClient.class.getCanonicalName() + "userId password");
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
            client = new RestUsersClient(  serverUrl  );
        else
            client = new GrpcUsersClient( serverUrl);

        Result<User> result = client.deleteUser(userId, password);
        if( result.isOK()  )
            Log.info("Deleted user:" + result.value() );
        else
            Log.info("Deleted user failed with error: " + result.error());

    }

}
