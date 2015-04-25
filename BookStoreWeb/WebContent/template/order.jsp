<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.*" %>
<%@page import="bookstore.entitybean.*" %>

<jsp:include page="./header.jsp" />

  <div class="panel panel-primary">
  
    <div class="panel-heading">
      <h3>我的订单</h3>
    </div>

    <table class="table table-striped" id="order-tb"> 
	  <tr>
	    <th class="cart-th">订单号</th>
	    <th class="cart-th">ISBN</th>
	    <th class="cart-th">数量</th>
	    <th class="cart-th">日期</th>
	  </tr>
	  
<%
  if(false) {
  ArrayList<OrderItemBean> items
    = (ArrayList<OrderItemBean>)request.getAttribute("order");
  for(OrderItemBean item : items) {
%>
<tr>
  <td><%= item.getId() %></td>
  <td><%= item.getIsbn() %></td>
  <td><%= item.getNum() %></td>
  <td>
<%
  Date dt = new Date(item.getTime() * 1000);
  out.print(dt.toString());
%>
  </td>
</tr>
<%  }} %>

   </table>
	
  </div><!-- panel -->
	
  <script src="./js/order.js" charset="UTF-8"></script>
	 
<jsp:include page="./footer.jsp" />