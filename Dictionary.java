/**
   Dictionary.java
   
   This is a dictionary implementation using a Map that holds the word and the definition. The dictionary that is used
   is Collins 15th Edition Dictionary (Official Scrabble Dictionary) containing upwards of 270,000 words, published
   in 2015.
   
   --------------------------------------------------------------------------------------------------------------------
   
   --File Summary--
   
   @@@@@@@@@@ CONSTRUCTORS @@@@@@@@@@@@@
   
   Dictionary() -- create a Dictionary with words and definitions
   
   Dictionary( boolean useDefs ) -- create a Dictionary with just words, or one with words and definitions
   
   Dictionary( String fileName, LanguageSpecs specs, boolean useDefs ) -- create a Dictionary using another language, with just words, or their defs too
   
   @@@@@@@@@@ PUBLIC FUNCTIONS @@@@@@@@@
   
   isWord( String word ) -- checks if the word is in the Dictionary
   isDef( String def ) -- checks if the definition is in the Dictionary
   getDef( String word ) -- get the definition of a word
   addDefs( String fileName ) -- add definitions to a list of words that have no definitions
   
   descramble( String[] str, String fileName ) -- finds all words of a list of letters allowing rearrangement and removal. Then removes
                                               -- duplicates, orders the words in increasing word length, and alphabetizes each set of length words
   descramble( String str, String fileName ) -- same as above
   descramble( char[] str, String fileName ) -- same as above
   descramble( String[] str ) -- find all words of a list of letters allowing rearrangement and removal
   descramble( String str ) -- same as above
   descramble( char[] str ) -- same as above
   scramble( String[] str ) -- find all permutations of all subsets of a list of letters
   scramble( String str ) -- same as above
   scramble( char[] str ) -- same as above

   descrambleAnagram( String str ) -- finds all anagrams of a String (same as descramble method, but only gets words of same length as input token)
   descrambleReps( String str ) -- finds all the anagrams of a String, allowing for repeated letters up to a set limit
   descrambleReps( String str, int limit ) -- finds all of the anagrams of a String, allowing for a limited number of repeated letters

   descrambleSpecs( String[] specs, String newFileName ) -- find all the words in the dictionary that match the given specs
   descrambleSpecs( String specs, String newFileName ) -- same as above
   descrambleSpecs( char[] specs, String newFileName ) -- same as above
   descrambleSpecs( String[] specs, String oldFileName, String newFileName ) -- find all the words in the text file that match the given specs,
                                                                             -- preserving the old text file
   descrambleSpecs( String specs, String oldFileName, String newFileName ) -- same as above
   descrambleSpecs( String[] specs, String str, String descrambleFileName, String newFileName ) -- find all the words made from the descrambled
                                                                                                  -- letters that match the given specs
   descrambleSpecs( String specs, String str, String descrambleFileName, String newFileName  ) -- same as above
   
   makeLanguage( LanguageSpecs specs, String fileName ) -- make a new language and store it in a text file;
                                                        -- based on randomization and approximation to another language (default English)
   processNewWord( LanguageSpecs specs, String word ) -- create a new word based on the specs of a new language based on the original word
   
   orderIncreasing( String fileName ) -- reorder a text file in increasing word length order
   orderIncreasing( String oldFileName, String newFileName ) -- reorder a text file in increasing word length order, preserving the old file
   alphabetize( String fileName ) -- alphabetize a text file (lexicographically)
   alphabetize( String oldFileName, String newFileName ) -- alphabetize a text file (lexicographically), preserving the old file
   alphabetizeSets( String fileName ) -- alphabetize a text file per each set of words of a given length
   alphabetizeSets( String oldFileName, String newFileName ) -- alphabetize a text file per each set of words of a given length, preserving the old file
   removeDuplicates( String fileName ) -- remove the duplicate entries from a text file
   removeDuplicates( String oldFileName, String newFileName ) -- remove the duplicate entries from a text file, preserving the old file
   removeDuplicates( ArrayList<?> list ) -- remove all duplicates in the list and returns the new list
   
   getXLetterWords( int length ) -- get words of specified length
   getWordsContainingX( String token ) -- get words containing a specified token
   getWordsContainingX( String[] tokens ) -- get words containing multiple tokens
   getAppends( String word ) -- get words containing a specified word
   getWordsContainingXButNotY( String token, String exclusionToken ) -- get words containing a specified token, but not containing a different one
   getWordsContainingXButNotY( String token, String[] exclusionTokens ) -- same as above, but can have multiple exclusion tokens
   
   getDefsContainingX( String token ) -- get definitions containing a specified token
   getDefsContainingX( String[] tokens ) -- get definitions containing any of a set of specified tokens
   
   write( String[] list, String fileName ) -- write a list to a text file
   write( ArrayList<String> list, String fileName ) -- write a list to a text file
   write( LinkedHashMap<String, String> list, String fileName ) -- write a list to a text file
   writeLimit( String[] list, String fileName, int upperLimit ) -- write a list to a text file, excluding words that are longer than a specified limit
   writeLimit( String[] list, String fileName, int lowerLimit, int upperLimit ) -- write a list to a text file, excluding words based on length limits
   
   removeWords( String fileName, String token ) -- remove all instances of the given word (do not remove if found in contained in another word)
   removeWords( String oldFileName, String newFileName, String token ) -- same as above, but preserve old file, and create a new one
   removeWordsContainingX( String fileName, String token ) -- edit a text file and replace it, removing words containing the given token
   removeWordsContainingX( String oldFileName, String newFileName, String token ) -- same as above, but preserve old file, and create a new one
   removeWordsContainingX( String fileName, String[] tokens ) -- edit a text file and replace it, removing words containing any of the given tokens
   removeWordsContainingX( String oldFileName, String newFileName, String[] tokens ) -- same as above, but preserve old file, and create a new one
   removeWordsLongerThanX( String fileName, int upperLimit ) -- edit a text file and replace it, removing all words longer than the given limit
   removeWordsLongerThanX( String oldFileName, String newFileName, int upperLimit ) -- same as above, but preserve old file, and create a new one
   
   clearFile( String fileName ) -- clear all contents of a text file so that it has nothing in it
   renameFile( String oldFileName, String newFileName ) -- renames file to new name
   
   fileContains( String fileName, String token ) -- True if the text file contains the token, false otherwise
   
   printFileList() -- prints the names of all the files in the current directory
   printList( String[] list ) -- print the list of words to the console
   printFile( String fileName ) -- print the text file to the console
   
   @@@@@@@@@@ PRIVATE FUNCTIONS @@@@@@@@
   
   containsAny( String word, String[] tokens ) -- finds whether the word contains any of the tokens (true) or not (false)
   containsAll( String word, String[] tokens ) -- finds whether the word contains all of the tokens (true) or not (false)
   
   addWords( Scanner scanner ) -- add the list of words to the Dictionary (no definitions)
   addWordsAndDefs( Scanner scanner ) -- add the list of words and their definitions to the Dictionary
   addArray( ArrayList<String> arrayList, String[] list ) -- add the contents of an array to the end of an ArrayList
   
   descrambleMain( String[] list ) -- adds the tokens that are words to the list and shrinks the list
   descrambleReg( String[] str, String fileName ) -- descramble a set of letters with no variables
   descrambleVar( String[] str, String fileName, int varIndex1, int varIndex2 ) -- descramble a set of letters with up to two variables
   descrambleSubsets( String[] str ) -- finds all subsets of a list of letters
   descramblePermutations( String prefix, String str, ArrayList<String> result ) -- finds all permutations of a list of letters
   
   descrambleSpecs( String[] specs, Scanner scanner ) -- find all matches that fit the specs and return them in a list
   
   removeReps( String str ) -- removes repeated letters from a String and returns the result
   
   alphabetizeSets( Scanner scanner ) -- alphabetize a text file per each set of words of a given length
   
   getScanner( String fileName ) -- gets a Scanner to read the text file
   getFileSize( Scanner scanner ) -- gets the number of lines in the file
   
   renameFileHandler( String oldFileName, String newFileName ) throws IOException -- renames file to new name
   shrinkArray( String[] list, int actualSize ) -- If an array as open, unfilled spots, use this to return and replace the array without those spots
   
   getWorkingDirectory() -- get the working directory direct path
   
   printExecutionTime( long beginTime ) -- print the total time used during a process
   SOPln( String message ) -- never type out System.out.println(..) again with this wonderful, short method; also reduces carpal tunnel
   
   @@@@@@@@@@ PRIVATE CLASSES @@@@@@@@@@
   
   BaseChain
   
   BaseChain( int base, int length ) -- create a BaseChain object with the given base-X system and chain length
   
   BaseChain( int base, int length, int limit ) -- create a BaseChain object with the given base-x system and chain length, with the limit
                                                   of repeated letter possibilities
   
   Fields
   - int base --> the base of the number system for counting with the chain
   - int length --> the length of the chain
   - int[] chain --> the elements stored in an array
   - int limit --> the limit imposed on the total allowed repeated letters for permuting
   - boolean updated --> for chains of no repeated letters, this is used for showing when a chain has been amended
   
   Public Methods
   - add() -- increments the chain by 1
   - get() -- gets the chain array
   - length() -- gets the length of the chain
   - numNonZero() -- gets the numbers of nonzero elements within the chain
   
   Private Methods
   - addNoLimit() -- for chains with no limit
   - addLimit() -- for chains with a limit 
   
   @@@@@@@@@@ GLOBALS @@@@@@@@@@@@@@@@@@
   
   DICTIONARY_NO_DEFS_FILE_NAME   -- the text file containing just words from Collin's 15th Edition Dictionary (2015)
   DICTIONARY_WITH_DEFS_FILE_NAME -- the text file containing words and their definitions from Collin's 15th Edition Dictionary
   
   NUM_WORDS -- holds the number of the words in the dictionary
   
   ENGLISH_ALPHABET_LIST -- the list of the letters in the English alphabet (default)
   ALPHABET_LIST         -- the list of the letters in the alphabet
   
   VOWELS -- determines what is considered a vowel. Check this variable if you want to include 'Y' as a vowel or not
   
   DIRECTORY_PATH -- the direct path of the directory -- used to print all text files. Allows you to have this file in one place,
                     and access text files in another place. Thus, choose a direct path that has the text files that you are using
   
   --------------------------------------------------------------------------------------------------------------------
   
   @author Peter Olson
   @version 10/15/21
   @see dictionary_no_defs.txt
   @see dictionary_defs.txt
   @see dictionary_rikitikita.txt
   @see DictionaryRunner.java
   @see LanguageSpecs.java
   @see WordFinderGame.java

*/

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.Random;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;

public class Dictionary extends LinkedHashMap {
   
   public final String DICTIONARY_NO_DEFS_FILE_NAME = "dictionary_no_defs.txt"; //276,643 words -- Collins 15th Edition Dictionary 2015
   public final String DICTIONARY_WITH_DEFS_FILE_NAME = "dictionary_defs.txt";
   public final int NUM_WORDS;
   public final String[] ENGLISH_ALPHABET_LIST = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
   public String[] ALPHABET_LIST;   //by default, this is English ^^; also can be set to another language
   public final String VOWELS = "AEIOU"; //@@@NOTE: If you want 'Y' to be a vowel, this String needs to be changed. (add 'Y')
   public final String DIRECTORY_PATH = "C://Users/Peter/Desktop/UnclePedro/Work/teaching/cs/Java Files/Java Worksheets and Assignments/teacher";
   
