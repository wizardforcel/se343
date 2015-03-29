package bookstore.remote;

import javax.ejb.*;

import bookstore.entitybean.UserBean;
import bookstore.sessionbean.QueryResultInfo;
import bookstore.sessionbean.ResultInfo;
import bookstore.sessionbean.UserResultInfo;

@Remote
public interface UserSysRemote 
{
	public UserResultInfo login(String un, String pw);
	public UserResultInfo reg(String un, String pw);
	public ResultInfo rm(int id);
	public QueryResultInfo<UserBean> getList();
}
