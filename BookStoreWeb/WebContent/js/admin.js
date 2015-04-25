$(function()
{
	var rmbtn_cb = function()
	{
		if(!confirm('真的要删除吗？'))
			return;
		var row = $(this).parent().parent();
		var id = row.children(':eq(0)').text();
		$.get("./admin?action=rmuser&id=" + id, function(res)
		{
			  var json = JSON.parse(res);
		      if(json.errno == 0)
		    	  row.remove();
			  else
				  alert("删除失败！" + json.errmsg);
		});
	};
	$(".rmbtn").click(rmbtn_cb);
});