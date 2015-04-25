$(function()
{
	var cartbtn_cb = function()
	{
		var cnt = prompt("请输入数量：", "");
		if(cnt == null) return;
		if(!/^\d+$/.test(cnt))
		{
			alert('数量必须为纯数字！');
		    return;
		}
		var row = $(this).parent().parent();
		var isbn = $(row.children()[0]).text();
		$.get("./book?action=addcart&isbn=" + isbn + 
		      "&count=" + cnt, function(res)
		{
		  var json = JSON.parse(res);
		  if(json.errno == 0)
			alert("添加成功！");
		  else
		    alert("添加失败！" + json.errmsg);
		});
	};
	$(".cartbtn").click(cartbtn_cb);
	
	var rmbtn_cb = function()
	{
		if(!confirm('真的要删除吗？'))
			return;
		var row = $(this).parent().parent();
		var name = row.children(":eq(1)").text();
		$.get("./book?action=rm&name=" + name, function(res)
		{
			  var json = JSON.parse(res);
		      if(json.errno == 0)
		    	  row.remove();
			  else
				  alert("删除失败！" + json.errmsg);
		});
	};
	$(".rmbtn").click(rmbtn_cb);
	
 
	 $("#addbtn").click(function()
	 {
		 var isbn = prompt("请输入ISBN：", "");
		 if(isbn == null) return;
		 if(!/^\d+$/.test(isbn))
		 {
			 alert('isbn格式有误！');
			 return;
		 }
		 var name = prompt("请输入书名：", "");
		 if(name == null) return;
		 if(name == "")
		 {
			 alert('书名不能为空！');
		     return;
		 }
		 $.get("./book?action=add&name=" + name + "&isbn=" + isbn, 
		       function(res)
		 {
		     var json = JSON.parse(res);
		     if(json.errno == 0)
		     {
		         var row = $("<tr></tr>");
			     var c1 = $("<td>" + isbn + "</td>");
			     var c2 = $("<td>" + htmlEnco(name) + "</td>");
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
			     $(".cartbtn").click(cartbtn_cb);
			     $(".rmbtn").click(rmbtn_cb);
		     }
		     else
		         alert("添加失败！" + json.errmsg);
		  });
	  }); 
	 
	 var getBookList = function()
	 {
		 $.ajax({
			 type: "GET",
			 async: false,
			 url: "./book",
			 success: function(data)
		     {
				 var json = JSON.parse(data);
				 if(json.errno != 0)
				 {
					 alert("图书获取失败！" + json.errmsg);
					 return;
				 }
				 for(var i in json.data)
				 {
					 var book = json.data[i];
					 var tr = $("<tr>");
					 var td1 = $("<td>" + book.isbn + "</td>\n");
					 var td2 = $("<td>" + htmlEnco(book.name) + "</td>\n");
					 var td3 = $("<td></td>");
					 var btn1 = $("<a class=\"cartbtn\">添加到购物车</a>");
					 var btn2 = $("<a class=\"rmbtn\">删除</a>");
					 td3.append(btn1);
					 td3.append(" | ");
					 td3.append(btn2);
					 tr.append(td1);
					 tr.append(td2);
					 tr.append(td3);
					 $("#book-tb").append(tr);
				 }
				 $(".cartbtn").click(cartbtn_cb);
			     $(".rmbtn").click(rmbtn_cb);
		     }
		 });
	 };
	 getBookList();
});