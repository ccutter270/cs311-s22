CS311: Programming Assignment #2
Caroline Cutter + Julia Fairbank

HONOR CODE:
We have neither given nor received unauthorized aid on this assignment.
All group members were present and contributing during all work on this project.


Files Required: SudokuPlayer.java



Known Bugs:

We understand that our program is not completely solving the puzzle, but we
beleive that we are very close. We spent a lot of time trying to figre out
why it wasn't solving but what we did find is that our Init, Alldiff, revise
and AC3 are working. We beleive that the bug is somewhere in the backtrack
method because when we print out allDomains at the end, it is very close to solving
the board (each cell only having 1 number in its domain) but there was always one
empty - so we beleive this is coming from the backtracking.




Customized Solver:

Our custom solver uses many of the same functions as our backtracking, but
we implemented forward checking to check the value that is being assigned
to the cell before it is assigned. We are running into the same bug
that we did for our normal solver, so we beleive that if we solved the bug from
the first part then we would also solve the bug from the second part.


We worked really hard on this assingment and still couldn't figure out what was
wrong with our code, but we would love to earn some points back when we are
back at school and could attend office hours!