   /**
      Create a dictionary and add all the words and definitions to it (note, Dictionary is a HashMap).
      NOTE: Default constructor adds words and definitions using the DICTIONARY_WITH_DEFS_FILE_NAME
      text file.
      
      @see DICTIONARY_WITH_DEFS_FILE_NAME --> see text file
   */
   public Dictionary () {
      Scanner scanner = getScanner( DICTIONARY_WITH_DEFS_FILE_NAME );
      ALPHABET_LIST = ENGLISH_ALPHABET_LIST;
      addWordsAndDefs( scanner );
      
      NUM_WORDS = this.size();
   }
   
   /**
      Create a dictionary and add all the words and definitions to it, or just the words
      (note, Dictionary is a HashMap).
      NOTE: Default constructor adds words and definitions using the DICTIONARY_WITH_DEFS_FILE_NAME
      text file.
      
      @param useDefs True --> add words and defs; False --> only add the words, no defs
      @see DICTIONARY_WITH_DEFS_FILE_NAME --> see text file
      @see DICTIONARY_NO_DEFS_FILE_NAME --> see text file
   */
   public Dictionary( boolean useDefs ) {
      ALPHABET_LIST = ENGLISH_ALPHABET_LIST;
      if( useDefs ) {
         Scanner scanner = getScanner( DICTIONARY_WITH_DEFS_FILE_NAME );
         addWordsAndDefs( scanner );
      } else {
         Scanner scanner = getScanner( DICTIONARY_NO_DEFS_FILE_NAME );
         addWords( scanner );
      }
     
      NUM_WORDS = this.size();    
   }
   
   /**
      Create a dictionary with a new language (note: dictionary is a HashMap).
      
      @param fileName The name of the text file containing the words to add
      @param specs The details for creating the new language
      @param fileName The file to check
      @see makeLanguage( LanguageSpecs specs, String fileName )
   */
   public Dictionary( String fileName, LanguageSpecs specs, boolean useDefs ) {
      Scanner scanner = getScanner( fileName );
      ALPHABET_LIST = specs.ALPHABET;
      
      if( useDefs )
         addWordsAndDefs( scanner );
      else
         addWords( scanner );
      
      NUM_WORDS = this.size();
   }
   
   /**
      Tells whether the given String is a word or not.
      
      @param word The word to check and see if it is in the Dictionary or not
      @return boolean True if it is a word, false otherwise
      @see containsKey( Key key )
   */
   public boolean isWord( String word ) {
      return containsKey( word.toUpperCase() );
   }
   
   /**
      Tells whether the given String is a definition of a word in the dictionary or not
      
      @param def The definition to check and see if it is associated with a word. Note: The given definition must be
                 exactly equal to the definition in the dictionary in order for this to return true
      @return boolean True if it is a definition, false otherwise
      @see containsValue( Value value )
   */
   public boolean isDef( String def ) {
      return containsValue( def );
   }
   
   /**
      Returns the definition associated with the word given. If the word is not in the dictionary,
      "That's not a word!" is returned.
      
      @param word The word to get the definition of
      @return String The definition of the word if it is in the dictionary, or the String "That's not a word!"
      @see HashMap.get( Key key )
   */
   public String getDef( String word ) {
      word = word.toUpperCase();
      if( isWord( word ) )
         return (String)this.get( word );
      else
         return "That's not a word!";
   }
   
   /**
      Given a text file with words that have no definitions, add their definitions to the same file (separated by tabs)
      
      @param fileName The text file to be editted to have words and their definitions
      @see write( String[] list, String fileName )
   */
   public void addDefs( String fileName ) {
      if( !fileName.contains(".txt") )
         fileName += ".txt";
   
      Scanner fileScanner = getScanner( fileName );
      int size = getFileSize( fileScanner );
      
      //Must declare new Scanner object so that the Scanner is pointing at the top of the file again
      fileScanner = getScanner( fileName ); 
      
      String[] list = new String[ size ];
      
      int i = 0;
      while( fileScanner.hasNextLine() ) {
         String word = fileScanner.nextLine();
         list[i] = word + "\t";
         list[i++] += getDef( word );
      }
      
      write( list, fileName );
      
      fileScanner.close();
   }
   
   /**
      Gets the total number of words in this dictionary
      
      @return int The total number of words in this dictionary
   */
   public int getNumWords() {
      return NUM_WORDS;
   }
   
   /**
      Gets the total number of words in this text file (Note that this returns the total number of lines, and is not a word count
      
      @param fileName The text file to scan / observe
      @return int The total number of words in the text file
      @see getFileSize( Scanner scanner )
      @see getScanner ( String fileName )
   */
   public int getNumWords( String fileName ) {
      if( !fileName.contains(".txt") )
         fileName += ".txt";
      Scanner scanner = getScanner( fileName );
      return getFileSize( scanner );
   }
   
   /**
      Create a dictionary filled with words of a new language. These new words are randomized using the processNewWord(..) method that is dependent
      upon the LanguageSpecs for that language. Each word added is unique
      
      @param specs The specs of the new language, including details on randomization, randomization length, randomization buffer, and the details on the letters of the alphabet
      @param fileName The file to be written to
      @return Dictionary A HashMap Dictionary object containing all of the words in the new language, which have been translated from the original language
   */
   public void makeLanguage( LanguageSpecs specs, String fileName ) {
      LinkedHashMap<String, String> dict = this;
      LinkedHashMap<String, String> newDict = new LinkedHashMap<String, String>(); //I don't understand why I have to do this instead of just calling this.keySet() below
      for( String word : dict.keySet() ) {
         String newWord = processNewWord( specs, word );
         //no repeated words
         while( newDict.get(newWord) != null ) {
            newWord = processNewWord( specs, word );
         }
         newDict.put( newWord, null );
      }
      
      write( newDict, fileName );
   }
   
   /**
    * Process a new word in the new language dependent upon the specs of the LanguageSpecs. These new words can be randomized, can be made to be close to the length of the word,
    * or random length, or the exact length of the original word.
    * 
    * @param specs The specs of the Language, providing details on randomization, randomization length, randomization buffers, and the language itself
    * @param word The word to be translated into a new word
    * @return String The word in the new language
    * @see makeLanguage( Language specs )
    */
   public String processNewWord( LanguageSpecs specs, String word ) {
      int engWordLength = word.length();
      int newWordNumLetterSets = 0;
      Random random = new Random();
      
      if( specs.MATCH_LENGTH ) {
         /* Since letters in another language might be multiple characters, divide by average letter length to approximate the correct number of
            characters for the new word in the new language
         */
         newWordNumLetterSets = engWordLength / specs.AVG_LETTER_LENGTH;
         if( specs.RANDOMIZE_LENGTH ) {
            /* The line below randomly adds a number within the range of the approximate length buffer. The number can be negative
               or positive, depending on the randomization. A line below assures that it will not be a set less than the
               MIN_RANDOMIZATION_LENGTH or longer than the MAX_RANDOMIZATION_LENGTH
            */
            newWordNumLetterSets += ( Math.pow(-1, random.nextInt(2) + 1 ) ) * random.nextInt( specs.APPROX_LENGTH_BUFFER );
            if( newWordNumLetterSets > specs.MAX_RANDOMIZATION_LENGTH )
               newWordNumLetterSets = specs.MAX_RANDOMIZATION_LENGTH;
            else if( newWordNumLetterSets < specs.MIN_RANDOMIZATION_LENGTH )
               newWordNumLetterSets = specs.MIN_RANDOMIZATION_LENGTH;
         }
      } else {
         // Set the number of letters to be equal to some number inbetween the max and min randomization range
         newWordNumLetterSets += random.nextInt( specs.MAX_RANDOMIZATION_LENGTH - specs.MIN_RANDOMIZATION_LENGTH + 1 ) + specs.MIN_RANDOMIZATION_LENGTH;
      }
      
      String newWord = "";
      int newAlphabetLength = specs.ALPHABET.length;
      
      //Put together random letters from the new alphabet to make the new word
      for( int i = 0; i < newWordNumLetterSets; i++ ) {
         newWord += specs.ALPHABET[ random.nextInt( newAlphabetLength ) ];
      }
      
      return newWord;
   }
   
   /**
      Given a set of letters, finds all the possible words that this set can make, and returns them in a list of Strings
      
      @param str The array of characters to be parsed and processed
      @return String[] The list of words that can be found by rearrangement and removal
      @see descramble( String[] str )
   */
   public String[] descramble( char[] str ) {
      return descramble( new String( str ) );
   }
   
   /**
      Given a set of letters, finds all the possible words that this set can make, removes the duplicates,
      orders the file in sets of same length words, and alphabetizes each set of length words.
      
      @param str The array of characters to be parsed and processed
      @return boolean True if there are 2 or less '?'s and the method is successful, false otherwise
      @see descramble( String[] str, String fileName )
   */
   public boolean descramble( char[] str, String fileName ) {
      return descramble( new String( str ), fileName );
   }
   
   /**
      Given a set of letters, finds all the possible words that this set can make, and returns them in a list of Strings
      
      @param str The String of letters to be parsed and processed
      @return String[] The list of words that can be found by rearrangement and removal
      @see descramble( String[] str )
   */
   public String[] descramble( String str ) {
      String[] listOfLetters = new String[ str.length() ];
      
      for( int i = 0; i < listOfLetters.length; i++ )
         listOfLetters[i] = String.valueOf( str.charAt(i) );
         
      return descramble( listOfLetters );
   }
   
   /**
      Given a set of letters, finds all the possible words that this set can make, removes the duplicates,
      orders the file in sets of same length words, and alphabetizes each set of length words.
      
      @param str The String of letters to be parsed and processed
      @param fileName The text file to be processed
      @return boolean True if there are 2 or less '?'s and the method is successful, false otherwise
      @see descramble( String[] str, String fileName )
   */
   public boolean descramble( String str, String fileName ) {
      String[] listOfLetters = new String[ str.length() ];
      
      for( int i = 0; i < listOfLetters.length; i++ )
         listOfLetters[i] = String.valueOf( str.charAt(i) );
         
      return descramble( listOfLetters, fileName );
   }
   
   /**
      Given a set of letters, finds all the possible words that this set can make, and returns them in a list of Strings
      
      @param str The list of letters to process-- Each element should be one letter long
      @return String[] The list of words that can be found by rearrangement and removal
      @see scramble( String[] str )
      @see shrinkArray( String[] list, int actualSize )
   */
   public String[] descramble( String[] str ) {
      for( int i = 0; i < str.length; i++ )
         str[i] = str[i].toUpperCase();
   
      String[] list = scramble( str );
   
      return descrambleMain( list );
   }
   
   /**
      Determines which tokens are words from the list and returns a list with just those words
      
      @param list The list of tokens to process
      @return String[] The list of words
      @see isWord( String word )
      @see descramble( String[] str )
      @see descrambleReg( String[] str, String fileName )
      @see descrambleVar( String[] str, String fileName )
   */
   private String[] descrambleMain( String[] list ) {
      String[] words = new String[ list.length ];
      int count = 0;
      for( int i = 0; i < list.length; i++ ) {
         if( isWord( list[i] ) ) {
            words[ count ] = list[i];
            count++;
         }
      }
      
      words = shrinkArray( words, count );
      
      return words;
   }
   
