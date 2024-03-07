package org.automation.seleniumdemo.pages;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.automation.seleniumdemo.utility.JDBCUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

//Bookstore page
public class BookStorePage {
	WebDriver driver;//Web driver object
	
	//webpage element identifiers and identifier types
	String bookstore_selecttitle;//link to go to page for a book
	String bookstore_gotoprofile;//go to user profile button
	String bookstore_gotoprofile_type;//identifier type for the button to go to the user profile
	String bookstore_titlecol;//book title, used as baseline to find other book information on the page
	String bookstore_authorcolextension;//extension info to find author information, will be added onto the end of bookstore_titlecol info
	String bookstore_pubcolextension;//extension info to find publisher information, will be added onto the end of bookstore_titlecol info
	
	//Page constructor. Run through database queries to pull necessary webpage element information
	public BookStorePage(WebDriver driver) throws Exception
	{
		this.driver=driver;
		Connection conn = JDBCUtil.getMySQLConnection();
		Statement stmt = conn.createStatement();
		
		ResultSet rs = stmt.executeQuery("SELECT element_data FROM WebElementDataTbl WHERE element_name = 'bookstore_selecttitle'");
		rs.next();
		this.bookstore_selecttitle = rs.getString(1);
		
		rs = stmt.executeQuery("SELECT element_data,identifier_type FROM WebElementDataTbl WHERE element_name = 'bookstore_gotoprofile'");
		rs.next();
		this.bookstore_gotoprofile = rs.getString(1);
		this.bookstore_gotoprofile_type = rs.getString(2);
		
		rs = stmt.executeQuery("SELECT element_data FROM WebElementDataTbl WHERE element_name = 'bookstore_titlecol'");
		rs.next();
		
		this.bookstore_titlecol = rs.getString(1);
		
		rs = stmt.executeQuery("SELECT element_data FROM WebElementDataTbl WHERE element_name = 'bookstore_authorcolextension'");
		rs.next();
		
		this.bookstore_authorcolextension = rs.getString(1);
		
		rs = stmt.executeQuery("SELECT element_data FROM WebElementDataTbl WHERE element_name = 'bookstore_pubcolextension'");
		rs.next();
		
		this.bookstore_pubcolextension = rs.getString(1);
		
		JDBCUtil.cleanupConnection(conn,stmt,rs);
	}
	
	//selects book based on user passed book title
	public void selectBook(String title) throws Exception
	{
		//find element, scroll to element, then click
		WebElement element = driver.findElement(By.id(bookstore_selecttitle+title));
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("arguments[0].scrollIntoView(true);", element);
		element.click();
	}
	
	//go to profile
	public void goToProfile() throws Exception
	{
		WebElement element = null;
		//determine identifier type, then use to find element
		if(bookstore_gotoprofile_type.equals("id"))
			element = driver.findElement(By.id(bookstore_gotoprofile));
		else
			element = driver.findElement(By.xpath(bookstore_gotoprofile));
		
		
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("arguments[0].scrollIntoView(true);", element);
		element.click();
	}
	
	//return string array of text about book, provide name of the book as parameter
	public String[] getBookText(String bookName) throws Exception
	{
		//build proper xpath identifier to use to find the Title element for a specific book
		String titleColumnXpath = bookstore_titlecol.replaceFirst("'BookTitle'", "\"" + bookName + "\"");
		
		//scroll to element for clear view
		WebElement element = driver.findElement(By.xpath(titleColumnXpath));
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("arguments[0].scrollIntoView(true);", element);
		
		//get text
		String pageBookTitle = driver.findElement(By.xpath(titleColumnXpath)).getText();
		
		//add extensions onto base xpath, and use to pull element text
		String pageAuthor = driver.findElement(By.xpath(titleColumnXpath+bookstore_authorcolextension)).getText();
		String pagePublisher = driver.findElement(By.xpath(titleColumnXpath+bookstore_pubcolextension)).getText();
		
		String []bookStoreText = {pageBookTitle, pageAuthor, pagePublisher};
		return bookStoreText;
	}
	

}
