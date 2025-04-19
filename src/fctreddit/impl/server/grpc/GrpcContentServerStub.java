package fctreddit.impl.server.grpc;

import java.util.List;

import fctreddit.api.Post;
import fctreddit.api.java.Content;
import fctreddit.api.java.Result;
import fctreddit.impl.grpc.util.DataModelAdaptor;
import fctreddit.impl.server.grpc.generated_java.ContentGrpc;
import fctreddit.impl.server.grpc.generated_java.ContentProtoBuf.ChangeVoteArgs;
import fctreddit.impl.server.grpc.generated_java.ContentProtoBuf.CreatePostArgs;
import fctreddit.impl.server.grpc.generated_java.ContentProtoBuf.CreatePostResult;
import fctreddit.impl.server.grpc.generated_java.ContentProtoBuf.DeletePostArgs;
import fctreddit.impl.server.grpc.generated_java.ContentProtoBuf.EmptyMessage;
import fctreddit.impl.server.grpc.generated_java.ContentProtoBuf.GetPostAnswersArgs;
import fctreddit.impl.server.grpc.generated_java.ContentProtoBuf.GetPostArgs;
import fctreddit.impl.server.grpc.generated_java.ContentProtoBuf.GetPostsArgs;
import fctreddit.impl.server.grpc.generated_java.ContentProtoBuf.GetPostsResult;
import fctreddit.impl.server.grpc.generated_java.ContentProtoBuf.GrpcPost;
import fctreddit.impl.server.grpc.generated_java.ContentProtoBuf.UpdatePostArgs;
import fctreddit.impl.server.grpc.generated_java.ContentProtoBuf.VoteCountResult;
import fctreddit.impl.server.java.JavaContent;
import io.grpc.BindableService;
import io.grpc.ServerServiceDefinition;
import io.grpc.stub.StreamObserver;

public class GrpcContentServerStub implements ContentGrpc.AsyncService, BindableService {

  final Content impl;

  public GrpcContentServerStub() {
    impl = new JavaContent();
  }

  @Override
  public final ServerServiceDefinition bindService() {
    return ContentGrpc.bindService(this);
  }

  @Override
  public void createPost(CreatePostArgs request, StreamObserver<CreatePostResult> responseObserver) {
    Result<String> res = impl.createPost(DataModelAdaptor.GrpcContent_to_Content(request.getPost()),
        request.getPassword());
    if (!res.isOK())
      responseObserver.onError(errorCodeToStatus(res.error()));
    else {
      responseObserver.onNext(CreatePostResult.newBuilder().setPostId(res.value()).build());
      responseObserver.onCompleted();
    }
  }

  @Override
  public void getPosts(GetPostsArgs request, StreamObserver<GetPostsResult> responseObserver) {
    Result<List<String>> res = impl.getPosts(request.getTimestamp(), request.getSortOrder());
    if (!res.isOK())
      responseObserver.onError(errorCodeToStatus(res.error()));
    else {
      Iterable<String> iterable = () -> res.value().iterator();
      GetPostsResult result = GetPostsResult.newBuilder().addAllPostId(iterable).build();
      responseObserver.onNext(result);
      responseObserver.onCompleted();
    }
  }

  @Override
  public void getPost(GetPostArgs request, StreamObserver<GrpcPost> responseObserver) {
    Result<Post> res = impl.getPost(request.getPostId());
    if (!res.isOK())
      responseObserver.onError(errorCodeToStatus(res.error()));
    else {
      responseObserver.onNext(DataModelAdaptor.Post_toGrpcPost(res.value()));
      responseObserver.onCompleted();
    }
  }

  @Override
  public void getPostAnswers(GetPostAnswersArgs request, StreamObserver<GetPostsResult> responseObserver) {
    Result<List<String>> res = impl.getPostAnswers(request.getPostId(), request.getTimeout());
    if (!res.isOK())
      responseObserver.onError(errorCodeToStatus(res.error()));
    else {
      GetPostsResult.Builder resultBuilder = GetPostsResult.newBuilder();
      for (String answerPostId : res.value()) {
        resultBuilder.addPostId(answerPostId);
      }
      responseObserver.onNext(resultBuilder.build());
      responseObserver.onCompleted();
    }
  }