   /**
      Given a set of letters, finds all the possible words that this set can make, removes the duplicates,
      orders the file in sets of same length words, and alphabetizes each set of length words.
      
      @param str The list of letters to process-- Each element should be one letter long
      @return boolean True if there are 2 or less '?'s and the method is successful, false otherwise
      @see scramble( String[] str )
      @see shrinkArray( String[] list, int actualSize )
      @see write( String[] list, String fileName )
      @see removeDuplicates( String fileName )
      @see orderIncreasing( String fileName )
      @see alphabetizeSets( String fileName )
   */
   public boolean descramble( String[] str, String fileName ) {
      if( !fileName.contains(".txt") )
         fileName += ".txt";
   
      boolean containsVariable = false;
      int count = 0;
      int index = -1;
      int index2 = -1;
      for( int i = 0; i < str.length; i++ ) {
         str[i] = str[i].toUpperCase();
         if( str[i].equals("?") && count == 0 ) {
            containsVariable = true;
            index = i;
            count++;
         } else if ( str[i].equals("?") && count == 1 ) {
            index2 = i;
            count++;
         } else if ( str[i].equals("?") && count > 1 ) {
            return false;
         }
      }
      
      if( !containsVariable )
         descrambleReg( str, fileName );
      else
         descrambleVar( str, fileName, index, index2 );
   
      return true;
   }
   
   /**
      Given a set of letters, finds all the possible words that this set can make, removes the duplicates,
      orders the file in sets of same length words, and alphabetizes each set of length words.
      
      @param str The list of letters to process-- Each element should be one letter long
      @return String[] The list of words that can be found by rearrangement and removal
      @see scramble( String[] str )
      @see shrinkArray( String[] list, int actualSize )
      @see write( String[] list, String fileName )
      @see removeDuplicates( String fileName )
      @see orderIncreasing( String fileName )
      @see alphabetizeSets( String fileName )
   */
   private void descrambleReg( String[] str, String fileName ) {
      String[] list = scramble( str );
      
      String[] words = descrambleMain( list );
      
      write( words, fileName );
      
      this.removeDuplicates( fileName );
      this.orderIncreasing( fileName );
      this.alphabetizeSets( fileName );
   }
   
   /**
      Given a set of letters, finds all the possible words that this set can make, removes the duplicates,
      orders the file in sets of same length words, and alphabetizes each set of length words. If a '?' is included with
      this set of letters, this is a variable that can be any letter. Thus, all 26 possibilities will be assessed and added
      
      Note that two variables is O(n!*(n^3)*(26^2)) (yikes!) -- Thus, for one variable, the total number of allowed letters
      to descramble (including the variable) is 10 letters still (approx. 3.6b * 26 = 94b ~approx 260 sec = 4 minutes and change)
      The max number of allowed letters for two variables is 7 letters ( 5 seconds ), since 8 letters is more than 20 minutes of computation time
      
      @param str The list of letters to process-- Each element should be one letter long
      @return String[] The list of words that can be found by rearrangement and removal
      @see addArray( ArrayList<String> arrayList, String[] list )
      @see scramble( String[] str )
      @see shrinkArray( String[] list, int actualSize )
      @see write( String[] list, String fileName )
      @see removeDuplicates( String fileName )
      @see orderIncreasing( String fileName )
      @see alphabetizeSets( String fileName )
   */
   private void descrambleVar( String[] str, String fileName, int varIndex1, int varIndex2 ) {
      ArrayList<String> varLists = new ArrayList<String>();
      String[] list = new String[ fact( str.length ) ];
      
      // Max allowed letters with 1 variable: 10 letters (4 mins)
      //                          2 varaibles: 8 letters (8 mins)
      if( varIndex2 == -1 )
         if( str.length > 10 )
            return;
         else
            if( str.length > 9 )
               return;
      
      // If varIndex == -1, then there is just 1 variable. Otherwise, there are two variables
      if( varIndex2 == -1 ) {
         for( int i = 0; i < ALPHABET_LIST.length; i++ ) {
            str[ varIndex1 ] = ALPHABET_LIST[i];
            list = scramble( str );
            varLists = addArray( varLists, list );
         }
      } else {
         for( int i = 0; i < ALPHABET_LIST.length; i++ ) {
            str[ varIndex1 ] = ALPHABET_LIST[i];
            for( int j = 0; j < ALPHABET_LIST.length; j++ ) {
               str[ varIndex2 ] = ALPHABET_LIST[j];
               list = scramble( str );
               varLists = addArray( varLists, list );
            }
         }
      }
      
      Object[] objArray = varLists.toArray();
      String[] varListArray = Arrays.copyOf( objArray, objArray.length, String[].class);
      
      String[] words = descrambleMain( varListArray );
      
      write( words, fileName );
      
      this.removeDuplicates( fileName );
      this.orderIncreasing( fileName ); 
      this.alphabetizeSets( fileName );
   }
   
   /**
      Given a set of letters, finds all the possible forms that this set can make, and returns them in a list of Strings
      
      @param str The array of characters to be parsed and processed
      @return String[] The list of Strings that can be found by rearrangement and removal
      @see scramble( String[] str )
   */
   public String[] scramble( char[] str ) {
      return scramble( new String( str ) );
   }
   
   /**
      Given a set of letters, finds all the possible forms that this set can make, and returns them in a list of Strings
      
      @param str The String of letters to be parsed and processed
      @return String[] The list of Strings that can be found by rearrangement and removal
      @see scramble( String[] str )
   */
   public String[] scramble( String str ) {
      String[] listOfLetters = new String[ str.length() ];
      
      for( int i = 0; i < listOfLetters.length; i++ )
         listOfLetters[i] = String.valueOf( str.charAt(i) );
         
      return scramble( listOfLetters );
   }
   
   /**
      Given a set of letters, finds all possible subsets of these letters and their permutations ie. returns a list of 
      all possible combinations of the letters, regardless of whether these are words or not
      
      @param str The list of letters to process-- Each element should be one letter long
      @return String[] The list of all permutations of all subsets
      @see descrambleSubsets( String[] str )
   */
   public String[] scramble( String[] str ) {
      //This computer (Lenovo y510P) runs on 2.4GHz --> approx 2.4 billion computations/sec
      //Based on an approximate O(n!*n^3) --> 13 --> 13 trillion (approx 13000 sec = 3.6 hours) cycles
      //                                  --> 10 --> 3.6 billion (approx 2 sec) cycles
      //                       1 variable --> 10 --> 94 billion (approx 260 sec ~approx 4 minutes) cycles
      //                      2 variables --> 8  --> ?? (more than 20 minutes) cycles
      //                                      7  --> ?? -- 5 seconds
      /*
         Variable limits are handled in descrambleVar(..) method
      
         Note that two variables is O(n!*(n^3)*(26^2)) (yikes!) -- Thus, for one variable, the total number of allowed letters
         to descramble (including the variable) is 10 letters still (approx. 3.6b * 26 = 94b ~approx 260 sec = 4 minutes and change)
         The max number of allowed letters for two variables is 8 letters (13b) ~approx 40 sec, or 9 letters (178b) ~approx 8 minutes
      */
      final int MAX_COMPUTATIONAL_LIMIT = 10;
      int size = str.length;
      
      if( size > MAX_COMPUTATIONAL_LIMIT || size < 2 )
         return str;
      
      ArrayList<String> result = descrambleSubsets( str );
      Object[] objArray = result.toArray();
      
      String[] strArray = Arrays.copyOf( objArray, objArray.length, String[].class);
      
      return strArray;
   }
   
   /**
      Given a String which presumably contains a list of jumbled letters, finds all of the
      descrambled words that can be found using all letters of that word.
      
      eg. descrambleAnagram( "dpiswbree" ) yields "spiderweb"
      eg. descrambleAnagram( "rstlnea" ) yields "rentals", "sternal", "slanter", "saltern", and "antlers"
      eg. descrambleAnagram( "zxcvbnm" ) yields <nothing>
      
      @param str The String to find the anagrams of
      @return String[] The list of descrambled anagram words
      @see descramblePermutations( String prefix, String token, ArrayList<String> list )
      @see removeDuplicates( ArrayList<?> list )
   */
   public String[] descrambleAnagram( String str ) {
      
      ArrayList<String> result = descramblePermutations( "", str, new ArrayList<String>() );
      int sizeList = result.size();
      ArrayList<String> words = new ArrayList<String>();
      
      //just add Strings that are words in the dct
      for( int i = 0; i < sizeList; i++ ) {
         if( this.isWord( result.get(i) ) )
            words.add( result.get(i) );
      }
      
      words = (ArrayList<String>)removeDuplicates( words );
      
      Object[] objArray = words.toArray();
      
      String[] strArray = Arrays.copyOf( objArray, objArray.length, String[].class);
      
      return strArray;
   }
   
   /**
      Given a String which presumably contains a list of jumbled letters, finds all of the
      descrambled words that can be found using all letters of that word, allowing for
      repetition of letters that are found in the word
      
      A limit must be made on the number of repetitions within the word due to the issue of
      permutations creating an intractable problem. The descramble functions operate on an
      order close to O(n^2) at worst, whereas this function results in a complexity of
      O(n^2)*O(a^b), where a is the average number of repetitions per letter, and b is the
      number of letters in the word.
      
      The descramble functions operate within a fraction of a second. At a cap of 7 letters
      descrambled with repetitions, we get the following operating times based on the max
      limits for repetitions per letter:
      
      2: 7^2 * 2^7 = ~627.2 to 6272 seconds [10 minutes to 100 minutes]
      3: 7^2 * 3^7 = ~10,716.3 to 107,163 seconds [2.9 hrs to 29 hours]
      ...
      
      In order to decrease these computational times, further reductions within the synthesis
      are necessary, such as setting restrictions on the repetitions of vowels and/or common
      consonants, and intraprocess checking
      
      Thus, a limit is set in order to reduce the load on the heap space. A limit of total
      additional reps is set per word, so the total repeated letters reduces the total
      permutations that are checked for finding words. Here is a chart of the adjusted
      times, considering factors such as word length, total repeats allowed per letter, and
      total allowed repeats in general:
      
      Word Length | Letter Repeats Max | Total Repeats Max | Average Computational Time
      ---------------------------------------------------------------------------------
            3     |        1           |        1          |            0ms
            4     |        1           |        1          |            1ms
            5     |        1           |        1          |            2ms
            6     |        1           |        1          |            7ms
            7     |        1           |        1          |            12ms
            8     |        1           |        1          |            31ms
            9     |        1           |        1          |            241ms
            10    |        1           |        1          |            2s
            11    |        1           |        1          |            21s
      ---------------------------------------------------------------------------------
            3     |        1           |        2          |            27ms
            4     |        1           |        2          |            33ms
            5     |        1           |        2          |            519ms
            6     |        1           |        2          |            49s
      ---------------------------------------------------------------------------------
            3     |        1           |        3          |            27ms
            4     |        1           |        3          |            33ms
            5     |        1           |        3          |            519ms
            6     |        1           |        3          |            49s
      ---------------------------------------------------------------------------------
      
      @param str The String to find the anagrams of. Does not need to contain repeat letters. The
             function will add the additional repeated letters on a permutative basis
      @param totalRepLimit The max allowed count of repeated letters to permutate
      @return String[] The list of descrambled anagram words
      @see descrambleAnagram( String str )
      @see REP_LIMIT The limit to the total number of allowed repeated letters
      @see removeReps( String str )
      @see BaseChain.numNonZero()
      @see BaseChain.add()
      @see Arrays.copyOf(...)
   */
   public String[] descrambleReps( String str, int totalRepLimit ) {
      final int BASE = 2;
      final int CHAR_REP_LIMIT = totalRepLimit;
      final int FULL = -1; //@see BaseChain.numNonZero()
   
      //@@DEBUG
      long beginTime = System.currentTimeMillis();
   
      String strNoReps = removeReps( str );
      char[] charList = strNoReps.toCharArray();
      ArrayList<String> masterList = new ArrayList<String>();
      
      int length = strNoReps.length();
      BaseChain bc = new BaseChain( BASE, length, CHAR_REP_LIMIT );
      int[] chain = new int[ length ];
      //check all permutations of repeated characters
      for( int i = 0; bc.numNonZero() != FULL; i++ ) {
         String newStr = "";
         //iterate through all letters of word
         for( int j = 0; j < length; j++ ) {
            //iterate through all reps of each letter
            for( int k = 0; k <= chain[j]; k++ ) {
               newStr += Character.toString( charList[j] );
            }
         }
         String[] wordList = descrambleAnagram( newStr );
         masterList.addAll( Arrays.asList( wordList ) );
         chain = bc.add();
      }
      
      masterList = (ArrayList<String>)removeDuplicates( masterList );
      
      Object[] objArray = masterList.toArray();
      
      String[] strArray = Arrays.copyOf( objArray, objArray.length, String[].class);
      
      //@@DEBUG
      printExecutionTime( beginTime );
      
      return strArray;
   }
   
