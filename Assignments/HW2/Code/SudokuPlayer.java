
/******************************

Julia Fairbank and Caroline Cutter
HW 2: Sudoku Player with CSP
Wednesday, March 9, 2022

HONOR CODE:
 - We have neither given nor received unauthorized aid on this assignment.
 - All group members were present and contributing during all work on this project.



** We took a late day on this assingment and due to break we were not able
to collaborate together until Monday, so we are submitting it Monday night
as 1 late day. 
********************************/




import javax.swing.*;


import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.DecimalFormat;
import java.awt.List;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Comparator;
import java.util.PriorityQueue;



public class SudokuPlayer implements Runnable, ActionListener {

    // final values must be assigned in vals[][]
    int[][] vals = new int[9][9];
    Board board = null;


    /// --- AC-3 Constraint Satisfaction --- ///

    // Useful but not required Data-Structures;
    ArrayList<Integer>[] globalDomains = new ArrayList[81];
    ArrayList<Integer>[] neighbors = new ArrayList[81];
    Queue<Arc> globalQueue = new LinkedList<Arc>();



	/*
	 * This method sets up the data structures and the initial global constraints
	 * (by calling allDiff()) and makes the initial call to backtrack().
	 * You should not change this method header.
 	 */
    private final void AC3Init(){
        //Do NOT remove these lines (required for the GUI)
        board.Clear();
		recursions = 0;

		//fill in globalDomains
    for(int i = 0; i < 81; i++){

      int row = i / 9;
      int col = i % 9;


				ArrayList<Integer> domain = new ArrayList<Integer>();
				if(vals[row][col] == 0) { //if there is nothing stored in cell.. domain can be any val 1-9
					domain.add(1);
					domain.add(2);
					domain.add(3);
					domain.add(4);
					domain.add(5);
					domain.add(6);
					domain.add(7);
					domain.add(8);
					domain.add(9);
					//globalDomains[k].addAll(domain);
          globalDomains[i] = domain;
					}
				else { //if there is a value in a cell, the domain is only that value
					domain.add(vals[row][col]);

          globalDomains[i] = domain;

			}
    }


        allDiff();



         // Initial call to backtrack() on cell 0 (top left)
        boolean success = backtrack(0,globalDomains);


        Finished(success);

    }



    /*
     *  This method defines constraints between a set of variables.
     *  Refer to the book for more details. You may change this method header.
     */

