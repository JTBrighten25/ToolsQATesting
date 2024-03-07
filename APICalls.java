package org.automation.seleniumdemo.utility;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.testng.Assert;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

//The various types of API calls for the bookstore. This class makes use of the RestAssured library
public class APICalls {
	static String baseUrl = "https://bookstore.toolsqa.com";
	
	//return the number of books in the bookstore
	public static int getBookStoreBookCount() throws Exception
	{
		//assign the base URL and the request specification
		RestAssured.baseURI = baseUrl;
		RequestSpecification request = RestAssured.given();
				
		//define the header for the following requests
		//request.header("Content-type", "application/json");
		
		//make request and save response
		Response response = request.get("/BookStore/v1/Books");
		
		//verify good response code
		Assert.assertEquals(response.getStatusCode(), 200, "Get request in getBookStoreBookCount did not get code 200, got " + response.getStatusCode() +  " instead.");
		
		//get count of books in bookstore
		String jsonString = response.asString();
		List<Map<String,String>> books = JsonPath.from(jsonString).get("books");
		return books.size();
	}
	
	//print the books in the bookstore.
	public static void printBookStoreList() throws Exception
	{
		//assign the base URL and the request specification
		RestAssured.baseURI = baseUrl;
		RequestSpecification request = RestAssured.given();
				
		//define the header for the following requests
		//request.header("Content-type", "application/json");
		
		//make request and save response
		Response response = request.get("/BookStore/v1/Books");
		
		//verify good response code
		Assert.assertEquals(response.getStatusCode(), 200, "Get request in printBookStoreList did not get code 200, got " + response.getStatusCode() +  " instead.");
		
		//print out list of books in bookstore.
		String jsonString = response.asString();
		List<Map<String,String>> books = JsonPath.from(jsonString).get("books");
		System.out.println(books.toString());
	}
	
	//return information about a selected book, pass the ISBN of the book
	public static String getBookInfo(String bookId) throws Exception
	{
		//assign the base URL and the request specification
		RestAssured.baseURI = baseUrl;
		RequestSpecification request = RestAssured.given();
						
		//define the header for the following requests
		//request.header("Content-type", "application/json");
		
		//make request, save response, print out info about the book
		Response response = request.queryParam("ISBN", bookId).get("/BookStore/v1/Book");
		Assert.assertEquals(response.getStatusCode(), 200, "Get request in getBookInfo did not get code 200, got " + response.getStatusCode() +  " instead.");
		return response.asString();
		//System.out.println(response.asString());
	}
	
	//returns true if the book is under the user.
	//Pass the bookname, the user's username, the user's password, and the user's userID.
	public static boolean isBookUnderUser(String bookname, String userName, String password, String userID) throws Exception
	{
		//assign the base URL and the request specification
		RestAssured.baseURI = baseUrl;
		RequestSpecification request = RestAssured.given();
						
		//define the header for the following requests
		request.header("Content-type", "application/json");
		
		//Generate token for Authorization
		//Response response = request.body("{ \"userName\":\"" + userName + "\", \"password\":\"" + password + "\"}")
			//.post("/Account/v1/GenerateToken");
		
		//post login information to receive authorization token
		JSONObject loginbody = new JSONObject();
		loginbody.put("userName", userName);
		loginbody.put("password", password);
		Response response = request.given().body(loginbody.toString()).post("/Account/v1/GenerateToken");
		
		//verify good status code
		Assert.assertEquals(response.getStatusCode(), 200, "Post request in isBookUnderUser did not get code 200, got " + response.getStatusCode() +  " instead.");
		
		//get the returned authorization token
		String jsonString = response.asString();
		Assert.assertTrue(jsonString.contains("token"));
		//keep token
		String token = JsonPath.from(jsonString).getString("token");//assigning token value here
		
		//update headers with authorization token
		request.header("Authorization", "Bearer " + token).header("Content-Type", "application/json");
		
		//get user account information (which should contain books under account)
		response = request.get("/Account/v1/User/" + userID);
		
		//verify good status code
		Assert.assertEquals(response.getStatusCode(), 200, "Get request in isBookUnderUser did not get code 200, got " + response.getStatusCode() +  " instead.");
		
		//convert response to a String
		jsonString = response.asString();
		List<Map<String, String>> booksOfUser = JsonPath.from(jsonString).get("books");
		
		//if selected book is contained in the list of books under the account, return true.
		return booksOfUser.toString().contains(bookname);
		//return booksOfUser.size();
	}
	