   /**
      @param str The String to find the anagrams of. Does not need to contain repeat letters. The
             function will add the additional repeated letters on a permutative basis
      @return String[] The list of descrambled anagram words
      @see descrambleReps( String str, int totalRepLimit )
   */
   public String[] descrambleReps( String str ) {
      final int DEFAULT_REP_LIMIT = 2;
      return descrambleReps( str, DEFAULT_REP_LIMIT );
   }
   
   /**
      BaseChain is a data structure that uses an array to count
      iteratively similar to counting in different base systems.
      Each subsequent index contains integers which are equal up
      to the value of the base counting system minus 1
      
      Eg. Base-2 BaseChains count from zero up to one
      Eg. Base-7 BaseChains count from zero up to six
      
      @see descrambleReps( String str )
   */
   private class BaseChain {
      
      private int base;    //Describes the base system of the chain
      private int length;  //Describes the number of chain indices
      private int[] chain; //Holds the quantities of each index
      private int limit;   //Describes the limit to the number of nonzero quantities of the chain
                           //--> by default, the limit is equal to the length of the chain
      private boolean updated = false;
      
      /**
         Create an empty chain (of zeros) of the stated base
         
         @param base The base of the chain
         @param length The number of links of the chain
      */
      public BaseChain( int base, int length ) {
         this.base = base;
         this.length = length;
         this.limit = length;
         chain = new int[ length ];
      }
      
      /**
         Create an empty chain (of zeros) of the stated base.
         The chain is limited to having a specified number of nonzero elements
         
         @param base The base of the chain
         @param length The number of links of the chain
      */
      public BaseChain( int base, int length, int limit ) {
         this.base = base;
         this.length = length;
         this.limit = limit;
         chain = new int[ length ];
      }
      
      /**
         Increase the basechain by 1
         
         @return int[] The int array of chain values
         @see addNoLimit();
         @see addLimit();
      */
      public int[] add( ) {
         updated = true;
         if( limit == length )
            return addNoLimit();
         else
            return addLimit();
      }
      
      /**
         Increase the basechain by 1
         
         @return int[] The int array of chain values
         @see add()
         @see addLimit()
      */
      private int[] addNoLimit( ) {

         boolean doContinue = true;
         for( int i = 0; i < length && doContinue; i++ ) {
            //add one since not at base value - 1
            if( chain[i] != base - 1 ) {
               chain[i] += 1;
               doContinue = false;
               
            //element is at max value, so reset to zero and continue loop
            } else {
               chain[i] = 0;
               doContinue = true;
            }
         }
         
         return chain;
      }
      
      /**
         Increase the limited basechain by 1.
         
         Since the chain is limited to a number of nonzero
         elements, adding must count according to the permutations
         that are allowed, disallowing there to be more than the
         limit-number of nonzero elements
         
         @return int[] The int array of chain values
         @see add()
         @see addNoLimit()
      */
      private int[] addLimit( ) {
         int totalNonZeros = numNonZero();
         
         boolean doContinue = true;
         for( int i = 0; i < length && doContinue; i++ ) {
            //add one if not at limit
            if( chain[i] != base - 1 && ( totalNonZeros != limit || doContinue ) ) {
               chain[i] += 1;
               doContinue = false;
            
            //set previous elements to zero and add one to end of contiguous nonzero elements
            //--> when the chain is full
            } else if( chain[i] != base - 1 && totalNonZeros == limit && !doContinue ) {
               int j = i+1;
               while( j < length && chain[j] != 0 ) {
                  chain[j++] = 0;
               }
               chain[j] += 1;
               doContinue = false;
            
            //element is at max value, so reset to zero and continue loop
            } else {
               chain[i] = 0;
               doContinue = true;
            }
         }
         
         return chain;
      }
      
      /**
         Get the int[] chain
         
         @return int[] The int[] chain
      */
      public int[] get( ) {
         return chain;
      }
      
      /**
         Get the length of the chain
         
         @return int The length of the chain
      */
      public int length() {
         return length;
      }
      
      /**
         Get the number of non-zero elements in the chain. Returns -1 if the
         chain only has non-zero elements
         
         @return int The total number of non-zero elements in the chain
         @see FULL If the chain only has non-zero elements, return -1
      */
      public int numNonZero() {
         int totalNonZero = 0;
         final int FULL = -1;
         int endingNonZeros = 0;
         boolean isFull = true; //this variable is used tell if the chain is not full
                                //but will not necessarilly be full if it finishes the
                                //for loop and is still true
         
         //For limits of 1, the chain must have all zeros, so return full
         if( limit == 1 && this.updated )
            return FULL;
         
         for( int i = chain.length - 1; i >= 0; i-- ) {
            //count the number of nonzero elements
            if( chain[i] != 0 )
               totalNonZero++;
            
            //determine whether the chain is full, whether the chain is limited or not
            if( chain[i] != 0 ) {
               endingNonZeros++;
            } else if( totalNonZero != limit ) {
               endingNonZeros = 0;
               isFull = false;
            }
         }
         
         //If the chain is full, return -1
         if( endingNonZeros == limit && isFull )
            return FULL;
         
         return totalNonZero;
      }
      
   }
   
   /**
      Remove all repeated characters of the String
      
      @param str The String to remove repeated characters from
      @return String A String that has no repeated characters
      @see descrambleReps( String str )
   */
   private String removeReps( String str ) {
      char[] charStr = str.toCharArray();
      ArrayList<Character> charList = new ArrayList<Character>();
      for( int i = 0; i < charStr.length; i++ ) {
         if( !charList.contains( charStr[i] ) )
            charList.add( charStr[i] );
      }

      String result = "";
      for( Character c : charList )
         result += Character.toString( c.charValue() );
      return result;
   }
   
   /**
      Print the time the between the provided beginning time
      and the current time
      
      @param beginTime The time at which the function began
   */
   private void printExecutionTime( long beginTime ) {
      long endTime = System.currentTimeMillis();
      long totalTimeSeconds = (endTime - beginTime) / 1000;
      
      long totalHours = 0;
      long totalMinutes = 0;
      long totalSeconds = 0;
      long totalMillis = 0;
      if( totalTimeSeconds > 3600 ) {
         totalHours = totalTimeSeconds / 3600;
         totalMinutes = totalTimeSeconds % 3600;
         totalSeconds = totalTimeSeconds % 60;
      } else if( totalTimeSeconds > 60 ) {
         totalMinutes = totalTimeSeconds / 60;
         totalSeconds = totalTimeSeconds % 60;
      } else if( totalTimeSeconds > 0 ) {
         totalSeconds = totalTimeSeconds;
      } else {
         totalMillis = endTime - beginTime;
      }
      
      if( totalSeconds == 0 )
         SOPln("The process took " + totalMillis + " milliseconds.\n");
      else if( totalMinutes == 0 )
         SOPln("The process took " + totalSeconds + " seconds.\n");
      else if( totalHours == 0 )
         SOPln("The process took " + totalMinutes + " minutes and " +
               totalSeconds + " seconds.\n");
      else
         SOPln("The process took " + totalHours + " hours, " + totalMinutes +
               " seconds, and " + totalSeconds + " seconds.\n");
   }
   
   /**
      Finds all subsets of a given list of letters and returns them in an ArrayList<String>
      
      @param str The list to be processed -- each element should be one letter long
      @return ArrayList<String> The list of all subsets
      @see descramblePermutations( String prefix, String str, ArrayList<String> result )
   */
   private ArrayList<String> descrambleSubsets( String[] str ) {
      String descramblePieces = "";
      ArrayList<String> words = new ArrayList<String>();
      
      int strLength = str.length;
   
      for ( int i = 0; i < ( 1 << strLength ); i++ ) {
         for ( int j = 0; j < strLength; j++ ) {
            if ( ( i & ( 1 << j ) ) > 0 )
               descramblePieces += str[j];
         }
         if( descramblePieces.length() >= 2 )
            words = descramblePermutations( "", descramblePieces, words );
         descramblePieces = "";
      }
   
      return words;
   }
   
   /**
      Finds all of the permutations of a given String and is returned in a list -- Each char of the String is viewed as a single token
      
      @param prefix The beginning of the token that is used to piece together the final String
      @param str The token that is being used to find the permutations of
      @param result The list containing all permutations
   */
   private ArrayList<String> descramblePermutations( String prefix, String str, ArrayList<String> result ) {
      int strLength = str.length();
      if ( strLength == 0 ) {
         result.add( prefix );
      } else {
         for ( int i = 0; i < strLength; i++ )
            descramblePermutations( prefix + str.charAt(i), str.substring( 0, i ) + str.substring( i + 1, strLength ), result );
      }
      
      return result;
   }
   
   /**
      Rewrite a file so that words of the same length are grouped together, from shortest to longest
      
      @param fileName The text file to be rearranged
      @see write( ArrayList<String>, String fileName );
   */
   public void orderIncreasing( String fileName ) {
      if( !fileName.contains(".txt") )
         fileName += ".txt";
   
      Scanner scanner = getScanner( fileName );
      int size = getFileSize( scanner );
      scanner = getScanner( fileName );   //@@@ YOU MUST ALWAYS DECLARE A NEW SCANNER AFTER getFileSize(..) @@@
      
      ArrayList<String> list = new ArrayList<String>();
      
      int wordLength = 1;
      int i = 0;
      while( size > i ) {
         while( scanner.hasNextLine() ) {
            String word = scanner.nextLine();
            if( word.length() == wordLength ) {
               list.add( word );
               i++;
            }
         }
         scanner = getScanner( fileName );
         wordLength++;
      }
      
      write( list, fileName );
      
      scanner.close();
   }
   
   /**
      Rewrite a file so that words of the same length are grouped together, from shortest to longest. Preserves the old text file
      and creates a new one with the new ordering
      
      @param oldFileName The text file to be rearranged
      @param newFileName The new text file to be written to
      @see write( ArrayList<String>, String fileName );
   */
   public void orderIncreasing( String oldFileName, String newFileName ) {
      if( !oldFileName.contains(".txt") )
         oldFileName += ".txt";
   
      Scanner scanner = getScanner( oldFileName );
      int size = getFileSize( scanner );
      scanner = getScanner( oldFileName );   //@@@ YOU MUST ALWAYS DECLARE A NEW SCANNER AFTER getFileSize(..) @@@
      
      ArrayList<String> list = new ArrayList<String>();
      
      int wordLength = 1;
      int i = 0;
      while( size > i ) {
         while( scanner.hasNextLine() ) {
            String word = scanner.nextLine();
            if( word.length() == wordLength ) {
               list.add( word );
               i++;
            }
         }
         scanner = getScanner( oldFileName );
         wordLength++;
      }
      
      write( list, newFileName );
      
      scanner.close();
   }
   
