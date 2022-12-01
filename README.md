### Checker Program for Java

Designed for Computer Science teachers, this program checks .java files by looking at the text within the files and by running unit tests for functions within that file.
Checker.java has the main method for running the program. Checker.java can check for the following things:

1. Compilation
2. Formatting
3. Variable Names
4. Unit Tests
5. Types of logic used
6. Answers to select questions

These tests can be all run, or only a portion of them may be run, based on the request of the user running the program. A grade is given based on the tests passed.
Unit testing is weighted, and can be adjusted within the program. For any unit tests, a text file should specify the expected input data for a particular function and
the expected output data. The formatting of these unit tests files are specified within UnitTester.java

### Unit Testing

Unit tests are stored in text files and have specific formatting requirements that use various punctuation as delimiters:

* \# separates the function's required name and the function's description
* | separates multiple parameters
* , separates elements in arrays
* $ separates arrays in arrays
* & separates the set of inputs and the expected output

Here is an example of a set of unit tests that are being used to test if a method can correctly return an array of the elements
found as a 2D array is traversed in a clockwise, spiral direction:

>#printSpiral#Method that gets a list of elements following a grid in a spiral, starting in the top left corner and going clockwise
>
>1,2,3$4,5,6$7,8,9 & 1,2,3,6,9,8,7,4,5
>
>1,2,3,4,5$6,7,8,9,10$11,12,13,14,15$16,17,18,19,20$21,22,23,24,25 & 1,2,3,4,5,10,15,20,25,24,23,22,21,16,11,6,7,8,9,14,19,18,17,12,13
>
>1,2$3,4$5,6$7,8$9,10 & 1,2,4,6,8,10,9,7,5,3

Each .txt file can have multiple methods that are tested and multiple unit tests per method. The name of the unit test text file
that will be used is specified by the user when running the program
