package org.automation.seleniumdemo.pages;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.automation.seleniumdemo.utility.JDBCUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

//login page
public class LoginPage {
	WebDriver driver;//Web driver object
	
	//webpage element identifiers and identifier types
	String login_username;//username field element
	String login_username_type;//identifier type for username field element
	String login_password;//password field element
	String login_password_type;//identifier type for password field element
	String login_loginbtn;//login button element
	String login_loginbtn_type;//identifer type for login button element
	
	//Page constructor. Run through database queries to pull necessary webpage element information
	public LoginPage(WebDriver driver) throws Exception
	{
		this.driver=driver;
		Connection conn = JDBCUtil.getMySQLConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT element_data,identifier_type FROM WebElementDataTbl WHERE element_name = 'login_username'");
		rs.next();
		this.login_username = rs.getString(1);
		this.login_username_type = rs.getString(2);
		
		rs = stmt.executeQuery("SELECT element_data,identifier_type FROM WebElementDataTbl WHERE element_name = 'login_password'");
		rs.next();
		this.login_password = rs.getString(1);
		this.login_password_type = rs.getString(2);
		
		rs = stmt.executeQuery("SELECT element_data,identifier_type FROM WebElementDataTbl WHERE element_name = 'login_loginbtn'");
		rs.next();
		this.login_loginbtn = rs.getString(1);
		this.login_loginbtn_type = rs.getString(2);
		JDBCUtil.cleanupConnection(conn,stmt,rs);
	}
	
	//enters username for login, pass username
	public void enterUsername(String uname) throws Exception
	{
		//determine identifier type, then use to find element, and send keys
		if(login_username_type.equals("id"))
			driver.findElement(By.id(login_username)).sendKeys(uname);
		else
			driver.findElement(By.xpath(login_username)).sendKeys(uname);
	}
	
	//enter password for login, pass password
	public void enterPassword(String pass) throws Exception
	{
		//determine identifier type, then use to find element, and send keys
		if(login_password_type.equals("id"))
			driver.findElement(By.id(login_password)).sendKeys(pass);
		else
			driver.findElement(By.xpath(login_password)).sendKeys(pass);
	}
	
	//clicks login button
	public void clickLogin() throws Exception
	{
		WebElement element = null;
		JavascriptExecutor js = (JavascriptExecutor)driver;
		//determine identifier type, then use to find element
		if(login_loginbtn_type.equals("id"))
			element = driver.findElement(By.id(login_loginbtn));
		else
			element = driver.findElement(By.xpath(login_loginbtn));
		
		//scroll to element, then click
		js.executeScript("arguments[0].scrollIntoView(true);", element);
		element.click();
	}
}
