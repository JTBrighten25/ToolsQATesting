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
import org.openqa.selenium.support.ui.WebDriverWait;

public class BookPage {
	WebDriver driver;//Web driver object
	
	//webpage element identifiers and identifier types
	String book_addtocollectionbtn; //'add to collection' button
	String book_addtocollectionbtn_type;//identifier type for 'add to collection' button
	String book_returntostorebtn;//'return to store' button
	String book_returntostorebtn_type;//identifier type for 'return to store' button
	String book_gotoprofile;//button to go to user profile
	String book_gotoprofile_type;//identifier type for button to go to user profile
	String book_isbntext;//ISBN text element
	String book_isbntext_type;//identifier type for ISBN text element
	String book_titletext;//title text element
	String book_titletext_type;//identifier type for title text element
	String book_subtitletext;//subtitle text element
	String book_subtitletext_type;//identifier type for subtitle text element
	String book_authortext;//author name text element
	String book_authortext_type;//identifier type for author name text element
	String book_publishertext;//publisher name text element
	String book_publishertext_type;//identifier type for publisher name text element
	String book_totalpagestext;//total pages text element
	String book_totalpagestext_type;//identifier type for total pages text element
	String book_descriptiontext;//book description text element
	String book_descriptiontext_type;//identifier type for book description text element
	String book_websitetext;//website text element
	String book_websitetext_type;//identifier type for website text element
	
	//Page constructor. Run through database queries to pull necessary webpage element information
	public BookPage(WebDriver driver) throws Exception
	{
		this.driver=driver;
		Connection conn = JDBCUtil.getMySQLConnection();
		Statement stmt = conn.createStatement();
		
		//add to collection button
		ResultSet rs = stmt.executeQuery("SELECT element_data,identifier_type FROM WebElementDataTbl WHERE element_name = 'book_addtocollectionbtn'");
		rs.next();
		this.book_addtocollectionbtn = rs.getString(1);
		this.book_addtocollectionbtn_type = rs.getString(2);
		
		//return to store button
		rs = stmt.executeQuery("SELECT element_data,identifier_type FROM WebElementDataTbl WHERE element_name = 'book_returntostorebtn'");
		rs.next();
		this.book_returntostorebtn = rs.getString(1);
		this.book_returntostorebtn_type = rs.getString(2);
		
		//go to profile button
		rs = stmt.executeQuery("SELECT element_data,identifier_type FROM WebElementDataTbl WHERE element_name = 'book_gotoprofile'");
		rs.next();
		this.book_gotoprofile = rs.getString(1);
		this.book_gotoprofile_type = rs.getString(2);
		
		//get info for book text on page, and get element identifier type, from database
		//isbn
		rs = stmt.executeQuery("SELECT element_data,identifier_type FROM WebElementDataTbl WHERE element_name = 'book_isbntext'");
		rs.next();
		this.book_isbntext = rs.getString(1);
		this.book_isbntext_type = rs.getString(2);
		
		//title
		rs = stmt.executeQuery("SELECT element_data,identifier_type FROM WebElementDataTbl WHERE element_name = 'book_titletext'");
		rs.next();
		this.book_titletext = rs.getString(1);
		this.book_titletext_type = rs.getString(2);
		
		//subtitle
		rs = stmt.executeQuery("SELECT element_data,identifier_type FROM WebElementDataTbl WHERE element_name = 'book_subtitletext'");
		rs.next();
		this.book_subtitletext = rs.getString(1);
		this.book_subtitletext_type = rs.getString(2);
		
		//author
		rs = stmt.executeQuery("SELECT element_data,identifier_type FROM WebElementDataTbl WHERE element_name = 'book_authortext'");
		rs.next();
		this.book_authortext = rs.getString(1);
		this.book_authortext_type = rs.getString(2);
		
		//publisher
		rs = stmt.executeQuery("SELECT element_data,identifier_type FROM WebElementDataTbl WHERE element_name = 'book_publishertext'");
		rs.next();
		this.book_publishertext = rs.getString(1);
		this.book_publishertext_type = rs.getString(2);
		
		//total pages
		rs = stmt.executeQuery("SELECT element_data,identifier_type FROM WebElementDataTbl WHERE element_name = 'book_totalpagestext'");
		rs.next();
		this.book_totalpagestext = rs.getString(1);
		this.book_totalpagestext_type = rs.getString(2);
		
		//description
		rs = stmt.executeQuery("SELECT element_data,identifier_type FROM WebElementDataTbl WHERE element_name = 'book_descriptiontext'");
		rs.next();
		this.book_descriptiontext = rs.getString(1);
		this.book_descriptiontext_type = rs.getString(2);
		
		//website
		rs = stmt.executeQuery("SELECT element_data,identifier_type FROM WebElementDataTbl WHERE element_name = 'book_websitetext'");
		rs.next();
		this.book_websitetext = rs.getString(1);
		this.book_websitetext_type = rs.getString(2);
		
		//cleanup connection
		JDBCUtil.cleanupConnection(conn,stmt,rs);
	}
	
