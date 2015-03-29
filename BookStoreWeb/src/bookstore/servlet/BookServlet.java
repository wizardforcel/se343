package bookstore.servlet;

import javax.ejb.EJB;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.json.simple.JSONObject;

import bookstore.entitybean.BookBean;
import bookstore.entitybean.CartItemBean;
import bookstore.entitybean.UserBean;
import bookstore.remote.BookListRemote;
import bookstore.remote.CartRemote;
import bookstore.remote.QueryResultInfo;
import bookstore.remote.ResultInfo;
import bookstore.utility.Common;
import bookstore.utility.PageName;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;

@WebServlet("/" + PageName.BOOK_PG) 
@SuppressWarnings("unchecked")
public class BookServlet extends HttpServlet 
{
	
	private BookListRemote bklstbean;
	private CartRemote cartbean;
	
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
		    if(usr.getId().compareTo("1") != 0)
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
	        if(!isbn.matches("^[\\d\\-]+$"))
	        {
	        	writer.write(Common.app_error(4, "ISBN格式有误"));
	        	return;
	        }
	
		    ResultInfo res = bklstbean.add(name, isbn);
		    writer.write(res.toJsonString());
	}
	
	private void doRm()
	{
			if(usr.getId().compareTo("1") != 0)
		    {
	           	writer.write(Common.app_error(3, "无操作权限"));
		       	return;
		    }	
					
			String name = request.getParameter("name");
		    if(name == null) name = "";
		    if(name.length() == 0)
		    {
		        writer.write(Common.app_error(4, "请输入名称"));
		        return;
		    }
			        
		    ResultInfo res = bklstbean.rm(name);
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
	
	        writer.write("{\"errno\":0,\"data\":[");
			for(BookBean b : res.getList())
				writer.write("{\"id\":" + b.getIsbn() + ",\"name\":\"" + b.getName() + "\"},");
			writer.write("]}");
	}
	
	@SuppressWarnings("resource")
	private void doAddCart()
	{
		String name = request.getParameter("name");
		if(name == null) name = "";
		if(name.length() == 0)
		{
			writer.write(Common.app_error(4, "请输入名称"));
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
			
		ResultInfo res = cartbean.add(name, count);
		writer.write(res.toJsonString());
	}

}
