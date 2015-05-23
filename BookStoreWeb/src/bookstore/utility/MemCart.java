package bookstore.utility;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;

import bookstore.entitybean.*;
import net.spy.memcached.*;

public class MemCart 
{
	private MemcachedClient mc = null;
	private String id;
	
	public MemCart(String id) 
		   throws IOException
	{
		this.id = id;
		
		mc = new MemcachedClient(new InetSocketAddress("localhost", 11211));
	}
	
	public List<CartItemBean> getCart()
	{
		String key = "cart" + id;
	    List<CartItemBean> cart 
	      = (List<CartItemBean>) mc.get(key);
	    if(cart == null)
	    {
	    	cart = new ArrayList<>();
	    	mc.set(key, 3600, cart);
	    }
	    return cart; 
	}
	
	public void setCart(List<CartItemBean> cart)
	{
		String key = "cart" + id;
		mc.set(key, 3600, cart);
	}
	
	public void clear()
	{
		String key = "cart" + id;
		mc.set(key, 3600, new ArrayList<>());
	}
	
	@Override
	protected void finalize() 
			  throws Throwable
	{
		close();
		super.finalize();
	}
	
	public void close()
	{
		mc.shutdown();
	}
}
