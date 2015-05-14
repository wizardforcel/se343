package bookstore.remote;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class SessionBeanFactory 
{
	public static UserSysRemote GetUserSysBean() 
	{
		try 
		{
			Context context = new InitialContext();
			return (UserSysRemote) context.lookup("UserSysBean");
		} 
		catch (NamingException e) 
		{
			e.printStackTrace();
			return null;
		} 
	}
	
	public static BookListRemote GetBookListBean()
	{
		try 
		{
			Context context = new InitialContext();
			return (BookListRemote) context.lookup("BookListBean");
		} 
		catch (NamingException e) 
		{
			e.printStackTrace();
			return null;
		} 
	}
	
	public static CartRemote GetCartListBean()
	{
		try 
		{
			Context context = new InitialContext();
			return (CartRemote) context.lookup("CartBean");
		} 
		catch (NamingException e) 
		{
			e.printStackTrace();
			return null;
		} 
	}
	
	public static OrderRemote GetOrderBean()
	{
		try 
		{
			Context context = new InitialContext();
			return (OrderRemote) context.lookup("OrderBean");
		} 
		catch (NamingException e) 
		{
			e.printStackTrace();
			return null;
		} 
	}
	
	public static AccountListRemote GetAccountListBean()
	{
		try 
		{
			Context context = new InitialContext();
			return (AccountListRemote) context.lookup("AccountListBean");
		} 
		catch (NamingException e) 
		{
			e.printStackTrace();
			return null;
		} 
	}
}
