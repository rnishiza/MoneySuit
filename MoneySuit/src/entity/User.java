package entity;

public class User {
	
	public static final String USER = "LOGIN";
	public static final String ID = "ID";
	
	private int id;
	private String user;
	
	public User(int id, String user) {
		this.id = id;
		this.user = user;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
}
