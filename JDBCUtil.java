package org.automation.seleniumdemo.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JDBCUtil {
	static final String DB_URL = "jdbc:mysql://localhost:3306/bookstore_database";
	
	//return database connection
	public static Connection getMySQLConnection() throws Exception
	{
		String USER = ConfigUtil.fetchPropertyValue("dbUser").toString();
		String PASS = ConfigUtil.fetchPropertyValue("dbPass").toString();
		Connection conn = null;
		conn = DriverManager.getConnection(DB_URL, USER, PASS);
		return conn;
	}
	
	//cleanup database connection. Pass Connection, Statement, and ResultSet
	public static void cleanupConnection(Connection conn, Statement st, ResultSet rs) throws Exception
	{
		try
		{
			if(rs!=null)
				rs.close();
			if(st!=null)
				st.close();
			if(conn!=null)
				conn.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	//return 2d array of objects containing login information
	public static Object[][] getLogin() throws Exception
	{
		//connect to database
		String USER = ConfigUtil.fetchPropertyValue("dbUser").toString();
		String PASS = ConfigUtil.fetchPropertyValue("dbPass").toString();
		
		Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
		Statement stmt = conn.createStatement();
		String sqlQry = "SELECT * FROM UserLoginTbl";
		ResultSet rs = stmt.executeQuery(sqlQry);
		List<Object> items = new ArrayList<Object>();
		int i=0;
		while(rs.next())
		{
			i++;
			items.add(rs.getString(1));
			items.add(rs.getString(2));
			items.add(rs.getBoolean(3));
			items.add(rs.getString(4));
		}

		Object loginArray[][]= new Object[i][4];
		for(int y=0; y<i;y++)
		{
			for(int x=0;x<4;x++)
			{
				loginArray[y][x]=items.get((y*4)+x);
			}
		}
		cleanupConnection(conn,stmt,rs);
		return loginArray;
	}
	
	//return 2d array of objects where login will succeed
	public static Object[][] getFunctionalLogin() throws Exception
	{
		Connection conn = getMySQLConnection();
		Statement stmt = conn.createStatement();
		String sqlQry = "SELECT username,pswrd,userid FROM UserLoginTbl WHERE valid_creds=true";
		ResultSet rs = stmt.executeQuery(sqlQry);
		List<Object> items = new ArrayList<Object>();
		int i=0;
		while(rs.next())
		{
			i++;
			items.add(rs.getString(1));
			items.add(rs.getString(2));
			items.add(rs.getString(3));
		}
		Object loginArray[][]= new Object[i][3];
		for(int y=0; y<i;y++)
		{
			for(int x=0;x<3;x++)
			{
				loginArray[y][x]=items.get((y*3)+x);
			}
		}
		cleanupConnection(conn,stmt,rs);
		return loginArray;
	}
	
	//return an array of login information for a single user.
	public static String[] getSingleFunctionalLogin() throws Exception
	{
		Connection conn = getMySQLConnection();
		Statement stmt = conn.createStatement();
		String sqlQry = "SELECT username,pswrd,userid FROM UserLoginTbl WHERE valid_creds=true";
		ResultSet rs = stmt.executeQuery(sqlQry);
		rs.next();
		String loginArray[]= {rs.getString(1),rs.getString(2),rs.getString(3)};
		cleanupConnection(conn,stmt,rs);
		return loginArray;
	}
	
	//return information on a single book in the database
	public static Object[] getSingleBookInfo() throws Exception
	{
		Connection conn = getMySQLConnection();
		Statement stmt = conn.createStatement();
		String sqlQry = "SELECT * FROM BookDataTbl";
		ResultSet rs = stmt.executeQuery(sqlQry);
		rs.next();
		Object bookArray[]= {rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6), 
				rs.getInt(7),rs.getString(8),rs.getString(9)};
		cleanupConnection(conn,stmt,rs);
		return bookArray;
	}
	
	//return the information on two books in the database
	public static Object[][] getTwoBookInfo() throws Exception
	{
		Connection conn = getMySQLConnection();
		Statement stmt = conn.createStatement();
		String sqlQry = "SELECT * FROM BookDataTbl";
		ResultSet rs = stmt.executeQuery(sqlQry);
		List<Object> books = new ArrayList<Object>();
		rs.next();
		books.add(rs.getString(1));
		books.add(rs.getString(2));
		books.add(rs.getString(3));
		books.add(rs.getString(4));
		books.add(rs.getString(5));
		books.add(rs.getString(6));
		books.add(rs.getInt(7));
		books.add(rs.getString(8));
		books.add(rs.getString(9));
		
		rs.next();
		books.add(rs.getString(1));
		books.add(rs.getString(2));
		books.add(rs.getString(3));
		books.add(rs.getString(4));
		books.add(rs.getString(5));
		books.add(rs.getString(6));
		books.add(rs.getInt(7));
		books.add(rs.getString(8));
		books.add(rs.getString(9));
		Object bookArray[][] = new Object[2][9];
		for(int y=0; y<2; y++)
		{
			for (int x=0; x<9; x++)
			{
				bookArray[y][x]=books.get((y*9)+x);
			}
		}
		cleanupConnection(conn,stmt,rs);
		return bookArray;
	}
	
	public static Object[][] getAllBookInfo() throws Exception
	{
		//connect to database
		String USER = ConfigUtil.fetchPropertyValue("dbUser").toString();
		String PASS = ConfigUtil.fetchPropertyValue("dbPass").toString();
		Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
		Statement stmt = conn.createStatement();
		String sqlQry = "SELECT * FROM BookDataTbl";
		ResultSet rs = stmt.executeQuery(sqlQry);
		List<Object> books = new ArrayList<Object>();
		int i=0;
		while(rs.next())
		{
			i++;
			books.add(rs.getString(1));//isbn
			books.add(rs.getString(2));//title
			books.add(rs.getString(3));//subtitle
			books.add(rs.getString(4));//author
			books.add(rs.getString(5));//publish_date
			books.add(rs.getString(6));//publisher
			books.add(rs.getInt(7));//pages
			books.add(rs.getString(8));//description
			books.add(rs.getString(9));//website
		}

		Object bookArray[][]= new Object[i][9];
		for(int y=0; y<i;y++)
		{
			for(int x=0;x<9;x++)
			{
				bookArray[y][x]=books.get((y*9)+x);
			}
		}
		cleanupConnection(conn,stmt,rs);
		return bookArray;
	}
}
