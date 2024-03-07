package org.automation.seleniumdemo.base;

import java.util.concurrent.TimeUnit;

import org.automation.seleniumdemo.utility.ConfigUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class DriverInstance {
	public WebDriver driver = null;
	
	//method initiates driver instance
	@BeforeMethod
	public void startDriverInstance() throws Exception
	{
		if(ConfigUtil.fetchPropertyValue("browserName").toString().equalsIgnoreCase("chrome"))
		{
			System.setProperty("webdriver.chrome.driver", "./Driver/chromedriver.exe");
			driver = new ChromeDriver();
		}
		else if(ConfigUtil.fetchPropertyValue("browserName").toString().equalsIgnoreCase("firefox"))
		{
			System.setProperty("webdriver.gecko.driver", "./Driver/geckodriver.exe");
			driver = new FirefoxDriver();
		}
		else if(ConfigUtil.fetchPropertyValue("browserName").toString().equalsIgnoreCase("edge"))
		{
			System.setProperty("webdriver.edge.driver", "./Driver/msedgedriver.exe");
			driver = new EdgeDriver();
		}
		else
		{
			System.setProperty("webdriver.chrome.driver", "./Driver/chromedriver.exe");
			driver = new ChromeDriver();
		}
		driver.manage().window().maximize();
		driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		driver.get(ConfigUtil.fetchPropertyValue("appURL").toString());//https://demoqa.com/login
	}
	
	
	//ends driver instance
	@AfterMethod
	public void closeDriverInstance()
	{
		System.out.println("Quit Driver");
		driver.quit();
	}
}
