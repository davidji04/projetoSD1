package fctreddit.impl.server.java;

import fctreddit.api.User;
import fctreddit.api.java.Result;
import fctreddit.api.rest.RestUsers;
import fctreddit.clients.java.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;

public class UserAuthenticationService extends Client {



    private WebTarget usersTarget;

    public UserAuthenticationService(URI usersServer) {
        this.usersTarget = ClientBuilder.newClient().target(usersServer);

    }



    public Result<User> authenticateUser(String userId, String userPassword){
        Response r = executeOperationGet(usersTarget.path(userId).queryParam(RestUsers.PASSWORD, userPassword).request().accept(MediaType.APPLICATION_JSON));

        if (r == null)
            return Result.error(Result.ErrorCode.TIMEOUT);

        int status = r.getStatus();
        if (status != Response.Status.OK.getStatusCode())
            return Result.error(getErrorCodeFrom(status));
        else
            return Result.ok(r.readEntity(User.class));
    }
}
