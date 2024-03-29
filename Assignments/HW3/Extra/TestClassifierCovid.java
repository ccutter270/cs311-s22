/******************************

Julia Fairbank and Caroline Cutter
HW 3: Decision Tree
Monday, April 18, 2022

HONOR CODE:
 - We have neither given nor received unauthorized aid on this assignment.
 - All group members were present and contributing during all work on this project.



NOTE: This code was modified from Professor Christman's
      "TestClassifierHepatitis.java" File
********************************/


// TestClassifierCovid.java
// Trains and tests a decision tree classifier.

import java.util.*;
import java.io.*;

public class TestClassifierCovid {

    // Random number generator
    static Random rand = new Random();

    // Constants to use
    static int featureCount = 20;
    static int trainexampleCount = 200;
    static int testexampleCount = 100;
    static int train_poscount = 100;
    static int train_negcount = 100;
    static int test_poscount = 50;
    static int test_negcount = 50;
    static Example[] trainPos, trainNeg;
    static Example[] testPos, testNeg;


    //added
    static ArrayList<Example> trainExs; //training examples

    // Process arguments.
    public static void main(String[] args) throws FileNotFoundException {

    	trainPos = new Example[train_poscount];
    	trainNeg = new Example[train_negcount];

    	testPos = new Example[test_poscount];
    	testNeg = new Example[test_negcount];

    	testClassifier();

        }


    // Train and test.
    private static void testClassifier() throws FileNotFoundException {

    	//Load training examples from input file
    	loadTrainExamples("covid-data-train.txt");

		// Train the tree
		DecisionTree tree = new DecisionTree();


	    //added
	    trainExs = new ArrayList<Example>();

	    System.out.println(trainPos.length+" "+trainNeg.length);

	    //added
	    for(int i=0; i<trainPos.length; i++) {
	    	trainPos[i].setLabel(true);
	    	trainExs.add(trainPos[i]);
	    }



	    //added
	    for(int i=0; i<trainNeg.length; i++) {
	    	trainNeg[i].setLabel(false);
	    	trainExs.add(trainNeg[i]);
	    }




	  //added
	  	tree.train(trainExs);

		//tree.train(trainPos, trainNeg);
		tree.print();

		//Load testing examples from input file
		loadTestExamples("covid-data-test.txt");

		// Evaluate on positives
		int correct = 0;
		for (Example e : testPos)
		    if (tree.classify(e))
			correct++;
		System.out.println("Positive examples correct: "+correct+" out of "+testPos.length);

		// Evaluate on negatives
		correct = 0;
		for (Example e : testNeg)
		    if (!tree.classify(e))
			correct++;
		System.out.println("Negative examples correct: "+correct+" out of "+testNeg.length);
		System.out.println();



    }

    /*********************************************
     * Methods to load examples.
     * @throws FileNotFoundException
     */
    private static void loadTrainExamples(String file) throws FileNotFoundException
    {
    	Scanner scan = new Scanner(new File(file));

    	int numpos=0, numneg=0;

    	//read first line
    	scan.nextLine();

    	for(int i=0; i<trainexampleCount; i++){

    		//read patient number
    		//scan.next();

    		//read whether postive or negative                                     ***** Fix these - put the first as COVID or not
    		String n = scan.next();

    		if (n.equals("positive")){

    			trainPos[numpos] = new Example(featureCount);

	    		for(int j=0; j<featureCount; j++){
	    			if(scan.hasNextBoolean()){
	    				boolean b = scan.nextBoolean();
	    				//System.out.println("setting trainPos["+i+"]."+j+" to "+b);
	    				trainPos[numpos].setFeatureValue(j, b);
	    			}
	    		}
	    		numpos++;
    		}
    		else if (n.equals("negative")){

    			trainNeg[numneg] = new Example(featureCount);

	    		for(int j=0; j<featureCount; j++){
	    			if(scan.hasNextBoolean()){
	    				boolean b = scan.nextBoolean();
	    				//System.out.println("setting trainNeg["+i+"]."+j+" to "+b);
	    				trainNeg[numneg].setFeatureValue(j, b);
	    			}
	    		}
	    		numneg++;
    		}
    		else{
    			System.out.println("ERROR: "+n);
    		}
    	}
    	scan.close();

    }


    private static void loadTestExamples(String file) throws FileNotFoundException
    {
    	Scanner scan = new Scanner(new File(file));

    	int numpos=0, numneg=0;

    	//read first line
    	scan.nextLine();

    	for(int i=0; i<testexampleCount; i++){

    		//read patient number
    		//scan.next();

    		//read whether postive or negative
    		String n = scan.next();

    		if (n.equals("positive")){

    			testPos[numpos] = new Example(featureCount);

	    		for(int j=0; j<featureCount; j++){
	    			if(scan.hasNextBoolean()){
	    				boolean b = scan.nextBoolean();

	    				//System.out.println("setting testPos["+i+"]."+j+" to "+b);
	    				testPos[numpos].setFeatureValue(j, b);
	    			}
	    		}
	    		numpos++;
    		}
    		else if (n.equals("negative")){

    			testNeg[numneg] = new Example(featureCount);

	    		for(int j=0; j<featureCount; j++){
	    			if(scan.hasNextBoolean()){
	    				boolean b = scan.nextBoolean();

	    				//System.out.println("setting testNeg["+i+"]."+j+" to "+b);
	    				testNeg[numneg].setFeatureValue(j, b);
	    			}
	    		}
	    		numneg++;
    		}
    		else{
    			System.out.println("ERROR: "+n);
    		}
    	}
    	scan.close();
    }


}
