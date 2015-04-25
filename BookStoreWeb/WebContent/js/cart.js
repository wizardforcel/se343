$(function()
{
	var rmbtn_cb = function()
	{
		if(!confirm('真的要删除吗？'))
			return;
		var row = $(this).parent().parent();
		var isbn = row.children(':eq(0)').text();
		$.get("./cart?action=rm&isbn=" + isbn, function(res)
		{
			var json = JSON.parse(res);
			if(json.errno == 0)
				row.remove();
			else
				alert("删除失败！" + json.errmsg);
		});
	};
	$(".rmbtn").click(rmbtn_cb);
	
	var fixbtn_cb = function()
	{
		var num = prompt("请输入数量：", "");
		if(num == null || !/^\d+$/.test(num))
		{
			alert('数量必须为纯数字！');
		    return;
		}
		var row = $(this).parent().parent();
		var isbn = row.children(':eq(0)').text();
		$.get("./cart?action=fix&isbn=" + isbn + 
			  "&num=" + num, function(res)
		{
			var json = JSON.parse(res);
			if(json.errno == 0)
			{
				row.children(':eq(2)').text(num);
				alert("修改成功！");
			}
			else
				alert("修改失败！" + json.errmsg);
		});
	};
	$(".fixbtn").click(fixbtn_cb);
	

	$("#clearbtn").click(function()
	{
		if(!confirm("确定要删除吗？"))
			return;
		$.get("./cart?action=clear", function(res)
		{
			var json = JSON.parse(res);
			if(json.errno != "0")
				alert("删除失败！" + json.errmsg);
			else
			    $(".cart-item").remove();
		});
	});
	
	$("#orderbtn").click(function()
	{
		$.get("./cart?action=addorder", function(res)
		{
			var json = JSON.parse(res);
			if(json.errno != "0")
				alert("下单失败！" + json.errmsg);
			else
			{
				$(".cart-item").remove();
				alert("下单成功！");
			}
		});
	});

});