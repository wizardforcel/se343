$(function(){

  $("#lgnbtn, #regbtn").click(function()
  {
    var untxt = $("#untxt").val();
	var pwtxt = $("#pwtxt").val();
	if(untxt == "" || pwtxt == "")
	{
	  alert("请输入用户名和密码！");
	  event.preventDefault();
	  return;
	}
	if(pwtxt.length < 6 || pwtxt.length > 16)
	{
	  alert("密码应为6~16位！");
	  event.preventDefault();
	  return;
	}
  });

});