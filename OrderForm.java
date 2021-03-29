package edu.ucalgary.ensf409;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.io.FileWriter;

public class OrderForm {
	 public void createFile() {
		 try {
			 File myObj = new File("orderForm.txt");
			 
			 if (myObj.createNewFile()) {
				 System.out.println("File created: " + myObj.getName());
			 	} else {
			 		System.out.println("File already exists or does not exist.");
			 	}
		 } catch (IOException e) {
			 e.printStackTrace();
		 }
		 
		 try {
			 String[] unique = Arrays.stream(LocateRequest.getOrderedIDs()).distinct().toArray(String[]::new);
			 FileWriter myWriter = new FileWriter("orderForm.txt");
			 
			 myWriter.write("Furniture Order Form\n\nFacultyName:\nContact:\nDate:\n\n");
			 myWriter.write("Original Request: " + LocateRequest.getType() + " " 
					 		+ LocateRequest.getFurniture() + ", " 
					 		+ LocateRequest.getAmount());
			 myWriter.write("\n\nItems Ordered\n" );
			 for (String val : unique) {
					myWriter.write("ID: " + val + "\n");
				}
			 myWriter.write("\nTotal Price: $" + LocateRequest.getTotalPrice() );
			 myWriter.close();
			 
			 System.out.println("Successfully wrote to the file.");
		 } catch (IOException e) {
			 e.printStackTrace();	 
		 }
	 }
}
