package bookstore.sessionbean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import javax.ejb.*;

import org.json.simple.*;

import bookstore.entitybean.OrderItemBean;
import bookstore.remote.OrderRemote;
import bookstore.remote.QueryResultInfo;
import bookstore.utility.AuthCode;
import bookstore.utility.DBConn;

@Stateless(mappedName="OrderBean")
public class OrderBean implements OrderRemote 
{
	@Override
	public QueryResultInfo<OrderItemBean> getList(String uid)
	{
		try
		{
			Connection conn = DBConn.getDbConn();
			conn.setAutoCommit(false);
	        conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			
			String sql = "SELECT o_id, isbn, b_num, o_time " +
	                   "FROM orders natural join orderitems where u_id=?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, uid);
			ResultSet res = stmt.executeQuery();
			conn.commit();
			
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

	@Override
	public String getListCryto(String uid) 
	{
		QueryResultInfo<OrderItemBean> r = getList(uid);
		JSONObject json = new JSONObject();
		json.put("errno", r.getErrno());
		json.put("errmsg", r.getErrmsg());
		JSONArray arr = new JSONArray();
		for(OrderItemBean e : r.getList())
		{
			JSONObject o = new JSONObject();
			o.put("id", e.getId());
			o.put("isbn", e.getIsbn());
			o.put("num", e.getNum());
			o.put("time", e.getTime());
			arr.add(o);
		}
		json.put("list", arr);
		String jsonStr = json.toJSONString();
		return AuthCode.Encode(jsonStr, "order");
	}
	
	
}
