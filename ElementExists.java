package org.automation.seleniumdemo.assertions;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.automation.seleniumdemo.utility.JDBCUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ElementExists {
	//validate that an element for a book exists
	//pass webdriver, and title of book. If element can be found, return true
	public static boolean validateProfileBookElementExists(WebDriver driver, String title) throws Exception
	{
		boolean flag = false;
		
		Connection conn = JDBCUtil.getMySQLConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT element_data,identifier_type FROM WebElementDataTbl WHERE element_name = 'profile_booktitle'");
		rs.next();
		String bookTitle = rs.getString(1);
		JDBCUtil.cleanupConnection(conn,stmt,rs);
		
		try
		{
			driver.findElement(By.id(bookTitle+title));
			flag = true;
		}
		catch(Exception e)
		{
			System.out.println(title + " was not found, but should have been.");
		}
		return flag;
	}
	
	//validate that an element for a book is not present
	//pass webdriver, and title of book. If element can be found, return false
	public static boolean validateProfileBookElementGone(WebDriver driver, String title) throws Exception
	{
		boolean flag = false;
		
		Connection conn = JDBCUtil.getMySQLConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT element_data,identifier_type FROM WebElementDataTbl WHERE element_name = 'profile_booktitle'");
		rs.next();
		String bookTitle = rs.getString(1);
		JDBCUtil.cleanupConnection(conn,stmt,rs);
		
		try
		{
			driver.findElement(By.id(bookTitle+title));
			System.out.println(title + " was found, but should not exist.");
		}
		catch(Exception e)
		{
			flag = true;
		}
		return flag;
	}
}
