package bookstore.remote;

import javax.ejb.*;

import bookstore.entitybean.AccountBean;
import bookstore.sessionbean.QueryResultInfo;

@Remote
public interface AccountListRemote 
{
	public QueryResultInfo<AccountBean> getList();
}
