$(function()
{
	var cartbtn_cb = function()
	{
		var cnt = prompt("请输入数量：", "");
		if(cnt == null || cnt == "")
		  return;
		var row = $(this).parent().parent();
		var name = $(row.children()[1]).text();
		$.get("./book?action=addcart&name=" + name + 
		      "&count=" + cnt, function(res)
		{
		  var json = eval("(" + res + ")");
		  if(json.errno == 0)
			alert("添加成功！");
		  else
		    alert("添加失败！" + json.errmsg);
		});
	};
	
	var rmbtn_cb = function()
	{
		if(!confirm('真的要删除吗？'))
			return;
		var row = $(this).parent().parent();
		var name = $(row.children()[1]).text();
		$.get("./book?action=rm&name=" + name, function(res)
		{
			  var json = eval("(" + res + ")");
		      if(json.errno == 0)
		    	  row.remove();
			  else
				  alert("删除失败！" + json.errmsg);
		});
	};
	
	$(".rmbtn").click(rmbtn_cb);
	$(".cartbtn").click(cartbtn_cb);
 
	 $("#addbtn").click(function()
	 {
		 var isbn = prompt("请输入ISBN：", "");
		 if(isbn == null || isbn == "")
		     return;
		 var name = prompt("请输入书名：", "");
		 if(name == null || name == "")
		     return;
		 $.get("./book?action=add&name=" + name + "&isbn=" + isbn, 
		       function(res)
		 {
		     var json = eval("(" + res + ")");
		     if(json.errno == 0)
		     {
		         var row = $("<tr class=\"book-item\"></tr>");
			     var c1 = $("<td class=\"b-isbn\">" + isbn + "</td>");
			     var c2 = $("<td class=\"b-name\">" + name + "</td>");
			     var c3 = $("<td></td>");
			     var btn1 = $("<a class=\"cartbtn\">添加到购物车</a>");
			     var btn2 = $("<a class=\"rmbtn\">删除</a>");
			     btn1.click(cartbtn_cb);
			     btn2.click(rmbtn_cb);
			     c3.append(btn1);
			     c3.append(" | ");
			     c3.append(btn2);
			     row.append(c1);
			     row.append(c2);
			     row.append(c3);
			     $("#book-tb").append(row);
		     }
		     else
		         alert("添加失败！" + json.errmsg);
		  });
	  });  

});