package fctreddit.clients;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Logger;
import fctreddit.Discovery;
import fctreddit.api.java.Result;
import fctreddit.clients.grpc.GrpcUsersClient;
import fctreddit.clients.java.UsersClient;
import fctreddit.clients.rest.RestUsersClient;
import fctreddit.api.User;



public class UpdateUserClient {

    private static Logger Log = Logger.getLogger(UpdateUserClient.class.getName());

    public static void main(String[] args) throws IOException {

        if( args.length != 5) {
            System.err.println( "Use: java " + UpdateUserClient.class.getCanonicalName() + " userId oldpwd fullName email password");
            return;
        }
        Discovery discovery = new Discovery(Discovery.DISCOVERY_ADDR);
        discovery.start();
        URI[] uris = discovery.knownUrisOf("Users",1);


        URI serverUrl = uris[0];
        String userId = args[0];
        String oldpwd = args[1];
        String fullName = args[2];
        String email = args[3];
        String password = args[4];

        User usr = new User( userId, fullName, email, password);

        UsersClient client = null;

        if(serverUrl.toString().endsWith("rest"))
            client = new RestUsersClient(serverUrl);
        else
            client = new GrpcUsersClient(serverUrl);

        Result<User> result = client.updateUser(userId,oldpwd,usr);
        if( result.isOK()  )
            Log.info("Updated user:" + result.value() );
        else
            Log.info("Updated user failed with error: " + result.error());

    }
}

