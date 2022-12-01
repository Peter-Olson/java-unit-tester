
/**
   DictionaryRunner.java
   
   @author Peter Olson
   @version 3/27/18
   @see Dictionary.java
*/

import java.util.Scanner;

public class DictionaryRunner {
   
   public static void main( String[] args ) {
      Scanner scanner = new Scanner( System.in );
      
      Dictionary dct = new Dictionary();
      
      dct.setCurrentDirectory( dct.DIRECTORY_PATH );
      
      boolean keepGoing = true;
      
      SOPln("Welcome to the Dictionary! Dictionaries are fun!");
      do {
         printMenu();
         
         String response = scanner.nextLine();
         response = response.toLowerCase();
         String response2 = "";
         String response3 = "";
         String fileName = "";
         String fileName2 = "";
         String specs = "";
         int num = 0;
         switch( response ) {
            case "a": SOPln("What would you like to know is a word or not?");
                      response2 = scanner.nextLine();
                      SOPln("It is " + dct.isWord( response2 ) + " that " + response2 + " is a word.");
                      break;
            case "b": SOPln("What would you like to know is a definition of not?");
                      response2 = scanner.nextLine();
                      SOPln("It is " + dct.isDef( response2 ) + " that " + response2 + " is a definition.");
                      break;
            case "c": SOPln("What would you like to know the definition of?");
                      response2 = scanner.nextLine();
                      SOPln("The definition of " + response2 + " is " + dct.getDef( response2 ) );
                      break;
            case "d": SOPln("What text file would you like to add definitions to?");
                      fileName = scanner.nextLine();
                      dct.addDefs( fileName );
                      SOPln("Would you like to see the text file? (y/n)");
                      if( isPositiveResponse() )
                        dct.printFile( fileName );
                      break;
            case "e": SOPln("What would you like to descramble?\nUp to 10 characters are allowed, with up to 1 variable."
                          + "\nUp to 7 characters are allowed with 2 variables.");
                      response2 = scanner.nextLine();
                      SOPln("What would you like your output text file to be called?");
                      fileName = scanner.nextLine();
                      dct.descramble( response2, fileName );
                      SOPln("Would you like to see the text file? (y/n)");
                      if( isPositiveResponse() )
                        dct.printFile( fileName );
                      break;
            case "f": SOPln("What would you like to scramble?\nUp to 10 characters are allowed, with up to 1 variable."
                          + "\nUp to 7 characters are allowed with 2 variables.");
                      response2 = scanner.nextLine();
                      String[] list = dct.scramble( response2 );
                      dct.printList( list );
                      SOPln("Would you like to save the list?");
                      if( isPositiveResponse() ) {
                         SOPln("Would would you like to name your text file?");
                         fileName = scanner.nextLine();
                         dct.write( list, fileName );
                      }
                      break;
            case "g": SOPln("What specs would you like to use for finding matching words?\n\t- '?'s denote variable letters"
                          + "\n\t- '*'s denote any number of variable letters" + "\n\t- '#'s denote any consonant letters"
                          + "\n\t- '^'s denote any number of vowel letters");
                      specs = scanner.nextLine();
                      SOPln("Would you like to:\n\t1.Find matches from the dictionary\n\t2.Find matches from a text file\n\t"
                          + "3.Descramble a String of letters and find matches from that text file");
                      num = scanner.nextInt();
                      scanner.nextLine();
                      switch( num ) {
                        case 1: SOPln("What would you like your output text file to be called?");
                                fileName = scanner.nextLine();
                                dct.descrambleSpecs( specs, fileName );
                                SOPln("Would you like to see the text file? (y/n)");
                                if( isPositiveResponse() )
                                   dct.printFile( fileName );
                                break;
                        case 2: SOPln("What text file would you like to use?");
                                fileName = scanner.nextLine();
                                SOPln("What would you like your output file to be called?");
                                fileName2 = scanner.nextLine();
                                dct.descrambleSpecs( specs, fileName, fileName2 );
                                SOPln("Would you like to see the text file? (y/n)");
                                if( isPositiveResponse() )
                                   dct.printFile( fileName2 );
                                break;
                        case 3: SOPln("What String would you like to descramble?\nUp to 10 characters are allowed, with up to 1 variable."
                                    + "\nUp to 7 characters are allowed with 2 variables.");
                                response3 = scanner.nextLine();
                                SOPln("What would you like your descramble text file to be called?");
                                fileName = scanner.nextLine();
                                SOPln("What would you like your output text file to be called?");
                                fileName2 = scanner.nextLine();
                                dct.descrambleSpecs( specs, response3, fileName, fileName2 );
                                SOPln("Would you like to see the text file? (y/n)");
                                if( isPositiveResponse() )
                                   dct.printFile( fileName2 );
                                break;
                      }
                      break;
            case "h": SOPln("What text file would you like to reorder?");
                      fileName = scanner.nextLine();
                      SOPln("Would you like to overwrite the text file?");
                      if( isPositiveResponse() ) {
                         dct.orderIncreasing( fileName );
                         SOPln("Would you like to see the text file? (y/n)");
                         if( isPositiveResponse() )
                            dct.printFile( fileName );
                      } else {
                         SOPln("What would you like to call your output text file?");
                         fileName2 = scanner.nextLine();
                         dct.orderIncreasing( fileName, fileName2 );
                         SOPln("Would you like to see the text file? (y/n)");
                         if( isPositiveResponse() )
                            dct.printFile( fileName2 );
                      }
                      break;
            case "i": SOPln("What text file would you like to alphabetize?");
                      fileName = scanner.nextLine();
                      SOPln("Would you like to overwrite the text file?");
                      if( isPositiveResponse() ) {
                         dct.alphabetize( fileName );
                         SOPln("Would you like to see the text file? (y/n)");
                         if( isPositiveResponse() )
                            dct.printFile( fileName );
                      } else {
                         SOPln("What would you like to call your output text file?");
                         fileName2 = scanner.nextLine();
                         dct.alphabetize( fileName, fileName2 );
                         SOPln("Would you like to see the text file? (y/n)");
                         if( isPositiveResponse() )
                            dct.printFile( fileName2 );
                      }
                      break;
            case "j": SOPln("What text file would you like to alphabetize?");
                      fileName = scanner.nextLine();
                      SOPln("Would you like to overwrite the text file?");
                      if( isPositiveResponse() ) {
                         dct.alphabetizeSets( fileName );
                         SOPln("Would you like to see the text file? (y/n)");
                         if( isPositiveResponse() )
                            dct.printFile( fileName );
                      } else {
                         SOPln("What would you like to call your output text file?");
                         fileName2 = scanner.nextLine();
                         dct.alphabetizeSets( fileName, fileName2 );
                         SOPln("Would you like to see the text file? (y/n)");
                         if( isPositiveResponse() )
                            dct.printFile( fileName2 );
                      }
                      break;
            case "k": SOPln("What text file would you like to remove duplicates from?");
                      fileName = scanner.nextLine();
                      SOPln("Would you like to overwrite the text file?");
                      if( isPositiveResponse() ) {
                         dct.removeDuplicates( fileName );
                         SOPln("Would you like to see the text file? (y/n)");
                         if( isPositiveResponse() )
                            dct.printFile( fileName );
                      } else {
                         SOPln("What would you like to call your output text file?");
                         fileName2 = scanner.nextLine();
                         dct.removeDuplicates( fileName, fileName2 );
                         SOPln("Would you like to see the text file? (y/n)");
                         if( isPositiveResponse() )
                            dct.printFile( fileName2 );
                      }
                      break;
            case "l": SOPln("What length of words would you like to find?");
                      num = scanner.nextInt();
                      scanner.nextLine();
                      String[] list2 = dct.getXLetterWords( num );
                      dct.printList( list2 );
                      SOPln("Would you like to save the list?");
                      if( isPositiveResponse() ) {
                         SOPln("Would would you like to name your text file?");
                         fileName = scanner.nextLine();
                         dct.write( list2, fileName );
                      }
                      break;
            case "m": SOPln("What token(s) would you like to search for?");
                      response2 = scanner.nextLine();
                      String[] responseList2 = response2.split(" ");
                      if( responseList2.length > 1 ) {
                         String[] resultsList = dct.getWordsContainingX( responseList2 );
                         dct.printList( resultsList );
                         SOPln("Would you like to save the list?");
                         if( isPositiveResponse() ) {
                            SOPln("Would would you like to name your text file?");
                            fileName = scanner.nextLine();
                            dct.write( resultsList, fileName );
                         }
                      } else {
                         String[] resultsList2 = dct.getWordsContainingX( responseList2[0] );
                         dct.printList( dct.getWordsContainingX( responseList2[0] ) );
                         SOPln("Would you like to save the list?");
                         if( isPositiveResponse() ) {
                            SOPln("Would would you like to name your text file?");
                            fileName = scanner.nextLine();
                            dct.write( resultsList2, fileName );
                         }
                      }
                      break;
            case "n": SOPln("What word would you like to append?");
                      response2 = scanner.nextLine();
                      String[] list3 = dct.getAppends( response2 );
                      dct.printList( list3 );
                      SOPln("Would you like to save the list?");
                      if( isPositiveResponse() ) {
                         SOPln("Would would you like to name your text file?");
                         fileName = scanner.nextLine();
                         dct.write( list3, fileName );
                      }
                      break;
            case "o": SOPln("What token would you like to search for?");
                      response2 = scanner.nextLine();
                      SOPln("What token(s) would you like to exclude?");
                      response3 = scanner.nextLine();
                      String[] responseList = response3.split(" ");
                      if( responseList.length > 1 ) {
                         String[] resultsList3 = dct.getWordsContainingXButNotY( response2, responseList );
                         dct.printList( resultsList3 );
                         SOPln("Would you like to save the list?");
                         if( isPositiveResponse() ) {
                            SOPln("Would would you like to name your text file?");
                            fileName = scanner.nextLine();
                            dct.write( resultsList3, fileName );
                         }
                      } else {
                         String[] resultsList4 = dct.getWordsContainingXButNotY( response2, responseList[0] );
                         dct.printList( dct.getWordsContainingXButNotY( response2, responseList[0] ) );
                         SOPln("Would you like to save the list?");
                         if( isPositiveResponse() ) {
                            SOPln("Would would you like to name your text file?");
                            fileName = scanner.nextLine();
                            dct.write( resultsList4, fileName );
                         }
                      }
                      break;
            case "p": SOPln("What token(s) would you like to find in a definition?");
                      response2 = scanner.nextLine();
                      String[] responseList3 = response2.split(" ");
                      if( responseList3.length > 1 ) {
                         String[] resultsList5 = dct.getDefsContainingX( responseList3 );
                         dct.printList( resultsList5 );
                         SOPln("Would you like to save the list?");
                         if( isPositiveResponse() ) {
                            SOPln("Would would you like to name your text file?");
                            fileName = scanner.nextLine();
                            dct.write( resultsList5, fileName );
                         }
                      } else {
                         String[] resultsList6 = dct.getDefsContainingX( responseList3[0] );
                         dct.printList( resultsList6 );
                         SOPln("Would you like to save the list?");
                         if( isPositiveResponse() ) {
                            SOPln("Would would you like to name your text file?");
                            fileName = scanner.nextLine();
                            dct.write( resultsList6, fileName );
                         }
                      }
                      break;
            case "r": SOPln("What token(s) would you like to remove?");
                      response2 = scanner.nextLine();
                      String[] responseList4 = response2.split(" ");
                      SOPln("What text file would you like to use?");
                      fileName = scanner.nextLine();
                      if( responseList4.length > 1 ) {
                         SOPln("Would you like to overwrite your text file?");
                         if( isPositiveResponse() ) {
                            dct.removeWordsContainingX( fileName, responseList4 );
                            SOPln("Would you like to see the text file? (y/n)");
                            if( isPositiveResponse() )
                               dct.printFile( fileName );
                         } else {
                            SOPln("What would you like to name your output text file?");
                            fileName2 = scanner.nextLine();
                            dct.removeWordsContainingX( fileName, fileName2, responseList4 );
                            SOPln("Would you like to see the text file? (y/n)");
                            if( isPositiveResponse() )
                               dct.printFile( fileName2 );
                         }
                      }else{
                         SOPln("Would you like to overwrite your text file?");
                         if( isPositiveResponse() ) {
                            dct.removeWordsContainingX( fileName, responseList4[0] );
                            SOPln("Would you like to see the text file? (y/n)");
                            if( isPositiveResponse() )
                               dct.printFile( fileName );
                         } else {
                            SOPln("What would you like to name your output text file?");
                            fileName2 = scanner.nextLine();
                            dct.removeWordsContainingX( fileName, fileName2, responseList4[0] );
                            SOPln("Would you like to see the text file? (y/n)");
                            if( isPositiveResponse() )
                               dct.printFile( fileName2 );
                         }
                      }
                      break;
            case "s": SOPln("What would you like to name your text file?");
                      fileName = scanner.nextLine();
                      SOPln("What is the longest word that you would like to include? (entering 7 removes all words longer than 8)");
                      num = scanner.nextInt();
                      scanner.nextLine();
                      SOPln("Would you like to overwrite your text file?");
                      if( isPositiveResponse() ) {
                         fileName2 = scanner.nextLine();
                         dct.removeWordsLongerThanX( fileName, fileName2, num );
                         SOPln("Would you like to see the text file? (y/n)");
                            if( isPositiveResponse() )
                               dct.printFile( fileName2 );
                      } else {
                         dct.removeWordsLongerThanX( fileName, num );
                         SOPln("Would you like to see the text file? (y/n)");
                            if( isPositiveResponse() )
                               dct.printFile( fileName2 );
                      }
                      break;
            case "t": SOPln("What text file would you like to rename?");
                      fileName = scanner.nextLine();
                      SOPln("What would you like to rename it to?");
                      fileName2 = scanner.nextLine();
                      dct.renameFile( fileName, fileName2 );
                      break;
            case "u": SOPln("What file would you like to print?");
                      fileName = scanner.nextLine();
                      dct.printFile( fileName );
                      break;
            case "v": dct.printFileList();
                      break;
            case "w": SOPln("\nWhat would you like to anagram?");
                      response2 = scanner.nextLine();
                      SOPln("");
                      dct.printList( dct.descrambleAnagram( response2 ) );
                      break;
            case "x": SOPln("\nWhat would you like to anagram?\nAllows for repeated letters " +
                            "up to a limit of 3");
                      response2 = scanner.nextLine();
                      SOPln("");
                      dct.printList( dct.descrambleReps( response2, 3 ) );
                      break;
            case "y": SOPln("\nWhat is the name of the text file you would like to store your new language?");
                      fileName = scanner.nextLine();
                      SOPln("Processing...");
                      dct.makeLanguage( new LanguageSpecs(), "dictionary_rikitikita.txt" );
                      SOPln("Complete!");
                      break;
            case "z": SOPln("\nWhat file would you like to use?");
                      fileName = scanner.nextLine();
                      SOPln("\nWhat length of letters do you want to find?");
                      num = scanner.nextInt();
                      scanner.nextLine();
                      int myNumbah = dct.getXLetterWords( num, fileName );
                      SOPln("There are " + myNumbah + " " + num + " letter words.");
                      /*
                      SOPln("Would you like to see the text file? (y/n)");
                      if( isPositiveResponse() )
                        dct.printFile( fileName );*/
                      break;
            case "q": keepGoing = false;
                      break;
            default:  SOPln("I didn't understand that input. Please enter a letter between 'a' and 'u'.");
                      break;
         }
      } while( keepGoing );
      
      SOPln("\nGoodbye nerd.");
   }
   
