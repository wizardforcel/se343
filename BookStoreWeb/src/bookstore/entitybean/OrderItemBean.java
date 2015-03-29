package bookstore.entitybean;

public class OrderItemBean 
{
	private int id;
	private String isbn;
	private int num;
	private int time;
	
    public int getId() { return this.id; }
    public void setId(int id) { this.id = id; }
    public String getIsbn() { return this.isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public int getNum() { return this.num; }
    public void setNum(int num) { this.num = num; }
    public int getTime() { return this.time; }
    public void setTime(int time) { this.time = time; }
}