    private final void allDiff(){

    	// fill in neighbors

     for(int i = 0; i < 81; i++) {
       ArrayList<Integer> neighborsOfI = new ArrayList<Integer>();

       int tile = i;

       // row constraint
       int row = tile / 9;
       int col = tile % 9;

       int tileNext = tile + 1;
       int tileDown = tile + 9;

       int start_of_row = tile - col;

       for(int j = 0; j < 9; j++){

           if((start_of_row + j) != tile){
             neighborsOfI.add((start_of_row + j));
             globalQueue.add(new Arc(tile, (start_of_row + j)));
             globalQueue.add(new Arc((start_of_row + j), tile));
           }

       }

       // column constraints

       int start_of_col = tile - (row*9);

       for(int j = 0; j < 9; j++){

         if((start_of_col + (j*9)) != tile){
         neighborsOfI.add(start_of_col + j*9);
         globalQueue.add(new Arc(tile, (start_of_col + j*9)));
         globalQueue.add(new Arc((start_of_col + j*9),tile));

       }
     }

      neighbors[tile]=(neighborsOfI);


     }


    	//hard-code regional constraints

		//region 1
    	int[] r1 = {0, 1, 2, 9, 10, 11, 18, 19, 20};

    	for (int v : r1){
    		ArrayList<Integer> regionNeighbors = new ArrayList<Integer>();
            for (int v2 : r1){
            	if (v != v2){
            		regionNeighbors.add(v2);
                globalQueue.add(new Arc(v, v2));
                globalQueue.add(new Arc(v2, v));
              }
            }
            neighbors[v].addAll(regionNeighbors);
    	}

    	//region 2
    	int[] r2 = {3, 4, 5, 12, 13, 14, 21, 22, 23};

    	for (int v : r2){
    		ArrayList<Integer> regionNeighbors = new ArrayList<Integer>();
            for (int v2 : r2){
            	if (v != v2){
            		regionNeighbors.add(v2);
                globalQueue.add(new Arc(v, v2));
                globalQueue.add(new Arc(v2, v));
                }
              }
            neighbors[v].addAll(regionNeighbors);
    	}

    	//region 3
    	int[] r3 = {6, 7, 8, 15, 16, 17, 24, 25, 26};

    	for (int v : r3){
    		ArrayList<Integer> regionNeighbors = new ArrayList<Integer>();
            for (int v2 : r3){
            	if (v != v2){
            		regionNeighbors.add(v2);
                globalQueue.add(new Arc(v, v2));
                globalQueue.add(new Arc(v2, v));
                }
              }
            neighbors[v].addAll(regionNeighbors);
    	}

    	//region 4
    	int[] r4 = {27, 28, 29, 36, 37, 38, 45, 46, 47};

    	for (int v : r4){
    		ArrayList<Integer> regionNeighbors = new ArrayList<Integer>();
            for (int v2 : r4){
            	if (v != v2){
            		regionNeighbors.add(v2);
                globalQueue.add(new Arc(v, v2));
                globalQueue.add(new Arc(v2, v));
                }
              }
            neighbors[v].addAll(regionNeighbors);
    	}

    	//region 5
    	int[] r5 = {30, 31, 32, 39, 40, 41, 48, 49, 50};

    	for (int v : r5){
    		ArrayList<Integer> regionNeighbors = new ArrayList<Integer>();
            for (int v2 : r5){
            	if (v != v2){
            		regionNeighbors.add(v2);
                globalQueue.add(new Arc(v, v2));
                globalQueue.add(new Arc(v2, v));
                }
              }
            neighbors[v].addAll(regionNeighbors);
    	}

    	//region 6
    	int[] r6 = {33, 34, 35, 42, 43, 44, 51, 52, 53};

    	for (int v : r6){
    		ArrayList<Integer> regionNeighbors = new ArrayList<Integer>();
            for (int v2 : r6){
            	if (v != v2){
            		regionNeighbors.add(v2);
                globalQueue.add(new Arc(v, v2));
                globalQueue.add(new Arc(v2, v));
                }
              }
            neighbors[v].addAll(regionNeighbors);
    	}

    	//region 7
    	int[] r7 = {54, 55, 56, 63, 64, 65, 72, 73, 74};

    	for (int v : r7){
    		ArrayList<Integer> regionNeighbors = new ArrayList<Integer>();
            for (int v2 : r7){
            	if (v != v2){
            		regionNeighbors.add(v2);
                globalQueue.add(new Arc(v, v2));
                globalQueue.add(new Arc(v2, v));
                }
              }
            neighbors[v].addAll(regionNeighbors);
    	}

    	//region 8
    	int[] r8 = {57, 58, 59, 66, 67, 68, 75, 76, 77};

    	for (int v : r8){
    		ArrayList<Integer> regionNeighbors = new ArrayList<Integer>();
            for (int v2 : r8){
            	if (v != v2){
            		regionNeighbors.add(v2);
                globalQueue.add(new Arc(v, v2));
                globalQueue.add(new Arc(v2, v));
                }
              }
            neighbors[v].addAll(regionNeighbors);
    	}

    	//region 9
    	int[] r9 = {60, 61, 62, 69, 70, 71, 78, 79, 80};

    	for (int v : r9){
    		ArrayList<Integer> regionNeighbors = new ArrayList<Integer>();
            for (int v2 : r9){
            	if (v != v2){
            		regionNeighbors.add(v2);
                globalQueue.add(new Arc(v, v2));
                globalQueue.add(new Arc(v2, v));
                }
              }


            neighbors[v].addAll(regionNeighbors);
    	}
      }




