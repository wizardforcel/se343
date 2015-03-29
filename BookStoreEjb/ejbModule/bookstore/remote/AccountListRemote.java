package bookstore.remote;

import javax.ejb.*;

import bookstore.entitybean.AccountBean;

@Remote
public interface AccountListRemote 
{
	public QueryResultInfo<AccountBean> getList();
}
