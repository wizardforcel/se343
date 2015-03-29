package bookstore.sessionbean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.ejb.*;

import bookstore.entitybean.*;
import bookstore.remote.AccountListRemote;
import bookstore.utility.Common;
import bookstore.utility.DBConn;
import bookstore.utility.PageName;

@Stateless
public class AccountListBean implements AccountListRemote
{
	@Override
	public QueryResultInfo<AccountBean> getList()
	{
		try
		{
			Connection conn = DBConn.getDbConn();
			String sql = "SELECT u_id, sum(b_num) FROM orders " +
	                     "natural join orderitems GROUP BY u_id";
			Statement stmt = conn.createStatement();
	        ResultSet res = stmt.executeQuery(sql);
	        
			ArrayList<AccountBean> list = new ArrayList<AccountBean>();
			while(res.next())
			{
				AccountBean item = new AccountBean();
				item.setUid(res.getInt(1));
				item.setNum(res.getInt(2));
				list.add(item);
			}
			return new QueryResultInfo<AccountBean>(0, "", list);
		}
	    catch(Exception ex)
        {
	    	return new QueryResultInfo<AccountBean>(1, ex.getMessage(), null);
	    }
	}
}
