package bookstore.servlet;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import bookstore.entitybean.AccountBean;
import bookstore.entitybean.BookBean;
import bookstore.entitybean.CartItemBean;
import bookstore.entitybean.OrderItemBean;
import bookstore.entitybean.UserBean;
import bookstore.utility.Common;
import bookstore.utility.DBConfig;
import bookstore.utility.DBConn;
import bookstore.utility.PageName;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;

@WebServlet("/" + PageName.INDEX_PG)
public class IndexServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	
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
		request.setAttribute("IN_USE", true);
		usr = new UserBean();
		usr.getCookie(request);
		if(usr.isValid())
		{
		    request.setAttribute("IS_LOGIN", true);
			if(usr.getId().compareTo("1") == 0)
			    request.setAttribute("IS_ADMIN", true);
		}
		
		String action = request.getParameter("action");
		if(action == null) action = "";
		if(action.equals("login"))
		    doLogin();
		else if(action.equals("logout"))
		    doLogout();
		else if(action.equals("reg"))
		    doReg();
		else if(action.equals("book"))
		    doBook();
		else if(action.equals("cart"))
		    doCart();
		else if(action.equals("order"))
		    doOrder();
		else if(action.equals("admin"))
			doAdmin();
		else if(action.equals("account"))
			doAccount();
		else //index.jsp
		{
		    request.setAttribute("un", usr.getUn());
			request.getRequestDispatcher("./template/index.jsp")
			       .forward(request, response);
		}
	}
	
	private void doAccount()
	{
		if(usr.isValid())
		{
			request.setAttribute("IS_LOGIN", true);
			if(usr.getId().equals("1"))
				request.setAttribute("IS_ADMIN", true);
			else
			{
				writer.write("<script>location.href=\"./" + PageName.INDEX_PG + "\";</script>");
		        return;
			}
		}
		else
	    {
	        writer.write("<script>location.href=\"./" + PageName.INDEX_PG + "\";</script>");
	        return;
	    }
		
		try
		{
			Connection conn = DBConn.getDbConn();
			String sql = "SELECT u_id, sum(b_num) FROM orders " +
	                     "natural join orderitems GROUP BY u_id";
			Statement stmt = conn.createStatement();
	        ResultSet res = stmt.executeQuery(sql);
	        
			ArrayList<AccountBean> list = new ArrayList<AccountBean>();
			while(res.next())
			{
				AccountBean item = new AccountBean();
				item.setUid(res.getInt(1));
				item.setNum(res.getInt(2));
				list.add(item);
			}
			request.setAttribute("list", list);
			request.getRequestDispatcher("./template/account.jsp")
  	        .forward(request, response);
		}
	    catch(Exception ex)
        {
	    	writer.write(Common.show_msg(ex.getMessage(), "./" + PageName.INDEX_PG)); 
	    }
	}
	
	private void doLogin()
	{
		try {
			
		if(usr.isValid())
		{
		    writer.write("<script>location.href=\"./" + PageName.INDEX_PG + "\";</script>");
		    return;
		}
		
		String un = request.getParameter("un"),
		       pw = request.getParameter("pw");
		if(un == null) un = "";
		if(pw == null) pw = "";
		if(un.length() == 0)
		{
		    request.getRequestDispatcher("./template/login.jsp")
		           .forward(request, response);
		}
		if(pw.length() == 0)
		{
		    writer.write(Common.show_msg("请输入密码!", "./" + PageName.INDEX_PG + "?action=login"));
		    return;
		}
		pw = Common.MD5(pw);
		
		Connection conn = DBConn.getDbConn();
		
		String sql = "SELECT * FROM Users WHERE u_un=? and u_pw=?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, un);
		stmt.setString(2, pw);
		ResultSet res = stmt.executeQuery();
		if(!res.next())
		{
		 	writer.write(Common.show_msg("用户不存在或密码错误!",
		      		     "./" + PageName.INDEX_PG + "?action=login"));
		  	return;
		}
		
		int uid = res.getInt(1);
		usr.setUInfo(String.valueOf(uid), un, pw);
		usr.setCookie(response);
		writer.write(Common.show_msg("登录成功!", "./" + PageName.INDEX_PG));
		
		} catch(SQLException sqlex) 
		{ writer.write(Common.show_msg("登录失败：" + sqlex.getMessage(),
		                               "./" + PageName.INDEX_PG)); }
		catch(Exception ex)
		{ writer.write(Common.show_msg("未知错误", "./" + PageName.INDEX_PG)); }
	}
	
	private void doLogout()
	{
		if(usr.isValid())
  	     {
  		     Cookie co = new Cookie("token", "");
  		     response.addCookie(co);
  	     }
   	     writer.write("<script>location.href=\"./" + PageName.INDEX_PG + "\";</script>");
	}
	
	private void doReg()
	{
		try {
    		
	    if(usr.isValid())
	    {
	         writer.write("<script>location.href=\"./" + PageName.INDEX_PG + "\";</script>");
	         return;
	    }  
	        	  
	    String un = request.getParameter("un"),
	 	       pw = request.getParameter("pw");
	 	if(un == null) un = "";
	 	if(pw == null) pw = "";
	 	if(un.length() == 0)
	 	{
	 	     request.getRequestDispatcher("./template/reg.jsp")
	 	            .forward(request, response);
	 	}
	 	if(pw.length() < 6 || pw.length() > 16)
	 	{
	 	     writer.write(Common.show_msg("密码应为6~16位!", "./" + PageName.INDEX_PG + "?action=reg"));
	 	     return;
	 	}
	 	pw = Common.MD5(pw);  
	 	     
	 	Connection conn = DBConn.getDbConn();
		     
		String sql = "INSERT INTO users (u_un, u_pw) VALUES (?,?)";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, un);
		stmt.setString(2, pw);
		/*if(stmt.executeUpdate() == 0)
		{
		  	 writer.write(Common.show_msg("用户已存在！", "./index.jsp?action=reg"));
		   	 return; 
		}*/
		try { stmt.executeUpdate(); }
		catch(Exception ex)
		{
			writer.write(Common.show_msg("用户已存在！", "./index.jsp?action=reg"));
		   	return;
		}
		
		ResultSet res = stmt.getGeneratedKeys();
		res.next();
		int uid = res.getInt(1);
		usr.setUInfo(String.valueOf(uid), un, pw);
		usr.setCookie(response);
		writer.write(Common.show_msg("注册成功!", "./" + PageName.INDEX_PG));
		     
	    } catch(SQLException sqlex) 
	 	{ writer.write(Common.show_msg("注册失败：" + sqlex.getMessage(),
	                                   "./" + PageName.INDEX_PG)); }
	 	catch(Exception ex)
	 	{ writer.write(Common.show_msg("未知错误", "./" + PageName.INDEX_PG)); }
	}
	
	private void doCart()
			throws ServletException, IOException
	{
	     //try {
   		 
	     if(!usr.isValid())
	     {
	        writer.write("<script>location.href=\"./" + PageName.INDEX_PG + "?action=login\";</script>");
	        return;
	     } 
	    	 
	     ArrayList<CartItemBean> cart = (ArrayList<CartItemBean>)session.getAttribute("cart");
       	 if(cart == null)
       	 {
       		cart = new ArrayList<CartItemBean>();
       	    session.setAttribute("cart", cart);
       	 }

       	 request.setAttribute("cart", cart);
      	 request.getRequestDispatcher("./template/cart.jsp")
      	        .forward(request, response);
	}
	
	private void doBook()
			throws ServletException, IOException
	{
		if(!usr.isValid())
        {
            writer.write("<script>location.href=\"./" + PageName.INDEX_PG + "?action=login\";</script>");
            return;
        } 
        
		ArrayList<BookBean> list = this.GetBookInfo();
        if(list == null)
        {
       	 writer.write(Common.show_msg("图书加载失败！", "./" + PageName.INDEX_PG));
        }
        else
        {
            request.setAttribute("books", list);
            request.getRequestDispatcher("./template/book.jsp")
                   .forward(request, response);
        }
	}
	
	private void doOrder()
			throws ServletException, IOException
	{
		if(!usr.isValid())
        {
            writer.write("<script>location.href=\"./" + PageName.INDEX_PG + "?action=login\";</script>");
            return;
        } 
		
		try
		{
			Connection conn = DBConn.getDbConn();
			String sql = "SELECT o_id, isbn, b_num, o_time " +
	                   "FROM orders natural join orderitems where u_id=?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, usr.getId());
			ResultSet res = stmt.executeQuery();
			
			ArrayList<OrderItemBean> list = new ArrayList<OrderItemBean>();
			while(res.next())
			{
				OrderItemBean item = new OrderItemBean();
				item.setId(res.getInt(1));
				item.setIsbn(res.getString(2));
				item.setNum(res.getInt(3));
				item.setTime(res.getInt(4));
				list.add(item);
			}
			request.setAttribute("order", list);
			request.getRequestDispatcher("./template/order.jsp")
                   .forward(request, response);
		}
		catch(Exception ex)
		{ 
		   	writer.write(Common.show_msg("未知错误：" + ex.getMessage(),
		   			                     "./" + PageName.INDEX_PG));
		}
	}
	
	private ArrayList<BookBean> GetBookInfo()
            throws IOException
    {
        try {
       	 
        Connection conn = DBConn.getDbConn();
        
        String sql = "SELECT * FROM Books";
        Statement stmt = conn.createStatement();
        ResultSet res = stmt.executeQuery(sql);
        ArrayList<BookBean> list = new ArrayList<BookBean>();
        while(res.next())
           list.add(new BookBean(res.getString(1), res.getString(2)));
        return list;

        } catch(Exception ex) { return null; }
    }
	
	private void doAdmin()
	{	
		try {
		
		if(usr.getId().compareTo("1") != 0)
		{
			writer.write("<script>location.href=\"./" + PageName.INDEX_PG + "?action=login\";</script>");
            return;
		}
		
		Connection conn = DBConn.getDbConn();
      	 
      	String sql = "SELECT u_id, u_un FROM users";
      	Statement stmt = conn.createStatement();
      	ResultSet res = stmt.executeQuery(sql);
      	ArrayList<UserBean> users = new ArrayList<UserBean>();
      	while(res.next())
      	{
      	   UserBean ui = new UserBean();
      	   ui.setUInfo(String.valueOf(res.getInt(1)), res.getString(2), "");
      	   users.add(ui);
      	}
      	request.setAttribute("users", users);
      	
        request.getRequestDispatcher("./template/admin.jsp")
               .forward(request, response);
		 
		} catch(SQLException sqlex) 
	    { writer.write(Common.show_msg("获取用户失败：" + sqlex.getMessage(),
	                                   "./" + PageName.INDEX_PG)); }
	    catch(Exception ex)
	    { writer.write(Common.show_msg("未知错误", "./" + PageName.INDEX_PG)); }
		
	}

}