  /*
     * This is the backtracking algorithm. If you change this method header, you will have
     * to update the calls to this method.
     */
    private final boolean backtrack(int cell, ArrayList<Integer>[] Domains) {

    	//Do NOT remove
    	recursions +=1;

        // YOUR CODE HERE

        // 1. If (cellnum > 80), return true
        if (cell > 80){
          return true;
        }


        // Find cell in val[][] array
        int row = cell / 9;
        int col = cell % 9;


        //check if cellnum assigned initial value
        // 2. If(vals[][] for cellnum != 0)
        if(vals[row][col] != 0){
          // call backtrack on next cell
          // 3. backtrack(cellnum+1, gD)
          backtrack(cell + 1, globalDomains);
        }





        //check if previous cellnum assignment is inconsistent
        // 4.  If (AC3() finds empty domain), return false
        //if(AC3(globalDomains) == false){
        if(AC3(globalDomains) == false){
          return false;
        }

        else{
          // 5. Else //find a value for this cellnum

          ArrayList<Integer> cell_domain = new ArrayList<Integer>();

          cell_domain = globalDomains[cell];


          // for the next value v in cellnum's domain
          for(int i=0; i < cell_domain.size(); i++) {



            int v = cell_domain.get(i);

            // Set gD[cellnum] = v -   Try assigning cellnum to v
            //globalDomains[cell] = v;
            ArrayList<Integer> v_arrayList = new ArrayList<Integer>();
            v_arrayList.add(v);
            globalDomains[cell] = v_arrayList;
            //globalDomains.set(cell, v);

            // Call backtrack(cellnum + 1, gD)
            // If backtrack() returns true
            if(backtrack(cell + 1, globalDomains) == true){
              // //assigning cellnum to v was consistent (AC3() returned true)

              // Return true
              return true;
            }

            else{

              // Else - assigning cellnum to v was inconsistent (AC3() returned
              // Try another V - do nothing in this loop

            }
          }
      }

      return false;

    }





    /*
     * This is the actual AC3 Algorithm. You may change this method header.
     */
    private final boolean AC3(ArrayList<Integer>[] Domains) {

		// YOUR CODE HERE

    // 1. Enqueue all arcs (U,V) and (V,U) to queue Q.

    // Q = globalQueue;


    //System.out.println("In AC3");

    // Set up Q <-- initially all values in global queue
    Queue<Arc> Q = new LinkedList<Arc>();

    //Queue<Arc> Q = globalQueue.getClass().newInstance();

    //**************************
    for(Arc e : globalQueue) {
        Q.add(e);
    }

    //Q = globalQueue.clone();



    // 2. while(true):
    while(true){

        // 3. if Q is empty, return success
        if(Q.isEmpty()){
          //System.out.println("Q is empty");
          return true;

        }

        // 4. (X, Y) = Q.dequeue()
        Arc current = Q.remove();
        int X = current.Xi;
        int Y = current.Xj;

        // 5. Revise(X, Y): Update X’s domain to make consistent with Y’s domain
        Boolean revised = Revise(current, globalDomains);

        // 6. If X’s domain becomes empty, return failure
        if(globalDomains[X].isEmpty()){
          return false;
        }

        // 7. If X’s domain shrinks: //can affect other neighbors of X
        if(revised == true){

          // 8. For all other neighbors Z of X:
         ArrayList<Integer> X_neighbors = new ArrayList<Integer>();
         X_neighbors = neighbors[X];

         for(int i = 0; i < X_neighbors.size(); i++) {
           int Z = X_neighbors.get(i);

           // add to queue if not already in queue
             Q.add(new Arc(Z, X));
         }
        }
      }

    }





    // Function to check if item is already in Queue
    private boolean in_Q(int Xi, int Zi, Queue<Arc> queue) {  // check if the arc already exists in Q
        // iterate through Queue


        for(int i = 0; i < queue.size(); i++){
          Arc current = queue.remove();
          int Xj = current.Xi;
          int Zj = current.Xj;

          if((Xi == Xj && Zi == Zj) || (Xi == Zj && Zi == Xj)){
            return true;
          }
        }
        return false;
      }





