package bookstore.entitybean;

import javax.servlet.http.*;

import bookstore.utility.AuthCode;

public class UserBean
{
	private String uid = "";
	private String un = "";
	private String pw = "";
	
	
	public void setCookie(HttpServletResponse response)
	{
		String token = uid + "|" + un + "|" + pw;
		token = AuthCode.Encode(token, "");
	    Cookie co = new Cookie("token", token);
	    co.setMaxAge(3600 * 24 * 30);
	    response.addCookie(co);	
	}
	
	public void getCookie(HttpServletRequest request)
	{
	    Cookie[] cookies = request.getCookies();
	    Cookie co = findCookie(cookies, "token");
	    if(co == null) return;
	    String token = co.getValue();
	    token = AuthCode.Decode(token, "");
	    if(token.length() == 0) return;
	    String[] arr = token.split("\\|");
	    if(arr.length < 3) return;
	    uid = arr[0];
	    un = arr[1];
	    pw = arr[2];
	}
	
	private static Cookie findCookie(Cookie[] cookies, String name)
	{
		if(cookies == null) return null;
		for(Cookie co : cookies)
	    {
	    	if(co.getName().compareTo(name) == 0)
	    	  return co;
	    }
		return null;
	}
	
	public String getId()
	{
		return uid;
	}
	
	public String getUn()
	{
		return un;
	}
	
	public String getPw()
	{
		return pw;
	}
	
	public void setUInfo(String _uid, String _un, String _pw)
	{
		uid = _uid;
		un = _un;
		pw = _pw;
	}
	
	public boolean  isValid()
	{
		return !uid.equals("") && !un.equals("") && !pw.equals("");
	}
	
}