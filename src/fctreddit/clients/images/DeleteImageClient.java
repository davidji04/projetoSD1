package fctreddit.clients.images;

import fctreddit.Discovery;
import fctreddit.api.java.Result;
import fctreddit.clients.grpc.GrpcImagesClient;
import fctreddit.clients.java.ImagesClient;
import fctreddit.clients.rest.RestImageClient;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Logger;

public class DeleteImageClient {
  private static Logger Log = Logger.getLogger(DeleteImageClient.class.getName());

  public static void main(String[] args) throws IOException {

    if (args.length != 3) {
      System.err.println("Use: java " + DeleteImageClient.class.getCanonicalName() + "userId imageId password");
      return;
    }
    Discovery discovery = new Discovery(Discovery.DISCOVERY_ADDR);
    discovery.start();
    URI[] uris = discovery.knownUrisOf("Users", 1);

    URI serverUrl = uris[0];
    String userId = args[0];
    String imageId = args[1];
    String password = args[2];

    ImagesClient client = null;

    if (serverUrl.toString().endsWith("rest"))
      client = new RestImageClient(serverUrl);
    else
      client = new GrpcImagesClient(serverUrl);

    Result<Void> result = client.deleteImage(userId, imageId, password);
    if (result.isOK())
      Log.info("Deleted image:" + result.value() + "\n");
    else
      Log.info("Deleted image failed with error: " + result.error() + "\n");

    discovery.stop();
  }

}
