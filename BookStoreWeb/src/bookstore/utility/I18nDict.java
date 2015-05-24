package bookstore.utility;

import java.util.*;

public class I18nDict 
{
	public static Map<String, String> getDict(String lang)
	{
		HashMap<String, String> map = new HashMap<>();
		
		if(lang.equals("zh"))
		{
			map.put("book list", "图书");
			map.put("isbn", "isbn");
			map.put("name", "名称");
			map.put("operation", "操作");
			map.put("add", "添加");
			map.put("delete", "删除");
			map.put("add to cart", "添加到购物车");
			map.put("language", "语言");
			map.put("select", "选择");
			map.put("input count", "请输入数量：");
			map.put("add success", "添加成功！");
			map.put("add fail", "添加失败！");
			map.put("rm success", "删除成功！");
			map.put("rm fail", "删除失败！");
			map.put("query fail", "图书获取失败！");
			map.put("cofirm rm", "真的要删除吗？");
			map.put("count number", "数量必须为纯数字！");
			map.put("input isbn", "请输入isbn：");
			map.put("isbn wrong", "isbn格式有误！");
			map.put("input name", "请输入书名：");
			map.put("name empty", "书名不能为空！");
		}
		else if(lang.equals("en"))
		{
			map.put("book list", "book list");
			map.put("isbn", "isbn");
			map.put("name", "name");
			map.put("operation", "operation");
			map.put("add", "add");
			map.put("delete", "delete");
			map.put("add to cart", "add to cart");
			map.put("language", "language");
			map.put("select", "select");
			map.put("input count", "Please input count:");
			map.put("add success", "Successful to add!");
			map.put("add fail", "Fail to add!");
			map.put("rm success", "Successful to delete!");
			map.put("rm fail", "Fail to delete!");
			map.put("query fail", "Fail to query books!");
			map.put("cofirm rm", "Sure to delete?");
			map.put("count number", "Count must be numeric!");
			map.put("input isbn", "Please input isbn:");
			map.put("isbn wrong", "Isbn format wrong!");
			map.put("input name", "Please input book name:");
			map.put("name empty", "Book name connot be empty!");
		}
		
		return map;
	}
	
	public static String toJs(Map<String, String> dict)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("var dict = {").append("\n");
	  	int i = 0;
		for(Map.Entry<String, String> pair : dict.entrySet()) {
			sb.append("\"").append(pair.getKey()).append("\":\"").append(pair.getValue()).append("\"");
			if(i != dict.size() - 1)
				sb.append(",");
			sb.append("\n");
			i++;
		}
		sb.append("};").append("\n");
		return sb.toString();
	}

}