	//returns the number of books under the user
	//Pass the user's username, the user's password, and the user's userID.
	public static int getUserBookCount(String userName, String password, String userID) throws Exception
	{
		//assign the base URL and the request specification
		RestAssured.baseURI = baseUrl;
		RequestSpecification request = RestAssured.given();
						
		//define the header for the following requests
		request.header("Content-type", "application/json");

		//post login information to receive authorization token
		JSONObject loginbody = new JSONObject();
		loginbody.put("userName", userName);
		loginbody.put("password", password);
		Response response = request.given().body(loginbody.toString()).post("/Account/v1/GenerateToken");
		
		//verify good status code
		Assert.assertEquals(response.getStatusCode(), 200, "Post request in getUserBookCount did not get code 200, got " + response.getStatusCode() +  " instead.");
		
		//get the returned authorization token
		String jsonString = response.asString();
		Assert.assertTrue(jsonString.contains("token"));
		String token = JsonPath.from(jsonString).getString("token");//assigning token value here
		
		//update headers with authorization token
		request.header("Authorization", "Bearer " + token).header("Content-Type", "application/json");
		
		//get user account information
		response = request.get("/Account/v1/User/" + userID);
		
		//verify good response code
		Assert.assertEquals(response.getStatusCode(), 200, "Get request in getUserBookCount did not get code 200, got " + response.getStatusCode() +  " instead.");
		
		//convert response to string, this print list of books
		jsonString = response.asString();
		List<Map<String, String>> booksOfUser = JsonPath.from(jsonString).get("books");
		return booksOfUser.size();
	}
	
	//return the list of books under the user
	//Pass the user's username, the user's password, and the user's userID.
	public static String getUserBookList(String userName, String password, String userID) throws Exception
	{
		//assign the base URL and the request specification
		RestAssured.baseURI = baseUrl;
		RequestSpecification request = RestAssured.given();
						
		//define the header for the following requests
		request.header("Content-type", "application/json");

		//post login information to receive authorization token
		JSONObject loginbody = new JSONObject();
		loginbody.put("userName", userName);
		loginbody.put("password", password);
		Response response = request.given().body(loginbody.toString()).post("/Account/v1/GenerateToken");
		
		//verify good status code
		Assert.assertEquals(response.getStatusCode(), 200, "Post request in getUserBookList did not get code 200, got " + response.getStatusCode() +  " instead.");
		
		//get the returned authorization token
		String jsonString = response.asString();
		Assert.assertTrue(jsonString.contains("token"));
		String token = JsonPath.from(jsonString).getString("token");//assigning token value here
		
		//update headers with authorization token
		request.header("Authorization", "Bearer " + token).header("Content-Type", "application/json");
		
		//get user account information
		response = request.get("/Account/v1/User/" + userID);
		
		//verify good response code
		Assert.assertEquals(response.getStatusCode(), 200, "Get request in getUserBookList did not get code 200, got " + response.getStatusCode() +  " instead.");
		
		//convert response to string, this print list of books
		jsonString = response.asString();
		List<Map<String, String>> booksOfUser = JsonPath.from(jsonString).get("books");
		return booksOfUser.toString();
	}
	
	//add book to user account
	//Pass the ISBN of the book, the user's username, the user's password, and the user's userID.
	public static void addBook(String bookId, String userName, String password, String userID) throws Exception
	{
		//assign the base URL and the request specification
		RestAssured.baseURI = baseUrl;
		RequestSpecification request = RestAssured.given();
						
		//define the header for the following requests
		request.header("Content-type", "application/json");
		
		//post login information to receive authorization token
		JSONObject loginbody = new JSONObject();
		loginbody.put("userName", userName);
		loginbody.put("password", password);
		Response response = request.given().body(loginbody.toString()).post("/Account/v1/GenerateToken");
		
		//verify good response code
		Assert.assertEquals(response.getStatusCode(), 200, "Post request for authorization token in addBook did not get code 200, got " + response.getStatusCode() +  " instead.");
		
		//get returned authorization token
		String jsonString = response.asString();
		Assert.assertTrue(jsonString.contains("token"));
		String token = JsonPath.from(jsonString).getString("token");//assigning token value here
		
		//update header with new authorization token
		request.header("Authorization", "Bearer " + token).header("Content-Type", "application/json");
		
		//organize json objects
		JSONObject body = new JSONObject();//body for request
		JSONObject books = new JSONObject();//list of books to be placed in request body
		body.put("userId", userID);//put userID into body
		books.put("isbn", bookId);//put the isbn into the json object
		List<JSONObject> bookList = Arrays.asList(books);//make a list of the books
		body.put("collectionOfIsbns", bookList);//put list of books into request body
		
		//make the request, verify the response code, and print the response
		//System.out.println(body.toString());
		response = request.given().body(body.toString()).post("/BookStore/v1/Books");
		Assert.assertEquals(response.getStatusCode(), 201, "Post request to add book in addBook did not get code 201, got " + response.getStatusCode() +  " instead.");
		//System.out.println(response.statusLine());
	}
	
