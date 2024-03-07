package org.automation.seleniumdemo.pages;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.automation.seleniumdemo.utility.JDBCUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

//user profile page
public class ProfilePage {
	WebDriver driver;//Web driver object
	
	//webpage element identifiers and identifier types
	String profile_bookstorebtn;//go to bookstore button element
	String profile_bookstorebtn_type;//identifier type for button element to go to bookstore 
	String profile_deleteonebook;//button element to delete a single book
	String profile_deleteonebookmodalbtn;//modal button element to delete a single book
	String profile_deleteonebookmodalbtn_type;//identifier type for modal button element to delete a single book
	String profile_deleteallbooks;//button element to delete all books
	String profile_deleteallbooks_type;//identifier type for button element to delete all books
	String profile_deletebooksmodalbtn;//modal button element to delete all books
	String profile_deletebooksmodalbtn_type;//identifier type for modal button element to delete all books
	String profile_titlecol;//book title, used as baseline to find other book information on the page
	String profile_authorcolextension;//extension info to find author information, will be added onto the end of bookstore_titlecol info
	String profile_pubcolextension;//extension info to find publisher information, will be added onto the end of bookstore_titlecol info
	String profile_showrowselector;//selection element to change the number of rows displayed on the page
	String profile_showrowselector_type;//identifier type for selection element
	String profile_selecttitle;//link to go to page for a book
	
	//Page constructor. Run through database queries to pull necessary webpage element information
	public ProfilePage(WebDriver driver) throws Exception
	{
		this.driver = driver;
		Connection conn = JDBCUtil.getMySQLConnection();
		Statement stmt = conn.createStatement();
		
		ResultSet rs = stmt.executeQuery("SELECT element_data,identifier_type FROM WebElementDataTbl WHERE element_name = 'profile_bookstorebtn'");
		rs.next();
		this.profile_bookstorebtn = rs.getString(1);
		this.profile_bookstorebtn_type = rs.getString(2);
		
		rs = stmt.executeQuery("SELECT element_data FROM WebElementDataTbl WHERE element_name = 'profile_deleteonebook'");
		rs.next();
		this.profile_deleteonebook = rs.getString(1);
		
		rs = stmt.executeQuery("SELECT element_data,identifier_type FROM WebElementDataTbl WHERE element_name = 'profile_deleteonebookmodalbtn'");
		rs.next();
		this.profile_deleteonebookmodalbtn = rs.getString(1);
		this.profile_deleteonebookmodalbtn_type = rs.getString(2);
		
		rs = stmt.executeQuery("SELECT element_data,identifier_type FROM WebElementDataTbl WHERE element_name = 'profile_deleteallbooks'");
		rs.next();
		this.profile_deleteallbooks = rs.getString(1);
		this.profile_deleteallbooks_type = rs.getString(2);
		
		rs = stmt.executeQuery("SELECT element_data,identifier_type FROM WebElementDataTbl WHERE element_name = 'profile_deletebooksmodalbtn'");
		rs.next();
		this.profile_deletebooksmodalbtn = rs.getString(1);
		this.profile_deletebooksmodalbtn_type = rs.getString(2);
		
		
		rs = stmt.executeQuery("SELECT element_data FROM WebElementDataTbl WHERE element_name = 'profile_titlecol'");
		rs.next();
		
		this.profile_titlecol = rs.getString(1);
		
		rs = stmt.executeQuery("SELECT element_data FROM WebElementDataTbl WHERE element_name = 'profile_authorcolextension'");
		rs.next();
		
		this.profile_authorcolextension = rs.getString(1);
		
		rs = stmt.executeQuery("SELECT element_data FROM WebElementDataTbl WHERE element_name = 'profile_pubcolextension'");
		rs.next();
		
		this.profile_pubcolextension = rs.getString(1);
		
		
		rs = stmt.executeQuery("SELECT element_data,identifier_type FROM WebElementDataTbl WHERE element_name = 'profile_showrowselector'");
		rs.next();
		this.profile_showrowselector = rs.getString(1);
		this.profile_showrowselector_type = rs.getString(2);
		
		rs = stmt.executeQuery("SELECT element_data,identifier_type FROM WebElementDataTbl WHERE element_name = 'profile_selecttitle'");
		rs.next();
		this.profile_selecttitle = rs.getString(1);
		
		JDBCUtil.cleanupConnection(conn,stmt,rs);
	}
	
