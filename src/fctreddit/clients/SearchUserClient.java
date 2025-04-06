package fctreddit.clients;

import fctreddit.api.User;
import fctreddit.api.java.Result;
import fctreddit.clients.grpc.GrpcUsersClient;
import fctreddit.clients.java.UsersClient;
import fctreddit.clients.rest.RestUsersClient;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.logging.Logger;

public class SearchUserClient {
    private static Logger Log = Logger.getLogger(SearchUserClient.class.getName());

    public static void main(String[] args) throws IOException {

        if( args.length != 6) {
            System.err.println( "Use: java " + SearchUserClient.class.getCanonicalName() + " query");
            return;
        }

        String serverUrl = args[0];
        String query = args[1];


        UsersClient client = null;

        if(serverUrl.endsWith("rest"))
            client = new RestUsersClient( URI.create( serverUrl ) );
        else
            client = new GrpcUsersClient( URI.create( serverUrl) );

        Result<List<User>> result = client.searchUsers(query);
        if( result.isOK()  ){
            for( User u : result.value() ){
                Log.info( u.toString() );
            }
        }
        else
            Log.info("Search users failed with error: " + result.error());

    }
}
