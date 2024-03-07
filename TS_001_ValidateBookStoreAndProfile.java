package org.automation.seleniumdemo.testcases;

import org.automation.seleniumdemo.assertions.Compare;
import org.automation.seleniumdemo.assertions.ElementExists;
import org.automation.seleniumdemo.assertions.VerifyBookInfo;
import org.automation.seleniumdemo.base.DriverInstance;
import org.automation.seleniumdemo.pages.BookStorePage;
import org.automation.seleniumdemo.pages.LoginPage;
import org.automation.seleniumdemo.pages.ProfileBookPage;
import org.automation.seleniumdemo.pages.ProfilePage;
import org.automation.seleniumdemo.utility.APICalls;
import org.automation.seleniumdemo.utility.CapScreenshot;
import org.automation.seleniumdemo.utility.JDBCUtil;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.Test;

//Test book management functionality
public class TS_001_ValidateBookStoreAndProfile extends DriverInstance{
	
	
	//check the APis
	@Test//get this to run before other checks, if the APIs don't work, then the webpages won't either
	public void tc_001_checkAPIs() throws Exception
	{
		System.out.println("Checking APIs");
		//retrieve login info for single user to run check
		String []loginDetails = JDBCUtil.getSingleFunctionalLogin();
		String localUserName = loginDetails[0];
		String localPassword = loginDetails[1];
		String localUserID = loginDetails[2];
		
		//Clear books, called API will verify command was executed
		APICalls.clearAllBooks(localUserName, localPassword, localUserID);
		//check account clear
		Assert.assertEquals(APICalls.getUserBookCount(localUserName, localPassword, localUserID), 0, "API call to clear profile failed.");
		
		Object [][]allDBBooks = JDBCUtil.getAllBookInfo();
		
		//check bookstore book count
		Assert.assertEquals(APICalls.getBookStoreBookCount(), allDBBooks.length, "Number of books in database does not match count returned by API.");
		
		//check book info returned by API vs info in database
		for (int i=0; i<allDBBooks.length; i++)
		{
			String dbBookInfo[] = {(String) allDBBooks[i][0], (String) allDBBooks[i][1],(String) allDBBooks[i][2],
					(String) allDBBooks[i][3], (String) allDBBooks[i][4], (String) allDBBooks[i][5],
					allDBBooks[i][6].toString(), (String) allDBBooks[i][7], (String) allDBBooks[i][8]};
			String apiBookInfo = APICalls.getBookInfo(dbBookInfo[0]);
			Assert.assertTrue(VerifyBookInfo.dbCompAPI(dbBookInfo, apiBookInfo), "Book info in API does not match database info.");
		}
		
		//pull single book info from database
		Object []recordedBookInfo = JDBCUtil.getSingleBookInfo();
		String isbn = (String) recordedBookInfo[0];
		String bookTitle = (String) recordedBookInfo[1];
		
		//add book
		APICalls.addBook(isbn, localUserName, localPassword, localUserID);
		
		//verify book presence
		Assert.assertEquals(APICalls.getUserBookCount(localUserName, localPassword, localUserID), 1, "Improper count of books in user profile.");
		Assert.assertTrue(APICalls.isBookUnderUser(bookTitle, localUserName, localPassword, localUserID), bookTitle + " was is not in user profile as expected.");
		
		//delete book and verify empty list
		APICalls.deleteBook(isbn, localUserName, localPassword, localUserID);
		Assert.assertEquals(APICalls.getUserBookCount(localUserName, localPassword, localUserID), 0, "API call to clear profile failed.");
		
		//add multiple books
		Object [][]multiBookDetails = JDBCUtil.getTwoBookInfo();
		APICalls.addBook((String) multiBookDetails[0][0], localUserName, localPassword, localUserID);
		APICalls.addBook((String) multiBookDetails[1][0], localUserName, localPassword, localUserID);
		
		//verify books are present
		Assert.assertEquals(APICalls.getUserBookCount(localUserName, localPassword, localUserID), 2, "Improper count of books in user profile.");
		Assert.assertTrue(APICalls.isBookUnderUser((String) multiBookDetails[0][1], localUserName, localPassword, localUserID), (String) multiBookDetails[0][1] + " was not in user profile as expected.");
		Assert.assertTrue(APICalls.isBookUnderUser((String) multiBookDetails[1][1], localUserName, localPassword, localUserID), (String) multiBookDetails[1][1] + " was not in user profile as expected.");
		
		//clear profile, and verify profile is clear
		APICalls.clearAllBooks(localUserName, localPassword, localUserID);
		Assert.assertEquals(APICalls.getUserBookCount(localUserName, localPassword, localUserID), 0, "API call to clear profile failed.");
	}
	
