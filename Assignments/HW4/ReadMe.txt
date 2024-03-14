CS311: Programming Assignment #4
Caroline Cutter + Julia Fairbank

HONOR CODE:
We have neither given nor received unauthorized aid on this assignment.
All group members were present and contributing during all work on this project.


Files Required: SentAnalysis.java, SentAnalysisBest.java, test folder, train folder 


*** NOTE *** 
We did the extra credit and made the SentAnalysisNeutral.java file, but to run this file the train and test folders must be named test-neutral and train-neutral (so they didn't get mixed up with the basic classifier files) 




Known Bugs: 
 - 




Reflection for Part 2: 


Examples of text that the classifier failed:

	1) Negative: "No one could make this better" 
		Although this should be considered a positive review because it is saying that this movie is the best, the phrasing makes it classify as a negative review, most likely because it has the word "No" in it or "could" which are typically seen as negative terms. 
	
	2) Positive: "I did not love this"
		This should be considered a negative review because the person did not like it, however it was classified as a positive review most likely because it has the word "love" in it.


	3) Positive: "The director is great but this movie is bad" 
		This should be a negative review because the user is saying that the movie is bad. However, the classifier makes it positive, most likely because it first says that the director is "great". 

	We can see from these examples a few fatal flaws in this classifier. The first is that when there is complex words or negation of words, the Classifier doesn't understand that the word is being negated. We can see this in the first two examples, because instead of saying "this is the best" they used a negative in the beginning by saying "no one could" which caused the classifier to make it negative. In the second example instead of saying "hate", the text was "did not love", so the classifier came out positive, most likely because the word love. Another flaw in the classifier is that it does not associate words with the subjects being talked about. In the third example, the person compliments the director, but the review is really about the movie and they said the movie is bad. But, since there were nice words about the director, then it classifies this as a positive review rather than a negative one. These are just a few flaw in the program. These could potentially be improved by using phrases instead of words (which might help with the negation and specific thing you are talking about issues) or possibly taking out words that don't really have any positive or negative meanings, like "i", "a", "there", etc..... These might sway the results slightly. 






Discussion for Part 3: 

SentAnalysis.java Output:

	Accuracy: 75.53
	Positive Precision: 74.43
	Negative Precision: 76.91



SentAnalysisBest.java Output: 

	Accuracy: 77.73
	Positive Precision: 76.76
	Negative Precision: 78.89


Although there is only a small improvement (~ 1-2%) for each category of accuracy and precision, there is still an improvement from the regular to the best classifier. We implemented the bigrams along with the unigrams that we were already implementing. What we hoped is that this would improve accuracy because it could change the meanings of words like "not great" from being classified as a positive review to a negative review. We believe that the program did this because it did show improvement in the test files. We also improved it further by taking out punctuation like "." and "!" so words could be classified together rather than "word!" and "word" being classified as different words. We explained why there were some flaw in the system in the discussion above. We think that we could improve this further by handling the punctuations such as "!" because this could change the meanings or classification of the review. As of right now, "!" is considered part of the words, but if we made them into their own key in the dictionary and handled them as their own word, then the frequencies could change the outcome of the classification. Another way we could improve this system is by taking longer chunks than just two words to classify the systems. I think that certain phrases like "I don not like" could be classified as negative if we allowed for more words. Another way to improve the system is by possibly taking out neutral words that have no meaning in positive or negative reviews and just might be due to there being more positive or negative reviews. For example, if there are 1000 "a" 's in the positive reviews but only 500 in the negative review, this may be due to there be uneven amounts of positive and negative reviews and not actually with the meaning / classification of "a". So, we could take out neutral words to improve the classifications further. 









