/******************************

Julia Fairbank and Caroline Cutter
HW 1: Solving 8-Puzzle Via Search
Wednesday, March 9, 2022

HONOR CODE:
 - We have neither given nor received unauthorized aid on this assignment.
 - All group members were present and contributing during all work on this project.


********************************/


// IMPORTS
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Comparator;
import java.util.PriorityQueue;

/*
 * Solves the 8-Puzzle Game (can be generalized to n-Puzzle)
 */

public class EightPlayer {

	static Scanner scan = new Scanner(System.in);
	static int size=3; //size=3 for 8-Puzzle.
	static int numnodes; //number of nodes generated
	static int nummoves; //number of moves required to reach goal

	static int total_moves; // keeps track of total moves (used for average with multiple boards)

	static Node solution_node;
	static int board_choice;


	public static void main(String[] args)
	{
		int numsolutions = 0;

		int boardchoice = getBoardChoice();
		board_choice = board_choice;               // this is used in print function to determine if the solution gets printed or not
		int algchoice = getAlgChoice();

		//determine numiterations based on user's choices
		int numiterations=0;

		if(boardchoice==0)
			numiterations = 1;
		else {

			switch (algchoice){
			case 0:
				numiterations = 100;//BFS
				break;
			case 1:
				numiterations = 1000;//A* with Manhattan Distance heuristic
				break;
			case 2:
				numiterations = 1000;//A* with your new heuristic
				break;
			}
		}



		Node initNode;

		for(int i=0; i<numiterations; i++){

			if(boardchoice==0)
				initNode = getUserBoard();
			else
				initNode = generateInitialState();//create the random board for a new puzzle

			boolean result=false; //whether the algorithm returns a solution

			switch (algchoice){
				case 0:
					result = runBFS(initNode); //BFS
					break;
				case 1:
					result = runAStar(initNode, 0); //A* with Manhattan Distance heuristic
					break;
				case 2:
					result = runAStar(initNode, 1); //A* with your new heuristic
					break;
			}


			//if the search returns a solution
			if(result){

				numsolutions++;

				printSolution(solution_node);


				System.out.println("Number of nodes generated to solve: " + numnodes);
				System.out.println("Number of moves to solve: " + nummoves);
				System.out.println("Number of solutions so far: " + numsolutions);
				System.out.println("_______");

			}
			else
				System.out.print(".");

		}//for


		nummoves = total_moves;   // This lets the individual outputs have their own number of moves
															// then the total moves is printed out in the last statment

		System.out.println();
		System.out.println("Number of iterations: " +numiterations);

		if(numsolutions > 0){
			System.out.println("Average number of moves for "+numsolutions+" solutions: "+nummoves/numsolutions);
			System.out.println("Average number of nodes generated for "+numsolutions+" solutions: "+numnodes/numsolutions);
		}
		else
			System.out.println("No solutions in "+numiterations+"iterations.");

	}


	public static int getBoardChoice()
	{

		System.out.println("single(0) or multiple boards(1)");
		int choice = Integer.parseInt(scan.nextLine());

		return choice;
	}

	public static int getAlgChoice()
	{

		System.out.println("BFS(0) or A* Manhattan Distance(1) or A* Rows and Columns(2)");
		int choice = Integer.parseInt(scan.nextLine());

		return choice;
	}


	public static Node getUserBoard()
	{

		System.out.println("Enter board: ex. 012345678");
		String stbd = scan.nextLine();

		int[][] board = new int[size][size];

		int k=0;

		for(int i=0; i<board.length; i++){
			for(int j=0; j<board[0].length; j++){
				//System.out.println(stbd.charAt(k));
				board[i][j]= Integer.parseInt(stbd.substring(k, k+1));
				k++;
			}
		}


		for(int i=0; i<board.length; i++){
			for(int j=0; j<board[0].length; j++){
				//System.out.println(board[i][j]);
			}
			//System.out.println();
		}


		Node newNode = new Node(null,0, board);

		return newNode;


	}




	/**
	 * Generates a new Node with the initial board
	 */
	public static Node generateInitialState()
	{
		int[][] board = getNewBoard();

		Node newNode = new Node(null,0, board);

		return newNode;
	}


