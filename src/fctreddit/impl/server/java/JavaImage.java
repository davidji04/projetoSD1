package fctreddit.impl.server.java;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.logging.Logger;

import fctreddit.ServiceRegistry;
import fctreddit.api.User;
import fctreddit.api.java.Image;
import fctreddit.api.java.Result;
import fctreddit.api.java.Result.ErrorCode;
import fctreddit.clients.grpc.GrpcContentClient;
import fctreddit.clients.grpc.GrpcUsersClient;
import fctreddit.clients.java.ContentClient;
import fctreddit.clients.java.UsersClient;
import fctreddit.clients.rest.RestContentClient;
import fctreddit.clients.rest.RestUsersClient;

public class JavaImage implements Image {

  private static Logger Log = Logger.getLogger(JavaUsers.class.getName());

  private static final String DIR = "image";

  private String IP_ADRESS;

  private UsersClient users;

  private ContentClient content;

  public JavaImage() {
    File dir = new File(DIR);
    if (!dir.exists()) {
      dir.mkdirs();
    }

    try {
      IP_ADRESS = InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }

    URI userUri = ServiceRegistry.getInstance().getLatestUri("Users");
    URI contentUri = ServiceRegistry.getInstance().getLatestUri("Content");
    if (userUri != null) {
      if (userUri.toString().contains("rest"))
        this.users = new RestUsersClient(userUri);
      else
        this.users = new GrpcUsersClient(userUri);
    }
    if (contentUri != null) {
      if (contentUri.toString().contains("rest"))
        this.content = new RestContentClient(contentUri);
      else
        this.content = new GrpcContentClient(contentUri);
    }

  }

  @Override
  public Result<String> createImage(String userId, byte[] imageContents, String password) {
    Log.info("createImage : user = " + userId + "; image = " + imageContents + "; pwd = " + password + "\n");
    if (users != null) {
      Result<User> r = users.getUser(userId, password);
      if (!r.isOK())
        return Result.error(r.error());
    }
    try {
      String imageId = UUID.randomUUID().toString();
      File dir = new File(DIR + "/" + userId);
      if (!dir.exists()) {
        dir.mkdirs();
      }
      String filename = DIR + "/" + userId + "/" + imageId;
      Files.write(Paths.get(filename), imageContents);
      return Result
          .ok("http://" + IP_ADRESS + ":8082/rest/image/" + userId + "/" + imageId);
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
    try {
      Path imagePath = Paths.get(DIR + "/" + userId + "/" + imageId);

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

  private boolean isInvalid(String s) {
    return s == null || s.trim().isEmpty();
  }

}
