package fctreddit.clients.grpc;

import java.net.URI;
import java.util.List;
import java.util.concurrent.TimeUnit;

import fctreddit.api.Post;
import fctreddit.api.java.Result;
import fctreddit.clients.java.ContentClient;
import fctreddit.impl.grpc.util.DataModelAdaptor;
import fctreddit.impl.server.grpc.generated_java.ContentGrpc;
import fctreddit.impl.server.grpc.generated_java.ContentProtoBuf.ChangeVoteArgs;
import fctreddit.impl.server.grpc.generated_java.ContentProtoBuf.CreatePostArgs;
import fctreddit.impl.server.grpc.generated_java.ContentProtoBuf.CreatePostResult;
import fctreddit.impl.server.grpc.generated_java.ContentProtoBuf.DeletePostArgs;
import fctreddit.impl.server.grpc.generated_java.ContentProtoBuf.GetPostAnswersArgs;
import fctreddit.impl.server.grpc.generated_java.ContentProtoBuf.GetPostArgs;
import fctreddit.impl.server.grpc.generated_java.ContentProtoBuf.GetPostsArgs;
import fctreddit.impl.server.grpc.generated_java.ContentProtoBuf.GetPostsResult;
import fctreddit.impl.server.grpc.generated_java.ContentProtoBuf.GrpcPost;
import fctreddit.impl.server.grpc.generated_java.ContentProtoBuf.UpdatePostArgs;
import fctreddit.impl.server.grpc.generated_java.ContentProtoBuf.VoteCountResult;
import io.grpc.Channel;
import io.grpc.LoadBalancerRegistry;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.internal.PickFirstLoadBalancerProvider;
import fctreddit.api.java.Result.ErrorCode;

public class GrpcContentClient extends ContentClient {

  static {
    LoadBalancerRegistry.getDefaultRegistry().register(new PickFirstLoadBalancerProvider());
  }

  final ContentGrpc.ContentBlockingStub stub;

  public GrpcContentClient(URI serverUrl) {
    Channel channel = ManagedChannelBuilder.forAddress(serverUrl.getHost(), serverUrl.getPort()).usePlaintext().build();
    stub = ContentGrpc.newBlockingStub(channel).withDeadlineAfter(READ_TIMEOUT, TimeUnit.MILLISECONDS);
  }

  @Override
  public Result<String> createPost(Post post, String userPassword) {
    try {
      CreatePostResult res = stub
          .createPost(
              CreatePostArgs.newBuilder().setPost(DataModelAdaptor.Post_toGrpcPost(post)).setPassword(userPassword)
                  .build());

      return Result.ok(res.getPostId());
    } catch (StatusRuntimeException sre) {
      return Result.error(statusToErrorCode(sre.getStatus()));
    }
  }

  @Override
  public Result<List<String>> getPosts(long timestamp, String sortOrder) {
    try {
      GetPostsResult res = stub.getPosts(GetPostsArgs.newBuilder()
          .setTimestamp(timestamp).setSortOrder(sortOrder)
          .build());

      return Result.ok(res.getPostIdList()); // nao sei se é isto
    } catch (StatusRuntimeException sre) {
      return Result.error(statusToErrorCode(sre.getStatus()));
    }
  }

  @Override
  public Result<Post> getPost(String postId) {
    try {
      GrpcPost res = stub.getPost(GetPostArgs.newBuilder()
          .setPostId(postId)
          .build());

      return Result.ok(DataModelAdaptor.GrpcContent_to_Content(res));
    } catch (StatusRuntimeException sre) {
      return Result.error(statusToErrorCode(sre.getStatus()));
    }
  }

  @Override
  public Result<List<String>> getPostAnswers(String postId, long maxTimeout) {
    try {
      GetPostsResult res = stub
          .getPostAnswers(GetPostAnswersArgs.newBuilder().setPostId(postId).setTimeout(maxTimeout).build());
      return Result.ok(res.getPostIdList()); // tambem nao sei se esta bem
    } catch (StatusRuntimeException sre) {
      return Result.error(statusToErrorCode(sre.getStatus()));
    }
  }

  @Override
  public Result<Post> updatePost(String postId, String userPassword, Post post) {
    try {
      GrpcPost res = stub
          .updatePost(UpdatePostArgs.newBuilder().setPostId(postId).setPassword(userPassword)
              .setPost(DataModelAdaptor.Post_toGrpcPost(post)).build());
      return Result.ok(DataModelAdaptor.GrpcContent_to_Content(res));
    } catch (StatusRuntimeException sre) {
      return Result.error(statusToErrorCode(sre.getStatus()));
    }
  }

  @Override
  public Result<Void> deletePost(String postId, String userPassword) {
    try {
      stub.deletePost(DeletePostArgs.newBuilder().setPostId(postId).setPassword(userPassword).build());
      return Result.ok();
    } catch (StatusRuntimeException sre) {
      return Result.error(statusToErrorCode(sre.getStatus()));
    }
  }

  @Override
  public Result<Void> upVotePost(String postId, String userId, String userPassword) {
    try {
      stub.upVotePost(
          ChangeVoteArgs.newBuilder().setPostId(postId).setUserId(userId).setPassword(userPassword).build());
      return Result.ok();
    } catch (StatusRuntimeException sre) {
      return Result.error(statusToErrorCode(sre.getStatus()));
    }

  }

  @Override
  public Result<Void> removeUpVotePost(String postId, String userId, String userPassword) {
    try {
      stub.removeUpVotePost(
          ChangeVoteArgs.newBuilder().setPostId(postId).setUserId(userId).setPassword(userPassword).build());
      return Result.ok();
    } catch (StatusRuntimeException sre) {
      return Result.error(statusToErrorCode(sre.getStatus()));
    }
  }

  @Override
  public Result<Void> downVotePost(String postId, String userId, String userPassword) {
    try {
      stub.downVotePost(
          ChangeVoteArgs.newBuilder().setPostId(postId).setUserId(userId).setPassword(userPassword).build());
      return Result.ok();
    } catch (StatusRuntimeException sre) {
      return Result.error(statusToErrorCode(sre.getStatus()));
    }
  }

  @Override
  public Result<Void> removeDownVotePost(String postId, String userId, String userPassword) {
    try {
      stub.removeDownVotePost(
          ChangeVoteArgs.newBuilder().setPostId(postId).setUserId(userId).setPassword(userPassword).build());
      return Result.ok();
    } catch (StatusRuntimeException sre) {
      return Result.error(statusToErrorCode(sre.getStatus()));
    }
  }

  @Override
  public Result<Integer> getupVotes(String postId) {
    try {
      VoteCountResult res = stub
          .getUpVotes(GetPostArgs.newBuilder().setPostId(postId).build());
      return Result.ok(res.getCount());
    } catch (StatusRuntimeException sre) {
      return Result.error(statusToErrorCode(sre.getStatus()));
    }

  }

  @Override
  public Result<Integer> getDownVotes(String postId) {
    try {
      VoteCountResult res = stub
          .getDownVotes(GetPostArgs.newBuilder().setPostId(postId).build());
      return Result.ok(res.getCount());
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
