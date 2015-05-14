package bookstore.messagebean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

import javax.jms.*;
import javax.ejb.*;

import bookstore.entitybean.CartItemBean;
import bookstore.utility.DBConn;

@MessageDriven(
	activationConfig = {
		@ActivationConfigProperty(propertyName="destinationLookup", propertyValue="queue/OrderQueue"),
		@ActivationConfigProperty(propertyName="destinationType", propertyValue="javax.jms.Queue")
	},
	mappedName="queue/OrderQueue")
public class OrderMessageBean implements MessageListener
{

	@Override
	public void onMessage(Message msg) 
	{	
		try
		{
			System.out.println("接收到的消息:");
			ObjectMessage message = (ObjectMessage)msg;
			System.out.println(message.getObject().toString());
			
			OrderMessage order = (OrderMessage)message.getObject();
			int uid = order.getUid();
			List<CartItemBean> cart = order.getList();
			
			Connection conn = DBConn.getDbConn();
			conn.setAutoCommit(false);
	        conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			
			String sql = "INSERT INTO orders (u_id, o_time) VALUES (?,?)";
			PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, uid);
			stmt.setLong(2, new Date().getTime() / 1000);
			stmt.executeUpdate();
			
			ResultSet rs = stmt.getGeneratedKeys();
			//if(!rs.next())
			//	return new ResultInfo(2, "创建订单失败！");
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
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
