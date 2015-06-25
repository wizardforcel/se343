package bookstore.jaas;

import javax.security.auth.callback.*;

public class ResultCallback implements Callback  
{
	private int errno;
	private String errmsg;
	private int id;
	
	public int getErrno() { return errno; }
	public void setErrno(int errno) { this.errno = errno; }
	
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	
	public String getErrmsg() { return errmsg; }
	public void setErrmsg(String errmsg) { this.errmsg = errmsg; }
	
}