	//check bookstore page information
	@Test(dependsOnMethods={"tc_001_checkAPIs"})
	public void tc_002_checkStorePage() throws Exception
	{
		System.out.println("Checking Bookstore");
		String []loginDetails = JDBCUtil.getSingleFunctionalLogin();
		String localUserName = loginDetails[0];
		String localPassword = loginDetails[1];
		//String localUserID = loginDetails[2];
		
		LoginPage login = new LoginPage(driver);//A login page object
		ProfilePage profile = new ProfilePage(driver);//A profile page object
		BookStorePage store = new BookStorePage(driver);//A bookstore page object
		//BookPage book = new BookPage(driver);
		
		//all books in database
		Object [][]allRecordedBooks = JDBCUtil.getAllBookInfo();
		
		//login
		login.enterUsername(localUserName);
		login.enterPassword(localPassword);
		login.clickLogin();//click login button
		
		Thread.sleep(1500);
		Assert.assertTrue(Compare.validatePageURL(driver, "https://demoqa.com/profile", "Profile_TC2"), "URL for profile does not match.");
		
		//go to library page and validate page url
		profile.clickBookStore();
		
		Thread.sleep(1500);
		Assert.assertTrue(Compare.validatePageURL(driver, "https://demoqa.com/books", "Bookstore_TC2"), "URL for profile does not match.");
		
		//loop through complete book list and verify correct information is displayed on screen (Title, author, publisher)
		String []pulledBookData = new String[3];
		for(int i=0; i<allRecordedBooks.length; i++)
		{
			pulledBookData = store.getBookText((String) allRecordedBooks[i][1]);
			CapScreenshot.screenshot(driver,"Bookstore_TC2"+((String) allRecordedBooks[i][1]));
			Assert.assertEquals(pulledBookData[0], (String) allRecordedBooks[i][1]);
			Assert.assertEquals(pulledBookData[1],(String) allRecordedBooks[i][3]);
			Assert.assertEquals(pulledBookData[2],(String) allRecordedBooks[i][5]);
		}
	}
	