   /**
      Alphabetize the given text file
      
      @param fileName The text file to be alphabetized
      @see Collections.sort( List<T> list )
      @see write( ArrayList<String> list )
   */
   public void alphabetize( String fileName ) {
      if( !fileName.contains(".txt") )
         fileName += ".txt";
   
      Scanner scanner = getScanner( fileName );
      
      ArrayList<String> list = new ArrayList<String>();
      
      while( scanner.hasNextLine() ) {
         String word = scanner.nextLine();
         list.add( word );
      }
      
      Collections.sort( list );
      
      write( list, fileName );
      
      scanner.close();
   }
   
   /**
      Alphabetize the given text file and place the results in a new text file ( preserves old text file )
      
      @param oldFileName The text file to be alphabetized
      @param newFileName The text file to be written to
      @see Collections.sort( List<T> list )
      @see write( ArrayList<String> list )
   */
   public void alphabetize( String oldFileName, String newFileName ) {
      if( !oldFileName.contains(".txt") )
         oldFileName += ".txt";
   
      Scanner scanner = getScanner( oldFileName );
      
      ArrayList<String> list = new ArrayList<String>();
      
      while( scanner.hasNextLine() ) {
         String word = scanner.nextLine();
         list.add( word );
      }
      
      Collections.sort( list );
      
      write( list, newFileName );
      
      scanner.close();
   }
   
   /**
      Alphabetize each set of x-lengthed words within a text file ie. alphabetize the 2-letter words, then the 3-letter words, etc.
      
      @param fileName The text file to be alphabetized by sets
      @see alphabetizeSets( Scanner scanner )
      @see Collections.sort( List<T> list )
      @see write( ArrayList<T> list, String fileName )
   */
   public void alphabetizeSets( String fileName ) {
      if( !fileName.contains(".txt") )
         fileName += ".txt";
   
      Scanner scanner = getScanner( fileName );
      
      ArrayList<String> resultList = alphabetizeSets( scanner );
            
      write( resultList, fileName );
      
      scanner.close();
   }
   
   /**
      Alphabetize each set of x-lengthed words within a text file and write to a new file
      (preserves old text file) ie. alphabetize the 2-letter words, then the 3-letter words, etc.
      
      @param oldTextFile The text file to be alphabetized by sets
      @param newFileName The text file to be written to
      @see alphabetizeSets( Scanner scanner )
      @see Collections.sort( List<T> list )
      @see write( ArrayList<T> list, String fileName )
   */
   public void alphabetizeSets( String oldTextFile, String newFileName ) {
      if( !oldTextFile.contains(".txt") )
         oldTextFile += ".txt";
   
      Scanner scanner = getScanner( oldTextFile );
      
      ArrayList<String> resultList = alphabetizeSets( scanner );
      
      write( resultList, newFileName );
      
      scanner.close();
   }
   
   /**
      Alphabetize each set of x-lengthed words within a text file and write to a new file
      (preserves old text file) ie. alphabetize the 2-letter words, then the 3-letter words, etc.
      
      @param oldTextFile The text file to be alphabetized by sets
      @param newFileName The text file to be written to
      @return ArrayList<String> The list of the alphabetized sets
      @see Collections.sort( List<T> list )
      @see write( ArrayList<T> list, String fileName )
   */
   private ArrayList<String> alphabetizeSets( Scanner scanner ) {
      ArrayList<String> resultList = new ArrayList<String>();
      ArrayList<String> setList;
      
      String word = "aa";
      int setSize = 2;
      boolean addLastWord = false;
      boolean addNextWord = false;
      while( scanner.hasNextLine() ) {
         setList = new ArrayList<String>();
         if( addNextWord ) {
            setList.add( word );
            addNextWord = false;
         }
         
         while( word.length() == setSize && scanner.hasNextLine() ) {
            word = scanner.nextLine();
            if( word.length() == setSize )
               setList.add( word );
            else if( !scanner.hasNextLine() )
               addLastWord = true;
            else
               addNextWord = true;
         }
         
         setSize++;
         Collections.sort( setList );
         resultList.addAll( setList );
         
         if( addLastWord ) {
            setList = new ArrayList<String>();
            setList.add( word );
            addLastWord = false;
            resultList.addAll( setList );
         }
      }
      
      return resultList;
   }
   
   /**
      Remove all duplicate entries from a text file
      
      @param fileName The text file to be processed
      @see write( LinkedHashMap<String, String> list, String fileName )
   */
   public void removeDuplicates( String fileName ) {
      if( !fileName.contains(".txt") )
         fileName += ".txt";
   
      Scanner scanner = getScanner( fileName );
      
      LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
      
      while( scanner.hasNextLine() ) {
         String word = scanner.nextLine();
      
         if( !map.containsKey( word ) )
            map.put( word, "" );
      }
      
      write( map, fileName );
      
      scanner.close();
   }
   
   /**
      Remove all duplicate entries from a text file, preserving the old text file
      
      @param oldFileName The text file to be processed
      @param newFileName The text file to write to
      @see write( LinkedHashMap<String, String> list, String fileName )
   */
   public void removeDuplicates( String oldFileName, String newFileName ) {
      if( !oldFileName.contains(".txt") )
         oldFileName += ".txt";
   
      Scanner scanner = getScanner( oldFileName );
      
      LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
      
      while( scanner.hasNextLine() ) {
         String word = scanner.nextLine();
         if( !map.containsKey( word ) )
            map.put( word, "" );
      }
      
      write( map, newFileName );
      
      scanner.close();
   }
   
   /**
      Removes all duplicate tokens in an ArrayList
      
      @param list The list of Objects to remove duplicates from
      @return ArrayList<?> The list without duplicates
      @see descrambleAnagram( String str )
   */
   public ArrayList<?> removeDuplicates( ArrayList<?> list ) {
   
      ArrayList<Object> newList = new ArrayList<Object>();
      Iterator iterator = list.iterator();
      
      while ( iterator.hasNext() ) {
         Object obj = iterator.next();
         if( !newList.contains( obj ) )
            newList.add( obj );
      }
      
      return newList;
   }
   
   /**
      Given a String of letters and a String of 'specs', and a descramble text file name, and a resulting text file name,
      first find all words you can make from descrambling the letters, and then find the matches that result from the
      specs
      
      @param specs The String of letters that follow a spec's notations. See notes on descrambleSpecs( String[] specs, String fileName )
      @param str The list of letters to be descrambled
      @param descrambleFileName The text file to place the descrambled words found within the list of letters
      @param newFileName The text file that will hold all of the matches words found within the text file of descrambled words
      @return True if nothing fails or is invalid, false otherwise
      @see descramble( String str, String fileName )
      @see descrambleSpecs( String[] specs, String oldFileName, String newFileName )
   */
   public boolean descrambleSpecs( String specs, String str, String descrambleFileName, String newFileName ) {
      descramble( str, descrambleFileName );
      return descrambleSpecs( specs, descrambleFileName, newFileName );
   }
   
   /**
      Given a String of letters and a set of 'specs', and a descramble text file name, and a resulting text file name,
      first find all words you can make from descrambling the letters, and then find the matches that result from the
      specs
      
      @param specs The list of letters that follow a spec's notations. See notes on descrambleSpecs( String[] specs, String fileName )
      @param str The list of letters to be descrambled
      @param descrambleFileName The text file to place the descrambled words found within the list of letters
      @param newFileName The text file that will hold all of the matches words found within the text file of descrambled words
      @return True if nothing fails or is invalid, false otherwise
      @see descramble( String str, String fileName )
      @see descrambleSpecs( String[] specs, String oldFileName, String newFileName )
   */
   public boolean descrambleSpecs( String[] specs, String str, String descrambleFileName, String newFileName ) {
      descramble( str, descrambleFileName );
      return descrambleSpecs( specs, descrambleFileName, newFileName );
   }
   
   /**
      Given a set of 'specs', find all matches within the dictionary. The specs are defined as follows:
      
      [a-z]: The positions of the letters must match up
      '?': Can denote any letter, will automatically be a match. So this is used as a placeholder to specify where the letters
           must be places eg. ?cat? --> 'acats', 'scate', 'scath', 'scato', 'scats', 'scatt'; but 'cats', 'scat', and others are not included
      '*': Can denote any number of places inbetween two letters eg. q*z --> 'quartz', 'quiz'
           However, this can also be zero places, so *go* would produce many, many words, but also 'go'
      
      Additional notes:
         - c*?a guarantees that the 'c' and the 'a' will at least have 1 letter inbetween them
         - * will produce all the letters in the dictionary
         - ??? will produce all the three letter words
         - cat will only match with 'cat' in the dictionary
         - *c* will produce all words that contain c in them
         - *c*j* will produce all words that have a c followed by a j, with an unspecified number of letters inbetween them
      
      @param specs A String of letters that follows the spec's notations. See notes above
      @param oldFileName The text file to be processed
      @param newFileName The text file name to be printed to
      @return boolean True if everything works, false if there is an invalid character
      @see descrambleSpecs( String[] specs, Scanner scanner )
   */
   public boolean descrambleSpecs( String specs, String oldFileName, String newFileName ) {
      if( !oldFileName.contains(".txt") )
         oldFileName += ".txt";
      if( !oldFileName.contains(".txt") )
         oldFileName += ".txt";
   
      //If there is an invalid character, return false
      for( int i = 0; i < specs.length(); i++ ) {
         if( !Character.isLetter( specs.charAt(i) ) && specs.charAt(i) != '?' &&
             specs.charAt(i) != '*' && specs.charAt(i) != '#' && specs.charAt(i) != '^' )
            return false;
      }
      
      String[] listOfLetters = new String[ specs.length() ];
      
      for( int i = 0; i < listOfLetters.length; i++ )
         listOfLetters[i] = String.valueOf( specs.charAt(i) );
      
      Scanner scanner = getScanner( oldFileName );
      
      ArrayList<String> matches = descrambleSpecs( listOfLetters, scanner );
      
      write( matches, newFileName );
      
      scanner.close();
      
      return true;
   }
   
   /**
      Given a set of 'specs', find all matches within the dictionary. The specs are defined as follows:
      
      [a-z]: The positions of the letters must match up
      '?': Can denote any letter, will automatically be a match. So this is used as a placeholder to specify where the letters
           must be places eg. ?cat? --> 'acats', 'scate', 'scath', 'scato', 'scats', 'scatt'; but 'cats', 'scat', and others are not included
      '*': Can denote any number of places inbetween two letters eg. q*z --> 'quartz', 'quiz'
           However, this can also be zero places, so *go* would produce many, many words, but also 'go'
      
      Additional notes:
         - c*?a guarantees that the 'c' and the 'a' will at least have 1 letter inbetween them
         - * will produce all the letters in the dictionary
         - ??? will produce all the three letter words
         - cat will only match with 'cat' in the dictionary
         - *c* will produce all words that contain c in them
         - *c*j* will produce all words that have a c followed by a j, with an unspecified number of letters inbetween them
      
      @param specs A list of letters that follows the spec's notations. See notes above
      @param oldFileName The text file to be processed
      @param newFileName The text file name to be printed to
      @return boolean True if everything works, false if there is an invalid character
      @see descrambleSpecs( String[] specs, Scanner scanner )
   */
   public boolean descrambleSpecs( String[] specs, String oldFileName, String newFileName ) {
      if( !oldFileName.contains(".txt") )
         oldFileName += ".txt";
      if( !oldFileName.contains(".txt") )
         oldFileName += ".txt";
   
      //If there is an invalid character, return false
      for( int i = 0; i < specs.length; i++ ) {
         if( !Character.isLetter( specs[i].charAt(0) ) && specs[i].charAt(0) != '?' && 
             specs[i].charAt(0) != '*' && specs[i].charAt(0) != '#' && specs[i].charAt(0) != '^' )
            return false;
      }
   
      Scanner scanner = getScanner( oldFileName );
      
      ArrayList<String> matches = descrambleSpecs( specs, scanner );
      
      write( matches, newFileName );
      
      scanner.close();
      
      return true;
   }
   
