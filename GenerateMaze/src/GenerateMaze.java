import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.shape.Line;

public class GenerateMaze extends Application {

	
  @SuppressWarnings("resource")
  @Override 
  public void start(Stage primaryStage) {  
    System.out.println("Enter no of rows:");
    Scanner scanner = new Scanner(System.in);
    int rows = Integer.valueOf(scanner.nextLine());
    System.out.println("Enter no of columns:");
    scanner = new Scanner(System.in);
    int columns = Integer.valueOf(scanner.nextLine());	  
	
    Scene scene = new Scene(new Maze(rows,columns), 200, 200);
    primaryStage.setTitle("Maze"); 
    primaryStage.setScene(scene); 
    primaryStage.show(); 
  }
  
  public static void main(String[] args){
	  launch(args);
  }
}

class Maze extends Pane{
	private int rows;
	private int columns;
	
	private int[][] grid;
	private int[][] index;
	private DisjSets ds;
	Map<Integer, Cell> cells;

	// for display
	private final int margin = 20;
	private final int lineWidth = 15;
	
	public Maze(int rows, int columns){
		this.rows = rows;
		this.columns = columns;
		generateMaze();
		displayMaze();
	}
	
	private void generateMaze(){
		grid = new int[rows][columns];
		index = new int[rows][columns];
		cells = new HashMap<Integer, Cell>();

		int temp = 0;
		for(int i = 0; i<rows;i++){
			for(int j = 0; j < columns; j++){
				grid[i][j] = 3;
				index[i][j] = temp;
				cells.put(temp, new Cell(i,j));
				temp++;
			}
		}
		
		ds = new DisjSets(rows*columns);
		
		Random rand = new Random();
		int size = rows*columns;
		while(size > 1){			
			
			int root1 = rand.nextInt(rows*columns);
			Cell cell1 = cells.get(root1);
			
			int r1 = cell1.getRow();
			int c1 = cell1.getColumn();
			
			while(getRoot2(root1, r1, c1)<0){
				root1 = rand.nextInt(rows*columns);
			}
			
			int root2 = getRoot2(root1, r1, c1);
			Cell cell2 = cells.get(root2);

			int r2 = cell2.getRow();
			int c2 = cell2.getColumn();

			int rmin = Math.min(r1, r2);
			int cmin = Math.min(c1, c2);
			
			if(!(ds.find(root1)==ds.find(root2))){
				ds.union(ds.find(root1), ds.find(root2));
				
				if(r1==r2){
					if(grid[r1][cmin]==3)
						grid[r1][cmin]=2;
					else
						grid[r1][cmin]=0;
				}
				
				else if(c1==c2){
					if(grid[rmin][c1]==3)
						grid[rmin][c1]=1;
					else
						grid[rmin][c1]=0;
				}
				
				size--;
			}					
		}
	}
	
	public int getRoot2(int root, int row, int column){
		int count = 0;
		
		int[] adjIndex = new int[4];
		for(int i = 0; i<adjIndex.length;i++)
			adjIndex[i] = -1;
		
		if(row!=0){
			if(grid[row-1][column] != 0 || grid[row-1][column] != 1){
				adjIndex[count] = index[row-1][column];
				count++;
			}
		}
		
		if(row!=rows-1){
			if(grid[row][column]!=0 || grid[row][column]!=1){
				adjIndex[count] = index[row+1][column];
				count++;
			}
		}
		
		if(column!=0){
			if(grid[row][column-1]!=0 || grid[row][column]!=2){
				adjIndex[count] = index[row][column-1];
				count++;
			}
		}
		
		if(column!=columns-1){
			if(grid[row][column]!=0 || grid[row][column]!=2){
				adjIndex[count] = index[row][column+1];
				count++;
			}
		}
		
		
		if(adjIndex[0] < 0 && adjIndex[1] < 0 && adjIndex[2] < 0 && adjIndex[3] < 0)
			return -1;
		
		// choosing random adjacent cell out of 4 adjacent cells
		Random rand = new Random();
		int index = rand.nextInt(count);
		while(adjIndex[index]<0){
			index = rand.nextInt(count);
		}
		
		return adjIndex[index];
	}
	
	private void displayMaze() {
		int	y = margin;
		for(int i = 0; i<rows;i++){
			int x = margin;
			for (int j = 0 ; j < columns ; j++){
				x += lineWidth;
				if((grid[i][j]==1||grid[i][j]==3) && j!=columns-1)
					addLine(x,y,x,y+lineWidth);
			}
			y+=lineWidth;
		}
		
		// adding borders
		addLine(margin+lineWidth,margin,margin+lineWidth*columns,margin);
		addLine(margin,margin+lineWidth*rows,margin+lineWidth*columns,margin+lineWidth*rows);
		addLine(margin,margin,margin,margin+lineWidth*rows);
		addLine(margin+lineWidth*columns,margin,margin+lineWidth*columns,margin+lineWidth*rows-lineWidth);
		
		y = margin;
		for(int i = 0;i<rows;i++){
			int x = margin;
			y+=lineWidth;
			for (int j = 0 ; j < columns ; j++){
				if((grid[i][j] == 2) ||(grid[i][j] == 3))
					addLine(x,y,x+lineWidth,y);
				x += lineWidth;
			}
		}
		
	}
	
	private void addLine(int x1, int y1, int x2, int y2){
		Line line = new Line(x1,y1,x2,y2);
		getChildren().add(line);
	}
	
	public class Cell{
		int row;
		int column;
		
		public Cell(int row, int column) {
			this.row = row;
			this.column = column;
		}
		
		public int getRow() {
			return row;
		}
		
		public int getColumn() {
			return column;
		}
	}
}