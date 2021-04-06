/**
 *  @author	Lauraine Baffot
 *  @author Rachel Renegado
 *  @author Chloe Bouchard
 *  @version 3.17
 *  @since 1.0
 */
 package edu.ucalgary.ensf409;

import java.sql.*;
import java.util.*;

/**
 * LocateRequest is a class that is responsible for searching and finding the best
 * permutation of furniture pieces from the database, inventory. If the cheapest
 * permutation is found, it is used in the output message and orderForm.txt file. 
 * If a completed furniture piece is not possible, an output message with the 
 * suggested manufactures is outputted and no orderForm.txt is created. This class 
 * utilizes classes OrderForm, Connect, and Input in its execution.
 */
public class LocateRequest {
	public static void main(String[] args) {
		new GUI(); //calls for panes for user inputs
	}

	private static Connect connection; // connection responsible for accessing the database
	private static String furniture; // stores user input for furniture
	private static String type; // stores user input for type of material
	private static int amount; // stores user input for amount of furniture
	private static boolean orderIsPossible; // returns true if the order can be fulfilled
	private String[][] foundRequest; // 2d array that represents the inventory matched for order
	private String[] bestCombination = null; // array that represents the best combination of IDs
	private String[] orderedIDs = null; // array that represents successful ID matches for order
	private int totalPrice = 0; // total price of furniture piece asked by user
	private int bestPrice; // used as a reference for the current best price
	private int hasExtra = 0; // number of extra furniture pieces

	/**
	 * Constructor for LocateRequest object.
	 * 
	 * @param dburl     the url for the database
	 * @param username  the username required to access the database
	 * @param password  the password required to access the database
	 * @param furniture the furniture category
	 * @param type      the furniture type
	 * @param amount    the amount of furniture desired by the user
	 *
	 */
	public LocateRequest(String dburl, String username, String password, String furniture, String type, int amount) {
		connection = new Connect(dburl, username, password);
		setFurniture(furniture.toLowerCase()); // convert to lowercase to prevent errors
		setType(type.toLowerCase()); // convert to lowercase to prevent errors
		setAmount(amount);
	}

	/**
	 * Getter method for private data member connection.
	 * 
	 * @return connection
	 */
	public static Connect getConnect() {
		return connection;
	}

	/**
	 * Getter method for private data member furniture.
	 * 
	 * @return furniture
	 */
	public static String getFurniture() {
		return furniture;
	}

	/**
	 * Setter method for private data member furniture.
	 * 
	 * @param furniture The category of furniture
	 */
	public void setFurniture(String furniture) {
		if (Input.isFurnitureValid(furniture.toLowerCase())) {
			LocateRequest.furniture = furniture;
		} else {
			System.err.print("The furniture provided is not valid");
		}
	}

	/**
	 * Getter method for private data member type.
	 * 
	 * @return type
	 */
	public static String getType() {
		return type;
	}

	/**
	 * Setter method for private data member type.
	 * 
	 * @param type
	 */
	public void setType(String type) {
		if (Input.isTypeValid(furniture.toLowerCase(), type.toLowerCase())) {
			LocateRequest.type = type; // makes sure that type is valid
		} else {
			// error message that type is not valid
			System.err.print("The furniture type provided is not valid");
		}
	}

	/**
	 * Getter method for private data member amount.
	 * 
	 * @return amount
	 */
	public static int getAmount() {
		return amount;
	}

