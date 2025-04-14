package fctreddit.clients.contents;

import fctreddit.Discovery;
import fctreddit.api.Post;
import fctreddit.api.java.Result;
import fctreddit.clients.contents.Parser.ArgsParser;
import fctreddit.clients.grpc.GrpcContentClient;
import fctreddit.clients.grpc.GrpcUsersClient;
import fctreddit.clients.java.ContentClient;
import fctreddit.clients.rest.RestContentClient;
import fctreddit.clients.rest.RestUsersClient;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.logging.Logger;

public class AddPostUpVoteClient {

    private static Logger Log = Logger.getLogger(AddPostUpVoteClient.class.getName());

    public static void main(String[] args) throws IOException {

        if (args.length < 3) {
            System.err.println(
                    "Use: java " + AddPostUpVoteClient.class.getCanonicalName() + " postId userId userPassword");
            return;
        }
        Discovery discovery = new Discovery(Discovery.DISCOVERY_ADDR);
        discovery.start();

        URI[] uris = discovery.knownUrisOf("Content", 1);

        URI serverUrl = uris[0];
        String postId = args[0];
        String userId = args[1];
        String userPassword = args[2];

        ContentClient client;

        if (serverUrl.toString().endsWith("rest"))
            client = new RestContentClient(serverUrl);
        else
            client = new GrpcContentClient(serverUrl);

        Result<Void> result = client.upVotePost(postId, userId, userPassword);
        if (result.isOK())
            Log.info("Up voted post:" + "\n");
        else
            Log.info("Failed to upvote post:" + "\n");

        discovery.stop();
    }
}
