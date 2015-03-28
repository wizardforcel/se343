package bookstore.servlet;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.json.simple.JSONObject;

import bookstore.entitybean.CartItemBean;
import bookstore.entitybean.UserBean;
import bookstore.utility.Common;
import bookstore.utility.PageName;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;

@WebServlet("/" + PageName.CART_PG)
@SuppressWarnings("unchecked")
public class CartServlet extends HttpServlet 
{
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
		  if(action.compareTo("clear") == 0)
		      doClear();
		  else if(action.compareTo("rm") == 0)
		      doRm();
		  else if(action.compareTo("fix") == 0)
		      doFix();
		  else
			  writer.print(Common.app_error(2, "未指定操作"));	  
	}
	
	private void doClear()
	{
		/*try {
	    	
		DBConfig cfg = new DBConfig();
		Class.forName(cfg.GetDriver());
		String url = "jdbc:mysql://" + cfg.GetServer() + ":" + cfg.GetPort() + 
		             "/" + cfg.GetName() + "?user=" + cfg.GetUsername() + 
		             "&password=" + cfg.GetPassword();
	    Connection conn = DriverManager.getConnection(url);
		    
	   		
	    String sql = "DELETE FROM carts WHERE u_id=?";
	    PreparedStatement stmt = conn.prepareStatement(sql);
	    stmt.setInt(1, Integer.parseInt(usr.GetUID()));
	    stmt.executeUpdate();
	    		
	    JSONObject json = new JSONObject();
	    json.put("errno", 0);
	    writer.write(json.toJSONString());
	    		
    	} catch(SQLException sqlex) 
	    { writer.write(Common.sql_error(sqlex)); }
        catch(Exception ex)
        { writer.write(Common.app_error(2, "未知错误")); }*/
		
		ArrayList<CartItemBean> cart = (ArrayList<CartItemBean>)session.getAttribute("cart");
		if(cart == null)
			cart = new ArrayList<CartItemBean>();
		else
			cart.clear();
		session.setAttribute("cart", cart);
		JSONObject json = new JSONObject();
	    json.put("errno", 0);
	    writer.write(json.toJSONString());
	}		
	
	private void doRm()
	{
		//try {
	    	
	   	String name = request.getParameter("name");
	    if(name == null) name = "";
	    if(name.length() == 0)
	    {
	        writer.write(Common.app_error(4, "请输入名称"));
	        return;
	    }
		    
	    ArrayList<CartItemBean> cart = (ArrayList<CartItemBean>)session.getAttribute("cart");
	    if(cart == null)
	    	cart = new ArrayList<CartItemBean>();
	    
	    boolean exist = false;
	    for(int i = 0; i < cart.size(); i++)
	    {
	    	if(cart.get(i).getName().equals(name))
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
	    writer.write(json.toJSONString());
	    
	    /*DBConfig cfg = new DBConfig();
		Class.forName(cfg.GetDriver());
		String url = "jdbc:mysql://" + cfg.GetServer() + ":" + cfg.GetPort() + 
		             "/" + cfg.GetName() + "?user=" + cfg.GetUsername() + 
		             "&password=" + cfg.GetPassword();
	    Connection conn = DriverManager.getConnection(url);
		    
	    String sql = "SELECT * FROM Books WHERE b_name=?";
	    PreparedStatement stmt = conn.prepareStatement(sql);
	    stmt.setString(1, name);
	    ResultSet res = stmt.executeQuery();
	    if(!res.next())
	    {
	    	writer.write(Common.app_error(7, "图书不存在"));
	       	return;
	    }
	    int bid = res.getInt("b_id");
		    
	    sql = "DELETE FROM carts WHERE u_id=? AND b_id=?";
	    stmt = conn.prepareStatement(sql);
	    stmt.setInt(1, Integer.parseInt(usr.GetUID()));
	    stmt.setInt(2, bid);
	    stmt.executeUpdate();
	    		
	    JSONObject json = new JSONObject();
	    json.put("errno", 0);
	    writer.write(json.toJSONString());
	    		
	   	} catch(SQLException sqlex) 
	    { writer.write(Common.sql_error(sqlex)); }
	    catch(Exception ex)
	    { writer.write(Common.app_error(2, "未知错误")); }*/
	}
	
	private void doFix()
	{
		//try {
	    	
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
	    int num = Integer.parseInt(numstr);
		    
	    ArrayList<CartItemBean> cart = (ArrayList<CartItemBean>)session.getAttribute("cart");
	    if(cart == null)
	    	cart = new ArrayList<CartItemBean>();
	    
	    boolean exist = false;
	    for(int i = 0; i < cart.size(); i++)
	    {
	    	if(cart.get(i).getName().equals(name))
	    	{
	    		cart.get(i).setCount(num);
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
	    writer.write(json.toJSONString());
	    
	    /*DBConfig cfg = new DBConfig();
		Class.forName(cfg.GetDriver());
		String url = "jdbc:mysql://" + cfg.GetServer() + ":" + cfg.GetPort() + 
		             "/" + cfg.GetName() + "?user=" + cfg.GetUsername() + 
		             "&password=" + cfg.GetPassword();
	    Connection conn = DriverManager.getConnection(url);
		    
	    String sql = "SELECT * FROM Books WHERE b_name=?";
	    PreparedStatement stmt = conn.prepareStatement(sql);
	    stmt.setString(1, name);
	    ResultSet res = stmt.executeQuery();
	    if(!res.next())
	    {
	    	writer.write(Common.app_error(7, "图书不存在"));
        	return;
	    }
	    int bid = res.getInt("b_id");
		    
	    sql = "UPDATE carts SET b_num=? WHERE u_id=? AND b_id=?";
	    stmt = conn.prepareStatement(sql);
	    stmt.setInt(1, Integer.parseInt(num));
	    stmt.setInt(2, Integer.parseInt(usr.GetUID()));
	    stmt.setInt(3, bid);
	    stmt.executeUpdate();
		    
	    JSONObject json = new JSONObject();
	    json.put("errno", 0);
	    writer.write(json.toJSONString());
		    
    	} catch(SQLException sqlex) 
	    { writer.write(Common.sql_error(sqlex)); }
        catch(Exception ex)
        { writer.write(Common.app_error(2, "未知错误")); }*/
	}

}
