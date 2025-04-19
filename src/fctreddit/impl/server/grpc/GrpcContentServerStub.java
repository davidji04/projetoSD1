package fctreddit.impl.server.grpc;

import fctreddit.api.java.Content;
import fctreddit.api.java.Result;
import fctreddit.clients.java.ImagesClient;
import fctreddit.clients.java.UsersClient;
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
import fctreddit.impl.server.grpc.generated_java.UsersProtoBuf.CreateUserArgs;
import fctreddit.impl.server.grpc.generated_java.UsersProtoBuf.CreateUserResult;
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

//  @Override
//  public void createPost(CreatePostArgs request, StreamObserver<CreatePostResult> responseObserver) {
//    Result<String> res = impl.createPost(DataModelAdaptor.GrpcUser_to_User(request.getUser()));
//    if (!res.isOK())
//      responseObserver.onError(errorCodeToStatus(res.error()));
//    else {
//      responseObserver.onNext(CreateUserResult.newBuilder().setUserId(res.value()).build());
//      responseObserver.onCompleted();
//    }
//  }
//
//  @Override
//  public void getPosts(GetPostsArgs request, StreamObserver<GetPostsResult> responseObserver) {
//    Result<String> res = impl.createUser(DataModelAdaptor.GrpcUser_to_User(request.getUser()));
//    if (!res.isOK())
//      responseObserver.onError(errorCodeToStatus(res.error()));
//    else {
//      responseObserver.onNext(CreateUserResult.newBuilder().setUserId(res.value()).build());
//      responseObserver.onCompleted();
//    }
//  }
//
//  @Override
//  public void getPost(GetPostArgs request, StreamObserver<GrpcPost> responseObserver) {
//    Result<String> res = impl.createUser(DataModelAdaptor.GrpcUser_to_User(request.getUser()));
//    if (!res.isOK())
//      responseObserver.onError(errorCodeToStatus(res.error()));
//    else {
//      responseObserver.onNext(CreateUserResult.newBuilder().setUserId(res.value()).build());
//      responseObserver.onCompleted();
//    }
//  }
//
//  @Override
//  public void getPostAnswers(GetPostAnswersArgs request, StreamObserver<GetPostsResult> responseObserver) {
//    Result<String> res = impl.createUser(DataModelAdaptor.GrpcUser_to_User(request.getUser()));
//    if (!res.isOK())
//      responseObserver.onError(errorCodeToStatus(res.error()));
//    else {
//      responseObserver.onNext(CreateUserResult.newBuilder().setUserId(res.value()).build());
//      responseObserver.onCompleted();
//    }
//  }
//
//  @Override
//  public void updatePost(UpdatePostArgs request, StreamObserver<GrpcPost> responseObserver) {
//    Result<String> res = impl.createUser(DataModelAdaptor.GrpcUser_to_User(request.getUser()));
//    if (!res.isOK())
//      responseObserver.onError(errorCodeToStatus(res.error()));
//    else {
//      responseObserver.onNext(CreateUserResult.newBuilder().setUserId(res.value()).build());
//      responseObserver.onCompleted();
//    }
//  }
//
//  @Override
//  public void deletePost(DeletePostArgs request, StreamObserver<EmptyMessage> responseObserver) {
//    Result<String> res = impl.createUser(DataModelAdaptor.GrpcUser_to_User(request.getUser()));
//    if (!res.isOK())
//      responseObserver.onError(errorCodeToStatus(res.error()));
//    else {
//      responseObserver.onNext(CreateUserResult.newBuilder().setUserId(res.value()).build());
//      responseObserver.onCompleted();
//    }
//  }
//
//  @Override
//  public void upVotePost(ChangeVoteArgs request, StreamObserver<EmptyMessage> responseObserver) {
//    Result<String> res = impl.createUser(DataModelAdaptor.GrpcUser_to_User(request.getUser()));
//    if (!res.isOK())
//      responseObserver.onError(errorCodeToStatus(res.error()));
//    else {
//      responseObserver.onNext(CreateUserResult.newBuilder().setUserId(res.value()).build());
//      responseObserver.onCompleted();
//    }
//  }
//
//  @Override
//  public void removeUpVotePost(ChangeVoteArgs request, StreamObserver<EmptyMessage> responseObserver) {
//    Result<String> res = impl.createUser(DataModelAdaptor.GrpcUser_to_User(request.getUser()));
//    if (!res.isOK())
//      responseObserver.onError(errorCodeToStatus(res.error()));
//    else {
//      responseObserver.onNext(CreateUserResult.newBuilder().setUserId(res.value()).build());
//      responseObserver.onCompleted();
//    }
//  }
//
//  @Override
//  public void downVotePost(ChangeVoteArgs request, StreamObserver<EmptyMessage> responseObserver) {
//    Result<String> res = impl.createUser(DataModelAdaptor.GrpcUser_to_User(request.getUser()));
//    if (!res.isOK())
//      responseObserver.onError(errorCodeToStatus(res.error()));
//    else {
//      responseObserver.onNext(CreateUserResult.newBuilder().setUserId(res.value()).build());
//      responseObserver.onCompleted();
//    }
//  }
//
//  @Override
//  public void removeDownVotePost(ChangeVoteArgs request, StreamObserver<EmptyMessage> responseObserver) {
//    Result<String> res = impl.createUser(DataModelAdaptor.GrpcUser_to_User(request.getUser()));
//    if (!res.isOK())
//      responseObserver.onError(errorCodeToStatus(res.error()));
//    else {
//      responseObserver.onNext(CreateUserResult.newBuilder().setUserId(res.value()).build());
//      responseObserver.onCompleted();
//    }
//  }
//
//  @Override
//  public void getupVotes(GetPostArgs request, StreamObserver<VoteCountResult> responseObserver) {
//    Result<String> res = impl.createUser(DataModelAdaptor.GrpcUser_to_User(request.getUser()));
//    if (!res.isOK())
//      responseObserver.onError(errorCodeToStatus(res.error()));
//    else {
//      responseObserver.onNext(CreateUserResult.newBuilder().setUserId(res.value()).build());
//      responseObserver.onCompleted();
//    }
//  }
//
//  @Override
//  public void getDownVotes(GetPostArgs request, StreamObserver<VoteCountResult> responseObserver) {
//    Result<String> res = impl.createUser(DataModelAdaptor.GrpcUser_to_User(request.getUser()));
//    if (!res.isOK())
//      responseObserver.onError(errorCodeToStatus(res.error()));
//    else {
//      responseObserver.onNext(CreateUserResult.newBuilder().setUserId(res.value()).build());
//      responseObserver.onCompleted();
//    }
//  }

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
