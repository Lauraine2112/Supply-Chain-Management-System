package edu.ucalgary.ensf409;

import static org.junit.Assert.*;
import org.junit.*;
import java.io.*;
import java.sql.DriverManager;
import java.util.*;
import java.sql.*;
import org.junit.contrib.java.lang.system.SystemErrRule;
import java.lang.*;

//if we have any System.exit() we can create test using this 
import org.junit.contrib.java.lang.system.ExpectedSystemExit;

public class ProjectTest {
	// jdbc:mysql://127.0.0.1:3306/inventory
	// localhost:3306/inventory
	public final static String DBURL = "jdbc:mysql://localhost/INVENTORY";
	public final static String USERNAME = "lauraine";
	public final static String PASSWORD = "ensf409";

	@Rule
	public final SystemErrRule systemErrRule = new SystemErrRule().enableLog();

// ***** The following tests ensure user inputs are correctly saved and used. *****

	@Test
	// Expected that user input for furniture was properly saved and ready for use
	// when creating a new object of LocateRequest with six arguments.
	// No changes are made to the database.
	public void checkUserInputFurniture() {
		LocateRequest myRequest = new LocateRequest(DBURL, USERNAME, PASSWORD, "chair", "Mesh", 1);
		String actualFurniture = "chair";
		assertEquals("User input for furniture was incorrect: ", actualFurniture, LocateRequest.getFurniture());
	}

	@Test
	// Expected that user input for type was properly saved and ready for use
	// when creating a new object of LocateRequest with six arguments.
	// No changes are made to the database.
	public void checkUserInputType() {
		LocateRequest myRequest = new LocateRequest(DBURL, USERNAME, PASSWORD, "chair", "Mesh", 1);
		String actualType = "mesh";
		assertEquals("User input for type was incorrect: ", actualType, LocateRequest.getType());
	}

	@Test
	// Expected that user input for amount was properly saved and ready for use
	// when creating a new object of LocateRequest with six arguments.
	// No changes are made to the database.
	public void checkUserInputAmount() {
		LocateRequest myRequest = new LocateRequest(DBURL, USERNAME, PASSWORD, "chair", "Mesh", 1);
		int actualAmount = 1;
		assertEquals("User input for amount was incorrect: ", actualAmount, LocateRequest.getAmount());
	}

// ***** The following tests ensure the program finds the proper total price and OrderIDs. *****

//	@Test
//	// Expected that the cheapest price is found for the request when
//	// amount is 1. The expected price should be $150.
//	// Changes made to the database. Inventory items with ID C9890
//	// and C0942 should be removed from database.
//	public void checkCorrectTotalPrice() {
//		LocateRequest myRequest = new LocateRequest(DBURL, USERNAME, PASSWORD, "chair", "Mesh", 1);
//		LocateRequest.getConnect().initializeConnection();
//		myRequest.getRequest();
//		assertEquals("Total price was incorrect: ", 150, LocateRequest.getTotalPrice());
//	}