   public static void printMenu() {
      SOPln("\nWhat would you like to do?\n");
      SOPln("a. isWord");
      SOPln("b. isDef");
      SOPln("c. getDef");
      SOPln("d. addDefs");
      SOPln("e. descramble");
      SOPln("f. scramble");
      SOPln("g. descrambleSpecs");
      SOPln("h. orderIncreasing");
      SOPln("i. alphabetize");
      SOPln("j. alphabetizeSets");
      SOPln("k. removeDuplicates");
      SOPln("l. getXLetterWords");
      SOPln("m. getWordsContainingX");
      SOPln("n. getAppends");
      SOPln("o. getWordsContainingXButNotY");
      SOPln("p. getDefsContainingX");
      SOPln("r. removeWordsContainingX");
      SOPln("s. removeWordsLongerThanX");
      SOPln("t. renameFile");
      SOPln("u. printFile");
      SOPln("v. printFileList");
      SOPln("w. anagramSolver");
      SOPln("x. anagramSolverWithReps");
      SOPln("y. makeLanguage");
      SOPln("z. getXLetterWordsFromTextFile");
      SOPln("\nq. quit");
   }
   
   /**
      Assuming the user will input some form of yes or no, assess the response and return a boolean interpretation
      
      @return boolean True if yes, false if no 
   */
   private static boolean isPositiveResponse( ) {
      Scanner scanner = new Scanner( System.in );
      String response = scanner.nextLine();
      response = response.toUpperCase();
      
      if( response.contains("Y") || response.contains("1") || response.contains("SURE") || response.contains("OK") )
         return true;
      return false;
   }
   
   /**
      Replaces System.out.println(..) because laziness.
      
      @param message The message to print
   */
   private static void SOPln( String message ) {
      System.out.println( message );
   }
   
}