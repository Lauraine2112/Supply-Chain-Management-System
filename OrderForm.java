package edu.ucalgary.ensf409;

import java.io.*;
import java.util.*;
import java.sql.*;

public class OrderForm {

	/**
	 * method createFile creates a text file called orderForm.txt
	 * it uses data from LocateRequest to fill out the form
	 * 
	 * @return void
	 */
	public static void createFile() {
		try {
			File myObj = new File("orderForm.txt"); 
			myObj.createNewFile(); //create new file with name orderForm.txt

//			 if (myObj.createNewFile()) {
//				 System.out.println("File created: " + myObj.getName());
//			 	} else {
//			 		System.out.println("File already exists or does not exist.");
//			 	}
		} catch (IOException e) {
			e.printStackTrace(); 
		}

		try {
			String[] unique = Arrays.stream(LocateRequest.getOrderedIDs()).distinct().toArray(String[]::new);
			FileWriter myWriter = new FileWriter("orderForm.txt");

			myWriter.write("Furniture Order Form\n\nFaculty Name:\nContact:\nDate:\n\n");
			myWriter.write("Original Request: " + LocateRequest.getType() + " "
					+ LocateRequest.getFurniture().toLowerCase() + ", " + LocateRequest.getAmount());
			myWriter.write("\n\nItems Ordered\n");
			for (String val : unique) {
				myWriter.write("ID: " + val + "\n");
			}
			myWriter.write("\nTotal Price: $" + LocateRequest.getTotalPrice());
			myWriter.close();

//			 System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * method deleteFile deletes the previous orderForm
	 * 
	 * @return void
	 */
	public static void deleteFile() {
		File myObj = new File("orderForm.txt");
		myObj.delete();
	}

	/**
	 * method printValidOutputMessage returns a message confirming that an order was completed
	 * 
	 * @return String
	 */
	public static String printValidOutputMessage() { // changed return type from void to String
		String[] unique = Arrays.stream(LocateRequest.getOrderedIDs()).distinct().toArray(String[]::new);
		StringBuffer validMessage = new StringBuffer();
		validMessage.append("User request: " + "\nType: " + LocateRequest.getType() + "\nFurniture category: "
				+ LocateRequest.getFurniture().toLowerCase() + "\nNumber of items requested: "
				+ LocateRequest.getAmount() + "\n");
		validMessage.append("Output: Purchase \n");
		for (String val : unique) {
			validMessage.append(" - " + val + "\n");
		}
		validMessage.append("for $" + LocateRequest.getTotalPrice());

//		System.out.println(validMessage.toString());
		return validMessage.toString();
	}

	/**
	 * method printInvalidOutputMessage returns a message saying that an order was not completed
	 * 
	 * @return String
	 */
	public static String printInvalidOutputMessage() { // changed return type from void to String
		StringBuffer invalidMessage = new StringBuffer();
		int rowNum = 0;
		String[] manuIDs;
		String[] uniqueIDs;
		String[] manuNames;

		try {
			Statement myStmtCount = Connect.getDbConnect().createStatement(); // creates a statement
			ResultSet rs = myStmtCount.executeQuery("SELECT COUNT(*) FROM " + LocateRequest.getFurniture());

			while (rs.next()) {
				rowNum = rs.getInt(1);
			}
			myStmtCount.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		manuIDs = new String[rowNum];

		try {
			Statement myStmtManufacturer = Connect.getDbConnect().createStatement(); // creates statement
			LocateRequest.getConnect()
					.setResults(myStmtManufacturer.executeQuery("SELECT * FROM " + LocateRequest.getFurniture()));

			int i = 0;
			while (Connect.getResults().next()) {
				manuIDs[i] = Connect.getResults().getString("ManuID");
				i++;
			}

			myStmtManufacturer.close(); // close statement
		} catch (SQLException e) {
			e.printStackTrace();
		}

		uniqueIDs = Arrays.stream(manuIDs).distinct().toArray(String[]::new);
		manuNames = new String[uniqueIDs.length];

		try {
			Statement myStmtInvalidOutput = Connect.getDbConnect().createStatement(); // creates statement
			LocateRequest.getConnect().setResults(myStmtInvalidOutput.executeQuery("SELECT * FROM MANUFACTURER"));

			int count = 0;
			while (Connect.getResults().next()) {
				for (String val : uniqueIDs) {
					if (val.equals(Connect.getResults().getString("ManuID"))) {
						manuNames[count] = Connect.getResults().getString("Name");
						count++;
						break;
					}
				}
			}
			myStmtInvalidOutput.close(); // close statement
		} catch (SQLException e) {
			e.printStackTrace();
		}

		invalidMessage
				.append("Order cannot be fulfilled based on current inventory. " + "Suggested manufacturers are:\n");
		for (String val : manuNames) {
			invalidMessage.append(val + "\n");
		}

//		System.out.println(invalidMessage.toString());
		return invalidMessage.toString();
//		System.exit(1);
	}
}
