/**
 *  @author Lauraine Baffot
 *  @author Rachel Renegado
 *  @author Chloe Bouchard
 *  @version 1.20
 *  @since 1.0
 */

package edu.ucalgary.ensf409;

import static org.junit.Assert.*;
import org.junit.*;
import java.io.*;
import java.util.*;
import java.sql.*;
import org.junit.contrib.java.lang.system.SystemErrRule; //System.err
import org.junit.runners.MethodSorters;

/**
 * ProjectTest is a class that performs JUnit testing to ensure that all project
 * functionalities are fulfilled. There are 20 different unit tests that
 * demonstrate that the main program executes properly.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//tests are performed in order based on method name
//this ensures that elements are not called after they are deleted

public class ProjectTest {
	public final static String DBURL = "jdbc:mysql://localhost/INVENTORY";
	public final static String USERNAME = "lauraine";
	public final static String PASSWORD = "ensf409";

	/*
	 * The following tests ensure the program finds properly saves the user inputs
	 * for furniture, type and amount.
	 */
	@Test
	// Expected that user input for furniture was properly saved and ready for use
	// when creating a new object of LocateRequest with six arguments.
	// No changes are made to the database.
	public void aCheckUserInputFurniture() {
		LocateRequest myRequest = new LocateRequest(DBURL, USERNAME, PASSWORD, "chair", "Mesh", 1);
		String actualFurniture = "chair";
		assertEquals("User input for furniture was incorrect: ", actualFurniture, LocateRequest.getFurniture());
	}

	@Test
	// Expected that user input for type was properly saved and ready for use
	// when creating a new object of LocateRequest with six arguments.
	// No changes are made to the database.
	public void bcheckUserInputType() {
		LocateRequest myRequest = new LocateRequest(DBURL, USERNAME, PASSWORD, "chair", "Mesh", 1);
		String actualType = "mesh";
		assertEquals("User input for type was incorrect: ", actualType, LocateRequest.getType());
	}

	@Test
	// Expected that user input for amount was properly saved and ready for use
	// when creating a new object of LocateRequest with six arguments.
	// No changes are made to the database.
	public void cCheckUserInputAmount() {
		LocateRequest myRequest = new LocateRequest(DBURL, USERNAME, PASSWORD, "chair", "Mesh", 1);
		int actualAmount = 1;
		assertEquals("User input for amount was incorrect: ", actualAmount, LocateRequest.getAmount());
	}

	/*
	 * The following tests ensure the program finds the proper total price and
	 * OrderIDs for a variety of cases. Cases such as amount being greater or equal
	 * to 1 or extra pieces being used in another funiture item.
	 */

	@Test
	// Expected that the cheapest price is found for the request when
	// amount is 1. The expected price should be $150.
	// Changes made to the database. Inventory items with ID C9890
	// and C0942 should be removed from database.
	public void dCheckCorrectTotalPrice() {
		LocateRequest myRequest = new LocateRequest(DBURL, USERNAME, PASSWORD, "chair", "Mesh", 1);
		LocateRequest.getConnect().initializeConnection();
		myRequest.getRequest();
		assertEquals("Total price was incorrect: ", 150, myRequest.getTotalPrice());
	}

	@Test
	// Expected that orderedIDs contains the appropriate and unique IDs for the
	// cheapest match when amount is 1.The expected String array should contain
	// {"C2483", "C7268", "C5784"}.
	// Changes made to the database. Inventory items with ID C2483, C7268,
	// and C5784 should be removed from database.
	public void eCheckOrderedIDs() {
		LocateRequest myRequest = new LocateRequest(DBURL, USERNAME, PASSWORD, "chair", "Executive", 1);
		LocateRequest.getConnect().initializeConnection();
		myRequest.getRequest();
		String[] actualOrderedIDS = { "C2483", "C7268", "C5784" };
		assertTrue("OrderedIDs was incorrect: ", Arrays.equals(actualOrderedIDS, myRequest.getOrderedIDs()));
	}

	@Test
	// Expected that totalPrice contains the cheapest price for the expected order
	// when amount is 2. The expected price should be $40.
	// Changes made to the database. Inventory items with ID L112, L342, L013, and
	// L208 should be removed from database.
	public void fCheckTotalPriceWithAmountTwo() {
		LocateRequest myRequest = new LocateRequest(DBURL, USERNAME, PASSWORD, "lamp", "desk", 2);
		LocateRequest.getConnect().initializeConnection();
		myRequest.getRequest();
		assertEquals("Total price when amount = 2 was incorrect: ", 40, myRequest.getTotalPrice());
	}

	@Test
	// Expected that orderedIDs contains the appropriate and unique IDs for the
	// cheapest match
	// when amount is 2. The expected String array should contain
	// {"L928","L223","L982"}
	// Changes made to the database. Inventory items with ID L928, L223, and L982
	// should be removed from database.
	public void gCheckOrderedIDWithAmountTwo() {
		LocateRequest myRequest = new LocateRequest(DBURL, USERNAME, PASSWORD, "lamp", "study", 2);
		LocateRequest.getConnect().initializeConnection();
		myRequest.getRequest();
		String[] actualOrderedIDS = { "L982", "L223", "L928" };
		assertTrue("OrderedIDs was incorrect: ", Arrays.equals(actualOrderedIDS, myRequest.getOrderedIDs()));
	}

	@Test
	// Expected that totalPrice contains the cheapest price for the expected order
	// when there are extra parts. The expected price should be $1050.
	// Changes made to the database. Inventory items with ID D5437, D3682, D1030,
	// D4475, D7373,
	// and D2746 should be removed from database.
	public void hTotalPriceForOrderWithExtraParts() {
		LocateRequest myRequest = new LocateRequest(DBURL, USERNAME, PASSWORD, "desk", "adjustable", 3);
		LocateRequest.getConnect().initializeConnection();
		myRequest.getRequest();
		assertEquals("Total price with extra parts was incorrect: ", 1050, myRequest.getTotalPrice());
	}

	@Test
	// Expected that orderedIDs contains the appropriate and unique IDs for the
	// cheapest match when there are extra parts. The expected String array
	// should contain {"F004", "F005", "F006","F001", "F013"}. Changes made
	// to the database. Inventory items with ID F004, F005, F006, F001, and
	// F013 should be removed from database.
	public void iOrderedIDsForOrderWithExtraParts() {
		LocateRequest myRequest = new LocateRequest(DBURL, USERNAME, PASSWORD, "filing", "Small", 3);
		LocateRequest.getConnect().initializeConnection();
		myRequest.getRequest();
		String[] actualOrderedIDs = { "F004", "F005", "F006", "F001", "F013" };
		assertTrue("OrderedIDs was incorrect: ", Arrays.equals(actualOrderedIDs, myRequest.getOrderedIDs()));
	}

	/*
	 * The following tests ensure the program has the correct outputs (that properly
	 * reflect whether or not it can fulfill the requested or not) and that the
	 * Output Form was appropriately created.
	 */

	@Test
	// Expected that the order can be made and the correct output String is made.
	// L053 and L096 should be removed from database. Order form should be made.
	public void jCheckOrderFormOutput() {
		LocateRequest myRequest = new LocateRequest(DBURL, USERNAME, PASSWORD, "lamp", "swing arm", 1);
		LocateRequest.getConnect().initializeConnection();
		myRequest.getRequest();
		String[] expectedID = { "L053", "L096" }; // L487 and L879
		String expectedOutput = "";
		expectedOutput += "User request: " + "\nType: " + "swing arm" + "\nFurniture category: " + "lamp"
				+ "\nNumber of items requested: " + "1" + "\n";
		expectedOutput += "Output: Purchase \n";

		for (String val : expectedID) {
			expectedOutput += " - " + val + "\n";
		}
		expectedOutput += "for $" + "30";
		System.out.println("Output FAIL!");
		assertEquals("Valid order output was incorrect: ", expectedOutput,
				OrderForm.printValidOutputMessage(myRequest.getOrderedIDs(), myRequest.getTotalPrice()));
	}

	@Test
	// Expected that the order form exists and pieces L487 and L879 have been
	// removed from inventory.
	public void kCheckOrderFormFileCreation() {
		LocateRequest myRequest = new LocateRequest(DBURL, USERNAME, PASSWORD, "lamp", "swing arm", 1);
		LocateRequest.getConnect().initializeConnection();
		myRequest.getRequest();
		assertTrue("Order form was not made.", OrderForm.getMyObj().exists());
	}

	@Test
