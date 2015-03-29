package bookstore.sessionbean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.ejb.*;

import bookstore.entitybean.OrderItemBean;
import bookstore.remote.OrderRemote;
import bookstore.utility.Common;
import bookstore.utility.DBConn;
import bookstore.utility.PageName;

@Stateless
public class OrderBean implements OrderRemote 
{
	@Override
	public QueryResultInfo<OrderItemBean> getList(String uid)
	{
		try
		{
			Connection conn = DBConn.getDbConn();
			String sql = "SELECT o_id, isbn, b_num, o_time " +
	                   "FROM orders natural join orderitems where u_id=?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, uid);
			ResultSet res = stmt.executeQuery();
			
			ArrayList<OrderItemBean> list = new ArrayList<OrderItemBean>();
			while(res.next())
			{
				OrderItemBean item = new OrderItemBean();
				item.setId(res.getInt(1));
				item.setIsbn(res.getString(2));
				item.setNum(res.getInt(3));
				item.setTime(res.getInt(4));
				list.add(item);
			}
			return new QueryResultInfo<OrderItemBean>(0, "", list);
			
		} catch(SQLException sqlex) 
        { return new QueryResultInfo<OrderItemBean>(sqlex.getErrorCode() + 1023, 
    		                               "数据库错误：" + sqlex.getMessage(), null ); }
		catch(Exception ex)
		{ 
			return new QueryResultInfo<OrderItemBean>(1, ex.getMessage(), null);
		}
	}
	
	
}
