package fctreddit.clients.grpc;

import java.net.URI;
import java.util.List;
import java.util.concurrent.TimeUnit;

import fctreddit.api.Post;
import fctreddit.api.java.Result;
import fctreddit.clients.java.ContentClient;
import fctreddit.impl.grpc.util.DataModelAdaptor;
import fctreddit.impl.server.grpc.generated_java.UsersGrpc;
import fctreddit.impl.server.grpc.generated_java.UsersProtoBuf.CreateUserArgs;
import fctreddit.impl.server.grpc.generated_java.UsersProtoBuf.CreateUserResult;
import io.grpc.Channel;
import io.grpc.LoadBalancerRegistry;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.internal.PickFirstLoadBalancerProvider;

public class GrpcContentClient extends ContentClient {

  static {
    LoadBalancerRegistry.getDefaultRegistry().register(new PickFirstLoadBalancerProvider());
  }

  final UsersGrpc.UsersBlockingStub stub;

  public GrpcContentClient(URI serverUrl) {
    Channel channel = ManagedChannelBuilder.forAddress(serverUrl.getHost(), serverUrl.getPort()).usePlaintext().build();
    stub = UsersGrpc.newBlockingStub(channel).withDeadlineAfter(READ_TIMEOUT, TimeUnit.MILLISECONDS);

  }

  @Override
  public Result<String> createPost(Post post, String userPassword) {
    try {
      CreateContentResult res = stub.createUser(CreateUserArgs.newBuilder()
          .setUser(DataModelAdaptor.User_to_GrpcUser(user))
          .build());

      return Result.ok(res.getUserId());
    } catch (StatusRuntimeException sre) {
      return Result.error(statusToErrorCode(sre.getStatus()));
    }
  }

  @Override
  public Result<List<String>> getPosts(long timestamp, String sortOrder) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getPosts'");
  }

  @Override
  public Result<Post> getPost(String postId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getPost'");
  }

  @Override
  public Result<List<String>> getPostAnswers(String postId, long maxTimeout) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getPostAnswers'");
  }

  @Override
  public Result<Post> updatePost(String postId, String userPassword, Post post) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'updatePost'");
  }

  @Override
  public Result<Void> deletePost(String postId, String userPassword) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deletePost'");
  }

  @Override
  public Result<Void> upVotePost(String postId, String userId, String userPassword) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'upVotePost'");
  }

  @Override
  public Result<Void> removeUpVotePost(String postId, String userId, String userPassword) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'removeUpVotePost'");
  }

  @Override
  public Result<Void> downVotePost(String postId, String userId, String userPassword) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'downVotePost'");
  }

  @Override
  public Result<Void> removeDownVotePost(String postId, String userId, String userPassword) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'removeDownVotePost'");
  }

  @Override
  public Result<Integer> getupVotes(String postId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getupVotes'");
  }

  @Override
  public Result<Integer> getDownVotes(String postId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getDownVotes'");
  }

}