   /**
      Given a set of 'specs', find all matches within the dictionary. The specs are defined as follows:
      
      [a-z]: The positions of the letters must match up
      '?': Can denote any letter, will automatically be a match. So this is used as a placeholder to specify where the letters
           must be places eg. ?cat? --> 'acats', 'scate', 'scath', 'scato', 'scats', 'scatt'; but 'cats', 'scat', and others are not included
      '*': Can denote any number of places inbetween two letters eg. q*z --> 'quartz', 'quiz'
           However, this can also be zero places, so *go* would produce many, many words, but also 'go'
      
      Additional notes:
         - c*?a guarantees that the 'c' and the 'a' will at least have 1 letter inbetween them
         - * will produce all the letters in the dictionary
         - ??? will produce all the three letter words
         - cat will only match with 'cat' in the dictionary
         - *c* will produce all words that contain c in them
         - *c*j* will produce all words that have a c followed by a j, with an unspecified number of letters inbetween them
      
      @param specs A list of letters that follows the spec's notations. See notes above
      @param newFileName The text file name to be printed to
      @return boolean True if everything works, false if there is an invalid character
      @see descrambleSpecs( String[] specs, String newFileName )
   */
   public boolean descrambleSpecs( char[] specs, String newFileName ) {
      return descrambleSpecs( new String( specs ), newFileName );
   }
   
   /**
      Given a set of 'specs', find all matches within the dictionary. The specs are defined as follows:
      
      [a-z]: The positions of the letters must match up
      '?': Can denote any letter, will automatically be a match. So this is used as a placeholder to specify where the letters
           must be places eg. ?cat? --> 'acats', 'scate', 'scath', 'scato', 'scats', 'scatt'; but 'cats', 'scat', and others are not included
      '*': Can denote any number of places inbetween two letters eg. q*z --> 'quartz', 'quiz'
           However, this can also be zero places, so *go* would produce many, many words, but also 'go'
      
      Additional notes:
         - c*?a guarantees that the 'c' and the 'a' will at least have 1 letter inbetween them
         - * will produce all the letters in the dictionary
         - ??? will produce all the three letter words
         - cat will only match with 'cat' in the dictionary
         - *c* will produce all words that contain c in them
         - *c*j* will produce all words that have a c followed by a j, with an unspecified number of letters inbetween them
      
      @param specs A String that follows the spec's notations. See notes above
      @param newFileName The text file name to be printed to
      @return boolean True if everything works, false if there is an invalid character
      @see descrambleSpecs( String[] specs, String newFileName )
   */
   public boolean descrambleSpecs( String specs, String newFileName ) {
      String[] listOfLetters = new String[ specs.length() ];
      
      for( int i = 0; i < listOfLetters.length; i++ )
         listOfLetters[i] = String.valueOf( specs.charAt(i) );
   
      return descrambleSpecs( listOfLetters, newFileName );
   }
   
   /**
      Given a set of 'specs', find all matches within the dictionary. The specs are defined as follows:
      
      [a-z]: The positions of the letters must match up
      '?': Can denote any letter, will automatically be a match. So this is used as a placeholder to specify where the letters
           must be places eg. ?cat? --> 'acats', 'scate', 'scath', 'scato', 'scats', 'scatt'; but 'cats', 'scat', and others are not included
      '*': Can denote any number of places inbetween two letters eg. q*z --> 'quartz', 'quiz'
           However, this can also be zero places, so *go* would produce many, many words, but also 'go'
      
      Additional notes:
         - c*?a guarantees that the 'c' and the 'a' will at least have 1 letter inbetween them
         - * will produce all the letters in the dictionary
         - ??? will produce all the three letter words
         - cat will only match with 'cat' in the dictionary
         - *c* will produce all words that contain c in them
         - *c*j* will produce all words that have a c followed by a j, with an unspecified number of letters inbetween them
      
      @param specs The list of Strings that are of length 1 that follow the spec's notations. See notes above
      @param newFileName The text file name to be printed to
      @return boolean True if everything works, false if there is an invalid character
      @see descrambleSpecs( String[] specs, Scanner scanner )
      @see getScanner( String fileName )
      @see Character.isLetter( char letter )
      @see write( ArrayList<String> list, String fileName )
   */
   public boolean descrambleSpecs( String[] specs, String newFileName ) {
      if( !newFileName.contains(".txt") )
         newFileName += ".txt";
   
      //If there is an invalid character, return false
      for( int i = 0; i < specs.length; i++ ) {
         if( !Character.isLetter( specs[i].charAt(0) ) && specs[i].charAt(0) != '?' &&
             specs[i].charAt(0) != '*' && specs[i].charAt(0) != '^' && specs[i].charAt(0) != '#' )
            return false;
      }
   
      Scanner scanner = getScanner( DICTIONARY_NO_DEFS_FILE_NAME );
      
      ArrayList<String> matches = descrambleSpecs( specs, scanner );
      
      write( matches, newFileName );
      
      scanner.close();
      
      return true;
   }
   
   /**
      Given a set of 'specs', find all matches within the dictionary. The specs are defined as follows:
      
      [a-z]: The positions of the letters must match up
      '?': Can denote any letter, will automatically be a match. So this is used as a placeholder to specify where the letters
           must be places eg. ?cat? --> 'acats', 'scate', 'scath', 'scato', 'scats', 'scatt'; but 'cats', 'scat', and others are not included
      '*': Can denote any number of places inbetween two letters eg. q*z --> 'quartz', 'quiz'
           However, this can also be zero places, so *go* would produce many, many words, but also 'go'
      
      Additional notes:
         - c*?a guarantees that the 'c' and the 'a' will at least have 1 letter inbetween them
         - * will produce all the letters in the dictionary
         - ??? will produce all the three letter words
         - cat will only match with 'cat' in the dictionary
         - *c* will produce all words that contain c in them
         - *c*j* will produce all words that have a c followed by a j, with an unspecified number of letters inbetween them
      
      @param specs The list of Strings that are of length 1 that follow the spec's notations. See notes above
      @param scanner The scanner attached to the text file
      @return ArrayList<String> The list of matches found
      @see descrambleSpecs( String[] specs, String newFileName )
   */
   private ArrayList<String> descrambleSpecs( String[] specs, Scanner scanner ) {
      String specString = "";
      // Convert to String, and make sure all chars are uppercase
      for( int i = 0; i < specs.length; i++ )
         specString += specs[i].toUpperCase();
      
      ArrayList<String> matches = new ArrayList<String>();
      
      //Asterisks don't necessarilly take up space (can have no letters where an asterisk is), so account for them
      //and subtract them from the true specString total later in the loop
      int numAsterisks = 0; 
      for( int i = 0; i < specString.length(); i++ ) {
         if( specString.charAt(i) == '*' )
            numAsterisks++;
      }
      
      boolean continueSearching = false; //when encountering an asterisk, this is used to skip over letters instead of breaking to the next word
      boolean breaked = false; //If within any of the if statements a break was hit, this automatically disqualifies the word from being a
                               //possible match, so this allows us to skip over the if statement after the for loop (which is needed, it's not just
                               //for efficiency
      while( scanner.hasNextLine() ) {
         String word = scanner.nextLine();
         int i, j; //i keeps track of specString index, j keeps track of word index; i is not incremented when searching
         for( i = 0, j = 0; j < word.length() && i < specString.length(); j++ ) {
            //If the word length is already shorter than the specString length (w/o the asterisks), we can break and move to the next work
            if( word.length() < specString.length() - numAsterisks ) {
               breaked = true;
               break;
            }
            if( Character.isLetter( specString.charAt(i) ) ) {
               if( word.charAt(j) == specString.charAt(i) ) {
                  continueSearching = false;
                  i++;
                  continue;
               } else {
                  if( continueSearching ) {
                     continue; //for asterisks, we move onto the next letter even if its the wrong letter (for conditional will stop if necessary)
                  } else {
                     //specString letter and word letter not matching, move on to next word
                     breaked = true;
                     break;
                  }
               }
            } else if( specString.charAt(i) == '?' ) {
               i++; // ^^ it's good no matter what, continue
               continue;
            } else if( specString.charAt(i) == '#' ) {
               if( VOWELS.indexOf( word.charAt(j) ) < 0 ) { //if this character is a consonant (not found in VOWEL String)
                  i++;
                  continueSearching = false;
                  continue;
               } else { //character is a vowel
                  if( continueSearching ) {
                     continue;
                  } else {
                     //specString consonant and word letter is not consonant, move on to next word
                     breaked = true;
                     break;
                  }
               }
            } else if( specString.charAt(i) == '^' ) {
               if( VOWELS.indexOf( word.charAt(j) ) >= 0 ) { //if this character is a vowel (found in VOWEL String)
                  i++;
                  continueSearching = false;
                  continue;
               } else { //character is a consonant
                  if( continueSearching ) {
                     continue;
                  } else {
                     //specString consonant and word letter is not consonant, move on to next word
                     breaked = true;
                     break;
                  }
               }
            } else if( specString.charAt(i) == '*' ) {
               i++;
               j--; //since asterisks dont actually match any values, have to make sure we don't skip a word letter
               continueSearching = true;
               continue;
            } else {
               SOPln("Invalid input character. Use only [a-z], '*', '?', '#', or '^'");
               return null;
            }
         } //end for
         
         if( !breaked && ( i >= specString.length() || (i >= specString.length() - 1 && specString.charAt( specString.length() - 1 ) == '*') ) &&
             ( j >= word.length() || (j <= word.length() - 1 && specString.charAt( specString.length() - 1) == '*') ) ) {
            matches.add( word );
         }
         
         breaked = false;
      } //end while
      
      return matches;
   }
   
   /**
      Given a String array and a file name, writes each token on sequential lines to a text file
      
      @param list The list of Strings to write to the text file
      @param fileName The name of the text file being written to. Note: If fileName does not end in '.txt', it will be appended
   */
   public void write( String[] list, String fileName ) {
      
      if( !fileName.contains(".txt") )
         fileName += ".txt";
   	
   	// Only .txt files accepted
      if( !fileName.contains(".txt") )
         System.out.println("Please only use a .txt file");
   	
      File file = null;
      PrintStream printStream = null;
      
      try {
         file = new File( fileName );
         printStream = new PrintStream( file );
      } catch( IOException e ) {
         System.out.println(e);
      }
      
      for( int i = 0; i < list.length; i++ ) {
         printStream.println( list[i] );
      }
      
      printStream.close();
   }
   
   /**
      Given an ArrayList and a file name, writes each token on sequential lines to a text file
      
      @param list The list of Strings to write to the text file
      @param fileName The name of the text file being written to. Note: If fileName does not end in '.txt', it will be appended
   */
   public void write( ArrayList<String> list, String fileName ) {
      
      if( !fileName.contains(".txt") )
         fileName += ".txt";
   	
   	// Only .txt files accepted
      if( !fileName.contains(".txt") )
         System.out.println("Please only use a .txt file");
   	
      File file = null;
      PrintStream printStream = null;
      
      try
      {
         file = new File( fileName );
         printStream = new PrintStream( file );
      } catch( IOException e ) {
         System.out.println(e);
      }
      
      for( int i = 0; i < list.size(); i++ ) {
         printStream.println( list.get(i) );
      }
      
      printStream.close();
   }
   
