package bookstore.utility;

import org.json.simple.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

@SuppressWarnings("unchecked")
public class Common
{

  public static String app_error(int errno, String errmsg)
  {
	  JSONObject json = new JSONObject();
	  json.put("errno", errno);
	  json.put("errmsg", errmsg);
	  return json.toJSONString();
  }
	
  public static String sql_error(SQLException sqlex)
  {
	  JSONObject json = new JSONObject();
	  json.put("errno", 1023 + sqlex.getErrorCode());
	  json.put("errmsg", "数据库访问错误");
	  json.put("info", sqlex.getMessage());
	  return json.toJSONString();
  }
  
  public static String show_msg(String msg, String from) 
  {
	  return "<html>\n" +
             "  <head>\n" + 
			 "    <title>提示信息</title>\n" + 
             "    <meta charset=\"utf-8\" />\n" + 
			 "  </head>\n" + 
             "  <body>\n" +
			 "    <p>\n" +
             "	  " + msg +
             "<a href=\"" + from + "\">返回</a>" + 
             "	</p>\n" +
             "  </body>\n" +
             "</html>\n";
  }
  
  
  public static String UnicodeEnco(String text)
  {
	  StringBuilder sb = new StringBuilder();
	  for(char c : text.toCharArray())
	  {
	    if((c >= 'A' && c <= 'Z') ||
	       (c >= 'a' && c <= 'z') ||
	       (c >= '0' && c <= '9') ||
	       c == '_')
	    	sb.append(c);
	    else
	    	sb.append("\\" + "u" + String.format("%4x", (int)c));
	  }
	  return sb.toString();
  }

  public static String MD5(String s)
    throws NoSuchAlgorithmException
  {
		    char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', 
		      '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		    byte[] btInput = s.getBytes();

		    MessageDigest mdInst = MessageDigest.getInstance("MD5");

		    mdInst.update(btInput);

		    byte[] md = mdInst.digest();

		    int j = md.length;
		    char[] str = new char[j * 2];
		    int k = 0;
		    for (int i = 0; i < j; i++) {
		      byte byte0 = md[i];
		      str[(k++)] = hexDigits[(byte0 >>> 4 & 0xF)];
		      str[(k++)] = hexDigits[(byte0 & 0xF)];
		    }
		    return new String(str);
  }
   
}
;