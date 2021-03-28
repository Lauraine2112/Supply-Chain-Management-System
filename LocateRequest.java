package edu.ucalgary.ensf409;

import java.sql.*;
import java.util.*;

public class LocateRequest {
	private final String DBURL; // store the database url information
	private final String USERNAME; // store the user's account username
	private final String PASSWORD; // store the user's account password
	private Connection dbConnect; // connection to desired database
	private ResultSet results; // table of the database result set
	private String furniture; // stores user input for furniture
	private String type; // stores user input for type of material
	private int amount; // stores user input for amount of furniture
//	private boolean requestStatus = true;
	private String[] bestCombination = null;
	private int bestPrice;
	private int totalPrice = 0;
	private String[] orderedIDs = null;
	private String[][] foundRequest;

	LocateRequest(String dburl, String username, String password, String furniture, String type, int amount) {
		this.DBURL = dburl;
		this.USERNAME = username;
		this.PASSWORD = password;
		setFurniture(furniture);
		setType(type);
		setAmount(amount);
	}

	/**
	 * Getter method for private data member dbConnect.
	 * 
	 * @return dbConnect
	 */
	public Connection getDbConnect() {
		return this.dbConnect;
	}

	/**
	 * Getter method for private data member results.
	 * 
	 * @return results
	 */
	public ResultSet getResults() {
		return this.results;
	}

	/**
	 * GSetter method for private data member results.
	 * 
	 * @param results Table of the database result set
	 * @return void
	 */
	public void setResults(ResultSet results) {
		this.results = results;
	}

	/**
	 * Getter method for private data member furniture.
	 * 
	 * @return furniture
	 */
	public String getFurniture() {
		return this.furniture;
	}

	/**
	 * Getter method for private data member furniture.
	 * 
	 * @param furniture The category of furniture
	 * @return void
	 */
	public void setFurniture(String furniture) {
		this.furniture = furniture;
	}