	/**
	 * Creates a randomly filled board with numbers from 0 to 8.
	 * The '0' represents the empty tile.
	 */
	public static int[][] getNewBoard()
	{

		int[][] brd = new int[size][size];
		Random gen = new Random();
		int[] generated = new int[size*size];
		for(int i=0; i<generated.length; i++)
			generated[i] = -1;

		int count = 0;

		for(int i=0; i<size; i++)
		{
			for(int j=0; j<size; j++)
			{
				int num = gen.nextInt(size*size);

				while(contains(generated, num)){
					num = gen.nextInt(size*size);
				}

				generated[count] = num;
				count++;
				brd[i][j] = num;
			}
		}


		// //Case 1: 12 moves
		// brd[0][0] = 1;
		// brd[0][1] = 3;
		// brd[0][2] = 8;
		//
		// brd[1][0] = 7;
		// brd[1][1] = 4;
		// brd[1][2] = 2;
		//
		// brd[2][0] = 0;
		// brd[2][1] = 6;
		// brd[2][2] = 5;


		//Case 2: 2 moves
		//string format: 123456078
		// brd[0][0] = 1;
		// brd[0][1] = 2;
		// brd[0][2] = 3;
		// brd[1][0] = 4;
		// brd[1][1] = 5;
		// brd[1][2] = 6;
		// brd[2][0] = 0;
		// brd[2][1] = 7;
		// brd[2][2] = 8;



		return brd;

	}

	/**
	 * Helper method for getNewBoard()
	 */
	public static boolean contains(int[] array, int x)
	{
		int i=0;
		while(i < array.length){
			if(array[i]==x)
				return true;
			i++;
		}
		return false;
	}



	/**
	 * TO DO:
     * Prints out all the steps of the puzzle solution and sets the number of moves used to solve this board.
  */
	public static void printSolution(Node node)
	{

			ArrayList<Node> searched_nodes = new ArrayList<Node>();

			Node curr = node;

			// reset num moves each time but keep track of total moves
			// The -- and -1 account for the fact that this prints the original board also (which doesnt count as a move)
			nummoves = -1;    
			total_moves --;

			// Add the nodes to teh ArrayList
			while(curr != null){
				searched_nodes.add(curr);
				nummoves++;
				total_moves++;
				curr = curr.getparent();
			}


				//Print out ArrayList in reverse order (start to finish)
				for(int i = searched_nodes.size() - 1; i >= 0; i-- ){
					searched_nodes.get(i).print();
			}
	   }



	/**
	 * TO DO:
	 * Runs Breadth First Search to find the goal state.
	 * Return true if a solution is found; otherwise returns false.
	 */
	public static boolean runBFS(Node initNode)
	{

		// 1. Initialize Frontier as a Queue and Explored as an ArrayList
		Queue<Node> Frontier = new LinkedList<Node>();
		ArrayList<Node> Explored = new ArrayList<Node>();

		// 2.  Frontier = {initial state}, explored = {empty list}
		Frontier.add(initNode);
		int maxDepth = 13;         // maximum depth of nodes in tree


		// 3. If initial state is goal return "Solution Found!" and exit
		if (initNode.isGoal()){
			solution_node = initNode;    // set global solution_node
			return true;
		}

		// 4. While Frontier is not empty:
		while(! (Frontier.isEmpty()) ){

			// 5. cur_state - remove the first node from fronteir list
			Node cur_state = Frontier.remove();


			// Terminate loop if depth of 13 is reached.
			if(cur_state.getdepth() >= maxDepth){
				//System.out.printf("%n%nMax depth of 13 reached. No Solution Found.");
				return false;
			}

			// 6. add cur_state to explored list
			Explored.add(cur_state);

			// 7. If cur_state = goal
			if (cur_state.isGoal()){

				// 8. return "Solution Found!" and set solution_node global
				solution_node = cur_state;
				return true;

			}
			// 9. else
			else {

					// 10. expand cur_state to get list of sucessor nodes;
					ArrayList<int[][]> expanded_states = new ArrayList<int[][]>();
					expanded_states = cur_state.expand();

					// 11. Add each sucessor to end of frontier list if not in explored or fronteir lists
					// 		 **NOTE** this loop runs through each element in expanded states, checks if
					//              it is in frontier and explored and only makes the node if it is not in either
					for (int i = 0; i < expanded_states.size(); i++)
						{

							boolean make_node = true;

							// Check if node is in Frontier
							for (Node item: Frontier) {
								if (item.isSameBoard(expanded_states.get(i))){
									make_node = false;
								}
							}

							// Check if node is in Explored
							for (int k = 0; k < Explored.size(); k++) {
								if (Explored.get(k).isSameBoard(expanded_states.get(i))){
									make_node = false;
								}
							}

							// 11. (Continued) If not in frontier or explored, make the node and add it to frontier
							if (make_node == true){

								//Make new node, increment nodes created, add to frontier
								Node new_node = new Node(cur_state, cur_state.getdepth() + 1, expanded_states.get(i));
								numnodes += 1;
								Frontier.add(new_node);
							}
						}
			}
		}
		// If no solution was found, return false
		return false;

	} // End BFS



