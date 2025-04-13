package fctreddit.clients.contents;

import fctreddit.Discovery;
import fctreddit.api.Post;
import fctreddit.api.java.Result;
import fctreddit.clients.contents.Parser.ArgsParser;
import fctreddit.clients.java.ContentClient;
import fctreddit.clients.rest.RestContentClient;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.logging.Logger;

public class CreatePostClient {


    private static Logger Log = Logger.getLogger(CreatePostClient.class.getName());

    public static void main(String[] args) throws IOException {

        if( args.length < 3) {
            System.err.println( "Use: java " + CreatePostClient.class.getCanonicalName() + "authorID content userPassword [--parentUrl url] [--mediaUrl url]");
            return;
        }
        Discovery discovery = new Discovery(Discovery.DISCOVERY_ADDR);
        discovery.start();

        URI[] uris = discovery.knownUrisOf("Content",1);

        Map<String,String> opt = ArgsParser.parseOptionalArgs(args, 3);
        URI serverUrl = uris[0];
        String authorId = args[0];
        String content = args[1];
        String userPassword = args[2];
        String mediaUrl = opt.get("mediaurl");
        String parentUrl = opt.get("parenturl");

        Post post;
        if( mediaUrl != null) {
            post = new Post(authorId, content, parentUrl, mediaUrl);
        }else if(parentUrl != null) {
            post = new Post(authorId, content, parentUrl);
        }else
            post = new Post(authorId, content);

        ContentClient client ;


        client = new RestContentClient( serverUrl);

        Result<String> result = client.createPost( post, userPassword);
        if( result.isOK()  )
            Log.info("Created post:" + result.value() + "\n" );
        else
            Log.info("Failed to create post:" + result.value() + "\n" );

        discovery.stop();
    }

}