	/**
	 * Getter method for private data member type.
	 * 
	 * @return type
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * Setter method for private data member type. param type
	 * 
	 * @param type
	 * @return void
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Getter method for private data member amount.
	 * 
	 * @return amount
	 */
	public int getAmount() {
		return this.amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	/**
	 * Getter method for private data member bestCombination.
	 * 
	 * @return bestCombination
	 */
	public String[] getBestCombination() {
		return this.bestCombination;
	}

	/**
	 * Getter method for private data member bestPrice.
	 * 
	 * @return bestPrice
	 */
	public int getBestPrice() {
		return this.bestPrice;
	}

	/**
	 * Getter method for private data member bestPrice.
	 * 
	 * @return bestPrice
	 */
	public int getTotalPrice() {
		return this.totalPrice;
	}

	/**
	 * Getter method for private data member orderedIDs.
	 * 
	 * @return orderedIDs
	 */
	public String[] getOrderedIDs() {
		return this.orderedIDs;
	}

	/**
	 * initializeConnection is a void method that creates a connection to the
	 * database. Uses the data from DBURL, USERNAME, PASSWORD to create the
	 * connection. No parameters or return.
	 */
	public void initializeConnection() {
		try {
			dbConnect = DriverManager.getConnection(this.DBURL, this.USERNAME, this.PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * getRequest is a void method that .... No parameters or return.
	 */
	public void getRequest() {
		int rowNum = 0, colNum = 0;
		try {
			Statement myStmtCount = dbConnect.createStatement(); // creates a statement
			ResultSet rs = myStmtCount
					.executeQuery("SELECT COUNT(*) FROM " + this.furniture + " WHERE Type = '" + this.type + "'");

			while (rs.next()) {
				rowNum = rs.getInt(1); // 2
			}

			rs = myStmtCount.executeQuery(
					"SELECT COUNT(*) FROM information_schema.columns WHERE table_name = '" + this.furniture + "'");
			while (rs.next()) {
				colNum = rs.getInt(1); // 8
			}

			myStmtCount.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		foundRequest = new String[rowNum][colNum];
		String query = "SELECT * FROM " + this.furniture + " WHERE Type = " + "'" + this.type + "'";
		try {
			Statement myStmntNames = dbConnect.createStatement(); // creates a statement
			// selecting all rows with matching desired type from desired furniture table
			results = myStmntNames.executeQuery(query);
			int counter = 0;
			while (results.next()) {// while there is a next row of "type"
				for (int j = 0; j < colNum; j++) { // goes through each column
					foundRequest[counter][j] = results.getString(j + 1);
				}
				counter++;
			}

			myStmntNames.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (foundRequest.length == 0) {
			printInvalidOutputMessage(); // terminates program
		}
		
		String[] outputRow = new String[countBoolean(foundRequest)];// countBoolean(foundRequest)];
		for (int i = 0; i < this.amount; i++) {
			System.out.println("\nfoundRequest in getRequest is: ");
			for (String[] row : foundRequest) {
				System.out.println(Arrays.toString(row));
			}

			findCombinations(2, foundRequest.length, outputRow.length, foundRequest, outputRow);
			if (this.bestCombination != null) {
				reset(foundRequest); // removes from foundRequest, resets bestCombination, updates orderedIDs
			} else {
				printInvalidOutputMessage(); // terminates program
			}
		}
		for (String val : orderedIDs) {
			removeFromInventory(val); // removes from database
		}

//		calls to OrderForm Class

	}

	public void printInvalidOutputMessage() {
		StringBuffer invalidMessage = new StringBuffer();
		invalidMessage
				.append("Order cannot be fulfilled based on current inventory. " + "Suggested manufacturers are:\n");

		try {
			Statement myStmtInvalidOutput = dbConnect.createStatement(); // creates statement
			results = myStmtInvalidOutput.executeQuery("SELECT Name FROM MANUFACTURER");

			while (results.next()) { // goes through all the rows of the studio table
				invalidMessage.append(results.getString(1) + "\n");
			}

			myStmtInvalidOutput.close(); // close statement
		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println(invalidMessage.toString());
		System.exit(1);
	}

	public int getPrice(String[] outputRow, String[][] formatRequest) {
		int tempPrice = 0;
		String[] unique = Arrays.stream(outputRow).distinct().toArray(String[]::new);
		for (String val : unique) {
			for (String[] row : formatRequest) {
				List<String> list = Arrays.asList(row);
				if (list.contains(val)) {
					tempPrice += Integer.valueOf(row[row.length - 2]);
				}
			}
		}
		return tempPrice;
	}

	public void reset(String[][] formatRequest) {
		String[] unique = Arrays.stream(this.bestCombination).distinct().toArray(String[]::new);
		this.totalPrice += this.bestPrice; // sets total price for OrderForm
		addID(unique); // adds the current best Ids to the the final orderedIDs
		removeFound();
		this.bestCombination = null; // resets combination in the case of furniture amount > 1
	}

	public void addID(String[] unique) {
		String[] newID;

		if (this.orderedIDs == null) {
			this.orderedIDs = new String[unique.length];
			System.arraycopy(unique, 0, orderedIDs, 0, unique.length);
		} else {
			int newIdLength = unique.length + orderedIDs.length;
			newID = new String[newIdLength];
			System.arraycopy(unique, 0, newID, 0, unique.length);
			System.arraycopy(orderedIDs, 0, newID, unique.length, orderedIDs.length);

			this.orderedIDs = new String[newIdLength];
			System.arraycopy(newID, 0, orderedIDs, 0, newID.length);
		}
	}

	public void removeFound() {
		String[] unique = Arrays.stream(this.bestCombination).distinct().toArray(String[]::new);
		int numToRemove = unique.length;
//		System.out.println("\n" + Arrays.toString(unique));
		int newLength = foundRequest.length - numToRemove;
//		for (String row[] : formatRequest) {
//			for (String val : row) {
//			}
//		}

		String[][] tempArr = new String[this.foundRequest.length][this.foundRequest[0].length];
		System.arraycopy(foundRequest, 0, tempArr, 0, this.foundRequest.length);

//		for (String row[] : tempArr) {
//			for (String val : row) {
//				System.out.print(" " + val);
//			}
//			System.out.println();
//		}
//		System.out.println("\ntempArr in removeFound is: ");
//		for (String[] row : tempArr) {
//			System.out.println(Arrays.toString(row));
//		}

	if (newLength != 0) {
			this.foundRequest = new String[newLength][this.foundRequest[0].length];
			int i = 0;
//			System.out.println();
			for (String[] row : tempArr) {
				if (checkRowToCopy(row)) {
					System.arraycopy(row, 0, this.foundRequest[i++], 0, row.length);
//					System.out.println(Arrays.toString(row));
				}

			}
		}
//		System.out.println("\nNew foundRequest in removeFound is: ");
//			for (String[] row : foundRequest) {
//				System.out.println(Arrays.toString(row));
//			}
	}

	public boolean checkRowToCopy(String[] row) {
		List<String> toCopy = Arrays.asList(row);
		for (String val : getBestCombination()) {
			if (toCopy.contains(val)) {
				return false;
			}
		}
		return true;
	}

	public void removeFromInventory(String ID) {
		try {
			// query to delete an item from its respective table
			String query = "DELETE FROM " + this.getFurniture() + " WHERE ID = ?";
			PreparedStatement myStmtDeleteItem = dbConnect.prepareStatement(query); // create statement

			myStmtDeleteItem.setString(1, ID); // sets String, ID, to the question mark
			myStmtDeleteItem.executeUpdate(); // execute statement
			myStmtDeleteItem.close(); // close statement
		} catch (SQLException e) { // SQL exception catch case
			e.printStackTrace();
		}
	}

	public int countBoolean(String[][] foundRequest) { // determines how many Y or N can be in one row
		int counter = 0;
		for (int i = 0; i < foundRequest[0].length; i++) {
			if (foundRequest[0][i].equals("N") || foundRequest[0][i].equals("Y")) {
				counter++;
			}
		}

		return counter;
	}

	public void findCombinations(int j, int row, int col, String[][] first, String[] outputRow) {
		// row = rows of found request
		// col = columns containing only the legs, arms, seats etc..
		// initially, j starts at 0
		//

		for (int i = 0; i < row; i++) {
			if (first[i][j].equals("Y")) {
				outputRow[j - 2] = first[i][0];
			} else {
				outputRow[j - 2] = "*";
			}
			// recursively continue to populate outputRow until we reach the last column (j
			// == col -1)
			if (j < col + 1) {
				findCombinations(j + 1, row, col, first, outputRow);
			}
			// we have reached the last column (j == col -1) so now we could print current
			// permutation
			if (j == col + 1) {
				List<String> list = Arrays.asList(outputRow);
				if (!list.contains("*")) {
					String[] fullOutput = new String[outputRow.length];
					System.arraycopy(outputRow, 0, fullOutput, 0, outputRow.length);
					checkOutputRow(fullOutput, first);
				}
			}
		}

		// finds all possible combinations for a given furniture type
		// double array
		// keeps track of the 'Y' and 'N' for specific rows
		// if element is 'Y', the ID gets added to that array spot/
		// if element is 'N', array spot is blank
		// example: for combination {N,N,Y,N}, it would look like { , ,c8138, }

	}

	public void checkOutputRow(String[] outputRow, String[][] first) {
		if (this.bestCombination == null) {
			this.bestCombination = new String[outputRow.length];
			this.bestCombination = outputRow;
			this.bestPrice = getPrice(outputRow, first);
		} else if (getPrice(outputRow, first) < this.bestPrice) {
			this.bestCombination = outputRow;
			this.bestPrice = getPrice(outputRow, first);
		}
	}

	/**
	 * close is void method with no arguments. This method closes results and
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
