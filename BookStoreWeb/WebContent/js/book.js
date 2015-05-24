$(function()
{
	var cartbtn_cb = function()
	{
		var cnt = prompt(dict['input count'], "");
		if(cnt == null) return;
		if(!/^\d+$/.test(cnt))
		{
			alert(dict['count number']);
		    return;
		}
		var row = $(this).parent().parent();
		var isbn = $(row.children()[0]).text();
		$.get("./book?action=addcart&isbn=" + isbn + 
		      "&count=" + cnt, function(res)
		{
		  var json = JSON.parse(res);
		  if(json.errno == 0)
			alert(dict["add success"]);
		  else
		    alert(dict["add fail"] + json.errmsg);
		});
	};
	$(".cartbtn").click(cartbtn_cb);
	
	var rmbtn_cb = function()
	{
		if(!confirm(dict['cofirm rm']))
			return;
		var row = $(this).parent().parent();
		var isbn = row.children(":eq(0)").text();
		$.get("./book?action=rm&isbn=" + isbn, function(res)
		{
			  var json = JSON.parse(res);
		      if(json.errno == 0)
		    	  row.remove();
			  else
				  alert(dict["rm fail"] + json.errmsg);
		});
	};
	$(".rmbtn").click(rmbtn_cb);
	
 
	 $("#addbtn").click(function()
	 {
		 var isbn = prompt(dict['input isbn'], "");
		 if(isbn == null) return;
		 if(!/^\d+$/.test(isbn))
		 {
			 alert(dict['isbn wrong']);
			 return;
		 }
		 var name = prompt(dict['input name'], "");
		 if(name == null) return;
		 if(name == "")
		 {
			 alert(dict["name empty"]);
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
			     var btn1 = $("<a class=\"cartbtn\">" + dict['add to cart'] + "</a>");
			     var btn2 = $("<a class=\"rmbtn\">" + dict['delete'] + "</a>");
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
		         alert(dict["add fail"] + json.errmsg);
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
					 alert(dict["query fail"] + json.errmsg);
					 return;
				 }
				 for(var i in json.data)
				 {
					 var book = json.data[i];
					 var tr = $("<tr>");
					 var td1 = $("<td>" + book.isbn + "</td>\n");
					 var td2 = $("<td>" + htmlEnco(book.name) + "</td>\n");
					 var td3 = $("<td></td>");
					 var btn1 = $("<a class=\"cartbtn\">" + dict['add to cart'] + "</a>");
					 var btn2 = $("<a class=\"rmbtn\">" + dict['delete'] + "</a>");
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
	 
	 $('#selectbtn').click(function()
	 {
		var lang = $('#lang-cmb').val();
		location.href = './index?action=book&lang=' + lang;
	 });
});