    /*
     * This is the Revise() procedure. You may change this method header.
     */
    private final boolean Revise(Arc t, ArrayList<Integer>[] Domains){


 		// YOUR CODE HERE
    int x = t.Xi;
    int y = t.Xj;

    boolean revised = false;

    ArrayList<Integer> X_domain = new ArrayList<Integer>();
    ArrayList<Integer> Y_domain = new ArrayList<Integer>();

    ArrayList<Integer> revised_X_domain = new ArrayList<Integer>();


    for(int e : globalDomains[x]) {
        X_domain.add(e);
        revised_X_domain.add(e);
    }

    for(int e : globalDomains[y]) {
        Y_domain.add(e);
    }



    // 1. For each value v in Dx:
    for(int i = 0; i < X_domain.size(); i++) {

      int v = X_domain.get(i);

      boolean delete = true;


      // 2. If no value in Dy satisfies the constraints if x
      //    was assigned to v, delete v from Dx
      // v, w can satisfy the constraint if they are not equal
      for(int j = 0; j < Y_domain.size(); j++) {

        int w = Y_domain.get(j);

        if(v != w){
          delete = false;

        }
      }

      if(delete == true){

        revised_X_domain.remove(new Integer(v));   // removes V from domain

        revised = true;

      }
    }
        // reset the global domains


        globalDomains[x] = revised_X_domain;

        return revised;
 	}














private final boolean forward_check(int X, int V) {

  // X is the cell number, V is the value we are going to set it at

  // Check if seeting x <- v creates an empty domain


  ArrayList<Integer> X_neighbors = new ArrayList<Integer>();

  for(int n : neighbors[X]) {
          X_neighbors.add(n);
  }



    // 1. For each unassigned neighbor y of x,

  for(int j = 0; j < X_neighbors.size(); j++){

    int neighbor = X_neighbors.get(j);
    ArrayList<Integer> neighbor_domain = new ArrayList<Integer>();

    neighbor_domain = globalDomains[neighbor];

      // If domian of y becomes empty because of seeting v to x, then return false

    ArrayList<Integer> not_domain = new ArrayList<Integer>();
    not_domain.add(V);



    if(neighbor_domain == not_domain){
      return false;
    }

  }


  // 2. Return true


  return true;


}






    private final boolean backtrack_w_forward(int cell, ArrayList<Integer>[] Domains) {
      //Do NOT remove
    	recursions +=1;


        // YOUR CODE HERE

        // 1. If (cellnum > 80), return true
        if (cell > 80){
          return true;
        }


        // Find cell in val[][] array
        int row = cell / 9;
        int col = cell % 9;


        //check if cellnum assigned initial value
        // 2. If(vals[][] for cellnum != 0)
        if(vals[row][col] != 0){
          // call backtrack on next cell
          // 3. backtrack(cellnum+1, gD)
          backtrack(cell + 1, globalDomains);
        }





        //check if previous cellnum assignment is inconsistent
        // 4.  If (AC3() finds empty domain), return false
        //if(AC3(globalDomains) == false){
        if(AC3(globalDomains) == false){
          return false;
        }

        else{
          // 5. Else //find a value for this cellnum


          ArrayList<Integer> cell_domain = new ArrayList<Integer>();

          cell_domain = globalDomains[cell];


          // for the next value v in cellnum's domain
          for(int i=0; i < cell_domain.size(); i++) {


          //  ArrayList<Integer>[] current_domains = new ArrayList[81];

            // for(int k = 0; k < 81; k++) {
            //     current_domains[k] = globalDomains[k];
            // }


            int v = cell_domain.get(i);

            if(forward_check(cell, v)){

            // Set gD[cellnum] = v -   Try assigning cellnum to v
            //globalDomains[cell] = v;
            ArrayList<Integer> v_arrayList = new ArrayList<Integer>();
            v_arrayList.add(v);
            globalDomains[cell] = v_arrayList;
            //globalDomains.set(cell, v);

            // Call backtrack(cellnum + 1, gD)
            // If backtrack() returns true
            if(backtrack(cell + 1, globalDomains) == true){
              // //assigning cellnum to v was consistent (AC3() returned true)

              // Return true
              return true;
            }

            // If forward check returns false, do nothing and go to the next value
            else{

            }
          }
          // If forward check returns false, do nothing and go to next value
          else{}



          }
      }

      return false;

    }