//  Expected that the order cannot be fulfilled and the correct output String is made.
//	 Database should remain the same. No order form should be made.
	public void lCheckManufactureOutput() {
		LocateRequest myRequest = new LocateRequest(DBURL, USERNAME, PASSWORD, "lamp", "swing arm", 2);
		LocateRequest.getConnect().initializeConnection();
		myRequest.getRequest();
		String expectedOutput = "Order cannot be fulfilled based on current inventory. "
				+ "Suggested manufacturers are:\n" + "Office Furnishings\n" + "Furniture Goods\n"
				+ "Fine Office Supplies\n";
		assertEquals(expectedOutput, OrderForm.printInvalidOutputMessage("lamp"));
	}

	@Test
//   Expected that the order cannot be fulfilled and the correct output String is made.
//	 Database should remain the same. No order form should be made.
	public void mCheckManufactureOutput() {
		LocateRequest myRequest = new LocateRequest(DBURL, USERNAME, PASSWORD, "chair", "executive", 2);
		LocateRequest.getConnect().initializeConnection();
		myRequest.getRequest();
		String expectedOutput = "Order cannot be fulfilled based on current inventory. "
				+ "Suggested manufacturers are:\n" + "Office Furnishings\n" + "Furniture Goods\n"
				+ "Fine Office Supplies\n" + "Chairs R Us\n";
		assertEquals(expectedOutput, OrderForm.printInvalidOutputMessage("chair"));
	}

	@Test
	// Expected that the order form does not exists and database remains the
	// same.
	public void nCheckOrderFormFileNotCreated() {
		OrderForm.deleteFile();
		LocateRequest myRequest = new LocateRequest(DBURL, USERNAME, PASSWORD, "chair", "executive", 2);
		LocateRequest.getConnect().initializeConnection();
		myRequest.getRequest();
		assertFalse("Order form is not meant to be created.", OrderForm.getMyObj().exists());
	}

	/*
	 * Tests involving expected System.err.print() when invalid user inputs occur.
	 * SystemErrRule is provided by import
	 * org.junit.contrib.java.lang.system.SystemErrRule; which can be downloaded as
	 * a .jar from https://stefanbirkner.github.io/system-rules/ Store together with
	 * the hamcrest and junit jar files, and include as part of javac/java calls:
	 * .:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar:lib/system-rules-1.19.0.jar
	 * (Mac & Linux)
	 * .;lib/junit-4.13.2.jar;lib/hamcrest-core-1.3.jar;lib/system-rules-1.19.0.jar
	 * (Windows)
	 */

	@Rule
	public final SystemErrRule systemErrRule = new SystemErrRule().enableLog();

	@Test
	// Expects an error message to be printed to the command-line due to invalid
	// furniture.
	// No changes made to the database. No order form should be created.
	public void oCheckInvalidFurniture() {
		systemErrRule.clearLog();
		// checks for proper handling for input "shoe"
		LocateRequest myRequest = new LocateRequest(DBURL, USERNAME, PASSWORD, "shoe", "mesh", 1);
		LocateRequest.getConnect().initializeConnection();
		myRequest.getRequest();
		assertEquals("The furniture provided is not valid", systemErrRule.getLog());
	}

	@Test
	// Expects an error message to be printed to the command-line due to invalid
	// type.
	// No changes made to the database. No order form should be created.
	public void pCheckInvalidOrderType() {
		systemErrRule.clearLog();
		// checks mismatch between furniture type and category
		LocateRequest myRequest = new LocateRequest(DBURL, USERNAME, PASSWORD, "lamp", "mesh", 1);
		LocateRequest.getConnect().initializeConnection();
		myRequest.getRequest();
		assertEquals("The furniture type provided is not valid", systemErrRule.getLog());
	}

	@Test
	// Expects an error message to be printed to the command-line due to invalid
	// amount.
	// No changes made to the database. No order form should be created.
	public void qCheckInvalidAmount() {
		systemErrRule.clearLog();
		// check out of bounds: -32
		// combination chair and ergonomic is valid, but amount -32 is invalid
		LocateRequest myRequest = new LocateRequest(DBURL, USERNAME, PASSWORD, "chair", "ergonomic", -32);
		LocateRequest.getConnect().initializeConnection();
		myRequest.getRequest();
		assertEquals("The amount provided is not valid", systemErrRule.getLog());
	}

	@Test
	// Expects an error message to be printed to the command-line due to invalid
	// amount.
	// No changes made to the database. No order form should be created.
	public void rCheckInvalidAmount() {
		systemErrRule.clearLog();
		// check out of bounds: 0
		// combination chair and ergonomic is valid, but amount 0 is invalid
		LocateRequest myRequest2 = new LocateRequest(DBURL, USERNAME, PASSWORD, "chair", "ergonomic", 0);
		LocateRequest.getConnect().initializeConnection();
		myRequest2.getRequest();
		assertEquals("The amount provided is not valid", systemErrRule.getLog());
	}

	@Test
	// Expects an error message to be printed to the command-line due to invalid
	// amount.
	// No changes made to the database. No order form should be created.
	public void sCheckInvalidAmount() {
		systemErrRule.clearLog();
		LocateRequest myRequest = new LocateRequest(DBURL, USERNAME, PASSWORD, "chair", "mesh", -1);
		LocateRequest.getConnect().initializeConnection();
		myRequest.getRequest();
		assertEquals("The amount provided is not valid", systemErrRule.getLog());
	}

	@Test
	// Expects an error message to be printed to the command-line due to invalid
	// amount.
	// No changes made to the database. No order form should be created.
	public void tCheckInvalidAmount() {
		systemErrRule.clearLog();
		// check out of bounds: -1
		// combination chair and ergonomic is valid, but amount -1 is invalid
		LocateRequest myRequest3 = new LocateRequest(DBURL, USERNAME, PASSWORD, "chair", "ergonomic", -1);
		LocateRequest.getConnect().initializeConnection();
		myRequest3.getRequest();
		assertEquals("The amount provided is not valid", systemErrRule.getLog());
	}
}