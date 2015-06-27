package example;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.*;
import org.apache.lucene.document.*;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.*;
import org.apache.lucene.util.*;

public class Program 
{
	
	public static void main(String[] args) 
		   throws IOException
	{
		//init();
		search();
	}

	private static void init() 
			throws IOException
	{
		String path = System.getProperty("user.dir");
		Directory dir = FSDirectory.open(new File(path + "\\index"));
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);  
        IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_40, analyzer); 
        IndexWriter writer = new IndexWriter(dir, iwc);
        String[] books = {"tom sawyer", "the da vinci code", "hunger games 1"};
        for(String book : books)
        {
        	System.out.println("build index for: " + book);
        	Document doc = new Document();
        	doc.add(new StringField("title", book, Store.YES));
        	StreamReader reader = new StreamReader(path + "\\rsrc\\" + book + ".txt");
        	doc.add(new TextField("content", reader.readToEnd(), Store.YES));
        	reader.close();
        	writer.addDocument(doc);
        }
        writer.close();
        System.out.println("build complete");
	}
	
	private static void search() 
			throws IOException
	{
		System.out.println("请输入需要查找的关键词：");
		StreamReader sr = new StreamReader(System.in);
		String name = sr.readLine();
		String path = System.getProperty("user.dir");
		Directory dir = FSDirectory.open(new File(path + "\\index"));  
        IndexReader reader = DirectoryReader.open(dir);  
        IndexSearcher searcher = new IndexSearcher(reader);  
        Term term = new Term("content", name);  
        TermQuery query = new TermQuery(term);  
        TopDocs topdocs = searcher.search(query, 5);  
        ScoreDoc[] scoreDocs = topdocs.scoreDocs;  
        System.out.println("found: " + topdocs.totalHits + " max score: " + topdocs.getMaxScore());  
        for(int i=0; i < scoreDocs.length; i++) {  
            int docId = scoreDocs[i].doc;  
            Document d = searcher.doc(docId);  
            System.out.println(i + " title: " + d.get("title") + " id: " + docId + 
            		           " scors: " + scoreDocs[i].score+" index: " + scoreDocs[i].shardIndex);  
        }  
        reader.close();  
	}
	
}
