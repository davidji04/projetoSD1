package fctreddit.clients.images;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Logger;

import fctreddit.Discovery;
import fctreddit.api.java.Result;
import fctreddit.clients.grpc.GrpcImagesClient;
import fctreddit.clients.java.ImagesClient;
import fctreddit.clients.rest.RestImageClient;

public class CreateImageClient {
  private static Logger Log = Logger.getLogger(CreateImageClient.class.getName());

  public static void main(String[] args) throws IOException {

    if (args.length != 3) {
      System.err.println("Use: java " + CreateImageClient.class.getCanonicalName() + " userId imageContent password");
      return;
    }
    Discovery discovery = new Discovery(Discovery.DISCOVERY_ADDR);
    discovery.start();

    URI[] uris = discovery.knownUrisOf("Images", 1);

    URI serverUrl = uris[0];
    String userId = args[0];
    byte[] imageContent = args[1].getBytes();
    String password = args[2];

    ImagesClient client = null;

    if (serverUrl.toString().endsWith("rest"))
      client = new RestImageClient(serverUrl);
    else
      client = new GrpcImagesClient(serverUrl);

    Result<String> result = client.createImage(userId, imageContent, password);
    if (result.isOK())
      Log.info("Created iamge:" + result.value() + "\n");
    else
      Log.info("Create image failed with error: " + result.error() + "\n");

    discovery.stop();
  }

}
