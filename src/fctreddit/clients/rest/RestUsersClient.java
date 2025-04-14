package fctreddit.clients.rest;

import java.net.URI;
import java.util.List;
import java.util.logging.Logger;

import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.GenericType;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;

import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import fctreddit.api.User;
import fctreddit.api.java.Result;
import fctreddit.api.java.Result.ErrorCode;
import fctreddit.api.rest.RestUsers;
import fctreddit.clients.java.UsersClient;

public class RestUsersClient extends UsersClient {
	private static Logger Log = Logger.getLogger(RestUsersClient.class.getName());
	
	final URI serverURI;
	final Client client;
	final ClientConfig config;

	final WebTarget target;
	
	public RestUsersClient( URI serverURI ) {
		this.serverURI = serverURI;

		this.config = new ClientConfig();
		
		config.property( ClientProperties.READ_TIMEOUT, READ_TIMEOUT);
		config.property( ClientProperties.CONNECT_TIMEOUT, CONNECT_TIMEOUT);

		
		this.client = ClientBuilder.newClient(config);

		target = client.target( serverURI ).path( RestUsers.PATH );
	}
		
	public Result<String> createUser(User user) {
		Response r = executeOperationPost(target.request().accept(MediaType.APPLICATION_JSON), Entity.entity(user, MediaType.APPLICATION_JSON));

		if (r == null)
			return Result.error(ErrorCode.TIMEOUT);

		int status = r.getStatus();
		if (status != Status.OK.getStatusCode())
			return Result.error(getErrorCodeFrom(status));
		else
			return Result.ok(r.readEntity(String.class));
	}

	public Result<User> getUser(String userId, String pwd) {
		Response r = executeOperationGet(target.path( userId )
				.queryParam(RestUsers.PASSWORD, pwd).request()
				.accept(MediaType.APPLICATION_JSON));

		if (r == null)
			return Result.error(ErrorCode.TIMEOUT);

		int status = r.getStatus();
		if (status != Status.OK.getStatusCode())
			return Result.error(getErrorCodeFrom(status));
		else
			return Result.ok(r.readEntity(User.class));
	}

	public Result<User> updateUser(String userId, String password, User user) {
		Response r = executeOperationPut(target.path(userId).queryParam(RestUsers.PASSWORD, password).request().accept(MediaType.APPLICATION_JSON)
				,Entity.entity(user, MediaType.APPLICATION_JSON));


		if (r == null)
			return Result.error(ErrorCode.TIMEOUT);


		int status = r.getStatus();
		if (status != Status.OK.getStatusCode())
			return Result.error(getErrorCodeFrom(status));
		else
			return Result.ok(r.readEntity(User.class));

	}

	public Result<User> deleteUser(String userId, String password) {
		Response r = executeOperationDelete(target.path(userId).queryParam(RestUsers.PASSWORD, password).request().accept(MediaType.APPLICATION_JSON));

		if(r == null)
			return Result.error(ErrorCode.TIMEOUT);

		int status = r.getStatus();
		if (status != Status.OK.getStatusCode())
			return Result.error(getErrorCodeFrom(status));
		else
			return Result.ok(r.readEntity(User.class));
	}



	public Result<List<User>> searchUsers(String pattern) {

		Response r = executeOperationGet(target.path("/").queryParam( RestUsers.QUERY, pattern).request()
				.accept(MediaType.APPLICATION_JSON));

		if (r == null)
			return Result.error(ErrorCode.TIMEOUT);

		int status = r.getStatus();
		if (status != Status.OK.getStatusCode())
			return Result.error(getErrorCodeFrom(status));
		else
			return Result.ok(r.readEntity(new GenericType<>() {
			}));
	}


}
