package bookstore.jaas;

import javax.security.auth.callback.*;

public class UserCallback implements Callback 
{
	private String name;
	
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	
	private String password;
	
	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }
}
