import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class Kruskals {
	
	public void getMST(){
	    int edgesAccepted = 0;
	    ArrayList<Edge> edgesAcceptedList = new ArrayList<Edge>();
	    DisjSets ds = new DisjSets(NUM_VERTICES);
	    PriorityQueue<Edge> pq = new PriorityQueue<Edge>( getEdges() );
	    Edge e; 

	    while (edgesAccepted < NUM_VERTICES - 1){
	        e = pq.poll();  
	        int uset = ds.find( e.getu() ); 
	        int vset = ds.find( e.getv() ); 
	         if (uset != vset) {
	        	     edgesAcceptedList.add(e);
	             edgesAccepted++;
	             ds.union(uset, vset); 
	         }
	   }
	  
	    System.out.println("Edges in the Minimum Spanning Tree are:");
	    int mstTotalDistance=0;		
		for(Edge edge: edgesAcceptedList){
			System.out.println("Edge:("+edgeIndexes.get(edge.getu())+", "+edgeIndexes.get(edge.getv())+") Distance:"+edge.getDistance());
			mstTotalDistance += edge.getDistance();
		}
		System.out.println("Total no. of edges in the MST:"+edgesAcceptedList.size());
		System.out.println("Sum of all the distances in the MST:"+mstTotalDistance);
	} 
	
	public ArrayList<Edge> getEdges(){
		return edges;
	}
	
	@SuppressWarnings("resource")
	public void readGraph(String file) {
		try {
			String line = "";
			BufferedReader br = new BufferedReader(new FileReader(file));
			int i = 0;
			edgesMap = new HashMap<String, Integer>();
			edges = new ArrayList<Edge>();
			edgeIndexes = new HashMap<Integer, String>();
			
            while ((line = br.readLine()) != null) {
            	    String[] data = line.split(",");
            	    
            	    if(!edgesMap.containsKey(data[0])) {
        	    			edgesMap.put(data[0], i);
        	    			edgeIndexes.put(i, data[0]);
        	    			i++;
            	    }
        	    
            	    for(int j=1;j<data.length;) {
	            	    	if(!edgesMap.containsKey(data[j])) {
	        	    			edgesMap.put(data[j], i);
	        	    			edgeIndexes.put(i, data[j]);
	        	    			edges.add(new Edge(edgesMap.get(data[0]),i,Double.parseDouble(data[j+1])));
	        	    			i++;
	            	    }else {
	            	    		edges.add(new Edge(edgesMap.get(data[0]),edgesMap.get(data[j]),Double.parseDouble(data[j+1])));
	            	    }	            	    	
	            	    j= j+2;   	
            	    }
            }
            
            NUM_VERTICES = i;

        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        } catch (IOException e) {
        		System.out.println("Error in reading the file. Error:"+e);
        } 
	}
	
	class Edge implements Comparable<Edge>{
		int u;
		int v;
		double dist;
		
		public Edge(int u, int v, double dist) {
			this.u = u;
			this.v = v;
			this.dist = dist;
		}
		
		public int getu() {
			return u;
		}
		
		public int getv() {
			return v;
		}
		
		public double getDistance() {
			return dist;
		}

		@Override
		public int compareTo(Edge other) {
			return (int) (this.getDistance() - other.getDistance());
		}
	}
	
	private int NUM_VERTICES;
	private ArrayList<Edge> edges;	
	private Map<String,Integer> edgesMap;	
	private Map<Integer,String> edgeIndexes;	
	
	public static void main(String args[]) {		
		Kruskals k = new Kruskals();
		k.readGraph("src/assn9_data.csv");
		k.getMST();
	}
}




