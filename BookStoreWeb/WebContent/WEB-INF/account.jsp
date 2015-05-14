<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.ArrayList" %>
<%@page import="bookstore.entitybean.*" %>

<jsp:include page="./header.jsp" />

  <div class="panel panel-primary">
  
    <div class="panel-heading">
      <h3>销售统计</h3>
    </div>

    <table class="table table-striped" id="book-tb"> 
	  <tr>
	    <th>UID</th>
	    <th>图书总数</th>
	  </tr>
	  
<%
	  	ArrayList<AccountBean> list
	       = (ArrayList<AccountBean>)request.getAttribute("list");
	     for(AccountBean item : list) {
%>
  <tr class="book-item">
    <td class="ac-uid"><%= item.getUid() %></td>
    <td class="ac-num"><%= item.getNum() %></td>
  </tr>
<% } %>

	</table>
	
  </div><!-- panel -->
	 
<jsp:include page="./footer.jsp" />
