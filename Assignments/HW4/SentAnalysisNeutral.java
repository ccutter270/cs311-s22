/*
 * Caroline Cutter and Julia Fairbank
 * HW4
 * All group members were present and contributing during all work on this project.
 */

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class SentAnalysisNeutral {

	final static File TRAINFOLDER = new File("train-neutral");


	public static void main(String[] args) throws IOException
	{
		ArrayList<String> files = readFiles(TRAINFOLDER);

		train(files);

		//if command line argument is "evaluate", runs evaluation mode
		if (args.length==1 && args[0].equals("evaluate")){
			evaluate();
		}
		else{//otherwise, runs interactive mode
			@SuppressWarnings("resource")
			Scanner scan = new Scanner(System.in);
			System.out.print("Text to classify>> ");
			String textToClassify = scan.nextLine();
			System.out.println("Result: "+classify(textToClassify));
		}
	}





	/*
	 * Takes as parameter the name of a folder and returns a list of filenames (Strings)
	 * in the folder.
	 */
	public static ArrayList<String> readFiles(File folder){

		System.out.println("Populating list of files");

		//List to store filenames in folder
		ArrayList<String> filelist = new ArrayList<String>();


		for (File fileEntry : folder.listFiles()) {
	        String filename = fileEntry.getName();
	        filelist.add(filename);
		}

		/*
		for (String fileEntry : filelist) {
	        System.out.println(fileEntry);
		}

		System.out.println(filelist.size());
		*/

		return filelist;
	}












	/*
	 * TO DO
	 * Trainer: Reads text from data files in folder datafolder and stores counts
	 * to be used to compute probabilities for the Bayesian formula.
	 * You may modify the method header (return type, parameters) as you see fit.
	 */


	// GLOBALS - hold information needed for the classify function
	public static HashMap<String, Double> positive = new HashMap<String, Double>();
	public static HashMap<String, Double> negative = new HashMap<String, Double>();
  public static HashMap<String, Double> neutral = new HashMap<String, Double>();

	public static double unique_neg_words = 0;
	public static double unique_pos_words = 0;
  public static double unique_neu_words = 0;

	public static double pos_count = 0;
	public static double neg_count = 0;
  public static double neu_count = 0;



	public static void train(ArrayList<String> files) throws FileNotFoundException
	{

		// For each file in given folder
		for (String file: files) {

			Scanner content = new Scanner(new File("train-neutral/"+file));

			// Get context of folder
			String content_list = "";
			while(content.hasNextLine()){
				content_list += content.nextLine();
			}

			content.close();

			// Split file contents into string array of words
			String contentArray[] = content_list.split(" ");


			// Check the file to see if 1 or 5 star review
			char check = file.charAt(file.indexOf("-") + 1);

			// Negative review
			if (check == '1') {

				// Add to negative file count
				neg_count += 1;


				// For each word in file
				for (String word: contentArray) {

					double frequency = 1;

					// if word is already in in dictionary, update frequency count
					if (negative.containsKey(word)) {

						frequency = negative.get(word) + 1;
						negative.put(word, frequency);
					}

					// if word is not in dictionary, put it in
					else {
						negative.put(word, frequency);
						unique_neg_words += 1;
					}
				}
			}

			// Positive Review
			else if (check == '5') {

				// Add to positive file count
				pos_count += 1;


				//for each word in file, check if word is in dictionary or not
				for (String word: contentArray) {

					double frequency = 1;

					// if word is already in dictionary, update frequency count
					if (positive.containsKey(word)) {
						frequency = positive.get(word) + 1;
						positive.put(word, frequency);
					}

					// if word is not in dictionary, put it in
					else {
						positive.put(word, frequency);
						unique_pos_words += 1;
					}
				}
			}
      // Neutral Review
      else{
        // Add to neutral file count
				neu_count += 1;


				//for each word in file, check if word is in dictionary or not
				for (String word: contentArray) {

					double frequency = 1;

					// if word is already in dictionary, update frequency count
					if (neutral.containsKey(word)) {
						frequency = neutral.get(word) + 1;
						neutral.put(word, frequency);
					}

					// if word is not in dictionary, put it in
					else {
						neutral.put(word, frequency);
						unique_neu_words += 1;
					}
				}
      }
		}
	}







	public static double lambda = 0.0001;

	/*
	 * Classifier: Classifies the input text (type: String) as positive or negative
	 */
	public static String classify(String text)
	{

		// Split the string into array of words
		String[] text_arr = text.split(" ");

		// EQUATION: log (𝑃𝑟(𝑝𝑜𝑠𝑖𝑡𝑖𝑣𝑒)) + log (𝑃𝑟(𝑓 |𝑝𝑜𝑠𝑖𝑡𝑖𝑣𝑒)) + log4𝑃𝑟(𝑓 |𝑝𝑜𝑠𝑖𝑡𝑖𝑣𝑒)5 + ⋯ !"+ log (𝑃𝑟(𝑓 |𝑝𝑜𝑠𝑖𝑡𝑖𝑣𝑒))
			// NOTE: Pr(wordi | positive) = (# wordi in positive docs) / (# unique words in all positive docs)

		double pos_prob = pos_count/(pos_count + neg_count + neu_count);      // Pr(positive)
		double neg_prob = neg_count/(pos_count + neg_count + neu_count);      // Pr(negative)
    double neu_prob = neu_count/(pos_count + neg_count + neu_count);      // Pr(neutral)


		double text_pos_probability = 0;
	  double text_neg_probability = 0;
    double text_neu_probability = 0;


		// For each word in input text, calculate Pr(f | pos) & Pr(f | neg)
		for (String word: text_arr) {

			// Change positive probability
			if(positive.containsKey(word)) {
				text_pos_probability += log2((positive.get(word)) / (unique_pos_words ));
			}
			else {
				text_pos_probability += log2((lambda) / (unique_pos_words + ((double)text_arr.length * lambda)));
			}

			// Change negative probability
			if(negative.containsKey(word)) {
				text_neg_probability += log2((negative.get(word)) / (unique_neg_words));
			}
			else {
				text_neg_probability += log2((lambda) / (unique_neg_words + ((double)text_arr.length * lambda)));
			}

      // Change neutral probability
      if(neutral.containsKey(word)) {
        text_neu_probability += log2((neutral.get(word)) / (unique_neu_words));
      }
      else {
        text_neu_probability += log2((lambda) / (unique_neu_words + ((double)text_arr.length * lambda)));
      }

		}


		// Compute final Max Likelihood
		text_pos_probability += log2(pos_prob);
		text_neg_probability += log2(neg_prob);
    text_neu_probability += log2(neu_prob);

		// Check if positive or negative has a higher probability

		// if positive has a higher probability, return positive
		if (text_pos_probability > text_neg_probability && text_pos_probability > text_neu_probability ) {
			return "Positive";
		}

		// if negative has a higher probability, return negative
		else if (text_neg_probability > text_pos_probability && text_neg_probability > text_neu_probability ){
			return "Negative";
		}

    else{
      return "Neutral";
    }
	}




	/*
	 * TO DO
	 * Classifier: Classifies all of the files in the input folder (type: File) as positive or negative
	 * You may modify the method header (return type, parameters) as you like.
	 */
	public static void evaluate() throws FileNotFoundException
	{

		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);

		System.out.print("Enter folder name of files to classify: ");
		String foldername = scan.nextLine();
		File folder = new File(foldername);


		// Total Classified
		double total_pos_classified = 0;
		double total_neg_classified = 0;
    double total_neu_classified = 0;

		// Total Correct Classified
		double num_pos = 0;
		double num_neg = 0;
    double num_neu=  0;




		// For each file in given folder name

		for(File file : folder.listFiles()) {

      // Get string to classify as either neg, pos or neu
			String fileName = file.getName();
			char check = fileName.charAt(fileName.indexOf("-") + 1);

      // Create array of words from the file
			Scanner content = new Scanner(new File(foldername+"/"+fileName));
	    String content_list = "";

	    while(content.hasNextLine()){
	    	content_list += content.nextLine();
	    }

      // Classify the list of words
			String result = classify(content_list);

      // If result is positve, check if correct
			if (result.equals("Positive")) {
				total_pos_classified += 1;
				if(check == '5') {
					num_pos += 1;
				}
			}

      // If result is negative, check if correct
			if (result.equals("Negative") ) {
				total_neg_classified += 1;
				if(check == '1') {
					num_neg += 1;
				}
			}

      // If result is neutral, check if correct
      if (result.equals("Neutral")) {
        total_neu_classified += 1;
        if(check !=  '5' && check != '1') {
          num_neu += 1;
        }
      }

		}


    // Calculate the accuracy and precision
		double accuracy = (num_pos + num_neg + num_neu) / (total_pos_classified + total_neg_classified + total_neu_classified) *100;
		double pos_precision = (num_pos / total_pos_classified) * 100;
		double neg_precision = (num_neg / total_neg_classified) * 100;
    double neu_precision = (num_neu / total_neu_classified) * 100;

    // Print the accuracy and precision 
		System.out.println("Accuracy: " + accuracy);
		System.out.println("Positive Precision: " + pos_precision);
		System.out.println("Negative Precision: " + neg_precision);
    System.out.println("Neutral Precision: " + neu_precision);


	}



	/**
	 * Computes log_2(d)
	 * @param d - value
	 * @return log_2(d)
	 */
	private static double log2(double d){
		return Math.log(d)/Math.log(2);
	}



}