	//go to book store
	public void clickBookStore() throws Exception
	{
		WebElement element = null;
		//determine identifier type, find element, scroll to element, then click
		if(profile_bookstorebtn_type.equals("id"))
			element = driver.findElement(By.id(profile_bookstorebtn));
		else
			element = driver.findElement(By.xpath(profile_bookstorebtn));
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("arguments[0].scrollIntoView(true);", element);
		element.click();
	}
	
	//expand displayed rows to show all books in profile
	public void showMaxRows() throws Exception
	{
		
		WebElement element = null;
		//determine identifier type, find element, scroll to element, then make selection
		if(profile_showrowselector_type.equals("id"))
			element = driver.findElement(By.id(profile_showrowselector));
		else
			element = driver.findElement(By.xpath(profile_showrowselector));
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("arguments[0].scrollIntoView(true);", element);
		Select sel = new Select(element);
		sel.selectByValue("100");
	}
	
	//selects book based on user passed book title
	public void selectBook(String title) throws Exception
	{
		//scroll to element then click
		WebElement element = driver.findElement(By.id(profile_selecttitle+title));
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("arguments[0].scrollIntoView(true);", element);
		element.click();
	}
	
	//delete a specific book
	public void deleteBook(String title) throws Exception
	{
		//path to deletion icon
		String deleteIconXPath= profile_deleteonebook.replaceFirst("'BookTitle'", "\"" + title + "\"");
		//System.out.println(deleteIconXPath);
		
		//find web element, scroll to element, then click
		WebElement element = driver.findElement(By.xpath(deleteIconXPath));
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("arguments[0].scrollIntoView(true);", element);
		element.click();
		
		//setup wait time
		WebDriverWait webWait = new WebDriverWait(driver, 3);//explicit wait
		
		//determine identifier type, wait until modal button is found, then click
		if(profile_deleteonebookmodalbtn_type.equals("id"))
		{
			webWait.until(ExpectedConditions.presenceOfElementLocated(By.id(profile_deleteonebookmodalbtn)));
			driver.findElement(By.id(profile_deleteonebookmodalbtn)).click();
		}
		else
		{
			webWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(profile_deleteonebookmodalbtn)));
			driver.findElement(By.xpath(profile_deleteonebookmodalbtn)).click();
		}
		
		//wait until alert occurs, then accept alert.
		webWait.until(ExpectedConditions.alertIsPresent());
		System.out.println("Alert Found");
		driver.switchTo().alert().accept();
		System.out.println("Item deleted");
	}
	
	//delete all books from profile list
	public void clickDeleteAllBooks() throws Exception
	{
		WebElement element = null;
		
		//determine identifier type, then use to find element
		if(profile_deleteallbooks_type.equals("id"))
			element = driver.findElement(By.id(profile_deleteallbooks));
		else
			element = driver.findElement(By.xpath(profile_deleteallbooks));
		//scroll to element, then click
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("arguments[0].scrollIntoView(true);", element);
		element.click();
		
		//set up wait time
		WebDriverWait webWait = new WebDriverWait(driver, 3);//explicit wait
		
		//determine identifier type, then use to find and click element
		if(profile_deletebooksmodalbtn_type.equals("id"))
		{
			webWait.until(ExpectedConditions.presenceOfElementLocated(By.id(profile_deletebooksmodalbtn)));
			driver.findElement(By.id(profile_deletebooksmodalbtn)).click();
		}
		else
		{
			webWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(profile_deletebooksmodalbtn)));
			driver.findElement(By.xpath(profile_deletebooksmodalbtn)).click();
		}
		//wait until alert occurs, then accept alert
		webWait.until(ExpectedConditions.alertIsPresent());
		System.out.println("Alert Found");
		driver.switchTo().alert().accept();
		System.out.println("Items deleted");
	}
	
	//return string array of text about book, provide name of the book as parameter
	public String[] getBookText(String bookName) throws Exception
	{
		//build proper xpath identifier to use to find the Title element for a specific book
		String titleColumnXpath = profile_titlecol.replaceFirst("'BookTitle'", "\"" + bookName + "\"");
		//scroll to element for clear view
		WebElement element = driver.findElement(By.xpath(titleColumnXpath));
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("arguments[0].scrollIntoView(true);", element);
		
		//get text
		String pageBookTitle = driver.findElement(By.xpath(titleColumnXpath)).getText();
		String pageAuthor = driver.findElement(By.xpath(titleColumnXpath+profile_authorcolextension)).getText();
		String pagePublisher = driver.findElement(By.xpath(titleColumnXpath+profile_pubcolextension)).getText();
		
		String []bookText = {pageBookTitle, pageAuthor, pagePublisher};
		return bookText;
	}
}