   /**
      Given a LinkedHashMap and a file name, writes each token on sequential lines to a text file
      
      @param list The LinkedHashMap of Strings to write to the text file
      @param fileName The name of the text file being written to. Note: If fileName does not end in '.txt', it will be appended
   */
   public void write( LinkedHashMap<String, String> list, String fileName ) {
      
      if( !fileName.contains(".txt") )
         fileName += ".txt";
   	
   	// Only .txt files accepted
      if( !fileName.contains(".txt") )
         System.out.println("Please only use a .txt file");
   	
      File file = null;
      PrintStream printStream = null;
      
      try
      {
         file = new File( fileName );
         printStream = new PrintStream( file );
      } catch( IOException e ) {
         System.out.println(e);
      }
      
      for( String word : list.keySet() ) {
         printStream.println( word );
      }
      
      printStream.close();
   }
   
   /**
      @see writeLimit( String[] list, String fileName, int lowerLimit, int upperLimit )
   */
   public void writeLimit( String[] list, String fileName, int upperLimit ) {
      writeLimit( list, fileName, 0, upperLimit );
   }
   
   /**
      Given a String array and a file name, writes each token on sequential lines to a text file
      
      @param list The list of Strings to write to the text file
      @param fileName The name of the text file being written to. Note: If fileName does not end in '.txt', it will be appended
   */
   public void writeLimit( String[] list, String fileName, int lowerLimit, int upperLimit ) {
      
      if(!fileName.contains(".txt"))
         fileName += ".txt";
   	
   	// Only .txt files accepted
      if(!fileName.contains(".txt"))
         System.out.println("Please only use a .txt file");
   	
      File file = null;
      PrintStream printStream = null;
      
      try
      {
         file = new File(fileName);
         printStream = new PrintStream(file);
      } catch( IOException e ) {
         System.out.println(e);
      }
      
      for( int i = 0; i < list.length; i++ ) {
         if( list[i].length() <= upperLimit && list[i].length() >= lowerLimit )
            printStream.println( list[i] );
      }
      
      printStream.close();
   }
   
   /**
      Get a list of all words in the dictionary of the given length.
      @WARNING: The following input values will yield the following sized arrays:
      2 letters- 124
      3 letters- 1341
      4 letters- 5625
      5 letters- 12917
      6 letters- 22938
      7 letters- 34167
      8 letters- 41882
      9 letters- 42290
      10 letters- 36593
      11 letters- 28617
      12 letters- 20775
      13 letters- 14185
      14 letters- 9312
      15 letters- 5877
      
      @param length The length of the word to find
      @return String[] The list of words in the dictionary of the given length
      @see getScanner( String fileName )
      @see HashMap.keySet()
      @see shrinkArray( Object[] list, int actualSize )
   */
   public String[] getXLetterWords( int length ) {
      Scanner scanner = getScanner( DICTIONARY_NO_DEFS_FILE_NAME );
      
      String[] wordList = new String[ NUM_WORDS ];
      
      int i = 0;
      LinkedHashMap<String, String> map = this; //I don't understand why I have to do this instead of just calling this.keySet() below
      for( String word : map.keySet() ) {
         if( word.length() == length )
            wordList[i++] = word;
      }
      
      wordList = shrinkArray( wordList, i );
      
      scanner.close();
      
      return wordList;
   }
   
   public int getXLetterWords( int length, String fileName ) {
      if(!fileName.contains(".txt"))
         fileName = fileName + ".txt";
   
      Scanner scanner = getScanner( fileName );
      
      int counter = 0;
      while(scanner.hasNextLine()){
         if(scanner.nextLine().length() == length)
            counter++;
      }
      /*
      String[] wordList = new String[ NUM_WORDS ];
      
      int i = 0;
      int counter = 0;
      LinkedHashMap<String, String> map = this; //I don't understand why I have to do this instead of just calling this.keySet() below
      for( String word : map.keySet() ) {
         if( word.length() == length ){
            wordList[i++] = word;
            counter++;
         }
      }
      
      wordList = shrinkArray( wordList, i );
      */
      scanner.close();
      
      return counter;
   }
   
   /**
      Find all words containing the given String and return them in a list
      
      @param token The String searched for within the words in the Dictionary
      @return String[] The list of words that contain the token
      @see String.contains( CharSequence cs )
   */
   public String[] getWordsContainingX( String token ) {
   
      String[] wordList = new String[ NUM_WORDS ];
      token = token.toUpperCase();
      
      int i = 0;
      LinkedHashMap<String, String> map = this; //I don't understand why I have to do this instead of just calling this.keySet() below
      for( String word : map.keySet() ) {
         if( word.contains( token ) )
            wordList[i++] = word;
      }
      
      wordList = shrinkArray( wordList, i );
      
      return wordList;
   }
   
   /**
      Find all words containing the given String and return them in a list
      
      @param token The String searched for within the words in the Dictionary
      @return String[] The list of words that contain the token
      @see String.contains( CharSequence cs )
   */
   public String[] getWordsContainingX( String[] tokens ) {
   
      String[] wordList = new String[ NUM_WORDS ];
      
      for( int i = 0; i < tokens.length; i++ )
         tokens[i] = tokens[i].toUpperCase();
      
      int i = 0;
      LinkedHashMap<String, String> map = this; //I don't understand why I have to do this instead of just calling this.keySet() below
      for( String word : map.keySet() ) {
         if( containsAll( word, tokens ) )
            wordList[i++] = word;
      }
      
      wordList = shrinkArray( wordList, i );
      
      return wordList;
   }
   
   /**
      Finds all words that contain the given word. Note: This method is fairly unnecessary, but exists as searching for
      extensions of words is often thought of differently as searching for a small token in a word.
      
      @param word The word of note to find extensions of
      @return String[] The list of words that contain the given word
      @see getWordsContainingX( String token )
   */
   public String[] getAppends( String word ) {
      return getWordsContainingX( word );
   }
   
   /**
      Find all words containing the given String, and not containing the second String, and return them in a list
      
      @param token The String searched for within the words in the Dictionary
      @return String[] The list of words that contain the token
      @see String.contains( CharSequence cs )
   */
   public String[] getWordsContainingXButNotY( String token, String exclusionToken ) {
   
      String[] wordList = new String[ NUM_WORDS ];
      token = token.toUpperCase();
      exclusionToken = exclusionToken.toUpperCase();
      
      int i = 0;
      LinkedHashMap<String, String> map = this; //I don't understand why I have to do this instead of just calling this.keySet() below
      for( String word : map.keySet() ) {
         if( word.contains( token ) && !word.contains( exclusionToken ) )
            wordList[i++] = word;
      }
      
      wordList = shrinkArray( wordList, i );
      
      return wordList;
   }
   
   /**
      Find all words containing the given String, and not containing the second String, and return them in a list
      
      @param token The String searched for within the words in the Dictionary
      @return String[] The list of words that contain the token
      @see String.contains( CharSequence cs )
      @see containsAny( String[] tokens )
   */
   public String[] getWordsContainingXButNotY( String token, String[] exclusionTokens ) {
   
      String[] wordList = new String[ NUM_WORDS ];
      token = token.toUpperCase();
      
      for( int i = 0; i < exclusionTokens.length; i++ )
         exclusionTokens[i] = exclusionTokens[i].toUpperCase();
      
      int i = 0;
      LinkedHashMap<String, String> map = this; //I don't understand why I have to do this instead of just calling this.keySet() below
      for( String word : map.keySet() ) {
         if( word.contains( token ) && !containsAny( word, exclusionTokens ) )
            wordList[i++] = word;
      }
      
      wordList = shrinkArray( wordList, i );
      
      return wordList;
   }
   
   /**
      Find all the definitions containing the given token and return them in a list
      
      @param token The String searched for within the definitions in the Dictionary
      @return String[] The list of definitions that contain the token
      @see String.contains( CharSequence cs )
   */
   public String[] getDefsContainingX( String token ) {
   
      String[] defList = new String[ NUM_WORDS ];
      token = token.toUpperCase();
      
      int i = 0;
      LinkedHashMap<String, String> map = this; //I don't understand why I have to do this instead of just calling this.keySet() below
      for( String word : map.keySet() ) {
         String def = this.getDef( word );
         if( def.contains( token ) )
            defList[i++] = word + '\t' + def;
      }
      
      defList = shrinkArray( defList, i );
      
      return defList;
   }
   
   /**
      Find all the definitions containing the given tokens and return them in a list
      
      @param tokens The tokens searched for within the definitions in the Dictionary
      @return String[] The list of definitions that contain the tokens
      @see containsAll( String[] list )
   */
   public String[] getDefsContainingX( String[] tokens ) {
   
      String[] defList = new String[ NUM_WORDS ];
      
      for( int i = 0; i < tokens.length; i++ )
         tokens[i] = tokens[i].toUpperCase();
      
      int i = 0;
      LinkedHashMap<String, String> map = this; //I don't understand why I have to do this instead of just calling this.keySet() below
      for( String word : map.keySet() ) {
         String def = this.getDef( word );
         if( containsAny( def, tokens ) )
            defList[i++] = word + '\t' + def;
      }
      
      defList = shrinkArray( defList, i );
      
      return defList;
   }
   
   /**
      Given a text file, removes all words in that file equal to a given word or token, and replaces the
      file with these words removed.
      
      @param fileName The name of the file to be edited
      @param token If words contain the token, they are removed.
   */
   public void removeWords( String fileName, String token ) {
      Scanner scanner = getScanner( fileName );
      
      ArrayList<String> list = new ArrayList<String>();
      
      while( scanner.hasNextLine() ) {
         String word = scanner.nextLine();
         if( !word.equals( token ) )
            list.add( word );
      }
      
      write( list, fileName );
      
      scanner.close();
   }
   
   /**
      Given a text file, removes all words in that file equal to a given word or token, and writes the new contents
      to a new file
      
      @param oldFileName The name of the file to be processed
      @param newFileName The name of the file to be written to
      @param token If words contain the token, they are removed.
   */
   public void removeWords( String oldFileName, String newFileName, String token ) {
      Scanner scanner = getScanner( oldFileName );
      
      ArrayList<String> list = new ArrayList<String>();
      
      while( scanner.hasNextLine() ) {
         String word = scanner.nextLine();
         if( !word.equals( token ) )
            list.add( word );
      }
      
      write( list, newFileName );
      
      scanner.close();
   }
   
   /**
      Given a text file, removes all words in that file containing a given word or token, and replaces the
      file with these words removed.
      
      @param fileName The name of the file to be editted
      @param token If words contain the token, they are removed.
   */
   public void removeWordsContainingX( String fileName, String token ) {
      Scanner scanner = getScanner( fileName );
      
      ArrayList<String> list = new ArrayList<String>();
      
      while( scanner.hasNextLine() ) {
         String word = scanner.nextLine();
         if( !word.contains( token ) )
            list.add( word );
      }
      
      write( list, fileName );
      
      scanner.close();
   }
   
   /**
      Given a text file, removes all words in that file containing a given word or token, and creates a new file with these words
      (preserves old file)
      
      @param oldFileName The name of the file to be used
      @param newFileName The name of the new file to be created
      @param token If words contain the token, they are removed.
   */
   public void removeWordsContainingX( String oldFileName, String newFileName, String token ) {
      Scanner scanner = getScanner( oldFileName );
      
      ArrayList<String> list = new ArrayList<String>();
      
      while( scanner.hasNextLine() ) {
         String word = scanner.nextLine();
         if( !word.contains( token ) )
            list.add( word );
      }
      
      write( list, newFileName );
      
      scanner.close();
   }
   
