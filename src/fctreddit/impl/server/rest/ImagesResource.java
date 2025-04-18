package fctreddit.impl.server.rest;

import java.util.logging.Logger;

import fctreddit.api.java.Image;
import fctreddit.api.java.Result;
import fctreddit.api.rest.RestImage;
import fctreddit.clients.java.ContentClient;
import fctreddit.clients.java.UsersClient;
import fctreddit.impl.server.java.JavaImage;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;

public class ImagesResource implements RestImage {

  private static final Logger Log = Logger.getLogger(ImagesResource.class.getName());

  final Image impl;

  public ImagesResource(UsersClient users, ContentClient content) {
    impl = new JavaImage(users, content);
  }

  @Override
  public String createImage(String userId, byte[] imageContents, String password) {
    Log.info("createImage : user = " + userId + "; image = " + imageContents + "; pwd = " + password + "\n");

    Result<String> res = impl.createImage(userId, imageContents, password);
    if (!res.isOK()) {
      throw new WebApplicationException(errorCodeToStatus(res.error()));
    }
    return res.value();
  }

  @Override
  public byte[] getImage(String userId, String imageId) {
    Log.info("getImage : user = " + userId + "; image = " + imageId + "\n");

    Result<byte[]> res = impl.getImage(userId, imageId);
    if (!res.isOK()) {
      throw new WebApplicationException(errorCodeToStatus(res.error()));
    }
    return res.value();
  }

  @Override
  public void deleteImage(String userId, String imageId, String password) {
    Log.info("deleteImage : user = " + userId + "; image = " + imageId + "; pwd = " + password + "\n");
    Result res = impl.deleteImage(userId, imageId, password);
    if (!res.isOK()) {
      throw new WebApplicationException(errorCodeToStatus(res.error()));
    }
  }

  protected static Status errorCodeToStatus(Result.ErrorCode error) {
    Status status = switch (error) {
      case NOT_FOUND -> Status.NOT_FOUND;
      case CONFLICT -> Status.CONFLICT;
      case FORBIDDEN -> Status.FORBIDDEN;
      case NOT_IMPLEMENTED -> Status.NOT_IMPLEMENTED;
      case BAD_REQUEST -> Status.BAD_REQUEST;
      default -> Status.INTERNAL_SERVER_ERROR;
    };

    return status;
  }
}
