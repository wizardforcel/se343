package bookstore.sessionbean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.ejb.*;

import org.json.simple.JSONObject;

import bookstore.entitybean.BookBean;
import bookstore.remote.BookListRemote;
import bookstore.utility.Common;
import bookstore.utility.DBConn;

@Stateless
public class BookListBean implements BookListRemote
{
	@Override
	public QueryResultInfo<BookBean> getList()
	{
		try {
	       	 
	        Connection conn = DBConn.getDbConn();
	        
	        String sql = "SELECT * FROM Books";
	        Statement stmt = conn.createStatement();
	        ResultSet res = stmt.executeQuery(sql);
	        ArrayList<BookBean> list = new ArrayList<BookBean>();
	        while(res.next())
	           list.add(new BookBean(res.getString(1), res.getString(2)));
	        return new QueryResultInfo<BookBean>(0, "", list);

		} catch(SQLException sqlex) 
	    { return new QueryResultInfo<BookBean>(sqlex.getErrorCode() + 1023, 
	    		                               "数据库错误：" + sqlex.getMessage(), null ); }
		  catch(Exception ex) 
	    { return new QueryResultInfo<BookBean>(1, ex.getMessage(), null); }
	}
	
	@Override
	public ResultInfo add(String name, String isbn)
	{
		try
		{
			Connection conn = DBConn.getDbConn();
		    
		    String sql = "INSERT INTO books VALUES (?,?)";
		    PreparedStatement stmt = conn.prepareStatement(sql);
		    stmt.setString(1, isbn);
		    stmt.setString(2, name);

		    try { stmt.executeUpdate(); }
		    catch(Exception ex)
		    { return new ResultInfo(6, "图书已存在"); }
		    
		    return new ResultInfo(0, "");
	    	
    	} catch(SQLException sqlex) 
	    { return new ResultInfo(sqlex.getErrorCode() + 1023, "数据库错误：" + sqlex.getMessage()); }
        catch(Exception ex)
        { return new ResultInfo(2, ex.getMessage()); }
	}
	
	@Override
	public ResultInfo rm(String name)
	{
		try
		{
			
			Connection conn = DBConn.getDbConn();
			
			String sql = "DELETE FROM Books WHERE b_name=?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, name);
			if(stmt.executeUpdate() == 0)
	        	return new ResultInfo(8, "图书不存在");
			
			return new ResultInfo(0, "");
		
    	} catch(SQLException sqlex) 
	    { return new ResultInfo(sqlex.getErrorCode() + 1023, "数据库错误：" + sqlex.getMessage()); }
        catch(Exception ex)
        { return new ResultInfo(2, ex.getMessage()); }
	}
	
}