   /**
      Given a text file, removes all words in that file containing any of the given tokens, and replaces the
      file with these words removed.
      
      @param fileName The name of the file to be editted
      @param token If words contain the token, they are removed.
   */
   public void removeWordsContainingX( String fileName, String[] tokens ) {
      Scanner scanner = getScanner( fileName );
      
      ArrayList<String> list = new ArrayList<String>();
      
      while( scanner.hasNextLine() ) {
         String word = scanner.nextLine();
         if( containsAny( word, tokens ) )
            list.add( word );
      }
      
      write( list, fileName );
      
      scanner.close();
   }
   
   /**
      Given a text file, removes all words in that file containing any of the given tokens, and creates a new file with the
      words that were not removed (preserves old file)
            
      @param oldFileName The name of the text file to be used
      @param newFileName The name of the new text file 
      @param token If words contain the token, they are removed.
   */
   public void removeWordsContainingX( String oldFileName, String newFileName, String[] tokens ) {
      Scanner scanner = getScanner( oldFileName );
      
      ArrayList<String> list = new ArrayList<String>();
      
      while( scanner.hasNextLine() ) {
         String word = scanner.nextLine();
         if( !containsAny( word, tokens ) )
            list.add( word );
      }
      
      write( list, newFileName );
      
      scanner.close();
   }
   
   /**
      Given a text file, remove all words longer than specified amount
      
      @param fileName The file to remove words from
      @param upperLimit All words as long as this limit and longer it will be removed
   */
   public void removeWordsLongerThanX( String fileName, int upperLimit ) {
      Scanner scanner = getScanner( fileName );
      
      ArrayList<String> list = new ArrayList<String>();
      
      while( scanner.hasNextLine() ) {
         String word = scanner.nextLine();
         if( word.length() < upperLimit )
            list.add( word );
      }
      
      write( list, fileName );
      
      scanner.close();
   }
   
   /**
      Given a text file, remove all words longer than specified amount, and create a new file to add the
      words left over to (preserves old text file)
      
      @param oldFileName The text file to be used / scanned
      @param newFileName The text file to be created with the words that were not removed
      @param upperLimit All words as long as this limit and longer it will be removed
   */
   public void removeWordsLongerThanX( String oldFileName, String newFileName, int upperLimit ) {
      Scanner scanner = getScanner( oldFileName );
      
      ArrayList<String> list = new ArrayList<String>();
      
      while( scanner.hasNextLine() ) {
         String word = scanner.nextLine();
         if( word.length() < upperLimit )
            list.add( word );
      }
      
      write( list, newFileName );
      
      scanner.close();
   }
   
   /**
      Clear the contents of a text file by overwriting the file with an empty String
      
      @param fileName
   */
   public void clearFile( String fileName ) {
      if( !fileName.contains(".txt") )
         fileName += ".txt";
   	
      File file = null;
      PrintWriter printWriter = null;
      
      try
      {
         file = new File( fileName );
         printWriter = new PrintWriter( file );
         printWriter.print("");
         printWriter.close();
      } catch( IOException e ) {
         System.out.println(e);
      }
      
   }
      
   /**
      Given a text file, rename it. This function is split into two parts so that the function doesn't need to be in
      a try catch block when you call it within other functions
      
      @param oldFileName The old text file name
      @param newFileName The new name of the text file
      @see renameFileHandler( String oldFileName, String newFileName )
   */
   public void renameFile( String oldFileName, String newFileName ) {
      try {
         renameFileHandler( oldFileName, newFileName );
      } catch( IOException e ) {
         e.printStackTrace();
      }
   }
   
   /**
      Sets the current directory to the path given
      
      @param directoryName The name of the directory to be set to
      @return True if successfully set, false otherwise
      @see File.getAbsoluteFile()
      @see System.setProperty(..)
   */
   public boolean setCurrentDirectory( String directoryName ) {
      boolean result = false;
      File newDirectory;
   
      newDirectory = new File( directoryName ).getAbsoluteFile();
      if ( newDirectory.exists() || newDirectory.mkdirs() )
         result = ( System.setProperty( "user.dir", newDirectory.getAbsolutePath() ) != null );
   
      return result;
   }
   
   /**
      Prints the names of all the files in the current directory
      
      @see File.listFiles( FilenameFilter filter )
      @see File.getName()
   */
   public void printFileList() {
      File directory = new File( DIRECTORY_PATH );
      
      FilenameFilter textFilter = 
         new FilenameFilter() {
            public boolean accept( File dir, String name ) {
               return name.endsWith(".txt");
            }
         };
      
      File[] files = directory.listFiles( textFilter );
      
      for( File file : files )
         SOPln( file.getName() );
   }
   
   /**
      Iterates through a list of words and prints each one sequentially to the console
      
      @param list The list of words to be printed. Note: Does not contain definitions
   */
   public void printList( String[] list ) {
      for( int i = 0; i < list.length; i++ )
         SOPln( list[i] );
   }
   
   /**
      Iterates through a text file and prints each line sequentially to the console
      
      @param fileName The text file to print to the console
      @see getScanner( String fileName )
   */
   public void printFile( String fileName ) {
      if(!fileName.contains(".txt"))
         fileName += ".txt";
         
      Scanner scanner = getScanner( fileName );
      
      while( scanner.hasNextLine() ) {
         SOPln( scanner.nextLine() );
      }
   }
   
   /**
      Returns true if the text file contains the token
      
      @param fileName The text file to be processed
      @param token The String of interest of whether it is contained in the text file or not
      @return boolean True if the token is contained in the text file, false otherwise
      @see String.contains( CharSequence token )
   */
   public boolean fileContains( String fileName, String token ) {
      if( !fileName.contains(".txt") )
         fileName += ".txt";
   
      Scanner scanner = getScanner( fileName );
      
      while( scanner.hasNextLine() ) {
         String word = scanner.nextLine();
         if( word.contains( token ) )
            return true;
      }
      
      return false;
   }
   
   /**
      If a word contains any of the specified tokens, return true
      
      @param word The String to check for contained tokens
      @param tokens The list of tokens to check for
      @return boolean True if the word contains any of the tokens, false otherwise
   */
   private boolean containsAny( String word, String[] tokens ) {
      for( int i = 0; i < tokens.length; i++ )
         if( word.contains( tokens[i] ) )
            return true;
      
      return false;
   }
   
   /**
      If a word contains all of the specified tokens, return true
      
      @param word The String to check for contained tokens
      @param tokens The list of tokens to check for
      @return boolean True if the word contains all of the tokens, false otherwise
   */
   private boolean containsAll( String word, String[] tokens ) {
      for( int i = 0; i < tokens.length; i++ )
         if( !word.contains( tokens[i] ) )
            return false;
      
      return true;
   }
   
   /**
      Returns the factorial eg. 5! = 120, of an integer. Returns -1 if the number given is already negative, which is invalid
      
      @param num The number to take the factorial of
      @return int The result of the factorial computation
   */
   private int fact( int num ) {
      if( num < 0 )
         return -1;
      if( num == 0 )
         return 1;
      else
         return fact( num - 1) * num;
   }
   
   /**
      Gets the number of lines in the file. NOTE: The Scanner object is closed at the end of this method.
      
      @param scanner The scanner holding the object being observed
      @return int The number of lines in this file
   */
   private int getFileSize( Scanner scanner ) {
      int size = 0;
      
      while( scanner.hasNextLine() ) {
         scanner.nextLine();
         size++;
      }
      
      scanner.close();
      
      return size;
   }
   
   /**
      Get the direct path of the working directory
      
      @return String The path of the working directory
      @see System.getProperty(..)
   */
   private String getWorkingDirectory() {
      return "Working Directory = " + System.getProperty("user.dir");
   }
   
   /**
      Replaces System.out.println(..) because laziness.
      
      @param message The message to print
   */
   private void SOPln( String message ) {
      System.out.println( message );
   }
   
   /**
      Given an array whose size exceeds its filled elements, returns an array with just the elements
      
      @param list The list of Strings that has excess space
      @param actualSize The number of elements in the array
      @return String[] The smaller list of Strings containing no excess space
   */
   private String[] shrinkArray( String[] list, int actualSize ) {
      String[] smallerList = new String[ actualSize ];
      
      for( int i = 0; i < actualSize; i++ )
         smallerList[i] = list[i];
      
      return smallerList;
   }
   
   /**
      Adds all the words from the text file that contains all the words (and no definitions) that make up the dictionary,
      to the Dictionary object (this)
      
      @param scanner The scanner that is reading the text file containing all the words
      @see HashMap.put( Key key, Value value )
   */
   private void addWords( Scanner scanner ) {
      while( scanner.hasNextLine() ) {
         String word = scanner.nextLine();
         this.put( word, null );
      }
   }
   
   /**
      Adds all the words and the definitions associated with those words from the text file that
      contains all the words and the definitions that make up the dictionary to the
      Dictionary object (this)
      
      @param scanner The scanner that is reading the text file containing all the words
      @see HashMap.put( Key key, Value value )
   */
   private void addWordsAndDefs( Scanner scanner ) {
      
      while( scanner.hasNextLine() ) {
         String line = scanner.nextLine();
         String[] linePieces = line.split("\t");
         if( linePieces.length > 0 ) {
            String word = linePieces[0];
            String def = linePieces[1];
            this.put( word, def );
         }
      }
   }
   
   /**
      Adds the contents of an array to the end of an ArrayList
      
      @param arrayList The ArrayList to add the array contents to
      @param list The array to add to the ArrayList
      @return ArrayList<String> Contains the contents of the original list plus the contents of the array
   */
   private ArrayList<String> addArray( ArrayList<String> arrayList, String[] list ) {
      for( int i = 0; i < list.length; i++ ) {
         arrayList.add( list[i] );
      }
      
      return arrayList;
   }
   
   /**
      Given a text file name, attach a reader and a scanner to that reader to read the text file.
      Throws a FileNotFoundException and return "File not found" is the text file cannot be found.
      
      @param fileName The text file to be read
      @return Scanner The scanner object now attached to the text file
      @see File.getAbsoluteFile()
   */
   private Scanner getScanner( String fileName ) {
      FileReader fr = null;
      Scanner scan = null;
   
      try {
         fr = new FileReader( fileName );
         scan = new Scanner( fr );
      } catch( FileNotFoundException e ) {
         try {
            fr = new FileReader( new File( fileName ).getAbsoluteFile() );
            scan = new Scanner( fr );
         } catch( FileNotFoundException e2 ) {
            System.out.println("File not found");
            return null;
         }
      }
      
      return scan;
   }

   /**
      Given a text file, rename it
      
      @param oldFileName The old text file name
      @param newFileName The new name of the text file
      @see File.renameTo( String fileName )
   */
   private void renameFileHandler( String oldFileName, String newFileName ) throws IOException {
      if( !newFileName.contains(".txt") )
         newFileName += ".txt";
      if( !oldFileName.contains(".txt") )
         oldFileName += ".txt";
      
      File newFile = new File( newFileName );
      
      if ( newFile.exists() )
         throw new IOException("New file name already exists");
      
      Path source = Paths.get( oldFileName );
   
      try {
         Files.move( source, source.resolveSibling( newFileName ) );
      } catch ( IOException e ) {
         System.err.println(e);
      }
      
   }
   
}