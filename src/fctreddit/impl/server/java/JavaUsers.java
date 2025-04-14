package fctreddit.impl.server.java;

import java.util.List;
import java.util.logging.Logger;

import fctreddit.api.User;
import fctreddit.api.java.Result;
import fctreddit.api.java.Result.ErrorCode;
import fctreddit.api.java.Users;
import fctreddit.impl.server.persistence.Hibernate;

public class JavaUsers implements Users {

	private static Logger Log = Logger.getLogger(JavaUsers.class.getName());

	private Hibernate hibernate;
	
	public JavaUsers() {
		hibernate = Hibernate.getInstance();
	}
	
	@Override
	public Result<String> createUser(User user) {
		Log.info("createUser : " + user + "\n");

		// Check if user data is valid
		if ( isInvalid(user.getUserId()) || isInvalid(user.getPassword()) || isInvalid(user.getFullName())
				|| isInvalid(user.getEmail())) {
			Log.info("User object invalid.\n");
			return Result.error(ErrorCode.BAD_REQUEST);
		}

		try {
			if(hibernate.get(User.class, user.getUserId()) != null) {
				Log.info("User already exists.\n");
				return Result.error(ErrorCode.CONFLICT);
			}
			hibernate.persist(user);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error(ErrorCode.INTERNAL_ERROR);

		}
		
		return Result.ok(user.getUserId());
	}

	@Override
	public Result<User> getUser(String userId, String password) {
		Log.info("getUser : user = " + userId + "; pwd = " + password+ "\n");

		// Check if user is valid
		if ( isInvalid(userId)) {
			Log.info("UserId null.\n");
			return Result.error(ErrorCode.BAD_REQUEST);
		}
		
		User user = null;
		try {
			user = hibernate.get(User.class, userId);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error(ErrorCode.INTERNAL_ERROR);
		}

		// Check if user exists
		if (user == null) {
			Log.info("User does not exist.\n");
			return Result.error(ErrorCode.NOT_FOUND);
		}

		// Check if the password is correct
		if (!user.getPassword().equals(password)) {
			Log.info("Password is incorrect.\n");
			return Result.error(ErrorCode.FORBIDDEN);
		}
		
		return Result.ok(user);

	}

	private boolean isInvalid(String s){
		return  s == null || s.trim().isEmpty() ;
	}

	@Override
	public Result<User> updateUser(String userId, String password, User user) {
	Log.info("updateUser : user = " + userId + "; pwd = " + password + "User: "+ user +"\n");

		// Check if user is valid
//		if (isInvalid(userId)) {
//			Log.info("UserId null.\n");
//			return Result.error(ErrorCode.BAD_REQUEST);
//		}
		try{

			Result<User> res = this.getUser(userId, password);
			if(!res.isOK())
				return Result.error(res.error());
			//User u = hibernate.get(User.class, userId);
//			if(u == null) {
//				Log.info("User does not exist.\n");
//				return Result.error(ErrorCode.NOT_FOUND);
//			}
//
//			if (!password.equals(u.getPassword())) {
//				Log.info("Password is incorrect.\n");
//				return Result.error(ErrorCode.FORBIDDEN);
//			}
			User u = res.value();
			if (user.getFullName() != null)
				u.setFullName(user.getFullName());

			if (user.getEmail() != null)
				u.setEmail(user.getEmail());

			if (user.getPassword() != null)
				u.setPassword(user.getPassword());

			if (user.getAvatarUrl() != null)
				u.setAvatarUrl(user.getAvatarUrl());
			user = u;
			hibernate.update(u);
		}catch(Exception e) {
			e.printStackTrace();
			return Result.error(ErrorCode.INTERNAL_ERROR);
		}

	return Result.ok(user);
	}

	@Override
	public Result<User> deleteUser(String userId, String password) {
		Log.info("deleteUser : user = " + userId + "; pwd = " + password + "\n");
		// Check if user is valid
//		if (isInvalid(userId)) {
//			Log.info("UserId or password null.\n");
//			return Result.error(ErrorCode.BAD_REQUEST);
//		}
		User u;
		try{
			Result<User> res = this.getUser(userId, password);
			if(!res.isOK())
				return Result.error(res.error());
			u = res.value();
//			if(u == null) {
//				Log.info("User does not exist.\n");
//				return Result.error(ErrorCode.NOT_FOUND);
//			}
//
//			if (!password.equals(u.getPassword())) {
//				Log.info("Password is incorrect.\n");
//				return Result.error(ErrorCode.FORBIDDEN);
//			}

			hibernate.delete(u);
		}catch(Exception e) {
			e.printStackTrace();
			return Result.error(ErrorCode.INTERNAL_ERROR);
		}
		return Result.ok(u);
	}


	@Override
	public Result<List<User>> searchUsers(String pattern) {
		Log.info("searchUsers : pattern = " + pattern + "\n");

		List<User> users = null;
		try{
			users = hibernate.jpql("SELECT u FROM User u WHERE u.userId LIKE '%" + pattern +"%'", User.class);
		}catch(Exception e){
			e.printStackTrace();
			return Result.error(ErrorCode.INTERNAL_ERROR);
		}
		return Result.ok(users);
	}

}
