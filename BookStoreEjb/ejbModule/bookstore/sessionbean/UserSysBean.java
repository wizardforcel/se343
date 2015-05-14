package bookstore.sessionbean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.ejb.*;

import bookstore.entitybean.UserBean;
import bookstore.remote.QueryResultInfo;
import bookstore.remote.ResultInfo;
import bookstore.remote.UserResultInfo;
import bookstore.remote.UserSysRemote;
import bookstore.utility.DBConn;

@Stateless(mappedName="UserSysBean")
public class UserSysBean implements UserSysRemote 
{
	@Override
	public UserResultInfo login(String un, String pw)
	{
		try 
		{
	        Connection conn = DBConn.getDbConn();
	        conn.setAutoCommit(false);
	        conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
	        
			String sql = "SELECT * FROM Users WHERE u_un=? and u_pw=?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, un);
			stmt.setString(2, pw);
			ResultSet res = stmt.executeQuery();
			conn.commit();
			if(!res.next())
			  	return new UserResultInfo(1, "用户不存在或密码错误!", 0);
			
			int uid = res.getInt(1);
			
			
			
			return new UserResultInfo(0, "", uid);
		
		} catch(SQLException sqlex) 
		{ return new UserResultInfo(1023 + sqlex.getErrorCode(), "数据库错误：" + sqlex.getMessage(), 0); }
		catch(Exception ex)
		{ return new UserResultInfo(1, ex.getMessage(), 0); }
	}
	
	@Override
	public UserResultInfo reg(String un, String pw)
	{
		try 
		{
			
			Connection conn = DBConn.getDbConn();
			conn.setAutoCommit(false);
	        conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
		     
			String sql = "INSERT IGNORE INTO users (u_un, u_pw) VALUES (?,?)";
			PreparedStatement stmt 
			  = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, un);
			stmt.setString(2, pw);
			int effect = stmt.executeUpdate();
			conn.commit();
			if(effect == 0)
				return new UserResultInfo(1, "用户已存在！", 0); 
			ResultSet res = stmt.getGeneratedKeys();
			res.next();
			int uid = res.getInt(1);
			
			return new UserResultInfo(0, "", uid);
		     
	    } catch(SQLException sqlex) 
	 	{ return new UserResultInfo(1023 + sqlex.getErrorCode(), "数据库错误：" + sqlex.getMessage(), 0); }
	 	catch(Exception ex)
	 	{ return new UserResultInfo(1, ex.getMessage(), 0); }
	}
	
	@Override
	public ResultInfo rm(int id)
	{
		try
		{
			Connection conn = DBConn.getDbConn();
			conn.setAutoCommit(false);
	        conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			
			String sql = "DELETE FROM Users WHERE u_id=?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			stmt.execute();
			if(stmt.getUpdateCount() == 0)
	        	return new ResultInfo(8, "用户不存在");
			return new ResultInfo(0, "");
		} catch(SQLException sqlex) 
	 	{ return new ResultInfo(1023 + sqlex.getErrorCode(), "数据库错误：" + sqlex.getMessage()); }
	      catch(Exception ex)
        { return new ResultInfo(2, ex.getMessage()); }
	}
	
	@Override
	public QueryResultInfo<UserBean> getList()
	{
		try
		{
			Connection conn = DBConn.getDbConn();
			conn.setAutoCommit(false);
	        conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
	     	 
	      	String sql = "SELECT u_id, u_un FROM users ORDER BY u_id ASC";
	      	Statement stmt = conn.createStatement();
	      	ResultSet res = stmt.executeQuery(sql);
	      	conn.commit();
	      	ArrayList<UserBean> users = new ArrayList<UserBean>();
	      	while(res.next())
	      	{
	      	   UserBean ui = new UserBean();
	      	   ui.setUInfo(String.valueOf(res.getInt(1)), res.getString(2), "");
	      	   users.add(ui);
	      	}
	      	return new QueryResultInfo<UserBean>(0, "", users);
		 
		} catch(SQLException sqlex) 
	    { 
			return new QueryResultInfo<UserBean>(sqlex.getErrorCode() + 1023,
					                             "数据库错误：" + sqlex.getMessage(), null); 
	    }
	    catch(Exception ex)
	    { return new QueryResultInfo<UserBean>(2, ex.getMessage(), null); }
	}
	
}
