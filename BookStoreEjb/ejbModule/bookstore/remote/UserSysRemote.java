package bookstore.remote;

import javax.ejb.*;

import bookstore.entitybean.UserBean;

@Remote
public interface UserSysRemote 
{
	public UserResultInfo login(String un, String pw);
	public UserResultInfo reg(String un, String pw);
	public ResultInfo rm(int id);
	public QueryResultInfo<UserBean> getList();
}
