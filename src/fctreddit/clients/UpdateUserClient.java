package fctreddit.clients;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Logger;


import fctreddit.api.java.Result;
import fctreddit.clients.grpc.GrpcUsersClient;
import fctreddit.clients.java.UsersClient;
import fctreddit.clients.rest.RestUsersClient;
import org.glassfish.jersey.client.ClientConfig;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import fctreddit.api.User;

import fctreddit.api.rest.RestUsers;

public class UpdateUserClient {

    private static Logger Log = Logger.getLogger(UpdateUserClient.class.getName());

    public static void main(String[] args) throws IOException {

        if( args.length != 6) {
            System.err.println( "Use: java " + UpdateUserClient.class.getCanonicalName() + " url userId oldpwd fullName email password");
            return;
        }

        String serverUrl = args[0];
        String userId = args[1];
        String oldpwd = args[2];
        String fullName = args[3];
        String email = args[4];
        String password = args[5];

        User usr = new User( userId, fullName, email, password);

        UsersClient client = null;

        if(serverUrl.endsWith("rest"))
            client = new RestUsersClient( URI.create( serverUrl ) );
        else
            client = new GrpcUsersClient( URI.create( serverUrl) );

        Result<User> result = client.updateUser(userId,oldpwd,usr);
        if( result.isOK()  )
            Log.info("Updated user:" + result.value() );
        else
            Log.info("Updated user failed with error: " + result.error());

    }
}

