<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.*" %>
<%@page import="bookstore.entitybean.*" %>

<jsp:include page="./header.jsp" />

  <div class="panel panel-primary">
  
    <div class="panel-heading">
      <h3>我的订单</h3>
    </div>

    <table class="table table-striped" id="cart-tb"> 
	  <tr>
	    <th class="cart-th">订单号</th>
	    <th class="cart-th">ISBN</th>
	    <th class="cart-th">数量</th>
	    <th class="cart-th">日期</th>
	  </tr>
	  
<%
	  	ArrayList<OrderItemBean> items
	  	      = (ArrayList<OrderItemBean>)request.getAttribute("order");
	  	    for(OrderItemBean item : items) {
%>
<tr>
  <td class="o-id"><%= item.getId() %></td>
  <td class="o-isbn"><%= item.getIsbn() %></td>
  <td class="o-cnt"><%= item.getNum() %></td>
  <td class="o-time"><%
  Date dt = new Date(item.getTime() * 1000);
  /*String dtstr = String.format("%d-%d-%d %d:%d:%d",
		                       dt.getYear(), dt.getMonth(), dt.getDate(),
		                       dt.getHours(), dt.getMinutes(), dt.getSeconds());*/
  out.print(dt.toString());
  %></td>
</tr>
<%  } %>

   </table>
	
  </div><!-- panel -->
	
  <script src="./js/order.js" charset="UTF-8"></script>
	 
<jsp:include page="./footer.jsp" />