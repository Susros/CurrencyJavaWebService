import seng3400a2.Sessions;

/**
 *	Login endpoint of identity
 *
 *	@author Zaw Moe Htat (c3193528@uon.edu.au)
 */
public class Login {
	
	/**
	 *	Check username and password.
	 *
	 *	@param username Username of user
	 *	@param password Password of user
	 *
	 *	@return String "INVALID" if username and password do not match,
	 *					session key if valid
	 */
	public String login(String username, String password) {
		
		// List of users and passwords
		String user[] = {"hayden", "josh"};
		String pass[] = {"1234", "4321"};
		
		// Check username and password
		for(int i = 0; i < user.length; i++) {
			if (user[i].equals(username)) {			// Username match
				if (pass[i].equals(password)) {		// Password match
					
					// Generate session key with 5 characters long
					String sessionKey = Sessions.uniqueKey(5);
					
					// Check if user already logged in
					if (Sessions.hasUser(username)) {

						// Remove this entry to override new session key
						Sessions.remove(Sessions.getKey(username));
					}
					
					// Remember session key and username
					Sessions.add(sessionKey, username);
					
					return sessionKey;
				}
				
				break;
			}
		}
		
		return "INVALID";
	}
	
	/**
	 *	Logout user
	 *
	 *	@param key Session key of user
	 *
	 *	@return boolean True if logout successful, false otherwise
	 */
	public boolean logout(String key) {
		
		// Check if session key exists
		if (Sessions.hasKey(key)) {
			Sessions.remove(key);
			return true;
		}
		
		return false;
	}
}