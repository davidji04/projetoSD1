package fctreddit.clients.contents;

import fctreddit.Discovery;
import fctreddit.api.java.Result;
import fctreddit.clients.contents.Parser.ArgsParser;
import fctreddit.clients.grpc.GrpcContentClient;
import fctreddit.clients.java.ContentClient;
import fctreddit.clients.rest.RestContentClient;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class GetPostAnswersClient {

    private static Logger Log = Logger.getLogger(GetPostAnswersClient.class.getName());

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("Use: java " + GetPostAnswersClient.class.getCanonicalName() + " postId");
        }

        Discovery discovery = new Discovery(Discovery.DISCOVERY_ADDR);
        discovery.start();
        Map<String, String> opt = ArgsParser.parseOptionalArgs(args, 1);

        URI[] uris = discovery.knownUrisOf("Content", 1);

        URI serverUrl = uris[0];
        String postId = args[0];
        long maxTimeout = Long.parseLong(opt.get("maxtimeout"));

        ContentClient client;

        if (serverUrl.toString().endsWith("rest"))
            client = new RestContentClient(serverUrl);
        else
            client = new GrpcContentClient(serverUrl);
        Result<List<String>> result = client.getPostAnswers(postId, maxTimeout);
        if (result.isOK())
            for (String s : result.value())
                Log.info(s + "\n");
        else
            Log.info("Failed to get post answers:" + result.value() + "\n");

        discovery.stop();
    }
}
