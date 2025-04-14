package fctreddit.impl.server.java;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import fctreddit.api.User;
import fctreddit.api.java.Image;
import fctreddit.api.java.Result;
import fctreddit.api.java.Result.ErrorCode;
import fctreddit.api.java.Users;

public class JavaImage implements Image {

  private static Logger Log = Logger.getLogger(JavaUsers.class.getName());

  private static final String DIR = "images";

  private Users users;

  public JavaImage(Users users) {
    File dir = new File(DIR);
    if (!dir.exists()) {
      dir.mkdirs();
    }
  }

  @Override
  public Result<String> createImage(String userId, byte[] imageContents, String password) {
    Log.info("createImage : user = " + userId + "; image = " + imageContents + "; pwd = " + password + "\n");

    Result<List<User>> userResult = users.searchUsers(userId);
    if (!userResult.isOK() || userResult.value() == null) {
      Log.info("User does not exist.\n");
      return Result.error(ErrorCode.NOT_FOUND);
    }
    try {
      String imageId = UUID.randomUUID().toString();
      String filename = DIR + "/" + imageId;
      Files.write(Paths.get(filename), imageContents);
      return Result.ok(imageId);
    } catch (IOException e) {
      e.printStackTrace();
      return Result.error(ErrorCode.INTERNAL_ERROR);
    }
  }

  @Override
  public Result<byte[]> getImage(String userId, String imageId) {
    if (userId == null || imageId == null) {
      return Result.error(ErrorCode.BAD_REQUEST);
    }
    Result<List<User>> userResult = users.searchUsers(userId);
    if (!userResult.isOK() || userResult.value() == null) {
      Log.info("User does not exist.\n");
      return Result.error(ErrorCode.NOT_FOUND);
    }

    try {
      String filename = DIR + "/" + imageId;
      Path imagePath = Paths.get(filename);
      if (!Files.exists(imagePath)) {
        return Result.error(ErrorCode.NOT_FOUND);
      }

      byte[] content = Files.readAllBytes(imagePath);
      return Result.ok(content);
    } catch (IOException e) {
      e.printStackTrace();
      return Result.error(ErrorCode.INTERNAL_ERROR);
    }
  }

  @Override
  public Result<Void> deleteImage(String userId, String imageId, String password) {
    Log.info("deleteImage: " + userId + " imageId: " + imageId);

    if (userId == null || imageId == null) {
      return Result.error(ErrorCode.BAD_REQUEST);
    }
    String filename = DIR + "/" + imageId;

    try {
      Path imagePath = Paths.get(filename);

      if (!Files.exists(imagePath)) {
        return Result.error(ErrorCode.NOT_FOUND);
      }

      String storedPassword = Files.readString(imagePath);

      if (password == null || !password.equals(storedPassword)) {
        return Result.error(ErrorCode.FORBIDDEN);
      }

      Files.delete(imagePath);
      return Result.ok(null);
    } catch (IOException e) {
      e.printStackTrace();
      return Result.error(ErrorCode.INTERNAL_ERROR);
    }
  }

}
