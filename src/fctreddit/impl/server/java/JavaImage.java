package fctreddit.impl.server.java;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.List;

import fctreddit.api.User;
import fctreddit.api.java.Image;
import fctreddit.api.java.Result;
import fctreddit.api.java.Result.ErrorCode;
import fctreddit.clients.java.ContentClient;
import fctreddit.clients.java.UsersClient;

public class JavaImage implements Image {

  private static Logger Log = Logger.getLogger(JavaUsers.class.getName());

  private static final String DIR = "images";

  private final UsersClient users;

  private final ContentClient content;

  public JavaImage(UsersClient users, ContentClient content) {
    File dir = new File(DIR);
    if (!dir.exists()) {
      dir.mkdirs();
    }
    this.users = users;
    this.content = content;
  }

  @Override
  public Result<String> createImage(String userId, byte[] imageContents, String password) {
    Log.info("createImage : user = " + userId + "; image = " + imageContents + "; pwd = " + password + "\n");

    Result<User> r = users.getUser(userId, password);

    if (!r.isOK())
      return Result.error(r.error());

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
    Result<List<User>> r = users.searchUsers(userId);
    if (!r.isOK())
      return Result.error(r.error());
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
      users.getUser(userId, password);
      Files.delete(imagePath);
      return Result.ok(null);
    } catch (IOException e) {
      e.printStackTrace();
      return Result.error(ErrorCode.INTERNAL_ERROR);
    }
  }

}
