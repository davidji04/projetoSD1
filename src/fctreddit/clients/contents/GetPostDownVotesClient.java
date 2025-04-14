package fctreddit.clients.contents;

import fctreddit.Discovery;
import fctreddit.api.java.Result;
import fctreddit.clients.grpc.GrpcContentClient;
import fctreddit.clients.java.ContentClient;
import fctreddit.clients.rest.RestContentClient;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Logger;

public class GetPostDownVotesClient {

    private static Logger Log = Logger.getLogger(GetPostUpVotesClient.class.getName());

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("Use: java " + GetPostUpVotesClient.class.getCanonicalName() + " postId");
        }

        Discovery discovery = new Discovery(Discovery.DISCOVERY_ADDR);
        discovery.start();

        URI[] uris = discovery.knownUrisOf("Content", 1);
        URI serverUrl = uris[0];
        String postId = args[0];

        ContentClient client;

        if (serverUrl.toString().endsWith("rest"))
            client = new RestContentClient(serverUrl);
        else
            client = new GrpcContentClient(serverUrl);
        Result<Integer> result = client.getDownVotes(postId);
        if (result.isOK())
            Log.info("Post down votes:" + result.value() + "\n");
        else
            Log.info("Failed to get post down votes:" + result.value() + "\n");

        discovery.stop();
    }
}
