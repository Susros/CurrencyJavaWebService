package seng3400a2;

import java.util.*;

/**
 *	Sessions
 *
 *	This is static class that stores all session keys and
 *	associated username
 *
 *	@author Zaw Moe Htat (c3193528@uon.edu.au)
 *
 *	@package seng3400a2
 */
public final class Sessions {
	
	/**
	 *	Hash table to store user sessions
	 */
	public static Map<String, String> userSessions = new HashMap<String, String>();
	
	/**
	 *	add session key with username
	 *
	 *	@param session  Session key of user
	 *	@param username Username of user
	 */
	public static void add(String session, String username) {
		Sessions.userSessions.put(session, username);
	}
	
	/**
	 *	Get username by session key
	 *
	 *	@param session Session key of user
	 *
	 *	@return String Username of user
	 */
	public static String getUsername(String session) {
		return Sessions.userSessions.get(session);
	}
	
	/**
	 *	Get session key by username
	 *
	 *	@param username Username of user
	 *
	 *	@return String Session key of user
	 */
	public static String getKey(String username) {
		Iterator l = Sessions.userSessions.entrySet().iterator();
		
		while(l.hasNext()) {
			Map.Entry m = (Map.Entry) l.next();
			
			if (m.getValue().equals(username)) {
				return (String) m.getKey();
			}
		}
		
		return null;
	}
	
	/**
	 *	Check if session key exists
	 *
	 *	@param session Session key of user
	 *
	 *	@return boolean True if session key exists, false otherwise.
	 */
	public static boolean hasKey(String session) {
		return Sessions.userSessions.containsKey(session);
	}
	
	/**
	 *	Check if user has been associated with session key
	 *
	 *	@param username Username of user
	 *
	 *	@return boolean True if user has been associated with session key, false otherwise
	 */
	public static boolean hasUser(String username) {
		return Sessions.userSessions.containsValue(username);
	}
	
	/**
	 *	Check if session is empty
	 *
	 *	@return boolean True if it is, false otherwise
	 */
	public static boolean isEmpty() {
		return Sessions.userSessions.isEmpty();
	}
	
	/**
	 *	Remove session key
	 *
	 *	@param session Session key of user
	 */
	public static void remove(String session) {
		Sessions.userSessions.remove(session);
	}
	
	/**
	 *	Get the size of sessions
	 *	
	 *	@return int Size of sessions
	 */
	public static int size() {
		return Sessions.userSessions.size();
	}
	
	/**
	 *	Generate unique key
	 *
	 *	@param length Length of key
	 *
	 *	@return String Unique key
	 */
	public static String uniqueKey(int length) {
		// Random characters to generate session key
		String chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

		// To store session keys
		String key = "";

		// To randomise characters
		Random r = new Random();

		// Generate [length] long key
		for(int j = 0; j < length; j++) {
			key += chars.charAt(r.nextInt(chars.length()));
		}
		
		return key;
	}
}