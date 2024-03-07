package org.automation.seleniumdemo.utility;
 
import java.io.File;
import java.util.Calendar;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

//this class allows the user to capture a screenshot of a webpage
public class CapScreenshot {
	
	//capture a screenshot of the current webpage
	//pass the webdriver, and a string describing the image being captured
	public static void screenshot(WebDriver driver, String shotOf) throws Exception
	{
		Calendar cal = Calendar.getInstance();
		String c = cal.getTime().toString().replace(":", "");
		TakesScreenshot shot = (TakesScreenshot)driver;
		File f = shot.getScreenshotAs(OutputType.FILE);
		File fileDest = new File("./Screenshots/" + shotOf + "_" + c + ".png");
		FileUtils.copyFile(f, fileDest);
	}
}
