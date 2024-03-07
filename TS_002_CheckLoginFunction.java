package org.automation.seleniumdemo.testcases;

import org.automation.seleniumdemo.assertions.Compare;
import org.automation.seleniumdemo.base.DriverInstance;
import org.automation.seleniumdemo.pages.LoginPage;
import org.automation.seleniumdemo.utility.JDBCUtil;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TS_002_CheckLoginFunction extends DriverInstance {
	
	//data provider for login test
	@DataProvider(name="LoginData")
	public Object[][] getLoginData() throws Exception
	{
		return JDBCUtil.getLogin();
	}
	
	//test user login
	@Test(dataProvider = "LoginData")
	public void tc_001_checkLogin(String username, String pswrd, boolean validCreds, String userID) throws Exception
	{
		LoginPage login = new LoginPage(driver);
		login.enterUsername(username);
		login.enterPassword(pswrd);
		login.clickLogin();
		Thread.sleep(1500);//delay thread so new page can load
		
		//if expected result of attempted login is a correct login, validate if the user has reached the correct page.
		if(validCreds)
		{
			System.out.println("True");
			Assert.assertTrue(Compare.validatePageURL(driver, "https://demoqa.com/profile", "Profile"), "Login failed when it should have succeeded.");
		}
		else
		{
			System.out.println("False");
			Assert.assertFalse(Compare.validatePageURL(driver, "https://demoqa.com/profile", "Login_PurposeFail"), "Login succeeded when it should have failed.");
		}
		System.out.println("end of login check");
	 }
}
