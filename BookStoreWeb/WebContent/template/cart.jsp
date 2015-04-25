<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.*" %>
<%@page import="bookstore.servlet.*" %>
<%@page import="bookstore.entitybean.*" %>
<%@page import="bookstore.utility.*" %>

<% if(request.getAttribute("IN_USE") == null) return; %>
<jsp:include page="./header.jsp" />

  <div class="panel panel-primary">
  
    <div class="panel-heading">
      <h3>购物车</h3>
    </div>

    <table class="table table-striped" id="cart-tb"> 
	  <tr>
	    <th>isbn</th>
	    <th>名称</th>
	    <th>数量</th>
	    <th>操作</th>
	  </tr>
	  
<%
ArrayList<CartItemBean> items = (ArrayList<CartItemBean>)request.getAttribute("cart");
	    for(CartItemBean item : items) 
	    {
	      out.print("<tr class=\"cart-item\">\n");
	      out.print("<td class=\"c-isbn\">" + item.getIsbn() + "</td>\n" +
	    		    "<td class=\"c-name\">" + Common.htmlEnco(item.getName()) + "</td>\n" + 
	                "<td class=\"c-cnt\">" + String.valueOf(item.getCount()) + "</td>\n" +
	                "<td class=\"c-ops\">" +
	                "<a class=\"fixbtn\">修改数量</a> | <a class=\"rmbtn\">删除</a>" + 
	                "</td>");
	      out.print("</tr>\n");
	    }
	  %>

	</table>

    <hr />
	
	<div class="panel-body">
      <div class="row btn-row">
        <!--<div class="col-md-3 cart-col">
          <input type="button" id="fixbtn" value="修改数量" class="btn btn-block btn-primary cart-btns"/>
	    </div>
	    <div class="col-md-3 cart-col">
          <input type="button" id="rmbtn" value="删除" class="btn btn-block btn-primary cart-btns"/>
	    </div>-->
	    <div class="col-md-3 cart-col">
          <input type="button" id="clearbtn" value="清空" class="btn btn-block btn-primary cart-btns"/>
	    </div>
	    <div class="col-md-3 cart-col">
          <input type="button" id="orderbtn" value="下单" class="btn btn-block btn-primary cart-btns"/>
	    </div>
      </div>
	</div><!-- panel-body -->
	
  </div><!-- panel -->
	
  <script src="./js/cart.js" charset="UTF-8"></script>
	 
<jsp:include page="./footer.jsp" />