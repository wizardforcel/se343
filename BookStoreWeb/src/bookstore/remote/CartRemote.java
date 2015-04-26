package bookstore.remote;

import java.util.List;

import javax.ejb.*;

import bookstore.entitybean.CartItemBean;

@Remote
public interface CartRemote 
{
	public ResultInfo add(String isbn, int count);
	public void clear();
	public ResultInfo rm(String isbn);
	public ResultInfo fix(String isbn, int count);
	public List<CartItemBean> getList();
	public ResultInfo addOrder(int uid);
}
