## README

#Author Information#
--------------------------------------------------------------------------------
Name:   Lauraine Baffot   
UCID:   30086699                     
Email:  lauraine.baffot@ucalgary.ca  

Name:   Rachel Renegado               
UCID:   30089329                      
Email:  rachel.renegado@ucalgary.ca  

Name:   Chloe Bouchard         
UCID:   30103458                     
Email:  chloe.bouchard@ucalgary.ca

#How to Run Program#
--------------------------------------------------------------------------------
Please follow these steps to ensure the program is run properly:

    1. Ensure that LocateRequest.java, OrderForm.java, Input.java, Connect.java
       and GUI.java are in the same edu/ucalgary/ensf409 directory folder.
       
    2. Ensure that mysql-connector-java-8.0.23.jar is in the lib folder. 
    
    3. Ensure that both the folders edu and lib are in the same directory.
    
    4. In the Terminal, go to the directory that edu and lib are located by using
       "cd" (change directory).
       
    5. Before compiling ensure that all files are in the correct directories by 
       typing "dir" (directory) for Windows users or "ls" (list) for linux users 
       sand press Enter.

    6. To compile the program on Windows, enter:
       "javac -cp .;lib/mysql-connector-java-8.0.23.jar 
       edu/ucalgary/ensf409/LocateRequest.java" 
       into the Terminal and press Enter.
       
       To compile the program on Mac, enter:
       "javac -cp .:lib/mysql-connector-java-8.0.23.jar 
       edu/ucalgary/ensf409/LocateRequest.java" 
       into the Terminal and press Enter.

    7. To run the program on Windows enter: 
       "java -cp .;lib/mysql-connector-java-8.0.23.jar 
       edu.ucalgary.ensf409.LocateRequest" 
       into the Terminal and press Enter.
       
       To run the program on Mac enter: 
       "java -cp .:lib/mysql-connector-java-8.0.23.jar 
       edu.ucalgary.ensf409.LocateRequest" 
       into the Terminal and press Enter.

    8. A GUI client should open and you will be prompted to continue or to exit 
       the program.

    9. If an order is completed, the orderForm.txt file will be created within
       the same folder as the edu and lib folders.

#How to Run JUnit Tests#
--------------------------------------------------------------------------------
Please follow these steps to ensure the program is run properly:

    1. Ensure that LocateRequest.java, OrderForm.java, Input.java, Connect.java,
       GUI.java, and ProjectTest.java are in the edu/ucalgary/ensf409 directory 
       folder.

    2. Ensure that junit-4.13.2.jar, hamcrest-core-1.3.jar, system-rules-1.19.0.jar,
       and mysql-connector-java-8.0.23.jar are in the lib folder. 

    3. Ensure that both the folders edu and lib are in the same directory.

    4. Open to edit ProjectTest.java. Change and save the private static data
       members DBURL, USERNAME, and PASSWORD to the users respective inputs. 
       Save file.
    
    5. Using the terminal, go to the directory where edu and lib are located.

    6. To compile the java file, enter "javac edu/ucalgary/ensf409/LocateRequest.java" 
       into the Terminal and press Enter.

    7. To compile the test java file on Windows, enter:
       "javac -cp .;lib/junit-4.13.2.jar;lib/hamcrest-core-1.3.jar;lib/
       system-rules-1.19.0.jar;lib/mysql-connector-java-8.0.23.jar edu/ucalgary/
       ensf409/ProjectTest.java" 
       into the Terminal and press Enter.
       
       To compile the test java file On Mac, enter:
       "javac -cp .:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar:lib/
       system-rules-1.19.0.jar:lib/mysql-connector-java-8.0.23.jar edu/ucalgary/
       ensf409/ProjectTest.java" 
       into the Terminal and press Enter.

    8. To run the test on Windows, enter: 
       "java -cp .;lib/junit-4.13.2.jar;lib/hamcrest-core-1.3.jar;lib/
       system-rules-1.19.0.jar;lib/mysql-connector-java-8.0.23.jar 
       org.junit.runner.JUnitCore edu.ucalgary.ensf409.ProjectTest" 
       into the Terminal and press Enter.
       
       To run the test on Mac, enter:
       "java -cp .:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar:lib/
       system-rules-1.19.0.jar:lib/mysql-connector-java-8.0.23.jar 
       org.junit.runner.JUnitCore edu.ucalgary.ensf409.ProjectTest"
       into the Terminal and press Enter.

    9. The output should read that 20 tests passed if the program executes properly. 