     /*
      * This is where you will write your custom solver.
      * You should not change this method header.
      */
    private final void customSolver(){

    	   //set 'success' to true if a successful board
    	   //is found and false otherwise.
    	   boolean success = true;
		   board.Clear();

	        System.out.println("Running custom algorithm");

	        //-- Your Code Here --

          // Initialize setup

          AC3Init();


          // Get domains of possible values




          boolean changed = true;

          while(changed = true){

            for(int i = 0; i < 81; i++){

              // if solution not already there

              changed = false;

              ArrayList<Integer> domain = new ArrayList<Integer>();
              ArrayList<Integer> i_neighbors = new ArrayList<Integer>();
              ArrayList<Integer> revised_domain = new ArrayList<Integer>();


              for(int e : globalDomains[i]) {
                      domain.add(e);
                      revised_domain.add(e);
              }
              for(int n : neighbors[i]) {
                      i_neighbors.add(n);
              }



              // check all neighbors, decrease domain of i
              for(int j = 0; j < i_neighbors.size(); j++){

                int neighbor = i_neighbors.get(j);

                  if(revised_domain.size() != 1){


                    if(globalDomains[neighbor].size() == 1){
                      int v = globalDomains[neighbor].get(0);

                      revised_domain.remove(new Integer(v));
                      changed = true;

                  }
                }
              }
              globalDomains[i] = revised_domain;

            }

          }




          // Now use forward checking
          backtrack_w_forward(0, globalDomains);





		   Finished(success);

    	}
































































    /// ---------- HELPER FUNCTIONS --------- ///
    /// ----   DO NOT EDIT REST OF FILE   --- ///
    /// ---------- HELPER FUNCTIONS --------- ///
    /// ----   DO NOT EDIT REST OF FILE   --- ///
    public final boolean valid(int x, int y, int val){

        if (vals[x][y] == val)
            return true;
        if (rowContains(x,val))
            return false;
        if (colContains(y,val))
            return false;
        if (blockContains(x,y,val))
            return false;
        return true;
    }

    public final boolean blockContains(int x, int y, int val){
        int block_x = x / 3;
        int block_y = y / 3;
        for(int r = (block_x)*3; r < (block_x+1)*3; r++){
            for(int c = (block_y)*3; c < (block_y+1)*3; c++){
                if (vals[r][c] == val)
                    return true;
            }
        }
        return false;
    }

    public final boolean colContains(int c, int val){
        for (int r = 0; r < 9; r++){
            if (vals[r][c] == val)
                return true;
        }
        return false;
    }

    public final boolean rowContains(int r, int val) {
        for (int c = 0; c < 9; c++)
        {
            if(vals[r][c] == val)
                return true;
        }
        return false;
    }

    private void CheckSolution() {
        // If played by hand, need to grab vals
        board.updateVals(vals);

        /*for(int i=0; i<9; i++){
	        for(int j=0; j<9; j++)
	        	System.out.print(vals[i][j]+" ");
	        System.out.println();
        }*/

        for (int v = 1; v <= 9; v++){
            // Every row is valid
            for (int r = 0; r < 9; r++)
            {
                if (!rowContains(r,v))
                {
                    board.showMessage("Value "+v+" missing from row: " + (r+1));// + " val: " + v);
                    return;
                }
            }
            // Every column is valid
            for (int c = 0; c < 9; c++)
            {
                if (!colContains(c,v))
                {
                    board.showMessage("Value "+v+" missing from column: " + (c+1));// + " val: " + v);
                    return;
                }
            }
            // Every block is valid
            for (int r = 0; r < 3; r++){
                for (int c = 0; c < 3; c++){
                    if(!blockContains(r, c, v))
                    {
                        return;
                    }
                }
            }
        }
        board.showMessage("Success!");
    }



