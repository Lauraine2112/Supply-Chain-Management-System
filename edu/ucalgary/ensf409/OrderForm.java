/**
 *  @author Lauraine Baffot
 *  @author Rachel Renegado
 *  @author Chloe Bouchard
 *  @version 2.6
 *  @since 1.0
 */

package edu.ucalgary.ensf409;

import java.io.*;
import java.util.*;

/**
 * OrderForm is a class that is responsible for creating and deleting a text
 * file called orderForm.txt. It is also responsible for creating the
 * appropriate string output message. This class utilizes the data from
 * LocateRequest.
 */
public class OrderForm {
	private static File myObj;

	public static File getMyObj() {
		return myObj;
	}

	/**
	 * method createFile creates a text file called orderForm.txt it uses data from
	 * LocateRequest to fill out the form
	 * 
	 * @return void
	 */
	public static void createFile(String[] orderedIDs, int totalPrice) {
		try {
			myObj = new File("orderForm.txt");
			myObj.createNewFile(); // create new file with name orderForm.txt
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			String[] unique = Arrays.stream(orderedIDs).distinct().toArray(String[]::new);
			FileWriter myWriter = new FileWriter("orderForm.txt");

			myWriter.write("Furniture Order Form\n\nFaculty Name:\nContact:\nDate:\n\n");
			myWriter.write("Original Request: " + LocateRequest.getType() + " "
					+ LocateRequest.getFurniture().toLowerCase() + ", " + LocateRequest.getAmount());
			myWriter.write("\n\nItems Ordered\n");
			for (String val : unique) {
				myWriter.write("ID: " + val + "\n");
			}
			myWriter.write("\nTotal Price: $" + totalPrice);
			myWriter.close();

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
		myObj = new File("orderForm.txt");
		myObj.delete();
	}

	/**
	 * method printValidOutputMessage returns a message confirming that an order was
	 * completed
	 * 
	 * @return String
	 */
	public static String printValidOutputMessage(String[] orderedIDs, int totalPrice) {
		String[] unique = Arrays.stream(orderedIDs).distinct().toArray(String[]::new);
		StringBuffer validMessage = new StringBuffer();
		validMessage.append("User request: " + "\nType: " + LocateRequest.getType() + "\nFurniture category: "
				+ LocateRequest.getFurniture().toLowerCase() + "\nNumber of items requested: "
				+ LocateRequest.getAmount() + "\n");
		validMessage.append("Output: Purchase \n");
		for (String val : unique) {
			validMessage.append(" - " + val + "\n");
		}
		validMessage.append("for $" + totalPrice);

		return validMessage.toString();
	}

	/**
	 * method printInvalidOutputMessage returns a message saying that an order was
	 * not completed
	 * 
	 * @return String
	 */
	public static String printInvalidOutputMessage(String furniture) {
		String[] manuNames = { "Academic Desks", "Office Furnishings", "Furniture Goods", "Fine Office Supplies",
				"Chairs R Us" };

		StringBuffer invalidMessage = new StringBuffer();
		invalidMessage
				.append("Order cannot be fulfilled based on current inventory. " + "Suggested manufacturers are:\n");

		if (furniture.equals("desk")) {
			for (int i = 0; i < manuNames.length - 1; i++) {
				invalidMessage.append(manuNames[i] + "\n");
			}
		} else if (furniture.equals("lamp") || furniture.equals("filing")) {
			for (int i = 1; i < manuNames.length - 1; i++) {
				invalidMessage.append(manuNames[i] + "\n");
			}
		} else if (furniture.equals("chair")) {
			for (int i = 1; i < manuNames.length; i++) {
				invalidMessage.append(manuNames[i] + "\n");
			}
		}

		return invalidMessage.toString();
	}
}
