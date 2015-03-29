package bookstore.servlet;

import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.json.simple.JSONObject;

import bookstore.entitybean.CartItemBean;
import bookstore.entitybean.UserBean;
import bookstore.remote.AccountListRemote;
import bookstore.remote.BookListRemote;
import bookstore.remote.CartRemote;
import bookstore.remote.OrderRemote;
import bookstore.remote.ResultInfo;
import bookstore.remote.UserSysRemote;
import bookstore.utility.Common;
import bookstore.utility.PageName;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;

@WebServlet("/" + PageName.CART_PG)
@SuppressWarnings("unchecked")
public class CartServlet extends HttpServlet 
{
	private CartRemote cartbean;
	
	private static final long serialVersionUID = 3L;
	
	private HttpServletRequest request;
	private HttpServletResponse response;
    private UserBean usr;
    private PrintWriter writer;
    private HttpSession session;
    
    private void initRemote()
    {
    	try
		{
			final Context context = new InitialContext(); 

			cartbean = (CartRemote) context.lookup("CartBean/remote");
		}
		catch(Exception e)
		{
            e.printStackTrace();
		}
    }
    
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
		  initRemote();
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
		
		JSONObject json = new JSONObject();
	    json.put("errno", 0);
	    writer.write(json.toJSONString());
	}		
	
	private void doRm()
	{
	   	String name = request.getParameter("name");
	    if(name == null) name = "";
	    if(name.length() == 0)
	    {
	        writer.write(Common.app_error(4, "请输入名称"));
	        return;
	    }
		    
	    ResultInfo res = cartbean.rm(name);
	    writer.write(res.toJsonString());
	}
	
	private void doFix()
	{
    	String name = request.getParameter("name");
	    if(name == null) name = "";
	    if(name.length() == 0)
	    {
	        writer.write(Common.app_error(4, "请输入名称"));
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
		    
	    ResultInfo res = cartbean.fix(name, count);
	    writer.write(res.toJsonString());
	}
	
	private void doAddOrder()
	{
		ResultInfo res = cartbean.addOrder(Integer.parseInt(usr.getId()));
	    writer.write(res.toJsonString());
	}

}
