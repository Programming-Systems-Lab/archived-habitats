package psl.habitats;

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

