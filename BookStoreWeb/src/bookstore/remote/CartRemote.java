package bookstore.remote;

import java.util.List;

import javax.ejb.*;

import bookstore.entitybean.CartItemBean;
import bookstore.sessionbean.ResultInfo;

@Remote
public interface CartRemote 
{
	public ResultInfo add(String name, int count);
	public void clear();
	public ResultInfo rm(String name);
	public ResultInfo fix(String name, int count);
	public List<CartItemBean> getList();
}
