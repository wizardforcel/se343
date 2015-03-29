package bookstore.remote;

import javax.ejb.*;

import bookstore.entitybean.OrderItemBean;
import bookstore.sessionbean.QueryResultInfo;

@Remote
public interface OrderRemote 
{
	public QueryResultInfo<OrderItemBean> getList(String uid);
}
