/******************************

Julia Fairbank and Caroline Cutter
HW 3: Decision Tree
Monday, April 18, 2022

HONOR CODE:
 - We have neither given nor received unauthorized aid on this assignment.
 - All group members were present and contributing during all work on this project.


NOTE: Our decision tree has a slightly different output on the set2 big dataset
			and found a more accurate tree - Professor Christman said this was okay!
********************************/




import java.util.ArrayList;


public class DecisionTree {

	private TreeNode root = null; //stores the root of the decision tree


	public void train(ArrayList<Example> examples){
		int numFeatures = 0;
		if(examples.size()>0) //get the number of featuers in these examples
			numFeatures = examples.get(0).getNumFeatures();

		//initialize empty positive and negative lists
		ArrayList<Example> pos = new ArrayList<Example>();
		ArrayList<Example> neg = new ArrayList<Example>();

		//paritition examples into positive and negative ones
		for(Example e: examples){
			if (e.getLabel())
				pos.add(e);
			else
				neg.add(e);
		}

		//create the root node of the tree
		root = new TreeNode(null, pos, neg, numFeatures);

		//call recursive train()  on the root node
		train(root, numFeatures);
	}






	/**
	 * TODO: Complete this method
	 * The recursive train method that builds a tree at TreeNode node
	 * @param node: current node to train
	 * @param numFeatures: total number of features
	 */
	private void train(TreeNode node, int numFeatures){

			// *** FOR BC 3*** See if there are any features left
			boolean features_left = false;

			for(int i = 0; i < numFeatures; i++){
				if(node.featureUsed(i) == false){
					features_left = true;
				}
			}





		// 4. If no more examples at this node (BC 2)
			if (node.pos.size() == 0 && node.neg.size() == 0){


				// 5. Set this node's label to the majority label of its parent's examples
				boolean majorityParent;
				TreeNode parent = node.parent;

 				if(parent.pos.size() > parent.neg.size()){
 					majorityParent = true;
 				}
 				else{
 					majorityParent = false;
 				}

				node.decision = majorityParent;

				// 6. set this node as a leaf
				node.isLeaf = true;
			}



			// 1. If all remaining examples at this node have the same label L (BC 1)
				else if(node.pos.size() == 0 || node.neg.size() == 0){

					// Find if all neg or all pos
					boolean label;
					if(node.pos.size() == 0){
						label = false;
					}
					else{
						label = true;
					}

						// 2. Set this node's label to L
						node.decision = label;

						// 3. set this node as a leaf
						node.isLeaf = true;

				}


		// 7. If no more features (BC 3)
			else if (features_left == false) {     // Note this is calculated in the top of the function

					// 8. Set this node's label to the majority label of this node's examples
					boolean majorityLabel;

					if(node.pos.size() > node.neg.size()){
						majorityLabel = true;
					}
					else{
						majorityLabel = false;
					}

					node.decision = majorityLabel;

					// 9. Set this node as a leaf
					node.isLeaf = true;
			}


	 // 10. Else:
			else {

				// // 11. pos = node.getPos(); neg = node.getNeg();

				// 12. Find the next feature to split on (best info gain)
				int bestFeature = 0;
				double bestInfoGain = 0;


				// Iterate through all the features
				for(int i = 0; i < numFeatures; i++ ){

					// If feature has not already been used
					if(node.featureUsed(i) == false){

						// information gain (f) = Entropy(n) – Remaining Entropy(f)

						// Calculate information gain
						double infoGain = getEntropy(node.pos.size(), node.neg.size()) - getRemainingEntropy(i, node);

						// If this features info gain is better than current best, swtich
						if(infoGain > bestInfoGain){

							bestFeature = i;
							bestInfoGain = infoGain;
						}
					}
					// If used, look at next feature
				}


				// If there is no info gain for any remaining features, set this as a leaf node and decision = majority
				if(bestInfoGain == 0){

						// Find if all neg or all pos
						boolean lab;
						if(node.pos.size() == 0){
							lab = false;
						}
						else{
							lab = true;
						}

							// Set this node's label to L
							node.decision = lab;

							// set this node as a leaf
							node.isLeaf = true;


				}

				//  If there is a feature with info gain, then train it
				else{

					// 13. Set this node’s feature as f
					node.setSplitFeature(bestFeature);

					// 14. createSubChildren(node) --> each node will have two subchildren: a true child and a false child
					createChildren(node, numFeatures);


					// 15. train(this node’s true child)
					train(node.trueChild, numFeatures);

					// 16. train(this node’s false child)
				 	train(node.falseChild, numFeatures);
				}
			}
		}














	/**
	 * TODO: Complete this method
	 * Creates the true and false children of TreeNode node
	 * @param node: node at which to create children
	 * @param numFeatures: total number of features
	 */
	private void createChildren(TreeNode node, int numFeatures){


		// Create a list of ALL the examples from the parent node
		ArrayList<Example> examples = new ArrayList<Example>();
		examples.addAll(node.pos);
		examples.addAll(node.neg);




		// Inititaliz arrays to store examples in the pos / neg of the subchildren
		ArrayList<Example> true_true = new ArrayList<Example>();                    // if pos of true child
		ArrayList<Example> true_false = new ArrayList<Example>();                   // if neg of true child
		ArrayList<Example> false_true = new ArrayList<Example>();                   // if pos of false child
		ArrayList<Example> false_false = new ArrayList<Example>();                  // if neg of false child

		int split_feature = node.getSplitFeature();

		//paritition examples into positive and negative ones
		for(Example e: examples){

			// If part of true child
			if(e.getFeatureValue(split_feature) == true){

				// if part of pos
				if(e.getLabel() == true){
					true_true.add(e);
				}
				// if part of neg
				else{
					true_false.add(e);
				}
			}

			// if part of false child
			else{

				// if part of pos
				if(e.getLabel() == true){
					false_true.add(e);
				}
				// if part of neg
				else{
					false_false.add(e);
				}
			}
		}

			// Create true child - contains examples that are true on the split feature
			node.trueChild = new TreeNode(node, true_true, true_false, numFeatures);

			// Create false child - contains examples that are false on the split feature
			node.falseChild = new TreeNode(node, false_true, false_false, numFeatures);
}
















