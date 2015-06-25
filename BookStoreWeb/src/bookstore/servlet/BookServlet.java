package bookstore.servlet;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.json.simple.*;

import bookstore.entitybean.BookBean;
import bookstore.entitybean.CartItemBean;
import bookstore.entitybean.UserBean;
import bookstore.remote.BookListRemote;
import bookstore.remote.CartRemote;
import bookstore.remote.QueryResultInfo;
import bookstore.remote.ResultInfo;
import bookstore.remote.SessionBeanFactory;
import bookstore.utility.Common;
import bookstore.utility.MemCart;
import bookstore.utility.PageName;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

@WebServlet("/" + PageName.BOOK_PG) 
public class BookServlet extends HttpServlet 
{
	private BookListRemote bklstbean
	  = SessionBeanFactory.GetBookListBean();
	private CartRemote cartbean
	  = SessionBeanFactory.GetCartListBean();
	
	private static final long serialVersionUID = 2L;
	
	private HttpServletRequest request;
	private HttpServletResponse response;
    private UserBean usr;
    private PrintWriter writer;
    private HttpSession session;
 
	//add.php?name=...&isbn=...
	//return {"errno":xxx,"errmsg":"xxx"}

	//rm.php?name=...
	//return {"errno":xxx,"errmsg":"xxx"[, "id"=xxx]}

	//query.php
	//return {"errno":xxx,"errmsg":"xxx","data":[...]}
        
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
    		throws IOException, ServletException
	{
    	req.setCharacterEncoding("UTF-8");
		res.setContentType("text/html;charset=UTF-8");
		request = req;
		response = res;
		writer = res.getWriter();
		session = req.getSession();
		doRequest();
	}
	
	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
    		throws IOException, ServletException
	{
		req.setCharacterEncoding("UTF-8");
		res.setContentType("text/html;charset=UTF-8");
		request = req;
		response = res;
		writer = res.getWriter();
		session = req.getSession();
		doRequest();
	}
	
	private void doRequest()
			throws IOException, ServletException
	{
		  usr = new UserBean();
		  usr.getCookie(request);
		  if(!usr.isValid())
		  {
			  writer.print(Common.app_error(1, "用户未登录"));
			  return;
		  }
		  
		  String action = request.getParameter("action");
		  if(action == null) action = "";
		  if(action.compareTo("add") == 0)
		      doAdd();
		  else if(action.compareTo("rm") == 0)
		      doRm();
		  else if(action.compareTo("addcart") == 0)
		      doAddCart();
		  else //query
		      doQuery();
	}
	
	private void doAdd()
	{
		    if(!usr.getId().equals("1"))
		    {
		       	writer.write(Common.app_error(3, "无操作权限"));
		       	return;
		    }
		        
	        String name = request.getParameter("name");
	        String isbn = request.getParameter("isbn");
	        if(name == null) name = "";
	        if(isbn == null) isbn = "";
	        if(name.length() == 0)
	        {
	        	writer.write(Common.app_error(4, "请输入名称"));
	        	return;
	        }
	        if(!isbn.matches("^\\d+$"))
	        {
	        	writer.write(Common.app_error(4, "ISBN应为数字"));
	        	return;
	        }
	
		    ResultInfo res = bklstbean.add(name, isbn);
		    writer.write(res.toJsonString());
	}
	
	private void doRm()
	{
			if(!usr.getId().equals("1"))
		    {
	           	writer.write(Common.app_error(3, "无操作权限"));
		       	return;
		    }	
					
			String isbn = request.getParameter("isbn");
		    if(isbn == null) isbn = "";
		    if(!isbn.matches("^\\d+$"))
	        {
	        	writer.write(Common.app_error(4, "ISBN应为数字"));
	        	return;
	        }
			        
		    ResultInfo res = bklstbean.rm(isbn);
		    writer.write(res.toJsonString());
	}
	
	
	private void doQuery()
	{
			QueryResultInfo<BookBean> res = bklstbean.getList();
			if(res.getErrno() != 0)
			{
				writer.write(res.toJsonString());
				return;
			}
			
			JSONObject json = new JSONObject();
			json.put("errno", 0);
			JSONArray arr = new JSONArray();
			for(BookBean b : res.getList())
			{
				JSONObject o = new JSONObject();
				o.put("isbn", b.getIsbn());
				o.put("name", Common.htmlEnco(b.getName()));
				arr.add(o);
			}
			json.put("data", arr);
			writer.write(json.toJSONString());
	}
	
	private void doAddCart() 
			throws IOException
	{
		String isbn = request.getParameter("isbn");
		if(isbn == null) isbn = "";
		if(!isbn.matches("^\\d+$"))
		{
			writer.write(Common.app_error(4, "isbn应为数字"));
	        return;
		}
		String countstr = request.getParameter("count");
		if(countstr == null) countstr = "";
		if(!countstr.matches("^\\d+$"))
		{
			writer.write(Common.app_error(9, "数量格式有误"));
	        return;
		}
		int count = Integer.parseInt(countstr);
			
		//ResultInfo res = cartbean.add(isbn, count);
		//writer.write(res.toJsonString());
		
		String name = bklstbean.getNameById(isbn);
		if(name.equals(""))
		{
			String json = Common.app_error(7, "图书不存在");
			writer.write(json);
			return;
		}
		
		MemCart mc = new MemCart(usr.getId());
	    List<CartItemBean> cart = mc.getCart();

	    boolean exist = false;
	    for(int i = 0; i < cart.size(); i++)
	    {
	    	if(cart.get(i).getIsbn().equals(isbn))
	    	{
	    		int ori_count = cart.get(i).getCount();
	    		cart.get(i).setCount(count + ori_count);
	    		exist = true;
	    		break;
	    	}
	    }
		if(!exist)
		{
		  CartItemBean item = new CartItemBean(isbn, name, count);
		  cart.add(item);
		}
		
		mc.setCart(cart);
		mc.close();
		
		writer.write(Common.app_error(0, "成功"));
	}

}
