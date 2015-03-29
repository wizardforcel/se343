package bookstore.sessionbean;

import javax.ejb.*;

import org.json.simple.JSONObject;

import bookstore.entitybean.CartItemBean;
import bookstore.remote.CartRemote;
import bookstore.remote.ResultInfo;
import bookstore.utility.DBConn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Stateful
public class CartBean implements CartRemote
{
	private List<CartItemBean> cart;
	
	public CartBean()
	{
		cart = new ArrayList<CartItemBean>();
	}
	
	@Override
	public ResultInfo add(String name, int count)
	{
		try
		{
			Connection conn = DBConn.getDbConn();
			
			String sql = "SELECT * FROM Books WHERE b_name=?";
		    PreparedStatement stmt = conn.prepareStatement(sql);
		    stmt.setString(1, name);
		    ResultSet res = stmt.executeQuery();
		    if(!res.next())
	        	return new ResultInfo(7, "图书不存在");
		    
		    String isbn = res.getString(1);
				
	
			boolean exist = false;
		    for(int i = 0; i < cart.size(); i++)
		    {
		    	if(cart.get(i).getName().equals(name))
		    	{
		    		int ori_count = cart.get(i).getCount();
		    		cart.get(i).setCount(count + ori_count);
		    		exist = true;
		    		break;
		    	}
		    }
			if(!exist)
			{
			  CartItemBean item = new CartItemBean(isbn, name, count);
			  cart.add(item);
			}
		    
			return new ResultInfo(0, "");
		
    	} catch(SQLException sqlex) 
	    { return new ResultInfo(sqlex.getErrorCode() + 1023, "数据库错误：" + sqlex.getMessage()); }
        catch(Exception ex)
        { return new ResultInfo(2, ex.getMessage()); }
	}
	
	@Override
	public void clear()
	{
		cart.clear();
	}
	
	@Override
	public ResultInfo rm(String name)
	{
		 boolean exist = false;
		 for(int i = 0; i < cart.size(); i++)
		 {
		  	if(cart.get(i).getName().equals(name))
		   	{
		   		cart.remove(i);
		   		exist = true;
		   		break;
		   	}
		 }
		 
		 if(exist)
			 return new ResultInfo(0, "");
		 else
			 return new ResultInfo(7, "图书不存在");
	}
	
	@Override
	public ResultInfo fix(String name, int count)
	{
		boolean exist = false;
	    for(int i = 0; i < cart.size(); i++)
	    {
	    	if(cart.get(i).getName().equals(name))
	    	{
	    		cart.get(i).setCount(count);
	    		exist = true;
	    		break;
	    	}
	    }
	    
	    if(exist)
			 return new ResultInfo(0, "");
		 else
			 return new ResultInfo(7, "图书不存在");
	}
	
	@Override
	public List<CartItemBean> getList()
	{
		return cart;
	}
	
}
