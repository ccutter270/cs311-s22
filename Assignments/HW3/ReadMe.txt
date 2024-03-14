CS311: Programming Assignment #3
Caroline Cutter + Julia Fairbank

HONOR CODE:
We have neither given nor received unauthorized aid on this assignment.
All group members were present and contributing during all work on this project.


Files Required: TreeNode.java, DecisionTree.java, Example.java, TestClassifier.java



Known Bugs: 
 - This isn't a bug but our output in set2 big is different then the sample output slightly. The tree is the same besides two leaf nodes are different labels and it produces a higher accuracy than the example which Professor Christman said was completely fine. 




Discussion for Part 2: 

1) Describe how your decision tree classifies patients.

2) Use Wikipedia or an on-line search engine to find out the meaning of the features whose names are unfamiliar to you



3) Report the percentage of correctly classified examples, the number of false positives and the number of false negatives.

	Our tree got these results: 
		Positive examples correct: 38 out of 42
		Negative examples correct: 7 out of 10

	Which means there were 4 false negatives (because positive 4 positive examples were missed) 
	and 3 false positives (because 3 negative examples were missed). This has an overall accuracy 
	of 86.5 % (because 45 correct / 52 total) 

	Here was our tree:
	
Feature 14 = True:
	Feature 19 = True:
		Feature 9 = True:
			Feature 7 = True:Positive
			Feature 7 = False:
				Feature 3 = True:
					Feature 0 = True:Negative
					Feature 0 = False:
						Feature 4 = True:Positive
						Feature 4 = False:Negative
				Feature 3 = False:Positive
		Feature 9 = False:Negative
	Feature 19 = False:
		Feature 15 = True:
			Feature 1 = True:
				Feature 6 = True:Positive
				Feature 6 = False:
					Feature 8 = True:
						Feature 9 = True:
							Feature 12 = True:
								Feature 16 = True:Positive
								Feature 16 = False:
									Feature 4 = True:Positive
									Feature 4 = False:Negative
							Feature 12 = False:Negative
						Feature 9 = False:Positive
					Feature 8 = False:Positive
			Feature 1 = False:Positive
		Feature 15 = False:
			Feature 8 = True:Positive
			Feature 8 = False:Negative
Feature 14 = False:
	Feature 21 = True:Negative
	Feature 21 = False:Positive






Discussion for Part 3: 

(a) The name of the dataset.
	
	Symptoms and COVID Presence (May 2020 data)

(b) From where you obtained the data.
	
	https://www.kaggle.com/datasets/hemanthhari/symptoms-and-covid-presence

(c) A brief (i.e. 1-2 sentences) description of the dataset including what the features are and what is being predicted.

	This data consists of boolean values of different symptoms of COVID, pre-existing conditions 	in a patient, and exposure places to COVID and says whether the patient tested positive for COVID-19 or negative. 



(d) The number of examples in the dataset.
	
	Training File = 200 total -->  100 positive COVID & 100 negative COVID

(e) The number of features for each example.
	
	20 Features for each example: 
		0) Contact with COVID Patient
		1) Visited Public Exposed Places
		2) Sore throat
		3) Chronic Lung Disease 
		4) Diabetes
		5) Heart Disease
		6) Breathing Problem
		7) Fever
		8) Dry Cough
		9) Running Nose
		10) Asthma
		11) Headache
		12) Hyper Tension
		13) Fatigue 
		14) Gastrointestinal 
		15) Abroad travel
		16) Attended Large Gathering
		17) Family working in Public Exposed Places
		18) Wearing Masks
		19) Sanitization from Market


Discuss how your decision tree performs on the data (as in Part 2(a), report the percentage of correctly classified examples, the number of false positives, and the number of false negatives).

	Our tree performed with 100% accuracy (Positives: 50/50, Negatives 50/50). We believe this is because for feature #6 (breathing problem), all the patients who tested positive said yes, and all the patients who tested negative said no, so the tree branched on #6 with all negative and positives. 

This is what our tree looked like: 

Feature 6 = True:Positive
Feature 6 = False:Negative
Positive examples correct: 50 out of 50
Negative examples correct: 50 out of 50




However, we were interested in exploring this data more so we took out all of these examples that would have all one boolean for the feature's negative and positive cases (we ended up removing the features breathing problem, fever, dry cough, wearing mask). We ran this again with the new data and got a much more interesting tree, but still with 100% accuracy (Positives: 50/50, Negatives 50/50)! Here was this tree: 


Feature 12 = True:Positive
Feature 12 = False:
	Feature 2 = True:Positive
	Feature 2 = False:
		Feature 11 = True:
			Feature 14 = True:
				Feature 6 = True:
					Feature 3 = True:Negative
					Feature 3 = False:
						Feature 0 = True:Positive
						Feature 0 = False:Negative
				Feature 6 = False:Positive
			Feature 14 = False:Negative
		Feature 11 = False:
			Feature 0 = True:
				Feature 6 = True:Negative
				Feature 6 = False:Positive
			Feature 0 = False:
				Feature 1 = True:
					Feature 7 = True:
						Feature 3 = True:Negative
						Feature 3 = False:Positive
					Feature 7 = False:
						Feature 3 = True:Positive
						Feature 3 = False:Negative
				Feature 1 = False:
					Feature 4 = True:
						Feature 3 = True:Positive
						Feature 3 = False:Negative
					Feature 4 = False:Positive
Positive examples correct: 50 out of 50
Negative examples correct: 50 out of 50




**** NOTE *****

We put the java and text files needed to run this in the "Extra" folder in our Zip file. To run the first tree (with all the data), compile and run "TestClassifierCovid.java". To run the data with the variables taken out, run "TestClassifierCovid2.java"






