package fctreddit.impl.server.java;

import java.util.List;
import java.util.logging.Logger;

import fctreddit.api.User;
import fctreddit.api.java.Image;
import fctreddit.api.java.Result;
import fctreddit.api.java.Result.ErrorCode;
import fctreddit.api.java.Users;
import fctreddit.impl.server.persistence.Hibernate;

public class JavaImage implements Image {

  private static Logger Log = Logger.getLogger(JavaUsers.class.getName());

  private Hibernate hibernate;

  private Users users;

  public JavaImage() {
    hibernate = Hibernate.getInstance();
  }

  @Override
  public Result<String> createImage(String userId, byte[] imageContents, String password) {
    Log.info("createImage : user = " + userId + "; image = " + imageContents + "; pwd = " + password + "\n");

    Result<List<User>> userResult = users.searchUsers(userId);
    if (!userResult.isOK() || userResult.value() == null) {
      Log.info("User does not exist.\n");
      return Result.error(ErrorCode.NOT_FOUND);
    }

    return null;
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
