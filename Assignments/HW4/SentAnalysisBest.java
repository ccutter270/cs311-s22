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
import java.util.*;

public class SentAnalysisBest {

	final static File TRAINFOLDER = new File("train");


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




	/* createBigrams
	 * Takes as parameter an array of strings and returns an array of Bigrams
	 * for the given strings
	 */
	public static String[] createBigrams(String[] words){


		if(words.length > 1){
			// Initialize Bigrams array
			String[] bigrams = new String[words.length - 1];


			// Iterate hrough all the words
			for(int i = 0; i < words.length - 1; i++){

				String bigram = words[i] + words[i+1];

				bigrams[i] = bigram;
			}

			return bigrams;
		}
		else{
			return words;


		}

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

	public static double unique_neg_words = 0;
	public static double unique_pos_words = 0;

	public static double pos_count = 0;
	public static double neg_count = 0;



	public static void train(ArrayList<String> files) throws FileNotFoundException
	{


		// For each file in given folder
		for (String file: files) {

			// Get contents of file

			// Open string as a file
			Scanner content = new Scanner(new File("train/"+file));

			// Get context of folder
			String content_list = "";
			while(content.hasNextLine()){
				content_list += content.nextLine();
			}

			content.close();

			// Split file contents into string array of words
			String contentArray[] = content_list.split(" |\\.|\\!");


			// Now get array of bigrams
			String bigramArray[] = createBigrams(contentArray);

			// combine the two arrays to search words from them
			String[] words = new String[contentArray.length + bigramArray.length];

			for (int i = 0; i < words.length; i++){
				if(i < contentArray.length){
					words[i] = contentArray[i];
				}
				else{
					words[i] = bigramArray[i - contentArray.length];
				}
			}



			// Check the file to see if 1 or 5 star review
			char check = file.charAt(file.indexOf("-") + 1);

			// Negative review
			if (check == '1') {

				// Add to negative file count
				neg_count += 1;

				// For each word in file
				for (String word: words) {

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
			else {

				// Add to positive file count
				pos_count += 1;

				//for each word in file, check if word is in dictionary or not
				for (String word: words) {

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
		}
	}













	public static double lambda = 0.0001;

	/*
	 * Classifier: Classifies the input text (type: String) as positive or negative
	 */
	public static String classify(String text)
	{

		// Split the string into array of words
		String[] text_arr = text.split(" |\\.|\\!");

		// Create Bigrams
		String bigrams[] = createBigrams(text_arr);

		// combine the two arrays to search words from them
		String[] texts = new String[text_arr.length + bigrams.length];

		for (int i = 0; i < texts.length; i++){
			if(i < text_arr.length){
				texts[i] = text_arr[i];
			}
			else{
				texts[i] = bigrams[i - text_arr.length];
			}
		}



		// EQUATION: log (𝑃𝑟(𝑝𝑜𝑠𝑖𝑡𝑖𝑣𝑒)) + log (𝑃𝑟(𝑓 |𝑝𝑜𝑠𝑖𝑡𝑖𝑣𝑒)) + log4𝑃𝑟(𝑓 |𝑝𝑜𝑠𝑖𝑡𝑖𝑣𝑒)5 + ⋯ !"+ log (𝑃𝑟(𝑓 |𝑝𝑜𝑠𝑖𝑡𝑖𝑣𝑒))
			// NOTE: Pr(wordi | positive) = (# wordi in positive docs) / (# unique words in all positive docs)

		double pos_prob = pos_count/(pos_count + neg_count);      // Pr(positive)
		double neg_prob = neg_count/(pos_count + neg_count);      // Pr(negative)


		double text_pos_probability = 0;
	  double text_neg_probability = 0;


		// For each word in input text, calculate Pr(f | pos) & Pr(f | neg)
		for (String word: texts) {


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
	}


		// Compute final Max Likelihood
		text_pos_probability += log2(pos_prob);
		text_neg_probability += log2(neg_prob);

		// Check if positive or negative has a higher probability

		// if positive has a higher probability, return positive
		if (text_pos_probability > text_neg_probability) {
			return "Positive";
		}

		// if negative has a higher probability, return negative
		else {
			return "Negative";
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

		// Total Correct Classified
		double num_pos = 0;
		double num_neg = 0;




		// For each file in given folder name
		for(File file : folder.listFiles()) {

			// Classify as either neg or pos based on name
			String fileName = file.getName();
			char check = fileName.charAt(fileName.indexOf("-") + 1);


			// Get contents from folder as array of string words
			Scanner content = new Scanner(new File(foldername+"/"+fileName));
		  String content_list = "";

		  while(content.hasNextLine()){
		   	content_list += content.nextLine();
		  }

			content.close();

			// Classify the string of words from the file
			String result = classify(content_list);

			// If result is positve, check if correct
			if (result.equals("Positive")) {
				total_pos_classified += 1;
				if(check == '5') {
					num_pos += 1;
				}
			}

			// If result negative, check if correct
			if (result.equals("Negative") ) {
				total_neg_classified += 1;
				if(check == '1') {
					num_neg += 1;
				}
			}

		}


		// Calcualte accuracy and precision
		double accuracy = (num_pos + num_neg) / (total_pos_classified + total_neg_classified) *100;
		double pos_precision = (num_pos / total_pos_classified) * 100;
		double neg_precision = (num_neg / total_neg_classified) * 100;


		// Print accuracy and precision 
		System.out.println("Accuracy: " + accuracy);
		System.out.println("Positive Precision: " + pos_precision);
		System.out.println("Negative Precision: " + neg_precision);


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
