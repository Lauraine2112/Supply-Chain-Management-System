package edu.ucalgary.ensf409;

import javax.swing.*;

public class GUI {
	String[] furnitureOptions = { "chair", "desk", "lamp", "filing" };
	String[] chairOptions = { "kneeling", "task", "mesh", "executive", "ergonomic" };
	String[] deskOptions = { "standing", "adjustable", "traditional" };
	String[] lampOptions = { "desk", "study", "swing arm" };
	String[] filingOptions = { "small", "medium", "large" };
	String[] startOptions = { "Yes, please start!", "No, I want to exit." };

	public GUI() {
		int start = JOptionPane.showOptionDialog(null,
				"ENSF409 Final Project\n" + "Written by: Lauraine Baffot, Rachel Renegado and Chloe Bouchard\n"
						+ "\nWould you like to start?",
				"Supply Chain Management", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, startOptions,
				startOptions[0]);
		if (start != JOptionPane.YES_OPTION) {
			System.exit(1);
		}

		String username = JOptionPane.showInputDialog("Please input username: ");
		if (username == null) {
			System.exit(1);
		}

		String password = JOptionPane.showInputDialog("Please input password: ");
		if (password == null) {
			System.exit(1);
		}

		String furniture = (String) JOptionPane.showInputDialog(null, "Please select a furniture category: ", "Input",
				JOptionPane.PLAIN_MESSAGE, null, furnitureOptions, furnitureOptions[0]);
		if (furniture == null) {
			System.exit(1);
		}

		String type = new String();
		if (furniture.equals(furnitureOptions[0])) {
			type = (String) JOptionPane.showInputDialog(null, "Please select a chair type for : ", "Input",
					JOptionPane.PLAIN_MESSAGE, null, chairOptions, chairOptions[0]);
		} else if (furniture.equals(furnitureOptions[1])) {
			type = (String) JOptionPane.showInputDialog(null, "Please select a desk type: ", "Input",
					JOptionPane.PLAIN_MESSAGE, null, deskOptions, deskOptions[0]);
		} else if (furniture.equals(furnitureOptions[2])) {
			type = (String) JOptionPane.showInputDialog(null, "Please select a lamp type: ", "Input",
					JOptionPane.PLAIN_MESSAGE, null, lampOptions, lampOptions[0]);
		} else if (furniture.equals(furnitureOptions[3])) {
			type = (String) JOptionPane.showInputDialog(null, "Please select a filing type: ", "Input",
					JOptionPane.PLAIN_MESSAGE, null, filingOptions, filingOptions[0]);
		}
		if (type == null) {
			System.exit(1);
		}

		String num = JOptionPane.showInputDialog("Please input number of items: ");
		if (num == null) {
			System.exit(1);
		}
		while (!Input.isNumeric(num) || (Integer.parseInt(num) < 1)) {
			if (!Input.isNumeric(num)) {
				num = JOptionPane.showInputDialog("This input is not a number. Please try again: ");
			} else if ((Integer.parseInt(num) < 1)) {
				num = JOptionPane.showInputDialog("This number of items is invalid. Please try again: ");
			}
			if (num == null) {
				System.exit(1);
			}
		}

		OrderForm.deleteFile();
		LocateRequest myJDBC = new LocateRequest("jdbc:mysql://localhost/INVENTORY", username.toLowerCase(),
				password.toLowerCase(), furniture.toLowerCase(), type.toLowerCase(), Integer.parseInt(num));
//		LocateRequest myJDBC = new LocateRequest("jdbc:mysql://localhost/INVENTORY", "lauraine", "ensf409", "filing", "large", 2);
		LocateRequest.getConnect().initializeConnection(); // opening connection
		myJDBC.getRequest();

		if (LocateRequest.getOrderIsPossible()) {
			JOptionPane.showMessageDialog(null, OrderForm.printValidOutputMessage(), "Supply Chain Management",
					JOptionPane.PLAIN_MESSAGE);
//			System.out.println(OrderForm.printValidOutputMessage());
		} else {
			JOptionPane.showMessageDialog(null, OrderForm.printInvalidOutputMessage(), "Supply Chain Management",
					JOptionPane.PLAIN_MESSAGE);
//			System.out.println(OrderForm.printInvalidOutputMessage());
		}

		LocateRequest.getConnect().close(); // closing Connect
	}
}