	/**
	 * TODO: Complete this method
	 * Computes and returns the remaining entropy if feature is chosen
	 * at node.
	 * @param feature: the feature number
	 * @param node: node at which to find remaining entropy
	 * @return remaining entropy at node
	 */
	private double getRemainingEntropy(int feature, TreeNode node){


		// Remaining_Entropy(f): entropy remaining in the sub- children created if we split n with feature f

		// Find sizes of trueChild pos and neg and falseChild pos and neg
		// Create a list of all the examples from the parent node
		ArrayList<Example> examples = new ArrayList<Example>();
		examples.addAll(node.pos);
		examples.addAll(node.neg);


		int true_true = 0;             // if pos of true child
		int true_false = 0;            // if neg of true child
		int false_true = 0;            // if pos of false child
		int false_false = 0;           // if neg of false child


		//paritition examples into positive and negative ones
		for(Example e: examples){

			// If part of true child
			if(e.getFeatureValue(feature) == true){

				// if part of pos
				if(e.getLabel() == true){
					true_true ++;
				}
				// if part of neg
				else{
					true_false ++;
				}
			}

			// if part of false child
			else{

				// if part of pos
				if(e.getLabel() == true){
					false_true ++;
				}
				// if part of neg
				else{
					false_false ++;
				}
			}
		}


		// Get a fraction of how many in order to calculate WEIGHTED entropy
		int total_true = true_true + true_false;
		int total_false = false_true + false_false;
		int total = total_true + total_false;

		// Create fractions for weighted entropy
		double fraction1 = (double) total_true / total;      // for true child
		double fraction2 = (double) total_false / total;     // for false child



		// Calculate weighted entropy for each child
		double trueChild_entropy = fraction1 * getEntropy(true_true, true_false);
		double falseChild_entropy = fraction2 * getEntropy(false_true, false_false);

		// Remaining entropy is the sum of the two children's entropy
		return (trueChild_entropy + falseChild_entropy);

	}
















	/**
	 * TODO: complete this method
	 * Computes the entropy of a node given the number of positive and negative examples it has
	 * @param numPos: number of positive examples
	 * @param numNeg: number of negative examples
	 * @return - entropy
	 */
	private double getEntropy(int numPos, int numNeg){

		// If only pos or neg examples, entropy = 0
		if(numPos == 0 || numNeg == 0){
			return 0;
		}

		// If there are examples in both pos and neg, calcualte entropy using equation
				// –Pr(YES)log2(Pr(YES)) – Pr(NO)log2(Pr(NO))
		else{

			double total = numPos + numNeg;

			// Pr(YES) = yes / total
			double p_yes = (double) numPos / total;

			// log2(Pr(YES)) = log(p_yes)
			double log_yes = log2(p_yes);

			// Pr(NO) = NO / total
			double p_no = (double) numNeg / total;

			// log2(Pr(NO)) = log(p_no)
			double log_no = log2(p_no);

			return (-1 * p_yes * log_yes) - (p_no * log_no);
		}
	}












	/**
	 * Computes log_2(d) (To be used by the getEntropy() method)
	 * @param d - value
	 * @return log_2(d)
	 */
	private double log2(double d){
		return Math.log(d)/Math.log(2);
	}









	/**
	 * TODO: complete this method
	 * Classifies example e using the learned decision tree
	 * @param e: example
	 * @return true if e is predicted to be  positive,  false otherwise
	 */
	public boolean classify(Example e){

		// Start at root
		TreeNode node = root;

		// While not at child (no value returned yet)
		while(true){

			int feature = node.getSplitFeature();

			// If at a leaf node, return decision
			if (node.isLeaf == true) {
				return node.decision;
			}

			// If not at leaf, set node to either true or false child
			else{

				// truechild
				if(e.getFeatureValue(feature) == true){
					node = node.trueChild;
				}
				// falseChild
				else{
					node = node.falseChild;
				}
			}
		}
	}













	//----------DO NOT MODIFY CODE BELOW------------------
	public void print(){
		printTree(root, 0);
	}



	private void printTree(TreeNode node, int indent){
		if(node== null)
			return;
		if(node.isLeaf){
			if(node.decision)
				System.out.println("Positive");
			else
				System.out.println("Negative");
		}
		else{
			System.out.println();
			doIndents(indent);
			System.out.print("Feature "+node.getSplitFeature() + " = True:" );
			printTree(node.trueChild, indent+1);
			doIndents(indent);
			System.out.print("Feature "+node.getSplitFeature() + " = False:" );//+  "( " + node.falseChild.pos.size() + ", " + node.falseChild.neg.size() + ")");
			printTree(node.falseChild, indent+1);
		}
	}

	private void doIndents(int indent){
		for(int i=0; i<indent; i++)
			System.out.print("\t");
	}
}
