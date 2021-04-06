/**
 *  @author Lauraine Baffot
 *  @author Rachel Renegado
 *  @author Chloe Bouchard
 *  @version 1.3
 *  @since 1.0
 */

package edu.ucalgary.ensf409;

import javax.swing.*;

/**
 * GUI is a class that is responsible for creating the user interface that will
 * allow the user to input their order in an alternative way to the terminal. It
 * has one constructor that creates the panels for the user inputs.
 */
public class GUI {
	private String[] furnitureOptions = { "chair", "desk", "lamp", "filing" }; // options for furniture
	private String[] chairOptions = { "kneeling", "task", "mesh", "executive", "ergonomic" }; // options for chair type
	private String[] deskOptions = { "standing", "adjustable", "traditional" }; // options for desk type
	private String[] lampOptions = { "desk", "study", "swing arm" }; // options for lamp type
	private String[] filingOptions = { "small", "medium", "large" }; // options for filing type
	private String[] startOptions = { "Yes, please start!", "No, I want to exit." }; // options for buttons on the
																						// starting pane
	private String[] pfOptions = { "OK", "Cancel" }; // options for buttons on the JOptionPane

	/**
	 * A constructor with no arguments. This constructor creates a user interface
	 * that allows the user to input their information to connect to the database
	 * and input their desired order.
	 */
	public GUI() {
		int start = JOptionPane.showOptionDialog(null,
				"ENSF409 Final Project\n" + "Written by: Lauraine Baffot, Rachel Renegado and Chloe Bouchard\n"
						+ "\nWould you like to start?",
				"Supply Chain Management", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, startOptions,
				startOptions[0]); // a panel that with an introduction message and the choice to continue or exit
		if (start != JOptionPane.YES_OPTION) {
			System.exit(1); // if any button but the YES button is pressed, exit the program
		}

		String username = JOptionPane.showInputDialog("Please input username: "); // mysql username
		if (username == null) {
			System.exit(1); // if any button but the OK button is pressed, exit the program
		}

		String password = new String();
		JPasswordField pf = new JPasswordField(); // Does not show the original characters of the password
		Object[] obj = { "Please input password: ", pf };

		int passwordInt = JOptionPane.showOptionDialog(null, obj, "Input", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, pfOptions, pfOptions[0]); // mysql password
		if (passwordInt != JOptionPane.OK_OPTION) {
			System.exit(1); // if any button but the OK button is pressed, exit the program
		} else {
			password = new String(pf.getPassword()); // turns JPasswordFiedl into a string
		}

		String furniture = (String) JOptionPane.showInputDialog(null, "Please select a furniture category: ", "Input",
				JOptionPane.PLAIN_MESSAGE, null, furnitureOptions, furnitureOptions[0]); // gives a dropdown with
																							// choices for available
																							// furniture
		if (furniture == null) {
			System.exit(1); // if any button but the OK button is pressed, exit the program
		}

		String type = new String();
		if (furniture.equals(furnitureOptions[0])) {
			type = (String) JOptionPane.showInputDialog(null, "Please select a chair type for : ", "Input",
					JOptionPane.PLAIN_MESSAGE, null, chairOptions, chairOptions[0]); // gives a dropdown with choices
																						// for available chair type
		} else if (furniture.equals(furnitureOptions[1])) {
			type = (String) JOptionPane.showInputDialog(null, "Please select a desk type: ", "Input",
					JOptionPane.PLAIN_MESSAGE, null, deskOptions, deskOptions[0]); // gives a dropdown with choices for
																					// available desk type
		} else if (furniture.equals(furnitureOptions[2])) {
			type = (String) JOptionPane.showInputDialog(null, "Please select a lamp type: ", "Input",
					JOptionPane.PLAIN_MESSAGE, null, lampOptions, lampOptions[0]); // gives a dropdown with choices for
																					// available lamp type
		} else if (furniture.equals(furnitureOptions[3])) {
			type = (String) JOptionPane.showInputDialog(null, "Please select a filing type: ", "Input",
					JOptionPane.PLAIN_MESSAGE, null, filingOptions, filingOptions[0]); // gives a dropdown with choices
																						// for available filing type
		}
		if (type == null) {
			System.exit(1); // if any button but the OK button is pressed, exit the program
		}

		String num = JOptionPane.showInputDialog("Please input number of items: "); // amount of items desired
		if (num == null) {
			System.exit(1); // if any button but the OK button is pressed, exit the program
		}
		while (!Input.isNumeric(num) || (Integer.parseInt(num) < 1)) { // loop until the input is a number or is equal
																		// or more than 1
			if (!Input.isNumeric(num)) {
				num = JOptionPane.showInputDialog("This input is not a number. Please try again: ");
			} else if ((Integer.parseInt(num) < 1)) {
				num = JOptionPane.showInputDialog("This number of items is invalid. Please try again: ");
			}
			if (num == null) {
				System.exit(1); // if any button but the OK button is pressed, exit the program
			}
		}

		OrderForm.deleteFile(); // if previous orderForm text file exists, deletes it
		LocateRequest myJDBC = new LocateRequest("jdbc:mysql://localhost/INVENTORY", username.toLowerCase(),
				password.toLowerCase(), furniture.toLowerCase(), type.toLowerCase(), Integer.parseInt(num));
		LocateRequest.getConnect().initializeConnection(); // opening connection
		myJDBC.getRequest(); // calls multiple other function to find the cheapest possible combination for
								// the desired furniture

		if (LocateRequest.getOrderIsPossible()) { // if the order could be completed
			JOptionPane.showMessageDialog(null,
					OrderForm.printValidOutputMessage(myJDBC.getOrderedIDs(), myJDBC.getTotalPrice()),
					"Supply Chain Management", JOptionPane.PLAIN_MESSAGE); // print valid message with the details of
																			// the purchase
		} else { // if order could not be fulfilled
			JOptionPane.showMessageDialog(null, OrderForm.printInvalidOutputMessage(furniture.toLowerCase()),
					"Supply Chain Management", JOptionPane.PLAIN_MESSAGE); // print invalid message with suggested
																			// manufacturers
		}

		LocateRequest.getConnect().close(); // closing connection
	}
}
