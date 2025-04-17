package fctreddit.impl.server.grpc;

import fctreddit.api.java.Content;
import fctreddit.clients.java.ImagesClient;
import fctreddit.clients.java.UsersClient;
import fctreddit.impl.server.grpc.generated_java.ContentGrpc;
import fctreddit.impl.server.java.JavaContent;
import io.grpc.BindableService;
import io.grpc.ServerServiceDefinition;

public class GrpcContentServerStub implements ContentGrpc.AsyncService, BindableService {

  final Content impl;

  public GrpcContentServerStub(ImagesClient imagesClient, UsersClient userClient) {
    impl = new JavaContent(userClient, imagesClient);
  }

  @Override
  public final ServerServiceDefinition bindService() {
    return ContentGrpc.bindService(this);
  }

}
