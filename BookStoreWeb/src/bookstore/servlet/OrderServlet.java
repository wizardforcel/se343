package bookstore.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import bookstore.entitybean.OrderItemBean;
import bookstore.entitybean.UserBean;
import bookstore.remote.OrderRemote;
import bookstore.remote.QueryResultInfo;
import bookstore.remote.SessionBeanFactory;
import bookstore.utility.Common;
import bookstore.utility.PageName;

@WebServlet("/" + PageName.ORDER_PG) 
public class OrderServlet extends HttpServlet 
{
	private static final long serialVersionUID = 2L;
	
	private HttpServletRequest request;
	private HttpServletResponse response;
    private UserBean usr;
    private PrintWriter writer;
    private HttpSession session;
    
    private OrderRemote ordbean
      = SessionBeanFactory.GetOrderBean();
    
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
		  
		  doQuery();
	}
	
	private void doQuery()
	{
		QueryResultInfo<OrderItemBean> res = ordbean.getList(usr.getId());
		if(res.getErrno() != 0)
		{
			writer.write(res.toJsonString());
			return;
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("{\"errno\":0,\"data\":[");
		for(OrderItemBean item : res.getList())
		{
			sb.append("{\"id\":")
			  .append(item.getId())
			  .append(",\"isbn\":")
			  .append(item.getIsbn())
			  .append(",\"count\":")
			  .append(item.getNum())
			  .append(",\"date\":\"")
			  .append(new Date(item.getTime() * 1000).toString())
			  .append("\"},");
		}
		sb.setLength(sb.length() - 1);
		sb.append("]}");
		writer.write(sb.toString());
	}

}