	@Test
	// Expected that orderedIDs contains the appropriate and unique IDs for the
	// cheapest match when amount is 1.The expected String array should contain
    // {"C2483", "C7268", "C5784"}.
	// Changes made to the database. Inventory items with ID ____, _____,
	// and ____ should be removed from database.
	public void checkOrderedIDs() {
		LocateRequest myRequest = new LocateRequest(DBURL, USERNAME, PASSWORD, "chair", "Executive", 1);
		LocateRequest.getConnect().initializeConnection();
		myRequest.getRequest();
		String[] actualOrderedIDS = { "C2483", "C7268", "C5784" };
		System.out.println(Arrays.toString(LocateRequest.getOrderedIDs()));
		assertTrue("OrderedIDs was incorrect: ", Arrays.equals(actualOrderedIDS, LocateRequest.getOrderedIDs()));
	}

//	 @Test
//  // Expected that totalPrice contains the cheapest price for the expected order
//  // when amount is 2. The expected price should be $40.
//  // Changes made to the database. Inventory items with ID L112, L342, L013, and L208 
//  // should be removed from database.
//  public void checkTotalPriceWithAmountTwo() {
//     LocateRequest myRequest = new LocateRequest(DBURL, USERNAME, PASSWORD, "lamp", "desk", 2);
//     LocateRequest.getConnect().initializeConnection();
//     myRequest.getRequest();
//     assertEquals("Total price when amount = 2 was incorrect: ", 40, LocateRequest.getTotalPrice());
//  }

//	 @Test
//  // Expected that orderedIDs contains the appropriate and unique IDs for the cheapest match
//  // when amount is 2. The expected String array should contain {"L928","L223","L982"}
//  // Changes made to the database. Inventory items with ID L928, L223, and L982
//  // should be removed from database.
//  public void checkOrderedIDWithAmountTwo() {
//     LocateRequest myRequest = new LocateRequest(DBURL, USERNAME, PASSWORD, "lamp", "study", 2);
//     LocateRequest.getConnect().initializeConnection();
//     myRequest.getRequest();
//     String actualOrderedIDS = {"L928","L223","L982"};
//     assertTrue("OrderedIDs was incorrect: ", Arrays.equals(actualOrderedIDS, LocateRequest.getOrderedIDs()));
//  }

//	 @Test
//  // Expected that totalPrice contains the cheapest price for the expected order
//  // when there are extra parts. The expected price should be _______.
//  // Changes made to the database. Inventory items with ID ____, _____, 
//  // and ____ should be removed from database.
//  public void TotalPriceForOrderWithExtraParts() {
//     LocateRequest myRequest = new LocateRequest(DBURL, USERNAME, PASSWORD, "_____", "____", ____);
//     LocateRequest.getConnect().initializeConnection();
//     myRequest.getRequest();
//     assertEquals("Total price with extra parts was incorrect: ", ______, LocateRequest.getTotalPrice());
//  }

//	 @Test
//  // Expected that orderedIDs contains the appropriate and unique IDs for the cheapest match
//  // when there are extra parts. The expected String array should contain {"F004", "F005", "F006", 
//  // "F001", "F013"}. Changes made to the database. Inventory items with ID F004, F005, F006,
//  // F001, and F013 should be removed from database.
////  public void OrderedIDsForOrderWithExtraParts() {
//     LocateRequest myRequest = new LocateRequest(DBURL, USERNAME, PASSWORD, "filing", "Small", 3);
//	     LocateRequest.getConnect().initializeConnection();
//	     myRequest.getRequest();
//	     String[] actualOrderedIDs = {"F004", "F005", "F006", "F001", "F013"};
//	     assertTrue("OrderedIDs was incorrect: ", Arrays.equals(actualOrderedIDs, myRequest.getOrderedIDs()));
//  }

// ***** The following tests are for when an order cannot be placed. 
//	 @Test
//  // Expected that nothing is changed to the database as there is nothing in inventory to
//  // create a fill lamp study after its been removed.
//  // No changes made to the database. No order form should be created.
//  public void OrderForRemovedItems() {
//     LocateRequest myRequest = new LocateRequest(DBURL, USERNAME, PASSWORD, "lamp", "study", 2);
//     LocateRequest.getConnect().initializeConnection();
//     myRequest.getRequest();
//     String actualOrderedIDS = {"L928","L223","L982"};
//     assertTrue("OrderedIDs was incorrect: ", Arrays.equals(actualOrderedIDS, LocateRequest.getOrderedIDs()));
//  }

// ***** The following tests are expected to have invalid input messages ***** 

	@Test
	// Expects an error message to be printed to the command-line due to invalid furniture.
	// No changes made to the database. No order form should be created.
	public void CheckInvalidFurniture() {
		systemErrRule.clearLog();
		LocateRequest myRequest = new LocateRequest(DBURL, USERNAME, PASSWORD, "shoe", "mesh", 1);
		LocateRequest.getConnect().initializeConnection();
		myRequest.getRequest();
		assertEquals("The furniture provided is not valid", systemErrRule.getLog());
	}

	@Test
	// Expects an error message to be printed to the command-line due to invalid type.
	// No changes made to the database. No order form should be created.
	public void CheckInvalidOrderType() {
		systemErrRule.clearLog();
		LocateRequest myRequest = new LocateRequest(DBURL, USERNAME, PASSWORD, "lamp", "mesh", 1);
		LocateRequest.getConnect().initializeConnection();
		myRequest.getRequest();
		assertEquals("The furniture type provided is not valid", systemErrRule.getLog());
	}

