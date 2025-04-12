package fctreddit.impl.server.grpc;

import fctreddit.impl.server.grpc.generated_java.ImageGrpc;
import fctreddit.impl.server.grpc.generated_java.ImageProtoBuf.CreateImageArgs;
import fctreddit.impl.server.grpc.generated_java.ImageProtoBuf.CreateImageResult;
import fctreddit.impl.server.grpc.generated_java.ImageProtoBuf.DeleteImageArgs;
import fctreddit.impl.server.grpc.generated_java.ImageProtoBuf.DeleteImageResult;
import fctreddit.impl.server.grpc.generated_java.ImageProtoBuf.GetImageArgs;
import fctreddit.impl.server.grpc.generated_java.ImageProtoBuf.GetImageResult;
import io.grpc.BindableService;
import io.grpc.ServerServiceDefinition;
import io.grpc.stub.StreamObserver;
import fctreddit.api.java.Result;
import fctreddit.impl.server.java.JavaImage;
import fctreddit.api.java.Image;

public class GrpcImagesServerStub implements ImageGrpc.AsyncService, BindableService {

  Image impl = new JavaImage();

  @Override
  public ServerServiceDefinition bindService() {
    return ImageGrpc.bindService(this);
  }

  @Override
  public void createImage(CreateImageArgs request, StreamObserver<CreateImageResult> responseObserver) {

  }

  @Override
  public void getImage(GetImageArgs request, StreamObserver<GetImageResult> responseObserver) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getImage'");
  }

  @Override
  public void deleteImage(DeleteImageArgs request, StreamObserver<DeleteImageResult> responseObserver) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deleteImage'");
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
