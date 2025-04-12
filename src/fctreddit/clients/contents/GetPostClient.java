package fctreddit.clients.contents;

import fctreddit.Discovery;
import fctreddit.api.Post;
import fctreddit.api.java.Result;
import fctreddit.clients.java.ContentClient;
import fctreddit.clients.rest.RestContentClient;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Logger;

public class GetPostClient {

    private static Logger Log = Logger.getLogger(GetPostClient.class.getName());

    public static void main(String[] args) throws IOException {
        if(args.length < 1) {
            System.err.println("Use: java " + GetPostClient.class.getCanonicalName() + " postId");
        }

        Discovery discovery = new Discovery(Discovery.DISCOVERY_ADDR);
        discovery.start();

        URI[] uris = discovery.knownUrisOf("Content",1);
        URI serverUrl = uris[0];
        String postId = args[0];

        ContentClient client;

        client = new RestContentClient( serverUrl);

        Result<Post> result = client.getPost(postId );
        if( result.isOK()  )
            Log.info("Post:" + result.value() + "\n" );
        else
            Log.info("Failed to get post:" + result.value() + "\n" );

        discovery.stop();
    }
}
