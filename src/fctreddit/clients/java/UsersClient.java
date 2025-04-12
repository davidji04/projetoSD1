package fctreddit.clients.java;

import java.util.List;

import fctreddit.api.User;
import fctreddit.api.java.Result;
import fctreddit.api.java.Users;

public abstract class UsersClient extends Client implements Users {

	
	abstract public Result<String> createUser(User user);

	abstract public Result<User> getUser(String userId, String password);
	
	abstract public Result<User> updateUser(String userId, String password, User user);

	abstract public Result<User> deleteUser(String userId, String password);

	abstract public Result<List<User>> searchUsers(String pattern);

}
