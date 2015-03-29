package bookstore.remote;

import javax.ejb.*;

import bookstore.entitybean.BookBean;
import bookstore.sessionbean.QueryResultInfo;
import bookstore.sessionbean.ResultInfo;

@Remote
public interface BookListRemote 
{
	public QueryResultInfo<BookBean> getList();
	public ResultInfo add(String name, String isbn);
	public ResultInfo rm(String name);
	
}
