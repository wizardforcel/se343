package bookstore.ws;

import javax.jws.*;

import java.util.*;

import bookstore.entitybean.*;
import bookstore.remote.*;

@WebService
public class BookWsdl 
{
	private BookListRemote bklstbean
	  = SessionBeanFactory.GetBookListBean();

	@WebMethod
	public QueryResultInfo<BookBean> getBookList()
	{
		return bklstbean.getList();
	}
	
	@WebMethod
	public ResultInfo addBook(@WebParam String name, @WebParam String isbn)
	{
		return bklstbean.add(name, isbn);
	}
	
	@WebMethod
	public ResultInfo deleteBook(@WebParam String isbn)
	{
		return bklstbean.rm(isbn);
	}
	
}
