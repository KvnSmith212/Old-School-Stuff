package edu.cs2340.supercoders.financialtracker.model;

import java.util.HashMap;

/**
 * The Class LoginData.
 * 
 * This class is used to store our list of users. In our application, this class
 * is instantiated within Welcome.java and can be called from any other activity
 * by getting it from there.
 */
public class LoginData {

	/** The registry. */
	private HashMap<String, User> registry;

	/** The current. */
	private User current;
	private HashMap<String, String> userNameRecovery;
	private HashMap<String, String> passwordRecovery;

	/**
	 * Not only am I setting up registry here, but I'm also setting up a default
	 * user named "admin" who can log on and already has a saving account set up
	 * for demonstration/debugging purposes.
	 */
	public LoginData() {
		registry = new HashMap<String, User>();
		userNameRecovery = new HashMap<String, String>();
		passwordRecovery = new HashMap<String, String>();

		User admin = new User("a", "p", "a@email.com", "Kevin", "Smith");
		registry.put(admin.getUserName(), admin);
		userNameRecovery.put(admin.getEmail(), admin.getUserName());
		passwordRecovery.put(admin.getUserName(), admin.getEmail());

		Account acc = new Account("Admin", "Savings", 00.00);
		admin.addAccount(acc);
	}

	/**
	 * Returns True if the username and password are correct, and false
	 * otherwise.
	 * 
	 * @param user
	 *            the user
	 * @param pass
	 *            the pass
	 * @return true, if successful
	 */
	public boolean checkUser(String user, String pass) {
		try {
			return registry.get(user).getPass().equals(pass);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Gets the user.
	 * 
	 * @param user
	 *            the user
	 * @return the user
	 */
	public User getUser(String user) {
		return registry.get(user);
	}

	/**
	 * Gets the current.
	 * 
	 * @return the current
	 */
	public User getCurrent() {
		return current;
	}

	/**
	 * Sets the current.
	 * 
	 * @param curr
	 *            the new current
	 */
	public void setCurrent(User curr) {
		current = curr;
	}

	/**
	 * Checks whether the desired username is available or not.
	 * 
	 * @param user
	 *            the user
	 * @return true, if successful
	 */
	public boolean checkAvailability(String user) {
		if (registry.get(user) == null)
			return true;
		else
			return false;
	}
	public String recoverUserName(String email) {
		if (userNameRecovery.containsKey(email)) {
			String userName = userNameRecovery.get(email);
			return userName;
		} else {
			return null;
		}
	}
	public String recoverPassword(String userName) {
		if (passwordRecovery.containsKey(userName)) {
			String email = passwordRecovery.get(userName);
			return email;
		} else {
			return null;
		}
	}
	/**
	 * Adds the new user.
	 * 
	 * @param u
	 *            the u
	 */
	public void addNewUser(User u) {
		registry.put(u.getUserName(), u);
	}
}
