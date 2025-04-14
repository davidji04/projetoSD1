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

public class SearchPostsClient {

    private static Logger Log = Logger.getLogger(SearchPostsClient.class.getName());

    public static void main(String[] args) throws IOException {

        Discovery discovery = new Discovery(Discovery.DISCOVERY_ADDR);
        discovery.start();

        URI[] uris = discovery.knownUrisOf("Content", 1);

        URI serverUrl = uris[0];
        Map<String, String> opt = ArgsParser.parseOptionalArgs(args, 0);
        String timestamp = opt.get("timestamp");
        long timeStampL = 0;
        if (timestamp != null)
            timeStampL = Long.parseLong(timestamp);
        String sortBy = opt.get("sortby");

        ContentClient client;

        if (serverUrl.toString().endsWith("rest"))
            client = new RestContentClient(serverUrl);
        else
            client = new GrpcContentClient(serverUrl);
        Result<List<String>> result = client.getPosts(timeStampL, sortBy);
        if (result.isOK())
            for (String s : result.value()) {
                Log.info(s + "\n");
            }
        else
            Log.info("Failed to list the posts:" + result.value() + "\n");

        discovery.stop();
    }

}
