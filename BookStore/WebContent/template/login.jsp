<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="bookstore.utility.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<% if(request.getAttribute("IN_USE") == null) return; %>
<jsp:include page="./header.jsp" />
    <form id="form" action="./<%= PageName.INDEX_PG %>" method="GET" role="form">
	  <div class="page-header">
	    <h2>用户登录</h2>
	  </div>
	  <input id="untxt" type="text" name="un" class="form-control" placeholder="用户名" required="" autofocus="" />
	  <br />
	  <input type="password" id="pwtxt" name="pw" class="form-control" placeholder="密码" required="" />
	  <br />
	  <input type="hidden" name="action" value="login" />
	  <input type="submit" id="lgnbtn" value="登录" class="btn btn-lg btn-primary btn-block"/>
	</form>
<jsp:include page="./footer.jsp" />