    /// ---- GUI + APP Code --- ////
    /// ----   DO NOT EDIT  --- ////
    enum algorithm {
        AC3, Custom
    }
    class Arc implements Comparable<Object>{
        int Xi, Xj;
        public Arc(int cell_i, int cell_j){
            if (cell_i == cell_j){
                try {
                    throw new Exception(cell_i+ "=" + cell_j);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
            Xi = cell_i;      Xj = cell_j;
        }

        public int compareTo(Object o){
            return this.toString().compareTo(o.toString());
        }

        public String toString(){
            return "(" + Xi + "," + Xj + ")";
        }
    }

    enum difficulty {
        easy, medium, hard, random
    }

    public void actionPerformed(ActionEvent e){
        String label = ((JButton)e.getSource()).getText();
        if (label.equals("AC-3"))
        	AC3Init();
        else if (label.equals("Clear"))
            board.Clear();
        else if (label.equals("Check"))
            CheckSolution();
            //added
        else if(label.equals("Custom"))
            customSolver();
    }

    public void run() {
        board = new Board(gui,this);

        long start=0, end=0;

        while(!initialize());
        if (gui)
            board.initVals(vals);
        else {
            board.writeVals();
            System.out.println("Algorithm: " + alg);
            switch(alg) {
                default:
                case AC3:
                	start = System.currentTimeMillis();
                	AC3Init();
                    end = System.currentTimeMillis();
                    break;
                case Custom: //added
                	start = System.currentTimeMillis();
                	customSolver();
                	end = System.currentTimeMillis();
                    break;
            }

            CheckSolution();

            if(!gui)
            	System.out.println("time to run: "+(end-start));
        }
    }

    public final boolean initialize(){
        switch(level) {
            case easy:
                vals[0] = new int[] {0,0,0,1,3,0,0,0,0};
                vals[1] = new int[] {7,0,0,0,4,2,0,8,3};
                vals[2] = new int[] {8,0,0,0,0,0,0,4,0};
                vals[3] = new int[] {0,6,0,0,8,4,0,3,9};
                vals[4] = new int[] {0,0,0,0,0,0,0,0,0};
                vals[5] = new int[] {9,8,0,3,6,0,0,5,0};
                vals[6] = new int[] {0,1,0,0,0,0,0,0,4};
                vals[7] = new int[] {3,4,0,5,2,0,0,0,8};
                vals[8] = new int[] {0,0,0,0,7,3,0,0,0};
                break;
            case medium:
                vals[0] = new int[] {0,4,0,0,9,8,0,0,5};
                vals[1] = new int[] {0,0,0,4,0,0,6,0,8};
                vals[2] = new int[] {0,5,0,0,0,0,0,0,0};
                vals[3] = new int[] {7,0,1,0,0,9,0,2,0};
                vals[4] = new int[] {0,0,0,0,8,0,0,0,0};
                vals[5] = new int[] {0,9,0,6,0,0,3,0,1};
                vals[6] = new int[] {0,0,0,0,0,0,0,7,0};
                vals[7] = new int[] {6,0,2,0,0,7,0,0,0};
                vals[8] = new int[] {3,0,0,8,4,0,0,6,0};
                break;
            case hard:
            	vals[0] = new int[] {1,2,0,4,0,0,3,0,0};
            	vals[1] = new int[] {3,0,0,0,1,0,0,5,0};
            	vals[2] = new int[] {0,0,6,0,0,0,1,0,0};
            	vals[3] = new int[] {7,0,0,0,9,0,0,0,0};
            	vals[4] = new int[] {0,4,0,6,0,3,0,0,0};
            	vals[5] = new int[] {0,0,3,0,0,2,0,0,0};
            	vals[6] = new int[] {5,0,0,0,8,0,7,0,0};
            	vals[7] = new int[] {0,0,7,0,0,0,0,0,5};
            	vals[8] = new int[] {0,0,0,0,0,0,0,9,8};
                break;
            case random:
            default:
                ArrayList<Integer> preset = new ArrayList<Integer>();
                while (preset.size() < numCells)
                {
                    int r = rand.nextInt(81);
                    if (!preset.contains(r))
                    {
                        preset.add(r);
                        int x = r / 9;
                        int y = r % 9;
                        if (!assignRandomValue(x, y))
                            return false;
                    }
                }
                break;
        }
        return true;
    }

    public final boolean assignRandomValue(int x, int y){
        ArrayList<Integer> pval = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9));

        while(!pval.isEmpty()){
            int ind = rand.nextInt(pval.size());
            int i = pval.get(ind);
            if (valid(x,y,i)) {
                vals[x][y] = i;
                return true;
            } else
                pval.remove(ind);
        }
        System.err.println("No valid moves exist.  Recreating board.");
        for (int r = 0; r < 9; r++){
            for(int c=0;c<9;c++){
                vals[r][c] = 0;
            }    }
        return false;
    }

