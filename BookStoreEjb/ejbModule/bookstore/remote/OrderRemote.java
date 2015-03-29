package bookstore.remote;

import javax.ejb.*;

import bookstore.entitybean.OrderItemBean;

@Remote
public interface OrderRemote 
{
	public QueryResultInfo<OrderItemBean> getList(String uid);
}
