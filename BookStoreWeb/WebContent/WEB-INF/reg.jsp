<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="bookstore.utility.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<jsp:include page="./header.jsp" />
    <form id="form" action="./<%= PageName.INDEX_PG %>" method="GET">
	  <div class="page-header">
	    <h2>用户注册</h2>
	  </div>
	  <input id="untxt" type="text" name="un" class="form-control" 
	         placeholder="用户名" required="required" autofocus="autofocus"
             pattern="[\w\u4e00-\u9fa5]{1,14}" title="请输入1~14位汉字、数字、字母或下划线。" />
	  <br />
	  <input type="text" id="pwtxt" name="pw" class="form-control" 
	         placeholder="密码" required="required" pattern="[\x20-\x7e]{6,16}"
             title="密码应为6~16位。" />
	  <br />
	  <input type="hidden" name="action" value="reg" />
	  <input type="submit" id="regbtn" value="登录" class="btn btn-lg btn-primary btn-block"/>
	</form>
<jsp:include page="./footer.jsp" />