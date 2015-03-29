<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.ArrayList" %>
<%@page import="bookstore.entitybean.*" %>

<jsp:include page="./header.jsp" />

  <div class="panel panel-primary">
  
    <div class="panel-heading">
      <h3>用户管理</h3>
    </div>
    
    <table class="table table-striped" id="cart-tb"> 
	  <tr>
	    <th>uid</th>
	    <th>用户名</th>
	    <th>操作</th>
	  </tr>

<% ArrayList<UserBean> users
   = (ArrayList<UserBean>)request.getAttribute("users");
   for(UserBean ui : users) { %>
  <tr class="admin">
    <td class="a-uid"><%= ui.getId().toString() %></td>
    <td class="a-un"><%= ui.getUn() %></td>
    <td class="a-ops">
      <a class="rmbtn">删除用户</a>
    </td> 
  </tr>
<% } %>

    </table>
    
  </div><!-- 。panel -->
  
  <script src="./js/admin.js" charset="UTF-8"></script>

<jsp:include page="./footer.jsp" />