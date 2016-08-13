COMPILATION AND RUNNING INSTRUCTIONS

Before compiling and running shopgoodwill_database.java, zipcode.csv and jsoup-1.8.1.jar 
(can be found at  http://jsoup.org/download) have to be in the same 
directory as shopgoodwill_database.java. Java has be installed and
set in your PATH system variable. 

if you are using Windows compile by entering the following command into the command prompt:
javac -cp .;jsoup-1.8.1.jar shopgoodwill_database.java 

To run program in Windows, enter:
java -cp .;jsoup-1.8.1.jar shopgoodwill_database

if you are using Linux/Unix, compile by entering:
javac -cp .:jsoup-1.8.1.jar shopgoodwill_database.java 

To run the program in Linux/Unix, enter:
java -cp .:jsoup-1.8.1.jar shopgoodwill_database

