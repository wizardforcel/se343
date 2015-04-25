$(function()
{
	var getOrderList = function()
	{
		$.ajax({
			type: "GET",
			async: false,
			url: "./order",
			success: function(data)
			{
				var json = JSON.parse(data);
				if(json.errno != 0)
				{
					alert("订单获取失败！" + json.errmsg);
					return;
				}
				
				for(var i in json.data)
				{
					var item = json.data[i];
					var tr = $("<tr></tr>");
					var td1 = $("<td>" + item.id + "</td>");
					var td2 = $("<td>" + item.isbn + "</td>");
					var td3 = $("<td>" + item.count + "</td>");
					var td4 = $("<td>" + item.date + "</td>");
					tr.append(td1);
					tr.append(td2);
					tr.append(td3);
					tr.append(td4);
					$('#order-tb').append(tr);
				}
			}
		});
	};
	getOrderList();
	
	
	
	
	
	
});