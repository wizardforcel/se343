package bookstore.jaas;

import java.io.IOException;

import javax.security.auth.callback.*;

public class SimpleCallbackHandler implements CallbackHandler
{

	private String name;
	private String password;
	private int errno;
	private String errmsg;
	private int id;
	
	public int getErrno() { return errno; }
	public void setErrno(int errno) { this.errno = errno; }
	
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	
	public String getErrmsg() { return errmsg; }
	public void setErrmsg(String errmsg) { this.errmsg = errmsg; }
	
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	
	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }
	
	public SimpleCallbackHandler(String name, String password)
	{
		this.name = name;
		this.password = password;
	}
	
	@Override
	public void handle(Callback[] callbacks) throws IOException,
			UnsupportedCallbackException {
		// TODO Auto-generated method stub
		for(Callback cb : callbacks)
		{
			if(cb instanceof UserCallback)
			{
				((UserCallback)cb).setName(name);
				((UserCallback)cb).setPassword(password);
			}
			else if (cb instanceof ResultCallback)
			{
				errno = ((ResultCallback)cb).getErrno();
				errmsg = ((ResultCallback)cb).getErrmsg();
				id =  ((ResultCallback)cb).getId();
			}
		}
	}
}
