package fctreddit.impl.server.java;

import fctreddit.api.Post;
import fctreddit.api.User;
import fctreddit.api.java.Content;
import fctreddit.api.java.Image;
import fctreddit.api.java.Result;
import fctreddit.clients.java.ImagesClient;
import fctreddit.clients.java.UsersClient;
import fctreddit.impl.server.persistence.Hibernate;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class JavaContent implements Content {

    private static Logger Log = Logger.getLogger(JavaContent.class.getName());

    private final Hibernate hibernate;

    private UsersClient usersClient;

    private ImagesClient imageClient;

    public JavaContent(UsersClient usersClient, ImagesClient imageClient) {
        hibernate = Hibernate.getInstance();
        this.usersClient = usersClient;
        this.imageClient = imageClient;
    }

    private boolean isInvalid(String s) {
        return s == null || s.trim().isEmpty();
    }

    @Override
    public Result<String> createPost(Post post, String userPassword) {
        Log.info("Create Post" + post);

        if (isInvalid(post.getAuthorId()) || isInvalid(post.getContent()))
            return Result.error(Result.ErrorCode.BAD_REQUEST);

        Result<User> r = usersClient.getUser(post.getAuthorId(), userPassword);
        if (!r.isOK())
            return Result.error(r.error());
        // Generate postId
        String id = UUID.randomUUID().toString();
        long creationTime = System.currentTimeMillis();

        Post newPost = new Post(id, post.getAuthorId(), creationTime, post.getContent(), post.getMediaUrl(),
                post.getParentUrl(), post.getUpVote(), post.getDownVote());

        try {
            hibernate.persist(newPost);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(Result.ErrorCode.INTERNAL_ERROR);
        }

        return Result.ok(id);
    }

    @Override
    public Result<List<String>> getPosts(long timestamp, String sortOrder) {
        Log.info("Get Posts" + timestamp + " " + sortOrder);
        String query = "SELECT p.postId FROM Post p WHERE p.parentUrl IS NULL";
        if (timestamp != 0) {
            query += " AND p.creationTimestamp > " + timestamp;
        }
        if (sortOrder != null) {
            if (sortOrder.equals(MOST_UP_VOTES))
                query += " ORDER BY p.upVotes DESC";
            else if (sortOrder.equals(MOST_REPLIES))
                query += " ORDER BY p.replies DESC";
        } else
            query += " ORDER BY p.creationTimestamp";

        List<String> post = null;
        try {
            post = hibernate.jpql(query, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(Result.ErrorCode.INTERNAL_ERROR);
        }
        return Result.ok(post);
    }

    @Override
    public Result<Post> getPost(String postId) {
        Log.info("Get Post" + postId);
        if (isInvalid(postId)) {
            Log.info("Invalid Post ID: " + postId);
            return Result.error(Result.ErrorCode.BAD_REQUEST);
        }
        Post p = null;
        try {
            p = hibernate.get(Post.class, postId);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(Result.ErrorCode.INTERNAL_ERROR);
        }
        if (p == null) {
            Log.info("Post does not exist.\n");
            return Result.error(Result.ErrorCode.NOT_FOUND);
        }

        return Result.ok(p);
    }

    @Override
    public Result<List<String>> getPostAnswers(String postId, long maxTimeout) {
        Log.info("Get PostAnswers " + postId);

        List<String> post = null;
        try {
            Result<Post> result = getPost(postId);
            if (!result.isOK())
                return Result.error(result.error());
            post = hibernate.jpql(
                    "SELECT p.postId FROM Post p WHERE p.parentUrl = '" + postId + "' ORDER BY p.creationTimestamp",
                    String.class);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(Result.ErrorCode.INTERNAL_ERROR);
        }
        return Result.ok(post);
    }

    @Override
    public Result<Post> updatePost(String postId, String userPassword, Post post) {
        Log.info("Update Post" + postId);

        Result<Post> result = getPost(postId);
        if (!result.isOK())
            return Result.error(result.error());

        if (isInvalid(post.getAuthorId()))
            return Result.error(Result.ErrorCode.BAD_REQUEST);

        Result<User> r = usersClient.getUser(post.getAuthorId(), userPassword);
        if (!r.isOK())
            return Result.error(r.error());

        try {
            Post p = result.value();
            if (post.getMediaUrl() != null)
                p.setMediaUrl(post.getMediaUrl());
            if (post.getContent() != null)
                p.setContent(post.getContent());
            hibernate.update(p);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(Result.ErrorCode.INTERNAL_ERROR);
        }
        return null;
    }

    @Override
    public Result<Void> deletePost(String postId, String userPassword) {
        return null;
    }

    @Override
    public Result<Void> upVotePost(String postId, String userId, String userPassword) {
        return null;
    }

    @Override
    public Result<Void> removeUpVotePost(String postId, String userId, String userPassword) {
        return null;
    }

    @Override
    public Result<Void> downVotePost(String postId, String userId, String userPassword) {
        return null;
    }

    @Override
    public Result<Void> removeDownVotePost(String postId, String userId, String userPassword) {
        return null;
    }

    @Override
    public Result<Integer> getupVotes(String postId) {
        return null;
    }

    // Not finished
    @Override
    public Result<Integer> getDownVotes(String postId) {
        Log.info("Get upVotes " + postId);
        Result<Post> result = getPost(postId);
        if (!result.isOK())
            return Result.error(result.error());

        try {

        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(Result.ErrorCode.INTERNAL_ERROR);
        }
        return null;
    }
}
