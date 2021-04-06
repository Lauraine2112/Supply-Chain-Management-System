/**
 *  @author	Lauraine Baffot
 *  @author Rachel Renegado
 *  @author Chloe Bouchard
 *  @version 2.3
 *  @since 1.0
 */
package edu.ucalgary.ensf409;

import java.sql.*;

/**
 * Connect is a class that is responsible for creating connections to a database
 * It requires a database url, username and password. It has two methods called
 * initializeConnection and close. These two methods are responsible for creating
 * the connection to the database and closing it.
 */
public class Connect {
	private final String DBURL; // store the database url information
	private final String USERNAME; // store the user's account username
	private final String PASSWORD; // store the user's account password
	private static Connection dbConnect; // connection to desired database
	private static ResultSet results; // table of the database result set

	/**
	 * Constructor for Connnect object.
	 * 
	 * @param dburl
	 * @param username
	 * @param password
	 */
	public Connect(String dburl, String username, String password) {
		this.DBURL = dburl; // sets DBURL to input dburl
		this.USERNAME = username; // sets USERNAME to input username
		this.PASSWORD = password; // sets PASSWORD to input password
	}

	/**
	 * Getter method for private data member dbConnect.
	 * 
	 * @return dbConnect
	 */
	public static Connection getDbConnect() {
		return dbConnect;
	}

	/**
	 * Getter method for private data member DBURL.
	 * 
	 * @return DBURL
	 */
	public String getDbUrl() {
		return DBURL;
	}

	/**
	 * Getter method for private data member USERNAME.
	 * 
	 * @return USERNAME
	 */
	public String getUsername() {
		return USERNAME;
	}

	/**
	 * Getter method for private data member results.
	 * 
	 * @return results
	 */
	public static ResultSet getResults() {
		return results;
	}

	/**
	 * GSetter method for private data member results.
	 * 
	 * @param results Table of the database result set
	 * @return void
	 */
	public void setResults(ResultSet results) {
		Connect.results = results;
	}

	/**
	 * initializeConnection is a void method that creates a connection to the
	 * database. Uses the data from DBURL, USERNAME, PASSWORD to create the
	 * connection. No parameters or return.
	 */
	public void initializeConnection() {
		try {
			dbConnect = DriverManager.getConnection(DBURL, USERNAME, PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * close is a void method with no arguments. This method closes results and
	 * dbConnect that is a private data member in Registration.
	 */
	public void close() {
		try {
			results.close(); // close result set
			dbConnect.close(); // close connection
		} catch (SQLException e) { // SQL exception catch case
			e.printStackTrace();
		}
	}
}
