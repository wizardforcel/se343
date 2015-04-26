package bookstore.servlet;

import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.jms.*;

import org.json.simple.JSONObject;

import bookstore.entitybean.CartItemBean;
import bookstore.entitybean.UserBean;
import bookstore.messagebean.OrderMessage;
import bookstore.remote.AccountListRemote;
import bookstore.remote.BookListRemote;
import bookstore.remote.CartRemote;
import bookstore.remote.OrderRemote;
import bookstore.remote.ResultInfo;
import bookstore.remote.SessionBeanFactory;
import bookstore.remote.UserSysRemote;
import bookstore.utility.Common;
import bookstore.utility.PageName;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/" + PageName.CART_PG)
@SuppressWarnings("unchecked")
public class CartServlet extends HttpServlet 
{
	private CartRemote cartbean
	  = SessionBeanFactory.GetCartListBean();
	
	private static final long serialVersionUID = 3L;
	
	private HttpServletRequest request;
	private HttpServletResponse response;
    private UserBean usr;
    private PrintWriter writer;
    private HttpSession session;
   
    
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
		  if(action.equals("clear"))
		      doClear();
		  else if(action.equals("rm"))
		      doRm();
		  else if(action.equals("fix"))
		      doFix();
		  else if(action.equals("addorder"))
		      doAddOrder();
		  else
			  writer.print(Common.app_error(2, "未指定操作"));	  
	}
	
	private void doClear()
	{		
		cartbean.clear();
		
		/*ArrayList<CartItemBean> cart 
		  = (ArrayList<CartItemBean>)session.getAttribute("cart");
		if(cart == null)
			cart = new ArrayList<CartItemBean>();
		else
			cart.clear();
		session.setAttribute("cart", cart);
		
		JSONObject json = new JSONObject();
	    json.put("errno", 0);
	    writer.write(json.toJSONString());*/
	}		
	
	private void doRm()
	{
	   	String isbn = request.getParameter("isbn");
	    if(isbn == null) isbn = "";
	    if(!isbn.matches("^\\d+$"))
	    {
	        writer.write(Common.app_error(4, "isbn应为数字"));
	        return;
	    }
		    
	    ResultInfo res = cartbean.rm(isbn);
	    writer.write(res.toJsonString());
	    
	    /*ArrayList<CartItemBean> cart 
	      = (ArrayList<CartItemBean>)session.getAttribute("cart");
	    if(cart == null)
	    	cart = new ArrayList<CartItemBean>();
	    
	    boolean exist = false;
	    for(int i = 0; i < cart.size(); i++)
	    {
	    	if(cart.get(i).getIsbn().equals(isbn))
	    	{
	    		cart.remove(i);
	    		exist = true;
	    		break;
	    	}
	    }
	    session.setAttribute("cart", cart);
	    
	    JSONObject json = new JSONObject();
	    if(exist)
	    {
	    	json.put("errno", 0);
	    }
	    else
	    {
	    	json.put("errno", 7);
	    	json.put("errmsg", "图书不存在");
	    }
	    writer.write(json.toJSONString());*/
	}
	
	private void doFix()
	{
    	String isbn = request.getParameter("isbn");
	    if(isbn == null) isbn = "";
	    if(!isbn.matches("^\\d+$"))
	    {
	        writer.write(Common.app_error(4, "isbn应为数字"));
	        return;
        }
    	String numstr = request.getParameter("num");
	    if(numstr == null) numstr = "";
	    if(!numstr.matches("^\\d+$"))
	    {
	    	writer.write(Common.app_error(9, "数量格式有误"));
	        return;
	    }
	    int count = Integer.parseInt(numstr);
		    
	    ResultInfo res = cartbean.fix(isbn, count);
	    writer.write(res.toJsonString());
	    
	    /*ArrayList<CartItemBean> cart
	      = (ArrayList<CartItemBean>)session.getAttribute("cart");
	    if(cart == null)
	    	cart = new ArrayList<CartItemBean>();
	    
	    boolean exist = false;
	    for(int i = 0; i < cart.size(); i++)
	    {
	    	if(cart.get(i).getIsbn().equals(isbn))
	    	{
	    		cart.get(i).setCount(count);
	    		exist = true;
	    		break;
	    	}
	    }
	    session.setAttribute("cart", cart);
	    
	    JSONObject json = new JSONObject();
	    if(exist)
	    {
	    	json.put("errno", 0);
	    }
	    else
	    {
	    	json.put("errno", 7);
	    	json.put("errmsg", "图书不存在");
	    }
	    writer.write(json.toJSONString());*/
	}
	
	private void doAddOrder()
	{
		try 
		{
			/*ArrayList<CartItemBean> cart
			  = (ArrayList<CartItemBean>)session.getAttribute("cart");
		    if(cart == null)
		    	cart = new ArrayList<CartItemBean>();
			ResultInfo res = cartbean.addOrder(Integer.parseInt(usr.getId()), cart);*/
			
			//ResultInfo res = cartbean.addOrder(Integer.parseInt(usr.getId()));
		    //writer.write(res.toJsonString());
			
			List<CartItemBean> list = cartbean.getList();
			if(list.size() == 0)
			{
				writer.write(Common.app_error(8, "购物车中无图书"));
				return;
			}
			
			OrderMessage order = new OrderMessage();
			order.setList(list);
			order.setUid(Integer.parseInt(usr.getId()));
			
			InitialContext ctx = new InitialContext();
			Queue dest = (Queue)ctx.lookup("queue/OrderMessageBean");
			QueueConnectionFactory factory
			  = (QueueConnectionFactory)ctx.lookup("ConnectionFactory");
			QueueConnection cnn = factory.createQueueConnection();
			QueueSession session = cnn.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
			QueueSender sender = session.createSender(dest);
			ObjectMessage msg = session.createObjectMessage();
			msg.setObject(order);
			sender.send(msg);
			System.out.println("消息已经发出...");
			cartbean.clear();
			writer.write(Common.app_error(0, ""));
		}
		catch(Exception ex)
		{
			writer.write(Common.app_error(1024, ex.getMessage()));
		}
	}

}
