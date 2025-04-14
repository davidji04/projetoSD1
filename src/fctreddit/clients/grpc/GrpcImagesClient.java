package fctreddit.clients.grpc;

import java.net.URI;

import fctreddit.api.java.Result;
import fctreddit.clients.java.ImagesClient;

public class GrpcImagesClient extends ImagesClient {

  public GrpcImagesClient(URI serverUrl) {
    // TODO Auto-generated constructor stub
  }

  @Override
  public Result<String> createImage(String userId, byte[] imageContents, String password) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'createImage'");
  }

  @Override
  public Result<byte[]> getImage(String userId, String imageId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getImage'");
  }

  @Override
  public Result<Void> deleteImage(String userId, String imageId, String password) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deleteImage'");
  }

}