	//click button to add book to collection/bookshelf
	public void clickAddToCollection() throws Exception
	{
		WebElement element = null;
		
		//determine identifier type, then use to find element
		if(book_addtocollectionbtn_type.equals("id"))
			element = driver.findElement(By.id(book_addtocollectionbtn));
		else
			element = driver.findElement(By.xpath(book_addtocollectionbtn));
		
		//scroll button into view, and click
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("arguments[0].scrollIntoView(true);", element);
		element.click();
		//handle popup
		WebDriverWait webWait = new WebDriverWait(driver, 3);
		webWait.until(ExpectedConditions.alertIsPresent());
		//System.out.println("Alert Found");
		driver.switchTo().alert().accept();
	}
	
	//click the return to store page button
	public void returnToStore() throws Exception
	{
		WebElement element = null;
		//determine identifier type, then use to find element
		if(book_returntostorebtn_type.equals("id"))
			element = driver.findElement(By.id(book_returntostorebtn));
		else
			element = driver.findElement(By.xpath(book_returntostorebtn));
		
		//scroll to element and click
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("arguments[0].scrollIntoView(true);", element);
		element.click();
	}
	
	//click go to profile button
	public void goToProfile() throws Exception
	{
		WebElement element = null;
		
		//determine identifier type, then use to find element
		if(book_gotoprofile_type.equals("id"))
			element = driver.findElement(By.id(book_gotoprofile));
		else
			element = driver.findElement(By.xpath(book_gotoprofile));
		
		//scroll to element and click
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("arguments[0].scrollIntoView(true);", element);
		element.click();
	}
	
	//return array of Strings containing text about book on the page
	public String[] gatherBookInfo() throws Exception
	{
		String []textOnPage = new String[8];//contains text that will be returned
		
		//determine which identifier to use, use to find element, and place text in array
		if(book_isbntext_type.equals("id"))
			textOnPage[0] = driver.findElement(By.id(book_isbntext)).getText();
		else
			textOnPage[0] = driver.findElement(By.xpath(book_isbntext)).getText();
		
		if(book_titletext_type.equals("id"))
			textOnPage[1] = driver.findElement(By.id(book_titletext)).getText();
		else
			textOnPage[1] = driver.findElement(By.xpath(book_titletext)).getText();
		
		if(book_subtitletext_type.equals("id"))
			textOnPage[2] = driver.findElement(By.id(book_subtitletext)).getText();
		else
			textOnPage[2] = driver.findElement(By.xpath(book_subtitletext)).getText();
		
		if(book_authortext_type.equals("id"))
			textOnPage[3] = driver.findElement(By.id(book_authortext)).getText();
		else
			textOnPage[3] = driver.findElement(By.xpath(book_authortext)).getText();
		
		if(book_publishertext_type.equals("id"))
			textOnPage[4] = driver.findElement(By.id(book_publishertext)).getText();
		else
			textOnPage[4] = driver.findElement(By.xpath(book_publishertext)).getText();
		
		if(book_totalpagestext_type.equals("id"))
			textOnPage[5] = driver.findElement(By.id(book_totalpagestext)).getText();
		else
			textOnPage[5] = driver.findElement(By.xpath(book_totalpagestext)).getText();
		
		if(book_descriptiontext_type.equals("id"))
			textOnPage[6] = driver.findElement(By.id(book_descriptiontext)).getText();
		else
			textOnPage[6] = driver.findElement(By.xpath(book_descriptiontext)).getText();
		
		if(book_websitetext_type.equals("id"))
			textOnPage[7] = driver.findElement(By.id(book_websitetext)).getText();
		else
			textOnPage[7] = driver.findElement(By.xpath(book_websitetext)).getText();
		
		return textOnPage;
		
	}
}
