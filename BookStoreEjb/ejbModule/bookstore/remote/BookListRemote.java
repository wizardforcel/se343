package bookstore.remote;

import javax.ejb.*;

import bookstore.entitybean.BookBean;

@Remote
public interface BookListRemote 
{
	public QueryResultInfo<BookBean> getList();
	public ResultInfo add(String name, String isbn);
	public ResultInfo rm(String isbn);
	String getNameById(String isbn);
	
}
