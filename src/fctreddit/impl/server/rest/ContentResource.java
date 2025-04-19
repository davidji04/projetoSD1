package fctreddit.impl.server.rest;

import fctreddit.api.Post;
import fctreddit.api.java.Content;
import fctreddit.api.java.Result;
import fctreddit.api.rest.RestContent;
import fctreddit.clients.java.ImagesClient;
import fctreddit.clients.java.UsersClient;
import fctreddit.clients.rest.RestImageClient;
import fctreddit.impl.server.java.JavaContent;
import jakarta.ws.rs.WebApplicationException;


import java.util.List;
import java.util.logging.Logger;

import static fctreddit.impl.server.rest.UsersResource.errorCodeToStatus;

public class ContentResource implements RestContent {

    private static final Logger Log = Logger.getLogger(ContentResource.class.getName());



    final Content impl;

    public ContentResource() {
        impl = new JavaContent();
    }

    private void checkResult(Result result) {
        if(!result.isOK())
            throw new WebApplicationException(errorCodeToStatus(result.error()));
    }

    @Override
    public String createPost(Post post, String userPassword) {
        Log.info("createPost : user = " + userPassword + "; post = " + post + "\n");

        Result<String> res = impl.createPost(post, userPassword);
        checkResult(res);
        return res.value();
    }

    @Override
    public List<String> getPosts(long timestamp, String sortOrder) {
        Log.info("getPosts : timestamp = " + timestamp + "; sortOrder = " + sortOrder + "\n");

        Result<List<String>> res = impl.getPosts(timestamp, sortOrder);
        checkResult(res);
        return res.value();
    }

    @Override
    public Post getPost(String postId) {
        Log.info("getPost : postId = " + postId + "\n");
        Result<Post> res = impl.getPost(postId);
        checkResult(res);
        return res.value();
    }

    @Override
    public List<String> getPostAnswers(String postId, long timeout) {
        Log.info("getPostAnswers : postId = " + postId + "\n");
        Result<List<String>> res = impl.getPostAnswers(postId, timeout);
        checkResult(res);
        return res.value();
    }

    @Override
    public Post updatePost(String postId, String userPassword, Post post) {
        Log.info("updatePost : postId = " + postId + "; post = " + post + "\n");
        Result<Post> res = impl.updatePost(postId, userPassword, post);
        checkResult(res);
        return res.value();
    }

    @Override
    public void deletePost(String postId, String userPassword) {
        Log.info("deletePost : postId = " + postId + "; userPassword = " + userPassword + "\n");
        Result<Void> res = impl.deletePost(postId, userPassword);
        checkResult(res);
    }

    @Override
    public void upVotePost(String postId, String userId, String userPassword) {
        Log.info("upvotePost : postId = " + postId + "; userId = " + userId + "; userPassword = " + userPassword + "\n");
        Result<Void> res = impl.upVotePost(postId, userId, userPassword);
        checkResult(res);
    }

    @Override
    public void removeUpVotePost(String postId, String userId, String userPassword) {
        Log.info("removeUpVotePost : postId = " + postId + "; userId = " + userId + "; userPassword = " + userPassword + "\n");
        Result<Void> res = impl.removeUpVotePost(postId, userId, userPassword);
        checkResult(res);
    }

    @Override
    public void downVotePost(String postId, String userId, String userPassword) {
        Log.info("downVotePost : postId = " + postId + "; userId = " + userId + "; userPassword = " + userPassword + "\n");
        Result<Void> res = impl.downVotePost(postId, userId, userPassword);
        checkResult(res);
    }

    @Override
    public void removeDownVotePost(String postId, String userId, String userPassword) {
        Log.info("removeDownVotePost : postId = " + postId + "; userId = " + userId + "; userPassword = " + userPassword + "\n");
        Result<Void> res = impl.removeDownVotePost(postId, userId, userPassword);
        checkResult(res);
    }

    @Override
    public Integer getupVotes(String postId) {
        Log.info("getupVotes : postId = " + postId + "\n");
        Result<Integer> res = impl.getupVotes(postId);
        checkResult(res);
        return res.value();
    }

    @Override
    public Integer getDownVotes(String postId) {
        Log.info("getDownVotes : postId = " + postId + "\n");
        Result<Integer> res = impl.getDownVotes(postId);
        checkResult(res);
        return res.value();
    }
}
