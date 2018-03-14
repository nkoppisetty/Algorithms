import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class WordPuzzle
{	
   private char[][] matrix;
   private MyHashTable<String> tableWithPrefixes = new MyHashTable<>();    
   private MyHashTable<String> tableWithoutPrefixes = new MyHashTable<>();    
   private int rows;
   private int columns;

   private int x[] = { 0, 0, 1, -1, -1, 1, -1, 1};
   private int y[] = { 1, -1, 0, 0, -1, 1, 1, -1};
	
   private String orientations[] = { "horizontally forward", "horizontally backward", "vertically downward", "vertically upward",
		   						    "diagonally upward left", "diagonally downward right",  "diagonally upward right", "diagonally downward left"};

   
   public WordPuzzle(int rows, int columns) {
	   this.rows = rows;
	   this.columns = columns;
	   matrix = generateGrid();
   }	
   
   private char[][] generateGrid(){
	   
	  Random r = new Random();
      char matrix[][] = new char[rows][columns];
      String alphabet = "abcdefghijklmnopqrstuvwxyz";

      for (int i = 0; i < rows; i++) {     
          for (int j = 0; j < columns; j++) {
        	   matrix[i][j] = alphabet.charAt(r.nextInt(alphabet.length()));
          }
      }
      
      return matrix;
   }	
	
   public void display() {
     System.out.println("Puzzle:");   
	 for (int i=0; i<matrix.length; i++){
        for (int j=0; j<matrix[i].length; j++)
           System.out.print(matrix[i][j] + " ");
        System.out.println();
      }
   }
   
   public MyHashTable<String> readDictionaryWithoutPrefixes(String path) throws IOException{	   
      BufferedReader in = new BufferedReader(new FileReader(path));
      String line = "";
      while ((line = in.readLine()) != null) {
    	  	tableWithoutPrefixes.insert(line, false);
      }
      in.close();
      
      return tableWithoutPrefixes;
   }	
   
   public void solveWithoutPrefixes(){
	   System.out.println("Without Enhancement:");
	   System.out.println("Words Found:");
		        
	   int count = 0;
	   long startTime = System.currentTimeMillis();
  	   StringBuilder sb = new StringBuilder();	
   
	   for (int i=0; i<rows; i++){
         for (int j=0; j<columns; j++){        	 
              sb.append(matrix[i][j]);
              if (tableWithoutPrefixes.contains(new String(sb))) {
                  System.out.println("Found \"" + sb +"\" at " + "(" + i + "," + j + ")");
                  count++;
              }    
              for(int d = 0 ; d < 8 ; d++) {
          	 	count += searchWithoutPrefixes(i,j,d);
              }
	      }
	    }
	      
	    long endTime = System.currentTimeMillis();  
	      
	    System.out.println("Total Words Found:"+count);
	    System.out.println("Time Taken:"+(endTime-startTime) +"milliseconds");
   }
   
   private int searchWithoutPrefixes(int row, int column, int d){
	   int count = 0;	   
	   StringBuilder sb = new StringBuilder();	
       sb.append(matrix[row][column]);
    	   	   
    	   for(int i = row + x[d], j = column + y[d]; i >= 0 && i < rows && j >= 0 && j < columns; i += x[d], j += y[d] ) {
    		   sb.append(matrix[i][j]);    
    		   if (tableWithoutPrefixes.contains(new String(sb))) {
              System.out.println("Found \"" + sb + "\" " + orientations[d] +" from " + "(" + row + "," + column + ") to ("+i+","+j+")");
               count++;
           } 
    	   }
    	   
	   return count;
   }
   
   public MyHashTable<String> readDictionaryWithPrefixes(String path) throws IOException{
      BufferedReader in = new BufferedReader(new FileReader(path));
      String line = "";
      while ((line = in.readLine()) != null) {
  	  	tableWithPrefixes.insert(line, false);
    	  	for(int i=2; i < line.length(); i++) {
    	  		tableWithPrefixes.insert(line.substring(0, i), true);
    	  	}
      }
      in.close();
      
      return tableWithPrefixes;
    }	
   
   public void solveWithPrefixes(){
	   System.out.println("With Enhancement:");
	   System.out.println("Words Found:");
		        
	   int count = 0;
	   long startTime = System.currentTimeMillis();
  	   StringBuilder sb = new StringBuilder();	
   
	   for (int i=0; i<rows; i++){
         for (int j=0; j<columns; j++){
              sb.append(matrix[i][j]);
              if (tableWithPrefixes.contains(new String(sb)) && !tableWithPrefixes.isPrefix(new String(sb))){
                 System.out.println("Found \"" + sb +"\" at " + "(" + i + "," + j + ")");
                  count++;
              } 
              for(int d = 0 ; d < 8 ; d++) {
          	 	count += searchWithPrefixes(i,j,d);

              }
	      }
	    }
	      
	    long endTime = System.currentTimeMillis();  
	      
	    System.out.println("Total Words Found:"+count);
	    System.out.println("Time Taken:"+(endTime-startTime) +"milliseconds");
   }
   
   private int searchWithPrefixes(int row, int column, int d){
	   int count = 0;	   
	   StringBuilder sb = new StringBuilder();	
       sb.append(matrix[row][column]);
      
	   for(int i = row + x[d], j = column + y[d]; i >= 0 && i < rows && j >= 0 && j < columns; i += x[d], j += y[d] ) {
 		   sb.append(matrix[i][j]);    
 		   if (tableWithPrefixes.contains(new String(sb))){	        	       
	   	       if(tableWithPrefixes.isPrefix(new String(sb))){
      	    	   		continue;
      	    	   }else{
        	    	     	System.out.println("Found \"" + sb + "\" " + orientations[d] +" from " + "(" + row + "," + column + ") to ("+i+","+j+")");
        	    	     	count++;
        	       }      	       
           }else{
        	   	   break;
           } 
	    } 	   
	   return count;
   }
   
   @SuppressWarnings("resource")
   public static void main(String args[]) throws IOException{	  
	  int rows = 0 ;
	  int columns = 0;
	  try {
		  System.out.println("Enter no of rows:");
		  Scanner scanner = new Scanner(System.in);
		  rows = Integer.valueOf(scanner.nextLine());
		  System.out.println("Enter no of columns:");
		  scanner = new Scanner(System.in);
		  columns = Integer.valueOf(scanner.nextLine());	   
	  }catch(Exception e){
		  throw new InvalidInputException();
	  }
	  
	  WordPuzzle puzzle = new WordPuzzle(rows,columns);
	  puzzle.display(); 
     
	  
	  // Solving the puzzle using prefixes table
	  puzzle.readDictionaryWithPrefixes("src/dictionary.txt");
      puzzle.solveWithPrefixes();


	  // Solving the puzzle using without prefixes table
	  puzzle.readDictionaryWithoutPrefixes("src/dictionary.txt");
      puzzle.solveWithoutPrefixes();

     
   }
}


class InvalidInputException extends RuntimeException
{
	InvalidInputException(){  
      super("Please provide valid input");  
   } 
}