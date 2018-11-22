import seng3400a2.Sessions;

/**
 *	Authorisation endpoint of identity
 *
 *	@author Zaw Moe Htat (c3193528@uon.edu.au)
 */
public class Authorisation {
	
	/**
	 *	Authorise users with session key.
	 *
	 *	@param key Session key of user
	 *
	 *	@return boolean True if key exists in sessions, false otherwise.
	 */
	public boolean authorise(String key) {
		return Sessions.hasKey(key);
	}
}