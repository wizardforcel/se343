<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.*" %>
<%@page import="bookstore.servlet.*" %>
<%@page import="bookstore.entitybean.*" %>
<%@page import="bookstore.utility.*" %>

<% Map<String, String> dict 
     = (Map<String, String>)request.getAttribute("dict"); %>

<jsp:include page="./header.jsp" />

  <div class="panel panel-primary">
  
    <div class="panel-heading">
      <h3><%= dict.get("book list") %></h3>
    </div>

    <table class="table table-striped" id="book-tb"> 
	  <tr>
	    <th><%= dict.get("isbn") %></th>
	    <th><%= dict.get("name") %></th>
	    <th><%= dict.get("operation") %></th>
	  </tr>
	  
<%
	  	/*boolean is_admin = request.getAttribute("IS_ADMIN") != null;
		ArrayList<BookBean> books = (ArrayList<BookBean>)request.getAttribute("books");
	    for(BookBean book : books) 
	    {
	      out.print("<tr>\n");
	      out.print("<td>" + book.getIsbn() + "</td>\n"); 
	      out.print("<td>" + Common.htmlEnco(book.getName()) + "</td>\n"); 
	      out.print("<td>");
	      out.print("<a class=\"cartbtn\">添加到购物车</a>");
	      if(is_admin)
	      	out.print(" | <a class=\"rmbtn\">删除</a>");
	      out.print("</td>\n");
	      out.print("</tr>\n");
	    }*/
%>

	</table>

    <hr />
	
	<div class="panel-body">
      <div class="row">
<% if(request.getAttribute("IS_ADMIN") != null) { %>
        <div class="col-md-3">
          <input type="button" id="addbtn" value="<%= dict.get("add") %>" 
                 class="btn btn-block btn-primary book-btns"/>
	    </div>
<% } %>
      </div>
	</div><!-- panel-body -->
	
	<hr />
	
	<div class="panel-body">
	  <div class="row">
	    <div class="col-md-3">
			<select id="lang-cmb" class="form-control">
				<option value="zh">简体中文</option>
                <option value="en">English</option>
			</select>
	    </div>
	    <div class="col-md-3">
	    	<input type="button" id="selectbtn" value="<%= dict.get("select") %>" 
				   class="btn btn-block btn-primary book-btns"/>
	    </div>
	  </div>
	</div><!-- panel-body -->
	
  </div><!-- panel -->
	
  <script>
  <%= I18nDict.toJs(dict) %>
  </script>
	
  <script src="./js/book.js" charset="UTF-8"></script>
	 
<jsp:include page="./footer.jsp" />