package fctreddit.clients.grpc;

import java.net.URI;

import fctreddit.api.java.Result;
import fctreddit.clients.java.ImagesClient;
import fctreddit.impl.grpc.util.DataModelAdaptor;
import fctreddit.impl.server.grpc.generated_java.ImageGrpc;
import fctreddit.impl.server.grpc.generated_java.ImageGrpc.ImageBlockingStub;
import fctreddit.impl.server.grpc.generated_java.ImageProtoBuf.CreateImageArgs;
import fctreddit.impl.server.grpc.generated_java.ImageProtoBuf.CreateImageResult;
import fctreddit.impl.server.grpc.generated_java.UsersProtoBuf.CreateUserArgs;
import fctreddit.impl.server.grpc.generated_java.UsersProtoBuf.CreateUserResult;
import io.grpc.LoadBalancerRegistry;
import io.grpc.ManagedChannelBuilder;
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
      CreateImageResult res = stub.createUser(CreateImageArgs.newBuilder()
          .setUser(DataModelAdaptor.User_to_GrpcUser(user))
          .build());

      return Result.ok(res.getUserId());
    } catch (StatusRuntimeException sre) {
      return Result.error(statusToErrorCode(sre.getStatus()));
    }
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
