<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="bookstore.utility.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<jsp:include page="./header.jsp" />

<div class="alert alert-info">
<% if(request.getAttribute("IS_LOGIN") == null) {  %>
      您还没有
      <a href="./<%= PageName.INDEX_PG %>?action=login">登录</a>
      或
      <a href="./<%= PageName.INDEX_PG %>?action=reg">注册</a>
       。
<% } else { %>
      <p>Welcome back, <b><%= request.getAttribute("un") %></b>.</p>
<% } %>
</div>

<jsp:include page="./footer.jsp" />