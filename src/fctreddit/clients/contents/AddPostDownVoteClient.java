package fctreddit.clients.contents;

import fctreddit.Discovery;
import fctreddit.api.java.Result;
import fctreddit.clients.java.ContentClient;
import fctreddit.clients.rest.RestContentClient;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Logger;

public class AddPostDownVoteClient {

    private static Logger Log = Logger.getLogger(AddPostDownVoteClient.class.getName());

    public static void main(String[] args) throws IOException {

        if( args.length < 3) {
            System.err.println( "Use: java " + AddPostDownVoteClient.class.getCanonicalName() + " postId userId userPassword");
            return;
        }
        Discovery discovery = new Discovery(Discovery.DISCOVERY_ADDR);
        discovery.start();

        URI[] uris = discovery.knownUrisOf("Content",1);

        URI serverUrl = uris[0];
        String postId = args[0];
        String userId = args[1];
        String userPassword = args[2];



        ContentClient client ;


        client = new RestContentClient( serverUrl);

        Result<Void> result = client.downVotePost(postId,userId,userPassword);
        if( result.isOK()  )
            Log.info("Down voted post:" + "\n" );
        else
            Log.info("Failed to down vote post:" + "\n" );

        discovery.stop();
    }
}
