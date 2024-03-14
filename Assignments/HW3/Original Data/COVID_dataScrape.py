"""
NAME: Caroline Cutter and Julia Fairbank

CSCI 0311A - Artificial Intelligence 

ASSIGNMENT: HW 3: Decision Trees 
    

DUE: Monday, April 18, 2022

"""

# IMPORTS

import csv 

# MAIN


if __name__ == "__main__":

    file = open("covid-data-train-revise.csv")
    
    csvreader = csv.reader(file)
    
    header = []
    header = next(csvreader)
    
    examples = []
    
    for example in csvreader:
        examples.append(example)
        
      
    file.close()

    
    file = open("covid-data-train-revise.txt", "a")

    print(len(examples))
    
    # Add to file 
    for example in examples:
        file.write(' '.join([element for element in example]) + '\n')
    file.close()
    