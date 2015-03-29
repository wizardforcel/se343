package bookstore.entitybean;

import javax.servlet.annotation.WebServlet;

public class BookBean 
{
  private String isbn;
  private String name;
  
  public BookBean(String isbn_, String name_)
  {
	  isbn = isbn_;
	  name = name_;
  }
  
  public String getIsbn() { return isbn; }
  public void setIsbn(String isbn_) { isbn = isbn_; }
  public String getName() { return name; }
  public void setName(String name_) { name = name_; }
}