    private void Finished(boolean success){

    	if(success) {
            board.writeVals();
            //board.showMessage("Solved in " + myformat.format(ops) + " ops \t(" + myformat.format(recursions) + " recursive ops)");
            board.showMessage("Solved in " + myformat.format(recursions) + " recursive ops");

    	} else {
            //board.showMessage("No valid configuration found in " + myformat.format(ops) + " ops \t(" + myformat.format(recursions) + " recursive ops)");
        	board.showMessage("No valid configuration found");
        }
         recursions = 0;

    }

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);

        System.out.println("Gui? y or n ");
        char g=scan.nextLine().charAt(0);

        if (g=='n')
            gui = false;
        else
            gui = true;

        if(gui) {
        	System.out.println("difficulty? \teasy (e), medium (m), hard (h), random (r)");

	        char c = '*';

	        while (c != 'e' && c != 'm' && c != 'n' && c != 'h' && c != 'r') {
	        	c = scan.nextLine().charAt(0);
	            if(c=='e')
	                level = difficulty.valueOf("easy");
	            else if(c=='m')
	                level = difficulty.valueOf("medium");
	            else if(c=='h')
	                level = difficulty.valueOf("hard");
	            else if(c=='r')
	                level = difficulty.valueOf("random");
	            else{
	                System.out.println("difficulty? \teasy (e), medium (m), hard (h), random(r)");
	            }
	        }

	        SudokuPlayer app = new SudokuPlayer();
	        app.run();

        }
        else { //no gui

        	boolean again = true;

        	int numiters = 0;
        	long starttime, endtime, totaltime=0;

        	while(again) {

        		numiters++;
        		System.out.println("difficulty? \teasy (e), medium (m), hard (h), random (r)");

        		char c = '*';

		        while (c != 'e' && c != 'm' && c != 'n' && c != 'h' && c != 'r') {
		        	c = scan.nextLine().charAt(0);
		            if(c=='e')
		                level = difficulty.valueOf("easy");
		            else if(c=='m')
		                level = difficulty.valueOf("medium");
		            else if(c=='h')
		                level = difficulty.valueOf("hard");
		            else if(c=='r')
		                level = difficulty.valueOf("random");
		            else{
		                System.out.println("difficulty? \teasy (e), medium (m), hard (h), random(r)");
		            }

		        }

	            System.out.println("Algorithm? AC3 (1) or Custom (2)");
	            if(scan.nextInt()==1)
	                alg = algorithm.valueOf("AC3");
	            else
	                alg = algorithm.valueOf("Custom");


		        SudokuPlayer app = new SudokuPlayer();

		        starttime = System.currentTimeMillis();

		        app.run();

		        endtime = System.currentTimeMillis();

		        totaltime += (endtime-starttime);


	        	System.out.println("quit(0), run again(1)");
	        	if (scan.nextInt()==1)
	        		again=true;
	        	else
	        		again=false;

	        	scan.nextLine();

        	}

        	System.out.println("average time over "+numiters+" iterations: "+(totaltime/numiters));
        }



        scan.close();
    }



    class Board {
        GUI G = null;
        boolean gui = true;

        public Board(boolean X, SudokuPlayer s) {
            gui = X;
            if (gui)
                G = new GUI(s);
        }

        public void initVals(int[][] vals){
            G.initVals(vals);
        }

        public void writeVals(){
            if (gui)
                G.writeVals();
            else {
                for (int r = 0; r < 9; r++) {
                    if (r % 3 == 0)
                        System.out.println(" ----------------------------");
                    for (int c = 0; c < 9; c++) {
                        if (c % 3 == 0)
                            System.out.print (" | ");
                        if (vals[r][c] != 0) {
                            System.out.print(vals[r][c] + " ");
                        } else {
                            System.out.print("_ ");
                        }
                    }
                    System.out.println(" | ");
                }
                System.out.println(" ----------------------------");
            }
        }

        public void Clear(){
            if(gui)
                G.clear();
        }

        public void showMessage(String msg) {
            if (gui)
                G.showMessage(msg);
            System.out.println(msg);
        }

        public void updateVals(int[][] vals){
            if (gui)
                G.updateVals(vals);
        }

    }

    class GUI {
        // ---- Graphics ---- //
        int size = 40;
        JFrame mainFrame = null;
        JTextField[][] cells;
        JPanel[][] blocks;

        public void initVals(int[][] vals){
            // Mark in gray as fixed
            for (int r = 0; r < 9; r++) {
                for (int c = 0; c < 9; c++) {
                    if (vals[r][c] != 0) {
                        cells[r][c].setText(vals[r][c] + "");
                        cells[r][c].setEditable(false);
                        cells[r][c].setBackground(Color.lightGray);
                    }
                }
            }
        }

        public void showMessage(String msg){
            JOptionPane.showMessageDialog(null,
                    msg,"Message",JOptionPane.INFORMATION_MESSAGE);
        }

        public void updateVals(int[][] vals) {

           // System.out.println("calling update");
            for (int r = 0; r < 9; r++) {
                for (int c=0; c < 9; c++) {
                    try {
                        vals[r][c] = Integer.parseInt(cells[r][c].getText());
                    } catch (java.lang.NumberFormatException e) {
                        System.out.println("Invalid Board: row col: "+(r+1)+" "+(c+1));
                        showMessage("Invalid Board: row col: "+(r+1)+" "+(c+1));
                        return;
                    }
                }
            }
        }

        public void clear() {
            for (int r = 0; r < 9; r++){
                for (int c = 0; c < 9; c++){
                    if (cells[r][c].isEditable())
                    {
                        cells[r][c].setText("");
                        vals[r][c] = 0;
                    } else {
                        cells[r][c].setText("" + vals[r][c]);
                    }
                }
            }
        }

        public void writeVals(){
            for (int r=0;r<9;r++){
                for(int c=0; c<9; c++){
                    cells[r][c].setText(vals[r][c] + "");
                }   }
        }

        public GUI(SudokuPlayer s){

            mainFrame = new javax.swing.JFrame();
            mainFrame.setLayout(new BorderLayout());
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JPanel gamePanel = new javax.swing.JPanel();
            gamePanel.setBackground(Color.black);
            mainFrame.add(gamePanel, BorderLayout.NORTH);
            gamePanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            gamePanel.setLayout(new GridLayout(3,3,3,3));

            blocks = new JPanel[3][3];
            for (int i = 0; i < 3; i++){
                for(int j =2 ;j>=0 ;j--){
                    blocks[i][j] = new JPanel();
                    blocks[i][j].setLayout(new GridLayout(3,3));
                    gamePanel.add(blocks[i][j]);
                }
            }

            cells = new JTextField[9][9];
            for (int cell = 0; cell < 81; cell++){
                int i = cell / 9;
                int j = cell % 9;
                cells[i][j] = new JTextField();
                cells[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                cells[i][j].setHorizontalAlignment(JTextField.CENTER);
                cells[i][j].setSize(new java.awt.Dimension(size, size));
                cells[i][j].setPreferredSize(new java.awt.Dimension(size, size));
                cells[i][j].setMinimumSize(new java.awt.Dimension(size, size));
                blocks[i/3][j/3].add(cells[i][j]);
            }

            JPanel buttonPanel = new JPanel(new FlowLayout());
            mainFrame.add(buttonPanel, BorderLayout.SOUTH);
            //JButton DFS_Button = new JButton("DFS");
            //DFS_Button.addActionListener(s);
            JButton AC3_Button = new JButton("AC-3");
            AC3_Button.addActionListener(s);
            JButton Clear_Button = new JButton("Clear");
            Clear_Button.addActionListener(s);
            JButton Check_Button = new JButton("Check");
            Check_Button.addActionListener(s);
            //buttonPanel.add(DFS_Button);
            JButton Custom_Button = new JButton("Custom");
            Custom_Button.addActionListener(s);
            //added
            buttonPanel.add(AC3_Button);
            buttonPanel.add(Custom_Button);
            buttonPanel.add(Clear_Button);
            buttonPanel.add(Check_Button);






            mainFrame.pack();
            mainFrame.setVisible(true);

        }
    }

    Random rand = new Random();

    // ----- Helper ---- //
    static algorithm alg = algorithm.AC3;
    static difficulty level = difficulty.easy;
    static boolean gui = true;
    static int numCells = 15;
    static DecimalFormat myformat = new DecimalFormat("###,###");

    //For printing
	static int recursions;
}
