package bookstore.remote;

public class UserResultInfo extends ResultInfo 
{
	private int uid;
	
	public UserResultInfo(int errno_, String errmsg_, int uid_)
	{
		super(errno_, errmsg_);
		uid = uid_;
	}
	
	public int getUid() { return uid; }
	public void setUid(int uid_) { uid = uid_; }
}
