package fctreddit.impl.server.java;

import fctreddit.api.java.Image;
import fctreddit.api.java.Result;

public class JavaImage implements Image {

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