	@Test
    // Expects an error message to be printed to the command-line due to invalid amount.
	// No changes made to the database. No order form should be created.
	public void checkInvalidAmount() {
        systemErrRule.clearLog();
		LocateRequest myRequest = new LocateRequest(DBURL, USERNAME, PASSWORD, "chair", "filing", 2);
		LocateRequest.getConnect().initializeConnection();
		myRequest.getRequest();
		assertEquals("The furniture type provided is not valid", systemErrRule.getLog());
	}

	@Test
    // Expects an error message to be printed to the command-line due to invalid amount.
	// No changes made to the database. No order form should be created.
	public void checkInvalidAmount2() {
        systemErrRule.clearLog();
		LocateRequest myRequest = new LocateRequest(DBURL, USERNAME, PASSWORD, "chair", "mesh", -1);
		LocateRequest.getConnect().initializeConnection();
		myRequest.getRequest();
		assertEquals("The amount provided is not valid", systemErrRule.getLog());

	}
    
//   @Test
//   public void checkCorrectTotalPrice2() {   
//	// the expected price should be $100
//         LocateRequest request2 = new LocateRequest(DBURL, USERNAME, PASSWORD, "filing", "small", 1);
//         LocateRequest.getConnect().initializeConnection();
//         request2.getRequest();
//         assertEquals("Total price was incorrect: ", 100, LocateRequest.getTotalPrice());
//   }
//	
	// the expected price should be $20
//         LocateRequest request3 = new LocateRequest(DBURL, USERNAME, PASSWORD, "lamp", "Desk", 1);
//         LocateRequest.getConnect().initializeConnection();
//         request2.getRequest();
//         assertEquals(20, request3.getBestPrice());
//
//	// the expected price should be $300
//         LocateRequest request4 = new LocateRequest(DBURL, USERNAME, PASSWORD, "filing", "small", 3);
//         LocateRequest.getConnect().initializeConnection();
//         request2.getRequest();
//         assertEquals(300, request4.getBestPrice());
//     }

	/**
	 * checkInvalidOutput method checks the expected output for different inputs the
	 * inputs should result in failure to order
	 * 
	 * @return void
	 */
//     @Test
//     public void checkInvalidOutput() {
//         LocateRequest myRequest = new LocateRequest(DBURL, USERNAME, PASSWORD, "chair", "Mesh", 2);
//         LocateRequest.getConnect().initializeConnection();
//         myRequest.getRequest();
//         String output = OrderForm.printInvalidOutputMessage();
//         assertEquals("Order cannot be fulfilled based on current inventory.  +" +
//                 "Suggested manufacturers are: Academic Desks\n", output);
//     }
//
//	/**
//	 * checkGetters method checks that the correct values are retrieved for getters
//	 * 
//	 * @return void
//	 */
//     @Test
//    public void checkGetters() {
//        LocateRequest myRequest = new LocateRequest(DBURL, USERNAME, PASSWORD, "chair", "Mesh", 1);
//        LocateRequest.getConnect().initializeConnection();
//        myRequest.getRequest();
//        assertEquals(LocateRequest.getFurniture(), "chair");
//        assertEquals(LocateRequest.getType(), "Mesh");
//        assertEquals(LocateRequest.getAmount(), 1);
//        assertEquals(LocateRequest.getTotalPrice(), 0);
//        myRequest.getRequest();
//        assertEquals(LocateRequest.getTotalPrice(), 150);
//
//        LocateRequest request2 = new LocateRequest(DBURL, USERNAME, PASSWORD, "lamp", "Swinging Arm", 3);
//        LocateRequest.getConnect().initializeConnection();
//        request2.getRequest();
//        assertEquals(LocateRequest.getFurniture(), "lamp");
//        assertEquals(LocateRequest.getType(), "swinging arm");
//        assertEquals(LocateRequest.getAmount(), 3);
//        assertEquals(LocateRequest.getTotalPrice(), 0);
//    }


}
