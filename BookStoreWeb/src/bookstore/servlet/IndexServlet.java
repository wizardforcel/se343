package bookstore.servlet;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import bookstore.entitybean.AccountBean;
import bookstore.entitybean.BookBean;
import bookstore.entitybean.OrderItemBean;
import bookstore.entitybean.UserBean;
import bookstore.remote.QueryResultInfo;
import bookstore.remote.UserResultInfo;
import bookstore.utility.Common;
import bookstore.remote.*;
import bookstore.utility.PageName;

import java.io.*;

@WebServlet("/" + PageName.INDEX_PG)
public class IndexServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	
	private HttpServletRequest request;
    private HttpServletResponse response;
    private UserBean usr;
    private PrintWriter writer;
    private HttpSession session;
	
    private UserSysRemote usrsysbean
      = SessionBeanFactory.GetUserSysBean();
    private BookListRemote bklstbean
      = SessionBeanFactory.GetBookListBean();
    private CartRemote cartbean
      = SessionBeanFactory.GetCartListBean();
    private OrderRemote ordbean
      = SessionBeanFactory.GetOrderBean();
    private AccountListRemote accbean
      = SessionBeanFactory.GetAccountListBean();
        
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
			throws ServletException, IOException
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
		else //index
		{
		    request.setAttribute("un", usr.getUn());
			request.getRequestDispatcher("./template/index.jsp")
			       .forward(request, response);
		}
	}
	
	private void doAccount() 
			throws ServletException, IOException
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
		
		QueryResultInfo<AccountBean> res = accbean.getList();
		if(res.getErrno() != 0)
		{
			writer.write(Common.show_msg("列表获取失败！" + res.getErrmsg(), "./" + PageName.INDEX_PG));
			return;
		}
		
		request.setAttribute("list", res.getList());
		request.getRequestDispatcher("./template/account.jsp")
	        .forward(request, response);
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
		if(!un.matches("^[\\w\\u4e00-\\u9fa5]{1,14}$"))
		{
			writer.write(Common.show_msg("登录失败！用户名应为1~14位汉字、数字、字母或下划线", 
					                     "./" + PageName.INDEX_PG + "?action=login"));
		    return;
		}
		
		if(!pw.matches("^[\\x20-\\x7e]{6,16}$"))
		{
		    writer.write(Common.show_msg("登录失败！密码应为6~16位", "./" + PageName.INDEX_PG + "?action=login"));
		    return;
		}
		pw = Common.MD5(pw);
		
		UserResultInfo res = usrsysbean.login(un, pw);
		if(res.getErrno() == 0)
		{
			int uid = res.getUid();
			usr.setUInfo(String.valueOf(uid), un, pw);
			usr.setCookie(response);
			writer.write(Common.show_msg("登录成功！", "./" + PageName.INDEX_PG));
		}
		else
		    writer.write(Common.show_msg("登录失败！" + res.getErrmsg(), "./" + PageName.INDEX_PG));
		
		} catch(Exception ex)
		{ writer.write(Common.show_msg("登录失败！" + ex.getMessage(), "./" + PageName.INDEX_PG)); }
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
	 	if(!un.matches("^[\\w\\u4e00-\\u9fa5]{1,14}$"))
		{
			writer.write(Common.show_msg("注册失败！用户名应为1~14位汉字、数字、字母或下划线", 
					                     "./" + PageName.INDEX_PG + "?action=login"));
		    return;
		}
	 	if(!pw.matches("^[\\x20-\\x7e]{6,16}$"))
	 	{
	 	     writer.write(Common.show_msg("注册失败！密码应为6~16位", "./" + PageName.INDEX_PG + "?action=reg"));
	 	     return;
	 	}
	 	pw = Common.MD5(pw);  
	 	     
	 	UserResultInfo res = usrsysbean.reg(un, pw);
	 	if(res.getErrno() == 0)
	 	{
	 		int uid = res.getUid();
	 		usr.setUInfo(String.valueOf(uid), un, pw);
			usr.setCookie(response);
			writer.write(Common.show_msg("注册成功！！", "./" + PageName.INDEX_PG));
	 	}
	 	else
		    writer.write(Common.show_msg("注册失败！" + res.getErrmsg(), "./" + PageName.INDEX_PG));
		     
	    } catch(Exception ex)
	 	{ writer.write(Common.show_msg("注册失败！" + ex.getMessage(), "./" + PageName.INDEX_PG)); }
	}
	
	private void doCart()
			throws ServletException, IOException
	{   		 
	     if(!usr.isValid())
	     {
	        writer.write("<script>location.href=\"./" + PageName.INDEX_PG + "?action=login\";</script>");
	        return;
	     } 
	    	 
       	 request.setAttribute("cart", cartbean.getList());
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
        
		QueryResultInfo<BookBean> res = bklstbean.getList();
        if(res.getErrno() != 0)
        {
       	 writer.write(Common.show_msg("图书加载失败！" + res.getErrmsg(), 
       			                      "./" + PageName.INDEX_PG));
        }
        else
        {
            request.setAttribute("books", res.getList());
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
		
		QueryResultInfo<OrderItemBean> res = ordbean.getList(usr.getId());
		if(res.getErrno() != 0)
		{
			writer.write(Common.show_msg("订单获取失败！" + res.getErrmsg(), 
	                      "./" + PageName.INDEX_PG));
		}
		
		request.setAttribute("order", res.getList());
		request.getRequestDispatcher("./template/order.jsp")
               .forward(request, response);
	}

	
	private void doAdmin() 
			throws ServletException, IOException
	{			
		if(usr.getId().compareTo("1") != 0)
		{
			writer.write("<script>location.href=\"./" + PageName.INDEX_PG + "?action=login\";</script>");
            return;
		}
		
		QueryResultInfo<UserBean> res = usrsysbean.getList();
		if(res.getErrno() != 0)
		{
			writer.write(Common.show_msg("列表获取失败！" + res.getErrmsg(), 
                                         "./" + PageName.INDEX_PG));
			return;
		}
		
      	request.setAttribute("users", res.getList());
        request.getRequestDispatcher("./template/admin.jsp")
               .forward(request, response);
		
	}

}