	//delete a book from the user account
	//Pass the ISBN of the book, the user's username, the user's password, and the user's userID.
	public static void deleteBook(String bookId, String userName, String password, String userID) throws Exception
	{
		//assign the base URL and the request specification
		RestAssured.baseURI = baseUrl;
		RequestSpecification request = RestAssured.given();
						
		//define the header for the following requests
		request.header("Content-type", "application/json");
		
		//post login information to receive authorization token
		JSONObject loginbody = new JSONObject();
		loginbody.put("userName", userName);
		loginbody.put("password", password);
		Response response = request.given().body(loginbody.toString()).post("/Account/v1/GenerateToken");
		
		//verify good status code, verify presence of token, and retrieve token
		Assert.assertEquals(response.getStatusCode(), 200, "Post request in deleteBook did not get code 200, got " + response.getStatusCode() +  " instead.");
		String jsonString = response.asString();
		Assert.assertTrue(jsonString.contains("token"));
		String token = JsonPath.from(jsonString).getString("token");//assigning token value here
		
		//update headers with authorization token
		request.header("Authorization", "Bearer " + token).header("Content-Type", "application/json");
		
		//build new request body containing isbn and userID.
		JSONObject body = new JSONObject();
		body.put("isbn", bookId);
		body.put("userId", userID);
		
		//make request
		response = request.given().body(body.toString()).delete("/BookStore/v1/Book");
		//response = request.body("{ \"isbn\": \"" + bookId + "\", \"userId\": \"" + userID + "\"}")
				//.delete("/BookStore/v1/Book");
		
		//verify desired response code
		Assert.assertEquals(response.getStatusCode(), 204, "Delete request in deleteBook did not get code 204, got " + response.getStatusCode() +  " instead.");
		//System.out.println("Delete Check: " + response.getStatusCode());
	}
	
	//replace a given book
	//Pass the ISBN of the old book, the ISBN of the new book, the user's username, the user's password, and the user's userID.
	public static void replaceBook(String bookId1, String bookId2, String userName, String password, String userID) throws Exception
	{
		//assign the base URL and the request specification
		RestAssured.baseURI = baseUrl;
		RequestSpecification request = RestAssured.given();
						
		//define the header for the following requests
		request.header("Content-type", "application/json");
		//Generate token for Authorization
		JSONObject loginbody = new JSONObject();
		loginbody.put("userName", userName);
		loginbody.put("password", password);
		Response response = request.given().body(loginbody.toString()).post("/Account/v1/GenerateToken");
		
		//verify good status code and presence of token
		Assert.assertEquals(response.getStatusCode(), 200, "Post request in replaceBook did not get code 200, got " + response.getStatusCode() +  " instead.");
		String jsonString = response.asString();
		Assert.assertTrue(jsonString.contains("token"));
		
		//keep token
		String token = JsonPath.from(jsonString).getString("token");//assigning token value here
		
		//update headers with authorization token
		request.header("Authorization", "Bearer " + token).header("Content-Type", "application/json");
		
		//make 'put' request to update books
		String ext = "/BookStore/v1/Books/" + bookId1;//url extension
		JSONObject body = new JSONObject();//request body
		body.put("userId", userID);
		body.put("isbn", bookId2);
		response = request.given().body(body.toString()).put(ext);
		
		//verify good response code
		Assert.assertEquals(response.getStatusCode(), 204, "Put request in replaceBook did not get code 204, got " + response.getStatusCode() +  " instead.");
		//System.out.println("Book replaced");
	}
	//clear all books from under user
	//Pass the user's username, the user's password, and the user's userID.
	public static void clearAllBooks(String userName, String password, String userID) throws Exception
	{
		//assign the base URL and the request specification
		RestAssured.baseURI = baseUrl;
		RequestSpecification request = RestAssured.given();
						
		//define the header for the following requests
		request.header("Content-type", "application/json");
		
		//post login for authorization token
		//Build message with JSON
		JSONObject loginbody = new JSONObject();
		loginbody.put("userName", userName);
		loginbody.put("password", password);
		Response response = request.given().body(loginbody.toString()).post("/Account/v1/GenerateToken");
		//Response response = request.body("{ \"userName\":\"" + userName + "\", \"password\":\"" + password + "\"}")
                //.post("/Account/v1/GenerateToken");
		
		//verify good code and that response contains authorization token
		Assert.assertEquals(response.getStatusCode(), 200, "Post request in clearAllBooks did not get code 200, got " + response.getStatusCode() +  " instead.");
		String jsonString = response.asString();
		Assert.assertTrue(jsonString.contains("token"));
		//keep token
		String token = JsonPath.from(jsonString).getString("token");//assigning token value here
		
		//updating header with authorization token
		request.header("Authorization", "Bearer " + token).header("Content-Type", "application/json");
		
		//send request to delete books under user
		response = request.delete("/BookStore/v1/Books?UserId="+userID);
		
		//verify good status code
		Assert.assertEquals(response.getStatusCode(), 204, "Delete request in clearAllBooks did not get code 204, got " + response.getStatusCode() +  " instead.");
		//System.out.println("User collection cleared.");
	}
}
