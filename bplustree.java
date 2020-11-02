import java.io.*;
import java.util.ArrayList;
import java.util.*;
 class bplustree 
{
	
	public static void main(String[] args) throws Exception {
		Map<String,String> inputt=new HashMap<String,String>();
		
		//this creates inputt and output files and also creates buffered readers and writers
		
		File inputFile = new File(args[0]);
		BufferedReader infile = new BufferedReader(new FileReader(inputFile+".txt"));
		
		File outputFile = new File("output_file.txt");
		if(!outputFile.exists()){
			outputFile.createNewFile();
			
		}
		FileWriter filewriter = new FileWriter(outputFile.getAbsoluteFile());
		PrintWriter printwriter = new PrintWriter(filewriter);
		
		
		int Node_degree = Integer.parseInt(infile.readLine().split("Initialize\\(")[1].split("\\)")[0]);
		BPTree BPlustree = new BPTree(Node_degree); //we are creating a bplus tree by passing the degree
		
		String input_line;
		
		while((input_line = infile.readLine()) != null){
			if(input_line.startsWith("Insert")){//this statement calls the insert method
					String s = input_line.split("Insert\\(")[1];
					String k = s.split(",")[0];
					System.out.println("k="+k);//to see the changing values of k while the loop executes
					Integer node_key = Integer.parseInt(k);
					String v=s.split(",")[1].split("\\)")[0];
					System.out.println("v="+v);//to see the changing values of v
					float value =Float.parseFloat(v);
					//inputt.put(input_line.split(Integer(node_key).toString(),value));
					inputt.put(k,v);
					BPlustree.inNode(node_key,value);
			
			}
			else if(input_line.startsWith("Delete"))
			{
			String s = input_line.split("Delete\\(")[1];
					String y=s.split("\\)")[0];
					int x =Integer.parseInt(y);
					BPlustree=BPlustree.delete(inputt,x);
				}
			else if(input_line.startsWith("Search")){//this calls search method
				if(input_line.contains(",")){ //we are using this here to call the range function
					String s=input_line.split("Search\\(")[1];
					String k=s.split(",")[0];
					int a=Integer.parseInt(k);
					String x=s.split(",")[1].split("\\)")[0];
					float z=Float.parseFloat(x);
					String range = BPlustree.node_search(a,z).trim();
					//System.out.println("range="+range);
					if( range.charAt(range.length() - 1) == ','){
						range = range.substring(0, range.length() - 1);
					}
					//System.out.println("rangefinal="+range);
					printwriter.println(range);
				}
				else { // these couple of statements are executed to call the node search method
					String s = input_line.split("Search\\(")[1];
					String y=s.split("\\)")[0];
					int x =Integer.parseInt(y);
					int zz = 0;
				
					for(int zzz =0;zzz<x;zzz++)
					{
						zz=zz+1;
					}
					String node_searchh = BPlustree.node_search(x).trim();
					//System.out.println("nodesearchsinglesearch="+node_searchh);
					if( node_searchh.charAt(node_searchh.length() - 1) == ','){
						node_searchh = node_searchh.substring(0, node_searchh.length() - 1);
					}
							//System.out.println("nodesearchsinglesearchfinal="+node_searchh);
					printwriter.println(node_searchh);
				}
				
			}
		}		
		filewriter.close();
		infile.close();
		//BPlustree=BPlustree.delete(inputt,21);
	}

}

    class BPTree {
	
	public static Bpnode root = null;
	public static int Node_degree;
	
	public BPTree(int Node_degree){
		this.Node_degree = Node_degree;
	}
	
static BPTree delete (Map inputt,Integer key){
		System.out.println(inputt);
		Iterator traverser=inputt.entrySet().iterator();
		BPTree bpt=new BPTree(Node_degree);
		String aoi= new Integer(key).toString();
		while(traverser.hasNext()){
			Map.Entry<String,String> mapelement=(Map.Entry)traverser.next();

			if(!mapelement.getKey().equals(aoi)){
				System.out.println(mapelement.getKey()+ " : "+  mapelement.getValue());
				bpt.inNode(Integer.parseInt(mapelement.getKey()),Float.parseFloat(mapelement.getValue()));
			}
		}
		return bpt;
	}
	
	
	protected static Bpnode inNode(Bpnode root, Bp_Datanode dataNode){
		//we are checking this to see whether root is leaf or not
		if(!root.Node_isleaf){ // if it is not
			root.Node_issplit = false;
			ArrayList<Integer> roots = new ArrayList<Integer>();
			roots = ((node_intNode)root).keys; // this shows arraylist of rootkeys
			int k = 0;
			while(k < roots.size()){
				//for inserting a brand new node
				if(roots.get(k) > dataNode.node_key || k == roots.size()-1) {
					int pos = roots.get(k) > dataNode.node_key ? k : k + 1;  //here we are checking the right positioning in the node
					
					Bpnode node = new Bpnode();
					node = inNode(((node_intNode)root).intPtrs.get(pos), dataNode); //for insertion at right place and to check everything recursively using pointers
					
					if(node.Node_issplit){ // after successful insertion if size of data node is eqal to degree the it splits
						
						((node_intNode)root).insert(((node_intNode)node).keys.get(0), ((node_intNode)node).intPtrs.get(0),
								((node_intNode)node).intPtrs.get(1)); // for insertion which is in the middle of the pointers
						if(((node_intNode)root).keys.size() == Node_degree){ //for check of degree
							int node_splitPos = ((int) Math.ceil(((double)Node_degree)/2)) - 1;
							node_intNode intNode = new node_intNode();
							node_intNode childrenNode = new node_intNode();
							intNode.insert(((node_intNode)root).keys.get(node_splitPos), root, childrenNode); //addition of the respective key at the designated split position for parent pointer
							((node_intNode)root).keys.remove(node_splitPos);//to remove in the internal node
							

							int i = node_splitPos, j = node_splitPos,lu=0;

							while( i < Node_degree-1){// we are splitting the node here at the split position from current node to adding a new node
								childrenNode.keys.add(((node_intNode)root).keys.remove(j));
								i++;
							}
							while(lu != 0)
							{
								System.out.println("checking the middle position");
							}
							 j = node_splitPos + 1;
							 i = node_splitPos+1;
							while(i <= Node_degree){ //here the pointer is updated 
								childrenNode.intPtrs.add(((node_intNode)root).intPtrs.remove(j));
								i++;
							}
							intNode.Node_issplit = true;						
							return intNode;
						}
						return root;
					}
					else // when there is no split happening
						return root;
					
				}
				k++;
			}
		}
		else{ //when root is a leaf node 
			((Node_leaf)root).inNode(dataNode); //for insertion of a node

			if(((Node_leaf)root).dataNodes.size() == Node_degree){ // if datanode is equal to size then split
				int position = Node_degree/2; //CENTER SPLIT
				Node_leaf leaf = new Node_leaf();
				for(int i = position, j = position; i < Node_degree; i++){ //we are splitting the node here
					leaf.inNode(((Node_leaf)root).dataNodes.remove(j));
					int cond=1;
					while(cond!=1)
					{
						System.out.println("condition check");
					} 
					}
				
				//we are updating the pointer for the resultant root
				leaf.next = ((Node_leaf)root).next;
				((Node_leaf)root).next = leaf;
				leaf.prev = ((Node_leaf)root);
				if(leaf.next != null)
					leaf.next.prev = leaf;
				
				//updation of split to the resultant root
				node_intNode split = new node_intNode();
				split.keys.add(leaf.dataNodes.get(0).node_key);
				split.Node_issplit = true;
				split.intPtrs.add((Node_leaf)root);
				split.intPtrs.add(leaf);
				int x_split=0;
				while(x_split>1)
				{
					if(leaf.next.prev ==leaf)
					{
						System.out.println("node split");
					}
					else
						{
						System.out.println("no node split");
						}
				}
				return split;
			}
			else
				return root;
		}
		
		return root;
	}

	


	
				//inside function for insertion of a new node
	protected static void inNode(int node_key, float value){
		Bp_Datanode dataNode = new Bp_Datanode(node_key, value);
		if(root == null){
			root = new Node_leaf();
			((Node_leaf)root).inNode(new Bp_Datanode(node_key, value));
		}
		else{
			root = inNode(root, dataNode);
			
		}
		
	}
	
	//this is code for search
	
	protected static String node_search(int node_key){
		Bpnode node = new Bpnode();
		node = root;
		//when node is not a leaf
		while(!node.Node_isleaf){
			int i = 0;
			while(i < ((node_intNode)node).keys.size() && ((node_intNode)node).keys.get(i) < node_key){ // we are travelling to the last node
				i++;
			}
			node = ((node_intNode)node).intPtrs.get(i); // travelling to the position where it finds the key
		}
		
		StringBuilder str = new StringBuilder();
		int fla= 0;
		
		while((Node_leaf)node != null ){int i=0; 
			while(i < ((Node_leaf)node).dataNodes.size()){
				int keyVal = ((Node_leaf)node).dataNodes.get(i).node_key; 
				if(keyVal == node_key)
				{ fla =1;
					str.append(((Node_leaf)node).dataNodes.get(i).node_val).append(", "); //addition of all the key values to the stringbuilder
				break;
				
				}
				i++;
				
				}
			
			node = ((Node_leaf)node).next;
			if(fla==1)break;
		}
		if(str.toString().equals("")){int lu=0; 
			while(lu ==1)
						{
						System.out.println("checking the middle position");
						}
			return "Null";	
		}
			
		else{
			return str.toString();
		}
	}


	
	//Implementation of range search
	protected static StringBuilder strHelp(StringBuilder str, Bpnode node, Double key1, Double key2){
		
		ArrayList<Bp_Datanode> bpt_LeafList = new ArrayList<Bp_Datanode>();
		bpt_LeafList = ((Node_leaf)node).dataNodes;
		int i = 0,lu=0;int dup=0;
		while(i < bpt_LeafList.size()){
			while(lu !=0)
				{
				System.out.println("checking the middle position");
				}
				//dup=bpt_LeafList.get(i).node_key ;
			if(bpt_LeafList.get(i).node_key >= key1 && bpt_LeafList.get(i).node_key <= key2 && (dup!=bpt_LeafList.get(i).node_key ))
			{	dup=bpt_LeafList.get(i).node_key;

				str.append(bpt_LeafList.get(i).node_val).append(","); //APPENDING ALL OUR RANGE KEY,VALUES
			}
			if(bpt_LeafList.get(i).node_key > key2){
				//System.out.println("strt=  "+str);
				return str;
				
			}
			i++;
		}
		//	System.out.println("strwwwt=  "+str);
		return str;
	}
	
	
	protected static String node_search(double key1, double key2){
		Bpnode node = new Bpnode();
		node = root;
		while(!node.Node_isleaf){ // we are moving down until and unless our node is leaf
			int index = 0;
			while(index < ((node_intNode)node).keys.size() && ((node_intNode)node).keys.get(index) < key1){ // moving down on the right path
				index++;
			int n_index=1;
			while(n_index!=1){
				if(n_index==index)
				{
					n_index++;
				}
				else
				{
					n_index=index+1;
				}
			}
			}
			node = ((node_intNode)node).intPtrs.get(index); // we are using poimters to get the first node
		}
	
		StringBuilder str = new StringBuilder();  //for addition of values ranging from key 1-key2
		str = strHelp(str, node, key1, key2); //addition with key1
		while(((Node_leaf)node).next != null){
			
			// here we ar echecking the condition for the next node
			if(((Node_leaf)node).next.dataNodes.get(0).node_key > key2){  
				break;
			}
			else{ // node has key in our range
				str = strHelp(str, ((Node_leaf)node).next, key1, key2); 
			}
			node = ((Node_leaf)node).next; // node is updated
			String value="null";
			while(value!="null")
			{
				System.out.println("checking whether node has key in our range");
			}
		}
		
		if(str.toString().equals("")){
			return "Null";	
		}	
		else{
			return str.toString();
		}
			
	}
	
}

   
   class node_intNode extends Bpnode {
	
	protected ArrayList<Integer> keys = new ArrayList<Integer>();
	protected ArrayList<Bpnode> intPtrs = new ArrayList<Bpnode>();
	
	/*here we are inserting in a sorted array*/
	protected void insert(int node_key, Bpnode ptr1, Bpnode ptr2){
		if(this.keys.isEmpty()){
			this.keys.add(node_key);
			this.intPtrs.add(ptr1);
			this.intPtrs.add(ptr2);
		}
		else{
			
			for(int i=0;i < this.keys.size();i++){
				if(this.keys.get(i) > node_key) {
				String value="null"; // to see how the flow changes
				while(value!="null")
				{ if(value!="null")
					System.out.println("checking");
					else
					{
					System.out.println("good to go");
					}
				}
					this.keys.add(i, node_key);
					this.intPtrs.add(i + 1, ptr2);
					
					return;
				}
				
			}
			this.keys.add(node_key);
			this.intPtrs.add(ptr2);
		}
		return;
	}
	
}

  class Bpnode {
	
	protected boolean Node_isleaf = false;
	protected int Node_degree;
	protected boolean Node_issplit = false;
	
}

 class Node_leaf extends Bpnode {
	
	
	
	protected ArrayList<Bp_Datanode> dataNodes = new ArrayList<Bp_Datanode>();
	protected Node_leaf prev = null;
	protected Node_leaf next = null;
	
			
	protected Node_leaf() {
		this.Node_isleaf = true;
	}
	
	/*insertion of a datanode to anode which is leaf*/
	protected void inNode(Bp_Datanode dataNode){
		if(dataNodes.isEmpty()){
			dataNodes.add(dataNode);
		}
		else{ /*data node is inserted in a right path*/
			for(int i = 0; i < dataNodes.size(); i++){
				if(dataNodes.get(i).node_key > dataNode.node_key){
					dataNodes.add(i, dataNode);
					return;

				}
			}
			dataNodes.add(dataNode);
		}
	}
}

   

class Bp_Datanode {
	protected float node_val;
	protected int node_key;
	
	
	public Bp_Datanode(int node_key, float node_val){
		this.node_key = node_key;
		this.node_val = node_val;
	}
}



