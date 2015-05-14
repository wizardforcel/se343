package bookstore.entitybean;

import java.io.Serializable;

public class AccountBean implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private int uid;
	private int num;
	
	public int getUid() { return this.uid; }
	public void setUid(int uid) { this.uid = uid; }
	public int getNum() { return this.num; }
	public void setNum(int num) { this.num = num; }
}
