package fctreddit.clients.grpc;

import java.net.URI;
import java.util.Iterator;

import fctreddit.api.java.Result;
import fctreddit.api.java.Result.ErrorCode;
import fctreddit.clients.java.ImagesClient;
import fctreddit.impl.server.grpc.generated_java.ImageGrpc;
import fctreddit.impl.server.grpc.generated_java.ImageGrpc.ImageBlockingStub;
import fctreddit.impl.server.grpc.generated_java.ImageProtoBuf.CreateImageArgs;
import fctreddit.impl.server.grpc.generated_java.ImageProtoBuf.CreateImageResult;
import fctreddit.impl.server.grpc.generated_java.ImageProtoBuf.DeleteImageArgs;
import fctreddit.impl.server.grpc.generated_java.ImageProtoBuf.GetImageArgs;
import fctreddit.impl.server.grpc.generated_java.ImageProtoBuf.GetImageResult;
import io.grpc.LoadBalancerRegistry;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.internal.PickFirstLoadBalancerProvider;
import io.grpc.Channel;

public class GrpcImagesClient extends ImagesClient {

  static {
    LoadBalancerRegistry.getDefaultRegistry().register(new PickFirstLoadBalancerProvider());
  }

  final ImageBlockingStub stub;

  public GrpcImagesClient(URI serverUrl) {
    Channel channel = ManagedChannelBuilder.forAddress(serverUrl.getHost(), serverUrl.getPort()).usePlaintext().build();
    stub = ImageGrpc.newBlockingStub(channel);
  }

  @Override
  public Result<String> createImage(String userId, byte[] imageContents, String password) {
    try {
      CreateImageResult res = stub.createImage(CreateImageArgs.newBuilder()
          .setUserId(userId).setImageContents(com.google.protobuf.ByteString.copyFrom(imageContents))
          .setPassword(password).build());
      return Result.ok(res.getImageId());
    } catch (StatusRuntimeException sre) {
      return Result.error(statusToErrorCode(sre.getStatus()));
    }
  }

  @Override
  public Result<byte[]> getImage(String userId, String imageId) {
    try {
      Iterator<GetImageResult> resIt = stub.getImage(GetImageArgs.newBuilder()
          .setUserId(userId).setImageId(imageId).build());
      if (resIt.hasNext()) {
        GetImageResult res = resIt.next();
        return Result.ok(res.getData().toByteArray());
      } else {
        return Result.error(ErrorCode.NOT_FOUND);
      }
    } catch (StatusRuntimeException sre) {
      return Result.error(statusToErrorCode(sre.getStatus()));
    }
  }

  @Override
  public Result<Void> deleteImage(String userId, String imageId, String password) {
    try {
      stub.deleteImage(DeleteImageArgs.newBuilder()
          .setUserId(userId).setImageId(userId).setPassword(password).build());
      return Result.ok();
    } catch (StatusRuntimeException sre) {
      return Result.error(statusToErrorCode(sre.getStatus()));
    }
  }

  static ErrorCode statusToErrorCode(Status status) {
    return switch (status.getCode()) {
      case OK -> ErrorCode.OK;
      case NOT_FOUND -> ErrorCode.NOT_FOUND;
      case ALREADY_EXISTS -> ErrorCode.CONFLICT;
      case PERMISSION_DENIED -> ErrorCode.FORBIDDEN;
      case INVALID_ARGUMENT -> ErrorCode.BAD_REQUEST;
      case UNIMPLEMENTED -> ErrorCode.NOT_IMPLEMENTED;
      default -> ErrorCode.INTERNAL_ERROR;
    };
  }

}
