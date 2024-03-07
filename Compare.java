package org.automation.seleniumdemo.assertions;

import org.automation.seleniumdemo.utility.CapScreenshot;
import org.openqa.selenium.WebDriver;

public class Compare {
	
	//validate page URL, capture image of page
	//pass driver, expected URL, and the title of the image to be captured. Return true if expected and actual URL match
	public static boolean validatePageURL(WebDriver driver, String expURL, String imgTitle) throws Exception
	{
		boolean flag = false;
		CapScreenshot.screenshot(driver, imgTitle);
		if(driver.getCurrentUrl().equalsIgnoreCase(expURL))
		{
			flag=true;
		}
		//System.out.println(imgTitle + ": " + flag);
		return flag;
	}
}