	/***************************A* Code Starts Here ***************************/

	/**
	 * TO DO:
	 * Runs A* Search to find the goal state.
	 * Return true if a solution is found; otherwise returns false.
	 * heuristic = 0 for Manhattan Distance, heuristic = 1 for your new heuristic
	 */
	public static boolean runAStar(Node initNode, int heuristic)
	{
		// 1.  Initialize Priority Queue for Frontier and ArrayList for Explored
		//      **NOTE** We created a compare function sets priority as the
		//               the f(n) value for a node - this is in "NodeComparor.java"
		PriorityQueue<Node> Frontier = new PriorityQueue<Node>(new NodeComparor());
		ArrayList<Node> Explored = new ArrayList<Node>();

		// 2. Set the f(n) value for Init Node (do this by seeting g and h values)
		if (heuristic == 0){
			initNode.sethvalue(initNode.evaluateHeuristic());      // Manhattan Heuristic
		}
		else{
			initNode.sethvalue(initNode.ourHeuristic());           // Our heuristic
		}


		// 3. Add init to Frontier, increment number of nodes
		Frontier.add(initNode);
		numnodes++;

		// 4. While goal is not found, Frontier is not empty, depth < 13 (Note depth 13 taken care of in while loop)
		boolean goal_found = false;

		while(goal_found == false && !(Frontier.isEmpty())) {

			// 5. Remove from Frontier the node with lowest F(n) value (this is front of priority queue)
			Node X = Frontier.remove();

			// 6. Add X to explored
			Explored.add(X);

			// 7. If X is the goal state, we return true! Set solution_node global
			if (X.isGoal()){
				solution_node = X;
				return true;
			}

			// 8. If X is not the goal state
			else{

				// Check the current depth, if it is at 13 or greater, set over_13 true to terminate while loop
				if(X.getdepth() >= 13){
					//System.out.printf("%n%nMax depth of 13 reached. No Solution Found.%n%n");
					return false;
				}

				// 9. For each child node of X

				// Expand X to get the children
				ArrayList<int[][]> expanded_states = new ArrayList<int[][]>();
				expanded_states = X.expand();

				for (int i = 0; i < expanded_states.size(); i++){

					// Create the child as a Node -- set depth, parent and board
					Node new_node = new Node(X, X.getdepth() + 1, expanded_states.get(i));


					// Check if the child is in explored
					boolean in_explored = false;
					for (int j = 0; j < Explored.size(); j++) {
						if (Explored.get(j).isSameBoard(new_node.getboard())){
							in_explored = true;
						}
					}

					// 10. If child is in explored, ignore it
					if(in_explored == true){
						// Do nothing
					}

					// 11. If child is not in explored
					else{

							// 12. Set the f(n) value for child -- do this by setting h and g values              ********** Evaluate heuristic
							new_node.setgvalue(X.getgvalue() + 1);

							if (heuristic == 0){
								new_node.sethvalue(new_node.evaluateHeuristic());     // Manhattan Heuristic
							}
							else{
								new_node.sethvalue(new_node.ourHeuristic());          // Our heuristic
							}


							// 13. Check to see if C is in Fronteir
							double new_node_fvalue;
							double previous_fvalue;

							boolean in_frontier = false;

							// 14. Check each item in Frontier to see if it matches the child
							for (Node item: Frontier) {

								// 15. If C is in Frontier -- check if it has a lower cost than previous
								if (item.isSameBoard(new_node.getboard())){

									in_frontier = true;

									// Find the f(n) value for the child and the matching node in Frontier
									new_node_fvalue = new_node.getgvalue() + new_node.gethvalue();
									previous_fvalue = item.getgvalue() + item.gethvalue();

									// If Child has lower cost, then include C with the lower cost
									//     **NOTE** doing this by over writing the values of node in Frontier to be values of C
										item.sethvalue(new_node.gethvalue());
										item.setgvalue(new_node.getgvalue());
										item.setdepth(new_node.getdepth());
										item.setparent(new_node.getparent());

								}

							}

							// 16. If child was not in frontier, add it to frontier and increment numnodes
							if (in_frontier == false){
								numnodes += 1;
								Frontier.add(new_node);
							}
						}
					}
				}
			}// End of While loop

		// If solution not found --
	 	//System.out.print("No Solution Found");
		return false;
	}




// End of class
}
