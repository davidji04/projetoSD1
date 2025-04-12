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

public class UpdatePostClient {

    private static Logger Log = Logger.getLogger(UpdatePostClient.class.getName());
    public static void main(String[] args) throws IOException {

        if( args.length < 4) {
            System.err.println( "Use: java " + UpdatePostClient.class.getCanonicalName() + "postID authorID content userPassword [--parentUrl url] [--mediaUrl url]");
            return;
        }
        Discovery discovery = new Discovery(Discovery.DISCOVERY_ADDR);
        discovery.start();

        URI[] uris = discovery.knownUrisOf("Content",1);

        Map<String,String> opt = ArgsParser.parseOptionalArgs(args, 4);
        URI serverUrl = uris[0];
        String postID = args[0];
        String authorId = args[1];
        String content = args[2];
        String userPassword = args[3];
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

        Result<Post> result = client.updatePost( postID, userPassword, post);
        if( result.isOK()  )
            Log.info("Updated post:" + result.value() + "\n" );
        else
            Log.info("Failed to update post:" + result.value() + "\n" );

        discovery.stop();
    }

}
