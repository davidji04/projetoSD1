package fctreddit.clients.images;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Logger;

import fctreddit.Discovery;
import fctreddit.api.java.Result;
import fctreddit.clients.grpc.GrpcImagesClient;
import fctreddit.clients.java.ImagesClient;
import fctreddit.clients.rest.RestImageClient;

public class GetImageClient {

  private static Logger Log = Logger.getLogger(GetImageClient.class.getName());

  public static void main(String[] args) throws IOException {

    if (args.length != 2) {
      System.err.println("Use: java " + GetImageClient.class.getCanonicalName() + " userId imageId");
      return;
    }
    Discovery discovery = new Discovery(Discovery.DISCOVERY_ADDR);
    discovery.start();
    URI[] uris = discovery.knownUrisOf("Images", 1);

    URI serverUrl = uris[0];
    String userId = args[0];
    String imageId = args[1];

    ImagesClient client = null;

    if (serverUrl.toString().endsWith("rest"))
      client = new RestImageClient(serverUrl);
    else
      client = new GrpcImagesClient(serverUrl);

    Result<byte[]> result = client.getImage(userId, imageId);
    if (result.isOK())
      Log.info("Get image:" + result.value() + "\n");
    else
      Log.info("Get image failed with error: " + result.error() + "\n");

    discovery.stop();
  }

}
