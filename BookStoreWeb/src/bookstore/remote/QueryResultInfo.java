package bookstore.remote;

import java.util.*;

import org.json.simple.JSONObject;

public class QueryResultInfo<T> extends ResultInfo 
{

	private static final long serialVersionUID = 1L;
	
	private List<T> list;
	
	public QueryResultInfo(int errno_, String errmsg_, List<T> list_) 
	{
		super(errno_, errmsg_);
		list = list_;
	}
	
	public List<T> getList() { return list; }
	public void setList(List<T> list_) { list = list_; }
}
