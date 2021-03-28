package edu.ucalgary.ensf409;

import java.util.*;
import java.sql.*;
import java.lang.*;

public class Main {

	public static void main(String[] args) {
//		Scanner myUser = new Scanner(System.in); //Create scanner object
//		System.out.println("Please input username: "); //prompt input from user
//		String userName = myUser.nextLine();  //Read user input
//
//		System.out.println("Please input password: "); //prompt input from user
//		String password = myUser.nextLine();  //Read user input

//		System.out.println("Please input furniture category: "); //prompt input from user
//		String furnitureCategory = myUser.nextLine();  //Read user input

//		System.out.println("Please input furniture type: "); //prompt input from user
//		String furnitureType = myUser.nextLine();  //Read user input

//		System.out.println("Please input number of items: "); //prompt input from user
//		String numItems = myUser.nextLine();  //Read user input
		
		
		LocateRequest myJDBC = new LocateRequest("jdbc:mysql://localhost/inventory", "lauraine", "ensf409", "desk", "Traditional", 2);
		myJDBC.initializeConnection();
		
		myJDBC.getRequest();
		System.out.println("The best price is: " + myJDBC.getBestPrice());
		System.out.println("The total price is: " + myJDBC.getTotalPrice());
//		System.out.print("The best combination is:");
//		for (String val: myJDBC.getBestCombination()){
//			System.out.print(" " + val);
//		}
		//System.out.println(Arrays.toString(row));
		System.out.println();
		System.out.print("The orderedIds is:");
		for (String val: myJDBC.getOrderedIDs()){
			System.out.print(" " + val);
		}

//		for (String[] inner: myJDBC.getAllCombination()){
//			for(String val: inner) {
//				System.out.print(val);
//			}
//			System.out.println();
//		}

		// for (String[] inner: myJDBC.getRequest()){
		// 	for(String val: inner){
		// 		System.out.print(val);
		// 	}
		// 	System.out.println();
		// }
		
		
		
		myJDBC.close();
	}

}
