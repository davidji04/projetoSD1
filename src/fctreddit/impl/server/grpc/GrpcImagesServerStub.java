package fctreddit.impl.server.grpc;

import fctreddit.impl.server.grpc.generated_java.ImageGrpc;
import io.grpc.BindableService;
import io.grpc.ServerServiceDefinition;
import fctreddit.api.java.Result;
import fctreddit.impl.server.java.JavaImage;
import fctreddit.api.java.Image;

public class GrpcImagesServerStub implements ImageGrpc.AsyncService, BindableService {

  Image impl = new JavaImage();

  @Override
  public ServerServiceDefinition bindService() {
    return ImageGrpc.bindService(this);
  }

  protected static Throwable errorCodeToStatus(Result.ErrorCode error) {
    var status = switch (error) {
      case NOT_FOUND -> io.grpc.Status.NOT_FOUND;
      case CONFLICT -> io.grpc.Status.ALREADY_EXISTS;
      case FORBIDDEN -> io.grpc.Status.PERMISSION_DENIED;
      case NOT_IMPLEMENTED -> io.grpc.Status.UNIMPLEMENTED;
      case BAD_REQUEST -> io.grpc.Status.INVALID_ARGUMENT;
      default -> io.grpc.Status.INTERNAL;
    };

    return status.asException();
  }

}
