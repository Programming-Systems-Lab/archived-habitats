 /*FileIO.java
 *
 *
 * Created: Tue Oct  2 04:44:27 2001
 *
 * @author Christy Lauridsen
 * @version
 *
 * This class contains methods to read, manipulate, and write data files.
 * Specifically, these methods are designed to work with files of Wisconsin
 * Breast Cancer Data.  It is assumed that the input file contains 569 data
 * sets each with 32 attributes, where the first attribute is an id number, the
 * second is a label ("M" or "B"), and the remaining 30 are decimal numbers.
 *
 * The constructor for this class reads and parses the input file.  The
 * writeRandRowsFile method creates a new data file, randomizing the order of
 * the samples from the input file.  The writeFile method creates a new data
 * file containing only a given number of attributes for each data sample.
 *
 */

import java.io.*;
import java.util.*;

public class FileIO {

    String[][] data;
    int[] cols;
    int[] rows;
    int numCols;
    int numRows;
    
    /* constructor - read and parse input file, stroing data to data array */ 
    public FileIO() { }

    
    public Hashtable readFile(String inputFilename, String delimiter) { 
	Hashtable h = new Hashtable();
        try {
	    
            FileReader fr = new FileReader(inputFilename);
	    BufferedReader input = new BufferedReader(fr);
	    String currentLine;
	    
	    // read first line of file
	    currentLine = input.readLine();
		
	    // store data to array
	    while (currentLine!=null) {
		StringTokenizer stok = 
		    new StringTokenizer(currentLine, delimiter);
		
		h.put(stok.nextToken(), stok.nextToken());

		// read next line of file
		currentLine = input.readLine();
	    } 
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.toString());
        } catch (IOException e) {
            System.out.println("IOException : " + e.toString());
        } catch (NumberFormatException e) {
            System.out.println("Invalid Data: " + e.toString());
        }

	return h;                
    }

    // write new datafile with only 'n' attributes per sample and a
    // label for each sample
    public void writeFile(String outputFilename, String habitatName) {
        try {
            FileWriter fw = new FileWriter(outputFilename);
            PrintWriter output = new PrintWriter(fw);

	    output.println(habitatName);
            output.flush();

	    fw.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.toString());
        } catch (IOException e) {
            System.out.println("IOException : " + e.toString());
        } catch (NumberFormatException e) {
            System.out.println("Invalid Data: " + e.toString());
        }
    }

    
} // FileIO

