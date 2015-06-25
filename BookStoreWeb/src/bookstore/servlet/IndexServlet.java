package bookstore.servlet;

import javax.security.auth.login.Configuration;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.sun.xml.rpc.processor.modeler.j2ee.xml.javaXmlTypeMappingType;

import java.util.*;

import bookstore.jaas.*;
import bookstore.entitybean.*;
import bookstore.remote.QueryResultInfo;
import bookstore.remote.UserResultInfo;
import bookstore.utility.Common;
import bookstore.utility.I18nDict;
import bookstore.utility.MemCart;
import bookstore.remote.*;
import bookstore.utility.PageName;
import net.spy.memcached.*;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

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
    //private BookListRemote bklstbean
    //  = SessionBeanFactory.GetBookListBean();
    private CartRemote cartbean
      = SessionBeanFactory.GetCartListBean();
    //private OrderRemote ordbean
    //  = SessionBeanFactory.GetOrderBean();
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
		usr = new UserBean();
		usr.getCookie(request);
		if(usr.isValid())
		{
		    request.setAttribute("IS_LOGIN", true);
			if(usr.getId().equals("1"))
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
			request.getRequestDispatcher("./WEB-INF/index.jsp")
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
				response.setStatus(301);
				response.setHeader("Location", "./" + PageName.INDEX_PG);
		        return;
			}
		}
		else
	    {
			response.setStatus(301);
			response.setHeader("Location", "./" + PageName.INDEX_PG);
	        return;
	    }
		
		QueryResultInfo<AccountBean> res = accbean.getList();
		if(res.getErrno() != 0)
		{
			writer.write(Common.show_msg("列表获取失败！" + res.getErrmsg(), "./" + PageName.INDEX_PG));
			return;
		}
		
		request.setAttribute("list", res.getList());
		request.getRequestDispatcher("./WEB-INF/account.jsp")
	        .forward(request, response);
	}
	
	private void doLogin() 
	{	
		try {
			
		if(usr.isValid())
		{
			response.setStatus(301);
			response.setHeader("Location", "./" + PageName.INDEX_PG);
		    return;
		}
		
		String un = request.getParameter("un"),
		       pw = request.getParameter("pw");
		if(un == null) un = "";
		if(pw == null) pw = "";
		if(un.length() == 0)
		{
		    request.getRequestDispatcher("./WEB-INF/login.jsp")
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
		
		SimpleLoginModule slm = new SimpleLoginModule();
		SimpleCallbackHandler handler = new SimpleCallbackHandler(un, pw);
		slm.initialize(null, handler, null, null);
		
		if(slm.login())
		{
			int uid = handler.getId();
			usr.setUInfo(String.valueOf(uid), un, pw);
			usr.setCookie(response);
			writer.write(Common.show_msg("登录成功！", "./" + PageName.INDEX_PG));
		}
		else
		    writer.write(Common.show_msg("登录失败！" + handler.getErrmsg(), "./" + PageName.INDEX_PG));
		
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
		 response.setStatus(301);
		 response.setHeader("Location", "./" + PageName.INDEX_PG);
	}
	
	private void doReg()
	{
		try {
    		
	    if(usr.isValid())
	    {
	    	response.setStatus(301);
			response.setHeader("Location", "./" + PageName.INDEX_PG);
	    }  
	        	  
	    String un = request.getParameter("un"),
	 	       pw = request.getParameter("pw");
	 	if(un == null) un = "";
	 	if(pw == null) pw = "";
	 	if(un.length() == 0)
	 	{
	 	     request.getRequestDispatcher("./WEB-INF/reg.jsp")
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
	    	 
	     MemCart mc = new MemCart(usr.getId());
		 List<CartItemBean> cart = mc.getCart();
		 mc.close();
	     
       	 request.setAttribute("cart", cartbean.getList());
      	 request.getRequestDispatcher("./WEB-INF/cart.jsp")
      	        .forward(request, response);
	}
	
	private void doBook()
			throws ServletException, IOException
	{
		if(!usr.isValid())
        {
			response.setStatus(301);
			response.setHeader("Location", "./" + PageName.INDEX_PG);
            return;
        } 
        
		//i18n
		String lang = request.getParameter("lang");
		if(lang == null)
		{
			Cookie[] cos = request.getCookies();
			boolean exist = false;
			for(Cookie c : cos)
			{
				if(c.getName().equals("lang"))
				{
					exist = true;
					lang = c.getValue();
					break;
				}
			}
			if(!exist) lang = "zh";
		}
		Cookie co = new Cookie("lang", lang);
		co.setMaxAge(3600 * 24 * 30);
		response.addCookie(co);
		Map<String, String> map = I18nDict.getDict(lang);
		request.setAttribute("dict", map);
		
		
		/*QueryResultInfo<BookBean> res = bklstbean.getList();
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
        }*/
		
		request.getRequestDispatcher("./WEB-INF/book.jsp")
               .forward(request, response);
	}
	
	private void doOrder()
			throws ServletException, IOException
	{
		if(!usr.isValid())
        {
			response.setStatus(301);
			response.setHeader("Location", "./" + PageName.INDEX_PG);
            return;
        } 
		
		/*QueryResultInfo<OrderItemBean> res = ordbean.getList(usr.getId());
		if(res.getErrno() != 0)
		{
			writer.write(Common.show_msg("订单获取失败！" + res.getErrmsg(), 
	                      "./" + PageName.INDEX_PG));
		}
		else
		{
		    request.setAttribute("order", res.getList());
		    request.getRequestDispatcher("./template/order.jsp")
                   .forward(request, response);
		}*/
		
		request.getRequestDispatcher("./WEB-INF/order.jsp")
        	   .forward(request, response);
	}

	
	private void doAdmin() 
			throws ServletException, IOException
	{			
		if(usr.getId().compareTo("1") != 0)
		{
			response.setStatus(301);
			response.setHeader("Location", "./" + PageName.INDEX_PG);
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
        request.getRequestDispatcher("./WEB-INF/admin.jsp")
               .forward(request, response);
		
	}

}