	/**
	 * Setter method for private data member amount. Checks if input amount is valid
	 * if amount is smaller or equal to zero, not valid
	 * 
	 * @param amount the amount of furniture desired by the user
	 */
	public void setAmount(int amount) {
		if (amount > 0) { // checks if amount is valid - larger than 0
			LocateRequest.amount = amount;
		} else {
			System.err.print("The amount provided is not valid"); // print message that input is not valid
		}
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
	 * Getter method for private data member orderedIDs.
	 * 
	 * @return orderedIDs
	 */
	public String[][] getFoundRequest() {
		return this.foundRequest;
	}

	/**
	 * Getter method for private data member orderIsPossible.
	 * 
	 * @return boolean
	 */
	public static boolean getOrderIsPossible() {
		return orderIsPossible;
	}

	/**
	 * Setter method for private data member orderIsPossible.
	 * 
	 * @param orderIsPossible the boolean value of order
	 *
	 * @return void
	 */
	public void setOrderIsPossible(boolean orderIsPossible) {
		LocateRequest.orderIsPossible = orderIsPossible;
	}

	/**
	 * getRequest is a void method that creates an array containing only elements
	 * with matching furniture category and type. It calls findCombinations. It also
	 * checks if a specific order results in extra parts finally, it calls
	 * removeFromInventory to update elements in database.
	 * 
	 * @return void
	 */
	public void getRequest() {
		int rowNum = 0, colNum = 0; // row and column counters start at 0
		try {
			Statement myStmtCount = Connect.getDbConnect().createStatement(); // creates a statement
			ResultSet rs = myStmtCount // creates a resultSet
					// counts how many elements there are in table 'furniture' with the matching
					// 'type'
					.executeQuery("SELECT COUNT(*) FROM " + furniture + " WHERE Type = '" + type + "'");

			while (rs.next()) {
				// used to count the number of rows that elements fill
				rowNum = rs.getInt(1); // keeps track of the last element that belongs in specific furniture and type
			}

			rs = myStmtCount.executeQuery(
					"SELECT COUNT(*) FROM information_schema.columns WHERE table_name = '" + furniture + "'");
			while (rs.next()) {
				// used to count the number of colums that a specific row has
				colNum = rs.getInt(1); // keeps track of the last element that belongs in specific row of type
										// 'furniture'
			}

			myStmtCount.close(); // close statement
			rs.close(); // close resultset
		} catch (SQLException e) {
			e.printStackTrace(); // catch an exception
		}

		this.foundRequest = new String[rowNum][colNum]; // initializes double array foundRequest
		String query = "SELECT * FROM " + furniture + " WHERE Type = " + "'" + type + "'";
		try {
			Statement myStmntNames = Connect.getDbConnect().createStatement();
			// selecting all rows with matching desired type from desired furniture table
			connection.setResults(myStmntNames.executeQuery(query));
			int counter = 0;
			while (Connect.getResults().next()) {// while there is a next row of "type"
				for (int j = 0; j < colNum; j++) { // goes through each column
					this.foundRequest[counter][j] = Connect.getResults().getString(j + 1);
				}
				counter++;
			}

			myStmntNames.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (this.foundRequest.length == 0) {
			setOrderIsPossible(false); // setOrder is not possible if length of foundRequest is 0
			return;
		}

		String[] outputRow = new String[colNum - 4];// countBoolean(foundRequest)];
		for (int i = 0; i < amount; i++) {
			// call to method findCombinations
			findCombinations(2, foundRequest.length, outputRow.length, foundRequest, outputRow);
			if (this.bestCombination != null) {
				// once a combination is found, checks if there are extra parts to be used in
				// furniture piece
				checkExtraParts(); // call to checkExtraParts to see if there are spare elements
				reset(foundRequest); // removes from foundRequest, resets bestCombination, updates orderedIDs
			} else {
				setOrderIsPossible(false);
				return;
			}
		}

		for (String val : orderedIDs) {
			removeFromInventory(val); // removes val from database
		}

		if (this.hasExtra > 0) { // amount must be greater than one to use an extra
			int length = 0; // will count how many extras there are
			for (String val : orderedIDs) {
				if (val.contains("Extra")) {
					length++;
				}
			}

			String[] newOrdered = new String[orderedIDs.length]; // new array, same length as orderedIDs
			System.arraycopy(orderedIDs, 0, newOrdered, 0, orderedIDs.length); // copy orderedIDs to newOrdered

			if (length == 0) {
				orderedIDs = new String[orderedIDs.length];
			} else if (length > 0) {
				orderedIDs = new String[orderedIDs.length - hasExtra];
			}

			int i = 0;
			for (String val : newOrdered) {
				if (!val.contains("Extra")) { // copying all ids that are not the extra pieces
					orderedIDs[i++] = val;
				}
			}
		}

		OrderForm.createFile(this.orderedIDs, this.totalPrice);
		setOrderIsPossible(true);
	}

	/**
	 * getPrice is a method that returns a temporary price based on inputs
	 * 
	 * @param outputRow     the array of a specific combination of furniture pieces
	 * @param formatRequest the double array of
	 * @return orderedIDs
	 */
	public int getPrice(String[] outputRow, String[][] formatRequest) {
		int tempPrice = 0; // tempPrice starts at 0
		// unique is a new array that eliminates the duplicate elements from outputRow
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

	/**
	 * method reset resets bestCombination to null
	 * 
	 * @param formatRequest the double array
	 * @return void
	 */
	public void reset(String[][] formatRequest) {
		String[] unique = Arrays.stream(this.bestCombination).distinct().toArray(String[]::new);
		totalPrice += this.bestPrice; // sets total price for OrderForm
		addID(unique); // adds the current best Ids to the the final orderedIDs
		removeFound(); // call to removeFound to get rid of IDs
		this.bestCombination = null; // resets combination in the case of furniture amount > 1
	}

	/**
	 * method addID creates a new array and copies all the IDs in unique to
	 * ordredIDs combines the old values of orderdIDs with the new ones contained in
	 * unique
	 * 
	 * @param unique the array of IDs that need to be ordered
	 * @return void
	 */
	public void addID(String[] unique) {
		String[] newID; // create an array

		if (orderedIDs == null) { // if orderedIDs has not been set yet:
			orderedIDs = new String[unique.length]; // orderedIDs is set to same size as unique
			System.arraycopy(unique, 0, orderedIDs, 0, unique.length); // copy values of unique to orderedIDs
		} else {
			int newIdLength = unique.length + orderedIDs.length;
			newID = new String[newIdLength]; // newID has length of combined unique and orderedIDs
			System.arraycopy(unique, 0, newID, 0, unique.length); // copy unique to part of newID
			System.arraycopy(orderedIDs, 0, newID, unique.length, orderedIDs.length); // copy orderedIDs to part of
																						// newID

			orderedIDs = new String[newIdLength]; // orderedIDs has new length of combined array
			System.arraycopy(newID, 0, orderedIDs, 0, newID.length); // copy back values of newID to orderedIDs
		}
	}

	/**
	 * method removeFound removes all unique values from bestCombination
	 * 
	 * @return void
	 */
	public void removeFound() {
		String[] unique = Arrays.stream(this.bestCombination).distinct().toArray(String[]::new);
		int numToRemove = unique.length;
		int newLength = foundRequest.length - numToRemove; // newLength is equal to foundRequest length - unique length
		// create new array tempArr of same size as foundRequest
		String[][] tempArr = new String[this.foundRequest.length][this.foundRequest[0].length];
		// copy values of of foundRequest to tempArr
		System.arraycopy(foundRequest, 0, tempArr, 0, this.foundRequest.length);

		// set foundRequest to new size [newLength[original foundRequest size]
		this.foundRequest = new String[newLength][this.foundRequest[0].length];
		int i = 0;
		for (String[] row : tempArr) { // goes through row
			if (checkRowToCopy(row)) { // if row must be copied:
				// copies values of row to foundRequest
				System.arraycopy(row, 0, this.foundRequest[i++], 0, row.length);
			}
		}
	}

	/**
	 * method checkRowToCopy checks if a row needs to be copied or not if an element
	 * is contained in bestCombination, returns false if an element is not
	 * containted, returns true
	 * 
	 * @param row the array that is being checked in bestCombination
	 * @return boolean
	 */
	public boolean checkRowToCopy(String[] row) {
		List<String> toCopy = Arrays.asList(row);
		for (String val : getBestCombination()) {
			if (toCopy.contains(val)) { // checks if value in row is contained in bestCombination
				return false;
			}
		}
		return true;
	}

	/**
	 * method removeFromInventory removes a specific elements from the database
	 * 
	 * @param ID the id of the element that needs to be removed
	 * @return void
	 */
	public void removeFromInventory(String ID) {
		try {
			// query to delete an item from its respective table
			String query = "DELETE FROM " + LocateRequest.getFurniture() + " WHERE ID = ?";
			PreparedStatement myStmtDeleteItem = Connect.getDbConnect().prepareStatement(query); // create statement

			myStmtDeleteItem.setString(1, ID); // sets String, ID, to the question mark
			myStmtDeleteItem.executeUpdate(); // execute statement
			myStmtDeleteItem.close(); // close statement
		} catch (SQLException e) { // SQL exception catch case
			e.printStackTrace();
		}
	}

	/**
	 * method findCombinations recursively finds all the permutations of elements
	 * for a specific furniture type and category keeps track of the 'Y' and 'N' for
	 * specific rows if element is 'Y', the ID gets added to that array spot if
	 * element is 'N', array spot is "*" example: for combination {N,N,Y,N}, it
	 * would look like { , ,c8138, }
	 * 
	 * @param j         the integer that keeps track of the recursion. Always starts
	 *                  at 0
	 * @param row       the number of rows in array foundRequest
	 * @param col       the number of elements in a specific row (arms, legs... etc)
	 * @param first     the double array of all elements for a specific furniture
	 *                  type and category
	 * @param outputRow a specific permutation for a furniture type and category
	 * @return void
	 */
	public void findCombinations(int j, int row, int col, String[][] first, String[] outputRow) {
		// row = rows of found request
		// col = columns containing only the legs, arms, seats etc..
		// initially, j starts at 0

		for (int i = 0; i < row; i++) {
			if (first[i][j].equals("Y")) { // checks if furniture piece at index is usable
				outputRow[j - 2] = first[i][0]; // if it is, outputRow is set to the value at a spefific index
			} else {
				outputRow[j - 2] = "*"; // sets value at index of outputRow to "*", showing not usable element
			}
			// recursively continue to populate outputRow until we reach the last column
			// (j== col -1)
			if (j < col + 1) { // checks if j is still smaller than the number of columns +1
				findCombinations(j + 1, row, col, first, outputRow); // calls findCombinations while incrementing j
			}
			// Reached the last column (j == col -1) so now we could print current
			// permutation
			if (j == col + 1) {
				List<String> list = Arrays.asList(outputRow);
				if (!list.contains("*")) { //takes only the full combinations
					String[] fullOutput = new String[outputRow.length];
					System.arraycopy(outputRow, 0, fullOutput, 0, outputRow.length);
					checkOutputRow(fullOutput, first);
				}
			}
		}
	}

	/**
	 * checkOutputRow is a method with two arguments. Checks if bestPrice is lower
	 * than current input
	 * 
	 * @param outputRow the array with a permutation of IDs
	 * @param first     the double array with
	 * 
	 * @param void
	 */
	public void checkOutputRow(String[] outputRow, String[][] first) {
		if (this.bestCombination == null) { // if bestCombination has not been found yet:
			this.bestCombination = new String[outputRow.length];
			this.bestCombination = outputRow;
			this.bestPrice = getPrice(outputRow, first); // best price is set to price of current outputRow
		} else if (getPrice(outputRow, first) < this.bestPrice) { // if bestPrice is higher than the current outputRow
																	// price:
			this.bestCombination = outputRow; // bestCombination is set to current outputRow
			this.bestPrice = getPrice(outputRow, first); // bestPrice is set to price of current outputRow
		}
	}

	/**
	 * checkExtraParts is a method with no arguments. Checks if current
	 * bestCombination has any extra pieces that are not used in the creation of the
	 * furniture piece. These extra pieces can be later used in next combination if
	 * the amount desired is greater than 1. If there are extra pieces present in
	 * bestCombination, the extra piece is formatted and added back into
	 * foundRequest as a possible piece to be added in the next combination.
	 * 
	 * The price of the extra piece 0, since it is already been added to the tab
	 * when looking for the furniture prior. The ID of the extra piece is also
	 * changes to have a "x" identifier to not be deleted from foundRequest.
	 * 
	 * @return void
	 */
	public void checkExtraParts() { // done before deleting
		String[] full = new String[this.bestCombination.length];
		String[] unique = Arrays.stream(this.bestCombination).distinct().toArray(String[]::new);
		String[] temp = new String[this.foundRequest[0].length];
		String[] newTemp = new String[this.foundRequest[0].length];

		for (String id : unique) {// sets temp[] to the row in foundRequest matching the id;
			for (String[] row : this.foundRequest) { // goes through each row in foundRequest
				List<String> list = Arrays.asList(row); // converts the row to a list
				if (list.contains(id)) { // if the row represents the desired id
					int i = 0;
					for (String element : row) {
						temp[i++] = element; // copying each element in the row to temp
					}
					// Example: temp = [id, type, y/n, y/n, y/n, y/n, price, manID]
				}
			}

			for (int i = 0; i < full.length; i++) { // For each element in full
				if (temp[i + 2].equals("Y") && full[i] == null) {
					full[i] = "Y";
					temp[i + 2] = "N";
				} else if (temp[i + 2].equals("Y") && full[i].equals("Y")) {
					newTemp = formatTemp(temp, full); // changes temp comparing to full: yn -> n and yy->y and nn-> n
					addExtraPieces(newTemp); // adds temp[] to extraPieces** ADDED METHOD TO ADD temp TO foundRequest
												// pass by value
				}
			}
		}
	}

	/**
	 * addExtraPieces is a method with one argument, a String Changes the Id number
	 * of temp by appending an 'X' and changes the price to 0 Adds the temp array to
	 * the 2D array foundRequest
	 * 
	 * @param temp
	 */
	public void addExtraPieces(String[] temp) {
		temp[0] += "Extra";
		temp[temp.length - 2] = "0"; // price = 0

		String[][] tempArr = new String[this.foundRequest.length][this.foundRequest[0].length];
		System.arraycopy(foundRequest, 0, tempArr, 0, this.foundRequest.length); //stores the original value of 
																					//foundRequest into a new array

		this.foundRequest = new String[this.foundRequest.length + 1][this.foundRequest[0].length]; //clears the array
																								//and add an element space

		int i = 0;
		for (String[] row : tempArr) {
			System.arraycopy(row, 0, this.foundRequest[i++], 0, row.length); // copies each element in temp to new found
																				// request
		}
		System.arraycopy(temp, 0, this.foundRequest[i], 0, temp.length); //adds the extra piece to the array
	}

	/**
	 * formatTemp is method with two arguments. This method treats 'N' as a logic 0
	 * treats 'Y' as a logic 1. YN = N, YY = Y checks String arrays temp and full
	 * and compares their contents at matching indexes assigns new values to String
	 * array outputArray, depending on contents of other arrays.
	 * 
	 * @param temp
	 * @param full
	 * @return outputArray
	 */
	public String[] formatTemp(String[] temp, String[] full) {
		String[] outputArray = new String[temp.length];
		System.arraycopy(temp, 0, outputArray, 0, temp.length);// copies temp to outputArray

		for (int i = 0; i < full.length; i++) { // changes elements if...
			if (full[i] == null) {
				outputArray[i + 2] = "N";
			} else if (temp[i + 2].equals("N") || full[i].equals("N")) { // if there is an N found in either of the
																			// arrays, outputArray[i] ='N'
				outputArray[i + 2] = "N";
			} else if (temp[i + 2].equals("Y") && full[i].equals("Y")) { // if both are 'Y', stays 'Y' in outputArray
				this.hasExtra++;
				outputArray[i + 2] = "Y";
			}
		}
		return outputArray;
	}
}