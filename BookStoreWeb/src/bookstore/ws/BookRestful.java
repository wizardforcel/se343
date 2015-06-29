package bookstore.ws;

import java.util.*;
import java.io.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import bookstore.entitybean.*;
import bookstore.remote.*;


@Path("/api/book")
public class BookRestful 
{
	private BookListRemote bklstbean
	  = SessionBeanFactory.GetBookListBean();
	
	@GET
	@Produces("application/xml")
	public String getBookList()
	{
		QueryResultInfo<BookBean> res = bklstbean.getList();
		if(res.getErrno() != 0)
		{
			return "<result errno=\"" + res.getErrno() + "\">\n" + 
		           "\t" + res.getErrmsg() + "\n" + 
				   "</result>";
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("<result errno=\"0\">\n");
		List<BookBean> list = res.getList();
		for(BookBean b : list)
		{
			sb.append("\t<book>\n")
			  .append("\t\t<isbn>").append(b.getIsbn()).append("</isbn>\n")
			  .append("\t\t<name>").append(b.getName()).append("</name>\n")
			  .append("\t</book>\n");
		}
		sb.append("</result>");
		return sb.toString();
	}
	
	@POST
	@Produces("application/xml")
	@Consumes("application/xml")
	public String addBook(InputStream is) 
		   throws SAXException, IOException, ParserConfigurationException
	{
		Document doc = DocumentBuilderFactory.newInstance()
						.newDocumentBuilder().parse(is);
		Element root = doc.getDocumentElement();
		NodeList li = root.getElementsByTagName("isbn");
		Element isbnEle = (Element)li.item(0);
		 li = root.getElementsByTagName("name");
		Element nameEle = (Element)li.item(0);
		Text isbnText = (Text)isbnEle.getFirstChild();
		Text nameText = (Text)nameEle.getFirstChild();
		String isbn = isbnText.getTextContent().trim();
		String name = nameText.getTextContent().trim();
		
		ResultInfo res = bklstbean.add(name, isbn);
		if(res.getErrno() != 0)
		{
			return "<result errno=\"" + res.getErrno() + "\">\n" + 
			       "\t" + res.getErrmsg() + "\n" + 
				   "</result>";
		}
		
		return "<result errno=\"0\" />";
	}
	
	@DELETE
	@Path("/{isbn}")
	@Produces("application/xml")
	public String deleteBook(@PathParam("isbn") String isbn) 
		   throws SAXException, IOException, ParserConfigurationException
	{
		
		ResultInfo res = bklstbean.rm(isbn);
		if(res.getErrno() != 0)
		{
			return "<result errno=\"" + res.getErrno() + "\">\n" + 
			       "\t" + res.getErrmsg() + "\n" + 
				   "</result>";
		}
		
		return "<result errno=\"0\" />";
	}
}
