package fctreddit.clients.rest;

import fctreddit.api.Post;
import fctreddit.api.java.Result;
import fctreddit.api.rest.RestContent;
import fctreddit.api.rest.RestUsers;
import fctreddit.clients.java.ContentClient;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;

import java.net.URI;
import java.util.List;

public class RestContentClient extends ContentClient {

    final URI serverURI;
    final Client client;
    final ClientConfig config;

    final WebTarget target;

    public RestContentClient(URI serverURI) {
        this.serverURI = serverURI;

        this.config = new ClientConfig();

        config.property(ClientProperties.READ_TIMEOUT, READ_TIMEOUT);
        config.property(ClientProperties.CONNECT_TIMEOUT, CONNECT_TIMEOUT);

        this.client = ClientBuilder.newClient(config);

        target = client.target(serverURI).path(RestUsers.PATH);
    }

    @Override
    public Result<String> createPost(Post post, String userPassword) {
        Response r = executeOperationPost(
                target.queryParam(RestContent.PASSWORD, userPassword).request().accept(MediaType.APPLICATION_JSON),
                Entity.entity(post, MediaType.APPLICATION_JSON));
        if (r == null)
            return Result.error(Result.ErrorCode.TIMEOUT);

        int status = r.getStatus();
        if (status != Response.Status.OK.getStatusCode())
            return Result.error(getErrorCodeFrom(status));
        else
            return Result.ok(r.readEntity(String.class));
    }

    @Override
    public Result<List<String>> getPosts(long timestamp, String sortOrder) {
        Response r = executeOperationGet(target.path(RestContent.PATH).queryParam(RestContent.TIMESTAMP, timestamp)
                .queryParam(RestContent.SORTBY, sortOrder)
                .request().accept(MediaType.APPLICATION_JSON));
        if (r == null)
            return Result.error(Result.ErrorCode.TIMEOUT);

        int status = r.getStatus();
        if (status != Response.Status.OK.getStatusCode())
            return Result.error(getErrorCodeFrom(status));
        else
            return Result.ok(r.readEntity(new GenericType<>() {
            }));
    }

    @Override
    public Result<Post> getPost(String postId) {
        Response r = executeOperationGet(target.path(postId).request().accept(MediaType.APPLICATION_JSON));

        if (r == null)
            return Result.error(Result.ErrorCode.TIMEOUT);

        int status = r.getStatus();
        if (status != Response.Status.OK.getStatusCode())
            return Result.error(getErrorCodeFrom(status));
        else
            return Result.ok(r.readEntity(Post.class));
    }

    @Override
    public Result<List<String>> getPostAnswers(String postId, long maxTimeout) {
        Response r = executeOperationGet(target.path(postId).queryParam(RestContent.TIMEOUT, maxTimeout)
                .request().accept(MediaType.APPLICATION_JSON));

        if (r == null)
            return Result.error(Result.ErrorCode.TIMEOUT);

        int status = r.getStatus();
        if (status != Response.Status.OK.getStatusCode())
            return Result.error(getErrorCodeFrom(status));
        else
            return Result.ok(r.readEntity(new GenericType<>() {
            }));
    }

    @Override
    public Result<Post> updatePost(String postId, String userPassword, Post post) {
        Response r = executeOperationPut(target.path(postId).queryParam(RestContent.PASSWORD, userPassword)
                .request().accept(MediaType.APPLICATION_JSON), Entity.entity(post, MediaType.APPLICATION_JSON));

        if (r == null)
            return Result.error(Result.ErrorCode.TIMEOUT);

        int status = r.getStatus();
        if (status != Response.Status.OK.getStatusCode())
            return Result.error(getErrorCodeFrom(status));
        else
            return Result.ok(r.readEntity(Post.class));
    }

    @Override
    public Result<Void> deletePost(String postId, String userPassword) {
        Response r = executeOperationDelete(target.path(postId).queryParam(RestContent.PASSWORD, userPassword)
                .request().accept(MediaType.APPLICATION_JSON));

        if (r == null)
            return Result.error(Result.ErrorCode.TIMEOUT);

        int status = r.getStatus();
        if (status != Response.Status.OK.getStatusCode())
            return Result.error(getErrorCodeFrom(status));
        else
            return Result.ok();
    }

    @Override
    public Result<Void> upVotePost(String postId, String userId, String userPassword) {
        return addVotePost(postId, userId, userPassword);
    }

    @Override
    public Result<Void> removeUpVotePost(String postId, String userId, String userPassword) {
        return removeVotePost(postId, userId, userPassword);
    }

    @Override
    public Result<Void> downVotePost(String postId, String userId, String userPassword) {
        return addVotePost(postId, userId, userPassword);
    }

    private Result<Void> addVotePost(String postId, String userId, String userPassword) {
        Post p = new Post();
        Response r = executeOperationPost(
                target.path(postId).path(userId).queryParam(RestContent.PASSWORD, userPassword)
                        .request().accept(MediaType.APPLICATION_JSON),
                Entity.entity(p, MediaType.APPLICATION_JSON));

        if (r == null)
            return Result.error(Result.ErrorCode.TIMEOUT);

        int status = r.getStatus();
        if (status != Response.Status.OK.getStatusCode())
            return Result.error(getErrorCodeFrom(status));
        else
            return Result.ok();
    }

    @Override
    public Result<Void> removeDownVotePost(String postId, String userId, String userPassword) {
        return removeVotePost(postId, userId, userPassword);
    }

    private Result<Void> removeVotePost(String postId, String userId, String userPassword) {
        Response r = executeOperationDelete(
                target.path(postId).path(userId).queryParam(RestContent.PASSWORD, userPassword)
                        .request().accept(MediaType.APPLICATION_JSON));

        if (r == null)
            return Result.error(Result.ErrorCode.TIMEOUT);

        int status = r.getStatus();
        if (status != Response.Status.OK.getStatusCode())
            return Result.error(getErrorCodeFrom(status));
        else
            return Result.ok();
    }

    @Override
    public Result<Integer> getupVotes(String postId) {
        return getVotes(postId);
    }

    @Override
    public Result<Integer> getDownVotes(String postId) {
        return getVotes(postId);
    }

    private Result<Integer> getVotes(String postId) {
        Response r = executeOperationGet(target.path(postId).request().accept(MediaType.APPLICATION_JSON));

        if (r == null)
            return Result.error(Result.ErrorCode.TIMEOUT);

        int status = r.getStatus();
        if (status != Response.Status.OK.getStatusCode())
            return Result.error(getErrorCodeFrom(status));
        else
            return Result.ok(r.readEntity(Integer.class));
    }
}
