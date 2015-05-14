package bookstore.messagebean;

import java.io.Serializable;
import java.util.List;

import bookstore.entitybean.CartItemBean;

public class OrderMessage implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private List<CartItemBean> list;
	private int uid;
	
	public List<CartItemBean> getList() { return list; }
	public void setList(List<CartItemBean> list) { this.list = list; }
	public int getUid() { return uid; }
	public void setUid(int uid) { this.uid = uid; }
}
