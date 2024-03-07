package org.automation.seleniumdemo.assertions;

import org.apache.commons.lang3.StringUtils;
import org.automation.seleniumdemo.utility.CapScreenshot;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class VerifyBookInfo {
	//validate info from book page matches database info. Capture image of book page for evidence
	//pass driver, 1d string array of book info from database, and 1d string array of book info from the webpage. If matching, return true
	public static boolean checkBookPage(WebDriver driver, String[] dbBookInfo, String[] pageBookInfo) throws Exception
	{
		String dbISBN = dbBookInfo[0];
		String dbTitle = dbBookInfo[1];
		String dbSubtitle = dbBookInfo[2];
		String dbAuthor = dbBookInfo[3];
		String dbPublisher = dbBookInfo[4];
		String dbPages = dbBookInfo[5];
		String dbDescription = dbBookInfo[6].trim();
		String dbWebsite = dbBookInfo[7];
		
		//zoom out for best snapshot
		((JavascriptExecutor)driver).executeScript("document.body.style.zoom = '75.0%'");
		CapScreenshot.screenshot(driver, "Book_TC4_" + dbISBN);
		((JavascriptExecutor)driver).executeScript("document.body.style.zoom = '100.0%'");
			
		if((dbISBN.equals(pageBookInfo[0])) && (dbTitle.equals(pageBookInfo[1])) &&
				(dbSubtitle.equals(pageBookInfo[2])) && (dbAuthor.equals(pageBookInfo[3])) &&
				(dbPublisher.equals(pageBookInfo[4])) && (dbPages.equals(pageBookInfo[5])) &&
				(dbDescription.equals(pageBookInfo[6])) && (dbWebsite.equals(pageBookInfo[7])))
		{
			return true;
		}
		else
		{
			System.out.println("----------------");
			System.out.println("This book doesn't match what is in the database: " + dbTitle + ", " + dbISBN);
			if(!dbISBN.equals(pageBookInfo[0]))
				System.out.println("ISBN does not match database record.");
			if(!dbTitle.equals(pageBookInfo[1]))
				System.out.println("Title does not match database record.");
			if(!dbSubtitle.equals(pageBookInfo[2]))
				System.out.println("Subtitle does not match database record.");
			if(!dbAuthor.equals(pageBookInfo[3]))
				System.out.println("Author does not match database record.");
			if(!dbPublisher.equals(pageBookInfo[4]))
				System.out.println("Publisher does not match database record.");
			if(!dbPages.equals(pageBookInfo[5]))
				System.out.println("Number of pages does not match database record.");
			if(!dbDescription.equals(pageBookInfo[6]))
				System.out.println("Description does not match database record.");
			if(!dbWebsite.equals(pageBookInfo[7]))
				System.out.println("Website of origin does not match database record.");
			System.out.println("----------------");
			return false;
		}
	}
	
	//validate book info returned by API matches info returned from database
	//pass 1d string array of book info from database, and string of book info from the API. If matching, return true
	public static boolean dbCompAPI(String[] dbBookInfo, String apiInfo) throws Exception
	{
		String dbISBN = dbBookInfo[0];
		String dbBookTitle = dbBookInfo[1];
		String dbSubTitle = dbBookInfo[2];
		String dbAuthor = dbBookInfo[3];
		String dbPublishDate = dbBookInfo[4];
		String dbPublisher = dbBookInfo[5];
		String dbBookPages = dbBookInfo[6];
		String dbBookDescription = dbBookInfo[7];
		String dbBookWebsite = dbBookInfo[8];
		
		//separate out string into individual segments
		String apiISBN = StringUtils.substringBetween(apiInfo, "isbn\":\"", "\",\"title");
		String apiBookTitle = StringUtils.substringBetween(apiInfo, "title\":\"", "\",\"subTitle");
		String apiSubTitle = StringUtils.substringBetween(apiInfo, "subTitle\":\"", "\",\"author");
		String apiAuthor = StringUtils.substringBetween(apiInfo, "author\":\"", "\",\"publish_date");
		String apiPublishDate = StringUtils.substringBetween(apiInfo, "publish_date\":\"", "\",\"publisher");
		String apiPublisher = StringUtils.substringBetween(apiInfo, "publisher\":\"", "\",\"pages");
		String apiBookPages = StringUtils.substringBetween(apiInfo, "pages\":", ",\"description");
		String apiBookDescription = StringUtils.substringBetween(apiInfo, "description\":\"", "\",\"website").replace("\\\\\\", "\\");// replace "\\\" with "\", since the database doesn't keep the characters the same way as the API returns them 
		String apiBookWebsite = StringUtils.substringBetween(apiInfo, "website\":\"", "\"}");
		
		if((dbISBN.equals(apiISBN)) && (dbBookTitle.equals(apiBookTitle)) && (dbSubTitle.equals(apiSubTitle))&&
				(dbAuthor.equals(apiAuthor)) && (dbPublishDate.equals(apiPublishDate)) && (dbPublisher.equals(apiPublisher)) &&
				(dbBookPages.equals(apiBookPages)) && (dbBookDescription.equals(apiBookDescription)) && (dbBookWebsite.equals(apiBookWebsite)))
		{
			return true;
		}
		else
		{
			System.out.println("Discrepancy detected between API and database for " + dbBookTitle);
			System.out.println("----------------------");
			if(!dbISBN.equals(apiISBN))
			{
				System.out.println("Database ISBN: " + dbISBN);
				System.out.println("API ISBN: " + apiISBN);
				System.out.println("----------------------");
			}
			if(!dbBookTitle.equals(apiBookTitle))
			{
				System.out.println("Database Title: " + dbBookTitle);
				System.out.println("API Title: " + apiBookTitle);
				System.out.println("----------------------");
			}
			if(!dbSubTitle.equals(apiSubTitle))
			{
				System.out.println("Database Subtitle: " + dbSubTitle);
				System.out.println("API Subtitle: " + apiSubTitle);
				System.out.println("----------------------");
			}
			if(!dbAuthor.equals(apiAuthor))
			{
				System.out.println("Database Author: " + dbAuthor);
				System.out.println("API Author: " + apiAuthor);
				System.out.println("----------------------");
			}
			if(!dbPublishDate.equals(apiPublishDate))
			{
				System.out.println("Database Publish Date: " + dbPublishDate);
				System.out.println("API Publish Date: " + apiPublishDate);
				System.out.println("----------------------");
			}
			if(!dbPublisher.equals(apiPublisher))
			{
				System.out.println("Database Publish Date: " + dbPublisher);
				System.out.println("API Publish Date: " + apiPublisher);
				System.out.println("----------------------");
			}
			if(!dbBookPages.equals(apiBookPages))
			{
				System.out.println("Database Pages: " + dbBookPages);
				System.out.println("API Pages: " + apiBookPages);
				System.out.println("----------------------");
			}
			if(!dbBookDescription.equals(apiBookDescription))
			{
				System.out.println("Database Description: " + dbBookDescription);
				System.out.println("API Description: " + apiBookDescription);
				System.out.println("----------------------");
			}
			if(!dbBookWebsite.equals(apiBookWebsite))
			{
				System.out.println("Database Website: " + dbBookWebsite);
				System.out.println("API Website: " + apiBookWebsite);
				System.out.println("----------------------");
			}
			return false;
		}
	}
}
