package fctreddit.clients.contents;

import fctreddit.Discovery;
import fctreddit.api.Post;
import fctreddit.api.java.Result;
import fctreddit.clients.contents.Parser.ArgsParser;
import fctreddit.clients.grpc.GrpcContentClient;
import fctreddit.clients.java.ContentClient;
import fctreddit.clients.rest.RestContentClient;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.logging.Logger;

public class DeletePostClient {

    private static Logger Log = Logger.getLogger(DeletePostClient.class.getName());

    public static void main(String[] args) throws IOException {

        if (args.length < 2) {
            System.err.println("Use: java " + DeletePostClient.class.getCanonicalName() + " postID userPassword");
            return;
        }
        Discovery discovery = new Discovery(Discovery.DISCOVERY_ADDR);
        discovery.start();

        URI[] uris = discovery.knownUrisOf("Content", 1);

        URI serverUrl = uris[0];
        String postId = args[0];
        String userPassword = args[1];

        ContentClient client;

        if (serverUrl.toString().endsWith("rest"))
            client = new RestContentClient(serverUrl);
        else
            client = new GrpcContentClient(serverUrl);
        Result<Void> result = client.deletePost(postId, userPassword);
        if (result.isOK())
            Log.info("Deleted post:" + result.value() + "\n");
        else
            Log.info("Failed to delete post:" + result.value() + "\n");

        discovery.stop();
    }
}