  @Override
  public void updatePost(UpdatePostArgs request, StreamObserver<GrpcPost> responseObserver) {
    Result<Post> res = impl.updatePost(request.getPostId(), request.getPassword(),
        DataModelAdaptor.GrpcContent_to_Content(request.getPost()));
    if (!res.isOK())
      responseObserver.onError(errorCodeToStatus(res.error()));
    else {
      responseObserver.onNext(DataModelAdaptor.Post_toGrpcPost(res.value()));
      responseObserver.onCompleted();
    }
  }

  @Override
  public void deletePost(DeletePostArgs request, StreamObserver<EmptyMessage> responseObserver) {
    Result<Void> res = impl.deletePost(request.getPostId(), request.getPassword());
    if (!res.isOK())
      responseObserver.onError(errorCodeToStatus(res.error()));
    else {
      responseObserver.onNext(EmptyMessage.newBuilder().build());
      responseObserver.onCompleted();
    }
  }

  @Override
  public void upVotePost(ChangeVoteArgs request, StreamObserver<EmptyMessage> responseObserver) {
    Result<Void> res = impl.upVotePost(request.getPostId(), request.getUserId(), request.getPassword());
    if (!res.isOK())
      responseObserver.onError(errorCodeToStatus(res.error()));
    else {
      responseObserver.onNext(EmptyMessage.newBuilder().build());
      responseObserver.onCompleted();
    }
  }

  @Override
  public void removeUpVotePost(ChangeVoteArgs request, StreamObserver<EmptyMessage> responseObserver) {
    Result<Void> res = impl.removeUpVotePost(request.getPostId(), request.getUserId(), request.getPassword());
    if (!res.isOK())
      responseObserver.onError(errorCodeToStatus(res.error()));
    else {
      responseObserver.onNext(EmptyMessage.newBuilder().build());
      responseObserver.onCompleted();
    }
  }

  @Override
  public void downVotePost(ChangeVoteArgs request, StreamObserver<EmptyMessage> responseObserver) {
    Result<Void> res = impl.downVotePost(request.getPostId(), request.getUserId(), request.getPassword());
    if (!res.isOK())
      responseObserver.onError(errorCodeToStatus(res.error()));
    else {
      responseObserver.onNext(EmptyMessage.newBuilder().build());
      responseObserver.onCompleted();
    }
  }

  @Override
  public void removeDownVotePost(ChangeVoteArgs request, StreamObserver<EmptyMessage> responseObserver) {
    Result<Void> res = impl.removeDownVotePost(request.getPostId(), request.getUserId(), request.getPassword());
    if (!res.isOK())
      responseObserver.onError(errorCodeToStatus(res.error()));
    else {
      responseObserver.onNext(EmptyMessage.newBuilder().build());
      responseObserver.onCompleted();
    }
  }

  @Override
  public void getUpVotes(GetPostArgs request, StreamObserver<VoteCountResult> responseObserver) {
    Result<Integer> res = impl.getupVotes(request.getPostId());
    if (!res.isOK())
      responseObserver.onError(errorCodeToStatus(res.error()));
    else {
      responseObserver.onNext(VoteCountResult.newBuilder().setCount(res.value()).build());
      responseObserver.onCompleted();
    }
  }

  @Override
  public void getDownVotes(GetPostArgs request, StreamObserver<VoteCountResult> responseObserver) {
    Result<Integer> res = impl.getDownVotes(request.getPostId());
    if (!res.isOK())
      responseObserver.onError(errorCodeToStatus(res.error()));
    else {
      responseObserver.onNext(VoteCountResult.newBuilder().setCount(res.value()).build());
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
