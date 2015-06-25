package bookstore.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.json.simple.*;
import org.json.simple.parser.*;

import sun.org.mozilla.javascript.internal.json.JsonParser;
import bookstore.entitybean.OrderItemBean;
import bookstore.entitybean.UserBean;
import bookstore.remote.OrderRemote;
import bookstore.remote.QueryResultInfo;
import bookstore.remote.SessionBeanFactory;
import bookstore.utility.AuthCode;
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
		try {
			
		String jsonStr = ordbean.getListCryto(usr.getId());
		jsonStr = AuthCode.Decode(jsonStr, "order");
		
		JSONObject json = (JSONObject) new JSONParser().parse(jsonStr);
		if(!json.containsKey("list"))
		{
			writer.write(json.toJSONString());
			return;
		}
		
		JSONArray arr = (JSONArray) json.get("list");
		json.remove("list");
		for(int i = 0; i < arr.size(); i++)
		{
			JSONObject o = (JSONObject) arr.get(i);
			o.put("count", o.get("num"));
			o.remove("num");
			long time = (long) o.get("time");
			o.put("date", new Date(time * 1000).toString());
			o.remove("time");
		}
		json.put("data", arr);
		writer.write(json.toJSONString());
		
		/*QueryResultInfo<OrderItemBean> res = ordbean.getList(usr.getId());
		if(res.getErrno() != 0)
		{
			writer.write(res.toJsonString());
			return;
		}
		
		JSONObject json = new JSONObject();
		json.put("errno", 0);
		JSONArray arr = new JSONArray();
		for(OrderItemBean item : res.getList())
		{
			JSONObject o = new JSONObject();
			o.put("id", item.getId());
			o.put("isbn", item.getIsbn());
			o.put("count", item.getNum());
			o.put("date", new Date(item.getTime() * 1000).toString());
			arr.add(o);
		}
		json.put("data", arr);
		writer.write(json.toJSONString());*/
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}
