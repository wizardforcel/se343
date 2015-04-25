$(function()
{
	var rmbtn_cb = function()
	{
		if(!confirm('真的要删除吗？'))
			return;
		var row = $(this).parent().parent();
		var name = row.children(':eq(0)').text();
		$.get("./cart?action=rm&name=" + name, function(res)
		{
			var json = JSON.parse(res);
			if(json.errno == 0)
				row.remove();
			else
				alert("删除失败！" + json.errmsg);
		});
	};
	
	var fixbtn_cb = function()
	{
		var num = prompt("请输入数量：", "");
		if(num == null || !/^\d+$/.test(num))
		{
			alert('数量必须为纯数字！');
		    return;
		}
		var row = $(this).parent().parent();
		var name = row.children(':eq(0)').text();
		$.get("./cart?action=fix&name=" + name + 
			  "&num=" + num, function(res)
		{
			var json = JSON.parse(res);
			if(json.errno == 0)
			{
				row.children(':eq(1)').text(num);
				alert("修改成功！");
			}
			else
				alert("修改失败！" + json.errmsg);
		});
	};
	
	$(".rmbtn").click(rmbtn_cb);
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