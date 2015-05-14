package bookstore.sessionbean;

import javax.ejb.*;

import org.json.simple.JSONObject;

import bookstore.entitybean.CartItemBean;
import bookstore.remote.CartRemote;
import bookstore.remote.ResultInfo;
import bookstore.utility.DBConfig;
import bookstore.utility.DBConn;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@Stateless(mappedName="CartBean")
public class CartBean implements CartRemote, Serializable
{
	private static final long serialVersionUID = 100L;
	
	private List<CartItemBean> cart
	  = new ArrayList<CartItemBean>();
	
	@Override
	public ResultInfo add(String isbn, int count)
	{
		try
		{
			Connection conn = DBConn.getDbConn();
			conn.setAutoCommit(false);
	        conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			
			String sql = "SELECT * FROM Books WHERE isbn=?";
		    PreparedStatement stmt = conn.prepareStatement(sql);
		    stmt.setString(1, isbn);
		    ResultSet res = stmt.executeQuery();
		    conn.commit();
		    if(!res.next())
	        	return new ResultInfo(7, "图书不存在");
				
		    String name = res.getString(2);
	
			boolean exist = false;
		    for(int i = 0; i < cart.size(); i++)
		    {
		    	if(cart.get(i).getIsbn().equals(isbn))
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
	public ResultInfo rm(String isbn)
	{
		 boolean exist = false;
		 for(int i = 0; i < cart.size(); i++)
		 {
		  	if(cart.get(i).getIsbn().equals(isbn))
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
	public ResultInfo fix(String isbn, int count)
	{
		boolean exist = false;
	    for(int i = 0; i < cart.size(); i++)
	    {
	    	if(cart.get(i).getIsbn().equals(isbn))
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

	@Override
	public ResultInfo addOrder(int uid) 
	{
		try 
		{
			if(cart.size() == 0)
	        	return new ResultInfo(8, "购物车中无图书");
			
			Connection conn = DBConn.getDbConn();
			conn.setAutoCommit(false);
	        conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			
			String sql = "INSERT INTO orders (u_id, o_time) VALUES (?,?)";
			PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, uid);
			stmt.setLong(2, new Date().getTime() / 1000);
			stmt.executeUpdate();
			
			ResultSet rs = stmt.getGeneratedKeys();
			if(!rs.next())
				return new ResultInfo(2, "创建订单失败！");
			int orderid = rs.getInt(1);
	
			sql = "INSERT INTO orderitems VALUES";
			for(CartItemBean item : cart)
			{
				sql += String.format(" (%d, %s, %d),",
						             orderid, item.getIsbn(), item.getCount());
			}
			sql = sql.substring(0, sql.length() - 1);
			Statement st = conn.createStatement();
			st.execute(sql);
			conn.commit();
			
			cart.clear();
			return new ResultInfo(0, "");
		}
		catch(SQLException sqlex)
		{
			return new ResultInfo(1023 + sqlex.getErrorCode(), "数据库错误：" + sqlex.getMessage());
		}
	    catch(Exception ex)
	    {
	    	return new ResultInfo(2, ex.getMessage());
	    }
	}
	
}
