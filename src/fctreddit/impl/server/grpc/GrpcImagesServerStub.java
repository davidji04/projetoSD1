package fctreddit.impl.server.grpc;

import fctreddit.impl.grpc.util.DataModelAdaptor;
import fctreddit.impl.server.grpc.generated_java.ImageGrpc;
import fctreddit.impl.server.grpc.generated_java.ImageProtoBuf.CreateImageArgs;
import fctreddit.impl.server.grpc.generated_java.ImageProtoBuf.CreateImageResult;
import fctreddit.impl.server.grpc.generated_java.ImageProtoBuf.DeleteImageArgs;
import fctreddit.impl.server.grpc.generated_java.ImageProtoBuf.DeleteImageResult;
import fctreddit.impl.server.grpc.generated_java.ImageProtoBuf.GetImageArgs;
import fctreddit.impl.server.grpc.generated_java.ImageProtoBuf.GetImageResult;
import fctreddit.impl.server.grpc.generated_java.UsersProtoBuf.CreateUserResult;
import fctreddit.impl.server.grpc.generated_java.UsersProtoBuf.GetUserResult;
import fctreddit.impl.server.java.JavaImage;
import io.grpc.BindableService;
import io.grpc.ServerServiceDefinition;
import io.grpc.stub.StreamObserver;
import fctreddit.api.java.Result;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import fctreddit.api.User;
import fctreddit.api.java.Image;

public class GrpcImagesServerStub implements ImageGrpc.AsyncService, BindableService {

  private final String IMAGE_DIR = "../images";
  Image impl = new JavaImage();

  @Override
  public ServerServiceDefinition bindService() {
    return ImageGrpc.bindService(this);
  }

  @Override
  public void createImage(CreateImageArgs request, StreamObserver<CreateImageResult> responseObserver) {
    try {
      String imageId = UUID.randomUUID().toString();
      String password = request.hasPassword() ? request.getPassword() : null;
      String filename = IMAGE_DIR + "/" + imageId;
      Files.write(Paths.get(filename), request.getImageContents().toByteArray());
      Result<String> res = impl.createImage(imageId, request.getImageContents().toByteArray(), password);
      if (!res.isOK())
        responseObserver.onError(errorCodeToStatus(res.error()));
      else {
        responseObserver.onNext(CreateImageResult.newBuilder().setImageId(res.value()).build());
        responseObserver.onCompleted();
      }
    } catch (IOException e) {
    }
  }

  @Override
  public void getImage(GetImageArgs request, StreamObserver<GetImageResult> responseObserver) {
    Result<byte[]> res = impl.getImage(request.getUserId(), request.getImageId());
    if (!res.isOK())
      responseObserver.onError(errorCodeToStatus(res.error()));
    else {
      GetImageResult result = GetImageResult.newBuilder().setData(com.google.protobuf.ByteString.copyFrom(res.value()))
          .build();
      responseObserver.onNext(result);
      responseObserver.onCompleted();
    }
  }

  @Override
  public void deleteImage(DeleteImageArgs request, StreamObserver<DeleteImageResult> responseObserver) {
    String password = request.hasPassword() ? request.getPassword() : null;

    Result<Void> res = impl.deleteImage(request.getUserId(), request.getImageId(), password);

    if (!res.isOK()) {
      responseObserver.onError(errorCodeToStatus(res.error()));
    } else {
      DeleteImageResult result = DeleteImageResult.newBuilder().build();
      responseObserver.onNext(result);
      responseObserver.onCompleted();
    }
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