	//add books via API, as bookstore book-page is down, check that correct books are in profile page and can be removed
	@Test(dependsOnMethods={"tc_001_checkAPIs"})
	public void tc_003_checkProfile() throws Exception
	{
		System.out.println("Checking Profile");
		//collect login details
		String []loginDetails = JDBCUtil.getSingleFunctionalLogin();
		String localUserName = loginDetails[0];
		String localPassword = loginDetails[1];
		String localUserID = loginDetails[2];
		
		//create needed page objects
		LoginPage login = new LoginPage(driver);//A login page object
		ProfilePage profile = new ProfilePage(driver);//A profile page object
		
		//build list of books to add
		Object [][]allRecordedBooks = JDBCUtil.getAllBookInfo();
		int bookAddAllBookIndex[] = {0, 1, 2, 3};//vertical index of books from allRecordedBooks that will be added
		int singleDelIndex[]= {0, 1};//vertical index of books from allRecordedBooks that will be deleted singly
		int otherIndex[] ={2, 3};//vertical index of books from allRecordedBooks that will not be deleted singly
		
		//clear profile bookshelf, then add books via API
		APICalls.clearAllBooks(localUserName, localPassword, localUserID);
		Assert.assertEquals(APICalls.getUserBookCount(localUserName, localPassword, localUserID), 0, "API call to clear profile failed.");
		for (int i=0; i<bookAddAllBookIndex.length; i++)
		{
			APICalls.addBook((String) allRecordedBooks[bookAddAllBookIndex[i]][0], localUserName, localPassword, localUserID);
		}
		Assert.assertEquals(APICalls.getUserBookCount(localUserName, localPassword, localUserID), bookAddAllBookIndex.length, "Failed to add proper number of books");
		
		//verify books in profile
		for (int i=0; i<bookAddAllBookIndex.length; i++)
		{
			Assert.assertTrue(APICalls.isBookUnderUser((String) allRecordedBooks[bookAddAllBookIndex[i]][1], localUserName, localPassword, localUserID), 
			"The book: " + ((String) allRecordedBooks[bookAddAllBookIndex[i]][1]) + " was not in the user's profile.");
		}
		
		//login, verify books are present on website, info is correct
		login.enterUsername(localUserName);
		login.enterPassword(localPassword);
		login.clickLogin();//click login button
		
		Thread.sleep(1500);
		Assert.assertTrue(Compare.validatePageURL(driver, "https://demoqa.com/profile", "Profile_TC3"), "URL for profile does not match.");
		
		//verify added titles exist, and have the correct attributes
		String []pulledBookData = new String[3];
		for(int i=0; i<bookAddAllBookIndex.length; i++)
		{
			Assert.assertTrue(ElementExists.validateProfileBookElementExists(driver, (String) allRecordedBooks[bookAddAllBookIndex[i]][1]));
			pulledBookData = profile.getBookText((String) allRecordedBooks[bookAddAllBookIndex[i]][1]);
			CapScreenshot.screenshot(driver,"Profile_TC3"+((String) allRecordedBooks[bookAddAllBookIndex[i]][1])+"_added");
			Assert.assertEquals(pulledBookData[0], (String) allRecordedBooks[bookAddAllBookIndex[i]][1]);
			Assert.assertEquals(pulledBookData[1], (String) allRecordedBooks[bookAddAllBookIndex[i]][3]);
			Assert.assertEquals(pulledBookData[2], (String) allRecordedBooks[bookAddAllBookIndex[i]][5]);
		}
		
		//delete a book and verify that it is gone, at least once
		for(int i=0; i<singleDelIndex.length; i++)
		{
			profile.deleteBook((String) allRecordedBooks[singleDelIndex[i]][1]);
			CapScreenshot.screenshot(driver,"Profile_TC3"+((String) allRecordedBooks[singleDelIndex[i]][1]) + "_removed");
			Assert.assertTrue(ElementExists.validateProfileBookElementGone(driver, (String) allRecordedBooks[singleDelIndex[i]][1]));
		}
		//verify other books are still present
		for(int i=0; i<otherIndex.length; i++)
		{
			Assert.assertTrue(ElementExists.validateProfileBookElementExists(driver, (String) allRecordedBooks[otherIndex[i]][1]));
		}
		
		//delete all books and verify they are gone
		profile.clickDeleteAllBooks();
		Thread.sleep(1500);
		
		//scroll to top of page for best snapshot
		((JavascriptExecutor)driver).executeScript("window.scrollTo(document.body.scrollHeight,0)");
		CapScreenshot.screenshot(driver,"Profile_TC3_AllGone");
		for(int i=0; i<bookAddAllBookIndex.length; i++)
		{
			Assert.assertTrue(ElementExists.validateProfileBookElementGone(driver, (String) allRecordedBooks[bookAddAllBookIndex[i]][1]));
		}
		Assert.assertEquals(APICalls.getUserBookCount(localUserName, localPassword, localUserID), 0, "Improper number of books shown in API response.");
	}

	
	//verify all book info, use ProfileBookPage, as the book page under the bookstore is currently broken
	@Test(dependsOnMethods={"tc_001_checkAPIs"})
	public void tc_004_checkBookInfo() throws Exception
	{
		System.out.println("Checking Books");
		//collect login details
		String []loginDetails = JDBCUtil.getSingleFunctionalLogin();
		String localUserName = loginDetails[0];
		String localPassword = loginDetails[1];
		String localUserID = loginDetails[2];
		
		//create needed page objects
		LoginPage login = new LoginPage(driver);//A login page object
		ProfilePage profile = new ProfilePage(driver);//A profile page object
		ProfileBookPage probook = new ProfileBookPage(driver);//Book page as accessed through profile.
		
		//build list of books to add
		Object [][]allRecordedBooks = JDBCUtil.getAllBookInfo();
		
		//add books via API
		APICalls.clearAllBooks(localUserName, localPassword, localUserID);
		Assert.assertEquals(APICalls.getUserBookCount(localUserName, localPassword, localUserID), 0, "API does not show profile has been correctly cleared.");
		for (int i=0; i<allRecordedBooks.length; i++)
		{
			APICalls.addBook((String) allRecordedBooks[i][0], localUserName, localPassword, localUserID);
		}
		
		Assert.assertEquals(APICalls.getUserBookCount(localUserName, localPassword, localUserID), allRecordedBooks.length, "Failed to add proper number of books.");
		
		//verify books in profile
		for (int i=0; i<allRecordedBooks.length; i++)
		{
			Assert.assertTrue(APICalls.isBookUnderUser((String) allRecordedBooks[i][0], localUserName, localPassword, localUserID), 
			"The book: " + ((String) allRecordedBooks[i][0]) + " was not in the user's profile.");
		}
		
		//login, verify books are present, info is correct
		login.enterUsername(localUserName);
		login.enterPassword(localPassword);
		login.clickLogin();//click login button
		
		
		Thread.sleep(1500);
		Assert.assertTrue(Compare.validatePageURL(driver, "https://demoqa.com/profile", "Profile_TC4"), "URL for profile does not match.");
		
		
		//verify added titles exist, have the correct attributes, and that the information on the book page is correct
		for (int i=0; i<allRecordedBooks.length; i++)
		{
			String []pulledBookData = new String[3];//data for book on profile page
			profile.showMaxRows();
			Thread.sleep(1500);
			
			pulledBookData = profile.getBookText((String) allRecordedBooks[i][1]);
			CapScreenshot.screenshot(driver,"Profile_TC4"+((String) allRecordedBooks[i][1])+"_added");
			Assert.assertEquals(pulledBookData[0], (String) allRecordedBooks[i][1]);//verify title
			Assert.assertEquals(pulledBookData[1], (String) allRecordedBooks[i][3]);//verify author
			Assert.assertEquals(pulledBookData[2], (String) allRecordedBooks[i][5]);//verify publisher
			
			
			String dbBookInfo[] = {(String) allRecordedBooks[i][0], (String) allRecordedBooks[i][1],
					(String) allRecordedBooks[i][2], (String) allRecordedBooks[i][3], (String) allRecordedBooks[i][5],
					allRecordedBooks[i][6].toString(), (String) allRecordedBooks[i][7], (String) allRecordedBooks[i][8]};
			profile.selectBook((String) allRecordedBooks[i][1]);
			//verify page URL
			Assert.assertTrue(Compare.validatePageURL(driver, "https://demoqa.com/profile?book="+dbBookInfo[0], "Book_TC4_Page for " + dbBookInfo[1]), "URL for Profile Book Page does not match.");
			
			//verify book info compared to database
			Assert.assertTrue(VerifyBookInfo.checkBookPage(driver, dbBookInfo, probook.gatherBookInfo()));
			Thread.sleep(1500);
			probook.returnToProfile();
			Thread.sleep(1500);
			Assert.assertTrue(Compare.validatePageURL(driver, "https://demoqa.com/profile", "Profile_TC4_Return"), "URL for profile does not match.");
		}
		//Run cleanup
		APICalls.clearAllBooks(localUserName, localPassword, localUserID);
	}
}