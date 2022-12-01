
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;
import javax.tools.*;
import java.awt.Color;
import java.net.URLClassLoader;
import java.net.URL;

/**
   Checks java files to see if they compile and checks for formatting, method names,
   and other basics as they are required within the classroom. The program then assigns
   a grade based on which components are passed and which are not. This program can be
   run with different stages of requirements for java files based on the current
   knowledge level of the students being evaluated. Each stage includes all of the
   previous stages' requirements plus the new requirements. The stages to which
   the program grades is based on the following structure:
   
   1. Checks for compiling without error
   2. Checks for formatting
   3. Checks for proper variable and method names
   4. Checks for outputs and unit tests
   5. Checks for in-document answers to HW questions

   @author Peter Olson
   @version 12/1/22
*/
public class Checker {
   
   //@@CHANGE final variable based on class stage; see comment above
   private static final int STAGE = 2;
   private static final int MAX_GRADE = 100;
   private static final int MAX_LINE_LENGTH = 150; //in chars
   private static final int MAX_CATEGORY_POINT_LOSS = 7; //see checkFormatting(..)
   private static final int INDENT_SIZE = 3; //number of spaces in an indent
   private static final int MAX_VAR_NAME_LENGTH = 50;
   private static final int PENALTY = 5;
   private static final String ANSWER_SPOT = "A:"; //The characters that designate where an answer begins on a line
   private static final String CURRENT_UNIT_TEST_FILE = "unit_tests_p2.txt"; //@@CHANGE depending on the project
   private static final int TOTAL_MINIMAL_OUTPUTS = 3;  //@@CHANGE depending on the total number of expected outputs (minimum)
   private static final int TOTAL_EXPECTED_ANSWERS = 0; //@@CHANGE depending on whether there are questions to answer within the project
   private static final int WEIGHT = 3; //Weight used for Unit Tests
   
   //Expected program outputs -- Project 1
   private static final int EO_PROGRAM_NUM = 0;
   private static final String EO_PROGRAM1 = "7\n10";
   private static final String[] EO_PROGRAM_LIST = { EO_PROGRAM1 };
   
   //Keep track of lines that have not finished
   private static int totalOpenParentheses = 0;
   
   /**
      Runs all of the different tests to check the program. Specifically, the main
      method checks for that the java file:
      
      1) compiles -->                     checkCompile(..)
      2) has correct formatting -->       checkFormatting(..)
      3) has correct variable names -->   checkNames(..)
      4) has correct outputs -->          checkOutputs(..)
      5) has correct answers -->          checkAnswers(..)
      6) has correct unit tests -->       checkUnitTests(..)
      7) has correct components -->       checkContains(..)
   */
   public static void main( String[] args ) {
      
      File[] javaFiles = getFiles();
      int[] grades = new int[ javaFiles.length ];
      
      switch( STAGE ) {
      
         case 1: grades = checkCompile( javaFiles ); break;
         
         //case 2: grades = checkFormatting( javaFiles ); break;
         
         //case 2: grades = checkNames( javaFiles ); break;
         
         /*
            Note that there are two versions of the checkOutputs(...) method.
            The first method requires outputs to match exactly, while the second
            method only counts the total number of outputs
            
            1) checkOutputs( File[] javaFiles )
            2) checkOutputs( File[] javaFiles, int totalMinimalOutputs )
         */
         //case 4: grades = checkOutputs( javaFiles, TOTAL_MINIMAL_OUTPUTS ); break;
         
         //case 4: grades = checkAnswers( javaFiles ); break;
         
         case 2: grades = checkUnitTests( javaFiles ); break;
         
         //case 5: grades = checkContains( javaFiles); break;
         
         default: SOPln("Error. STAGE value does not match total check methods being run."); break;
      }
      
      grades = proportionalize( grades );
      printGrades( grades, javaFiles );
   }
   
   /**
      Checks whether the java file compiles or not
      
      Compiles: 100/100
      Does not compile: 50/100
      
      @param javaFiles The list of java files in the current directory
      @return int[] The list of grades that correspond in parallel with the java files
   */
   private static int[] checkCompile( File[] javaFiles ) {
      JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
      int[] results = new int[ javaFiles.length ];
      
      for( int i = 0; i < javaFiles.length; i++ ) {     
         //returns 0 if compiles; returns a non-zero number if it does not compile
         results[i] = compiler.run( null, null, null, javaFiles[i].getPath() );
         
         //@@DEBUG
         SOPln( "Compile grade for " + javaFiles[i].getName() + ": " + results[i] + "\n");
         
         if( results[i] == 0 )
            results[i] = 100;
         else
            results[i] = (int)(MAX_GRADE / 2);
      }
      
      return results;
   }
   
   /**
      Checks whether the file is properly indented, spaced, and semi-coloned.
      Checks that lines are not too long, that java doc comments exist or each
      function/method, that global variables are in the correct location, and
      that curly braces are used for logic blocks.
      
      Notes:
      * do while loops are not supported (yet)
      * One-line logic that forgoes the use of curly braces are not allowed (and are
        penalized accordingly)
      * private classes are not supported (yet)
      
      ------------------------------------------------------------------------------
      
      Full Details on Grading:
      
         Indentation
         * An indent can be typed using the 'TAB' key
         * Indents are used for the organization of what are called 'blocks'. A block
           is any section that is contained by left and right curly braces '{' and '}'
         * Each block contained in another block should be indented from the original
           block position. For example:
           
           public class Example {
              
              public void exampleMethod() {
                 int number = 1;
              }
              
           }
         * The exampleMethod method is contained in the Example class block, so it is
           indented. Any lines of code inside exampleMethod should be indented as well
         * The size of indents are dependent upon the settings of the Java Development
           Environment you are using. Typically, indents are equivalent to 2-4 spaces
           in a row. This particular java file was written using indents of 3 spaces
         * Comments do not need to be indented, but are usually indented anyways
         * Most programs automatically indent for you
         * A line that is improperly indented with receive one error for indentation
         * You can have a max of 1 indent error per line of code. The contents of
           comments are not checked for indentation
         
         Spacing
         * In Java, whitespace is 'ignored' by the compiler. However, having a bunch of
           weird spaces and tabs everywhere doesn't look so nice; eg:
           
              int   whySoManySpaces         =                                             7          ;
         
         * That ^ would compile, but in this program that would earn two penalties
         * Checker only allows for a max of two penalties per line for spacing
         * You can have whatever spacing you like in comments without any penalty
         * You can have any number of spaces after the end of a line
         
         Semi-colons
         * Semi-colons are used in a number of ways in Java. A misuse of semi-colons
           or a line that lacks a semi-colon should produce a compile-time error. However,
           Checker gives one penalty per semi-colon that is missing regardless
         * Semi-colons are used for...
           * the end of declaring variables, eg. int var = 3;
           * the end of import statements, eg. import java.util.Scanner;
           * within for loops, eg. for( int i = 0; i < list.length; i++ )
           * do while loops, eg. do {...}while(...);
           * continue and break statements
           * method calls, eg. doMethod();
           * global variables, eg. public final double PI = 3.14159;
           * other statements, such as the ?: operator, eg. int max = a > b ? a : b;
           * ... and other weird uses (probably)
         
         Line Length
         * Typically, line length should not exceed anywhere from 80 to 100 characters
         * Usually, this length would far exceed your monitor screen space to the point
           that you would need to scroll to the right to see the whole line
         * However, if you have a really long monitor, you should mark a place that is
           consistently shorter than 100 characters so as to not exceed the limit and
           incur any penalties
         * Note that line length DOES matter for comments
         * For reference, this line here, including spaces, is in fact, exactly a hundred characters
         * I might change the line length max depending on what seems to be the best value
           for everyone
         
         Java Doc Comments
         * Java Doc comments are a special kind of comment that is used to automatically
           create documentation to help you and other programers understand (or remember)
           what your code does
         * Uses /** ... comments here ... followed by *<backslash>, which cannot be written here
           without messing up THIS java doc comment
         * Java doc comments generate automatically and can be viewed through your JDE
         * These need to be used before every class declaration line, as well as before each
           method that you write. The program does not check for flags within the java doc
           comments [yet]. See below bullet point below
         * Each method or class can be additionally marked with recognized flags:
           * @author       -- used to note the author(s) of the java file; eg. @author Mr. Olson
           * @version      -- used to note the version of the file or the date; eg. @version 1.0
           * @param        -- used to note the intent behind each parameter of the function
                              or of the constructor; eg. public int letterCounter( String word ) {..}
                                                     ---> @param word The word whose letters are counted
           * @return       -- used to note the intent behind the return value
                              ...using same function example as @param:
                              ----> @return int The number of letters that are in the word
           * @exception    -- list what exceptions are thrown
           * @see          -- used to point the reader to a different method for reference (or to a
                              different place in the code)
           * @since        -- lists what version the method was implemented in
           * @serial       -- provide data for serializeable forms
           * @ deprecated   -- notes methods that are no longer used (remove the space b/w @ and deprecated)
         * For our purposes, we will mainly use the @author, @version, @param, @return, and @see tags
         * Java doc comments should also be used to describe the code in sentence form, such as the
           sentences you are currently reading
         
         Global Variables
         * A global variable is any variable that is declared outside of a method (but inside a class),
           and is thus accessible to any method within the class
         * While generally speaking it is not good practice to have too many global variables,
           this program does not check for the number of global variables, but rather that they are
           all in the correct place
         * All global variables should be declared right after the class header is declared, which is
           before the first function (whether it is a constructor or a method)
         * One penalty is assigned for any line of code (assumed to be global variables) that is found
           outside of the class that is not an import statement or a comment, or within the class, but
           between methods inside the class
         
         Curly Braces
         * Curly braces are these: '{' and '}'
         * Curly braces are used to mark the beginning and end of class, method, or logic blocks
         * The standard for where curly braces should be used is not readily agreed upon;
           some people (like me) like curly braces directly after class/method headers or logic
           lines, like so:
           
              for( int i = 0; i < list.length; i++ ) {
                 ...
              }
           
           Some like to use curly braces on the next line, like so:
           
              for( int i = 0; i < list.length; i++ )
              {
                 ...
              }
           
         * You can choose whatever style you like, neither incurs a penalty
         * Java allows for logic blocks to not require curly braces if the contents are
           just one line long. However, adding an additional line of code with in the
           block will lead to unforeseen errors, namely, the second, third, or subsequent
           lines of code not being evaluated conditionally. Therefore, this Checker
           program penalizes any logic blocks that do not use curly braces. In fact, the
           program double penalizes for such errors, so be sure to make sure that you
           always use those curly braces
         * A program will produce a compile-time error if a method or class header is
           missing curly braces, but will not produce any compile-time errors for logic
           blocks that are missing curly braces if they only have one line of code in them
         * Example of logic code using curly braces:
         
              if( i > j ) {
                 doTheThing();
              }
              
              for( int i = 0; i < list.length; i++ ) {
                 j *= i;
                 System.out.println("j is: " + j );
              }
         * Example of logic code that will compile not using curly braces (will incur penalties!)
         
            if ( i > j )
               doTheThing();
            
            for( int i = 0; i < list.length; i++ )
               j *= i;
         
         General Penalties
         * There are a few penalties that you can get for other silly things that don't
           fit the previous categories. If you all are doing weird stuff, I'll have to add
           more general penalties, but I will let you know what these are when they are
           written and added
      
      @param javaFiles The list of student java files
      @return int[] The list of grades that correspond with the students' java files
      @see checkCompile( File[] javaFiles)
      @see hasOpenParentheses( String line )
   */
   private static int[] checkFormatting( File[] javaFiles ) {
      int[] grades = checkCompile( javaFiles );
      
      //check each java file
      for( int i = 0; i < javaFiles.length; i++ ) {
         Scanner scanner = null;
         
         try{
            scanner = new Scanner( javaFiles[i] );
         } catch( IOException e ) {
            e.printStackTrace();
         }
         
         int generalErrorCount = 0;
         int indentErrorCount = 0;
         int spaceErrorCount = 0; final int MAX_SPACE_ERRORS_PER_LINE = 2; int spaceErrorCounter = 0;
         int semicolonErrorCount = 0;
         int lineLengthErrorCount = 0; //line length should not exceed MAX_LINE_LENGTH
         int javaDocMissingCount = 0;
         int globalVarErrorCount = 0;
         int curlyBracesMissingCount = 0;
         
         boolean inClass = false;
         boolean inMethod = false;
         int     inLogic = 0; //taking into account nested logic
         boolean inComment = false;
         boolean inJavaComment = false;
         boolean inSingleComment = false;
         boolean inParentheses = false;
         boolean previousHadOpenParentheses = false;
         
         boolean lastLineJavaComment = false;
         int     lastLineJavaCommentBuffer = 1;
         boolean afterClass = false;
         boolean alreadyPenalized = false;
         boolean addIndent = false;
         
         String previousComment = "";
         String previousLine = "";
         final int PUBLIC_CLASS_CHARS = 11;
         int indentCount = 0;
         int totalMethods = 0;
         int lineNumber = 0;

         //check each line of java file
         while( scanner.hasNextLine() ) {
            String line = scanner.nextLine();
            //change lines of only tabs or spaces to be empty
            line.replaceAll(" +", "  ");
            String lineTrim = line.trim();
            lineNumber++;
            
            String[] splits = line.split(" ");
            if( inSingleComment && inComment ) {
               inComment = false;
               inSingleComment = false;
            }

            if( lineTrim.equals("") )
               splits = new String[0];
            
            //Handles open parentheses
            previousHadOpenParentheses = inParentheses;
            inParentheses = hasOpenParentheses( lineTrim );
            
            //check only if line is not empty
            if( splits.length > 0 ) {
            
               //check if comment has closed
               if( inComment ) {
                  
                  //check line until find comment
                  for( int j = 0; j < splits.length; j++ ) {
                     if( splits[j].contains("*/") ) {
                        if( inJavaComment ) {
                           previousComment += line;
                           lastLineJavaCommentBuffer--;
                           lastLineJavaComment = true;
                        }
                        inComment = false;
                        inJavaComment = false;
                        
                        //check for bad commenting format
                        if( j != splits.length - 1 ) {
                           generalErrorCount++;
                           SOPln("Error line # + " + lineNumber + ": " + lineTrim + " @Formatting:GeneralError");
                        }
                     }
                  }
               
               //check if block has closed
               } else if( inClass || inMethod || inLogic > 0 ) {
               
                  //check if block is closing
                  String firstToken = lineTrim.split(" ")[0];
                  if( firstToken.contains("}") && firstToken.indexOf("}") == 0 ) {
                     if( inLogic > 0 )   inLogic--;
                     else if( inMethod ) inMethod = false;
                     else if( inClass )  inClass  = false;
                     
                     indentCount--;
                  }
                  
               } //end block close check
               
            /* v---- Run through line, checking for errors and comments ----v */
               int blankCounter = 0;
               int counter = 0;
                  
               //find first non-space String and number of indents
               while( splits[blankCounter++].length() == 0 ) {}
               int indents = (int)(blankCounter / INDENT_SIZE);

               String[] splitsTrim = lineTrim.split(" ");
               
               //check if in class
               if( !inComment && !inClass && ( splitsTrim[counter].contains("public") || splitsTrim[counter].contains("private") ||
                     splitsTrim[counter].contains("protected") ) ) { 
                  
                  //check class header
                  if( splitsTrim[++counter].contains("class") ) {
                  
                     //check java docs comment
                     if( lastLineJavaCommentBuffer < 0 || previousComment.equals("") ) {
                        javaDocMissingCount++;
                        SOPln("Error line #" + lineNumber + ": " + lineTrim + " @Formatting:JavaDocError1");
                     }
                     
                     //check curly braces
                     if( !splitsTrim[ splitsTrim.length - 1 ].endsWith("{") && !inParentheses ) {
                        curlyBracesMissingCount++;
                        alreadyPenalized = true;
                        SOPln("Error line #" + lineNumber + ": " + lineTrim + " @Formatting:CurlyError1");
                     }
                        
                     inClass = true;
                     addIndent = true;
                     
                  //improper global vars location
                  } else {
                     globalVarErrorCount++;
                     SOPln("Error line #" + lineNumber + ": " + lineTrim + " @Formatting:GlobalVarError1");
                  }
               
               //check if in method
               } else if( !inComment && inClass && ( splitsTrim[counter].contains("public") || splitsTrim[counter].contains("private") ||
                          splitsTrim[counter].contains("protected") ) &&
                          !splitsTrim[ splitsTrim.length - 1 ].endsWith(";") ) {
               
                  //check java doc comment
                  if( lastLineJavaCommentBuffer < 0 || previousComment.equals("") ) {
                     javaDocMissingCount++;
                     SOPln("Error line #" + lineNumber + ": " + lineTrim + " @Formatting:JavaDocError2");
                  }
                  
                  //check curly braces
                  if( !splitsTrim[ splitsTrim.length - 1 ].endsWith("{") && !inParentheses ) {
                     curlyBracesMissingCount++;
                     alreadyPenalized = true;
                     SOPln("Error line #" + lineNumber + ": " + lineTrim + " @Formatting:CurlyError2");
                  }
                  
                  inMethod = true;
                  addIndent = true;
                  totalMethods++;
               
               //check globals
               } else if( !inComment && ( splitsTrim[counter].contains("public") || splitsTrim[counter].contains("private") ||
                          splitsTrim[counter].contains("protected") ) &&
                          splitsTrim[ splitsTrim.length - 1 ].endsWith(";") && ( inMethod || totalMethods != 0 ) ) {
               
                  globalVarErrorCount++;
                  SOPln("Error line #" + lineNumber + ": " + lineTrim + " @Formatting:GlobalVarError2");
               
               //check globals part 2
               } else if( !inComment && ( !inClass && splitsTrim[ splitsTrim.length - 1 ].endsWith(";") && 
                            !splitsTrim[0].contains("import") && !inComment && !inJavaComment ) ||
                          ( inClass && !inMethod && splitsTrim[ splitsTrim.length - 1 ].endsWith(";") && totalMethods != 0 ) ) {
               
                  globalVarErrorCount++;
                  SOPln("Error line #" + lineNumber + ": " + lineTrim + " @Formatting:GlobalVarError3");
               
               //check if in logic
               } else if( !inComment && inClass && inMethod && splitsTrim[ splitsTrim.length - 1 ].contains("{") ) {
                  
                  inLogic++;
                  addIndent = true;
               
               //check lack of curlies
               } else if( !inComment && inClass && inMethod && !inParentheses && (((previousLine.contains(" if(") ||
                          previousLine.contains(" for(") ||
                          previousLine.contains(" while( ") || previousLine.contains(" else") ||
                          previousLine.contains(" if (") || previousLine.contains(" for (") ||
                          previousLine.contains(" while ( ") ) &&
                          ( !line.contains("{") && !previousLine.contains("{") ) ) ||
                          ( (line.contains(" if(") || line.contains(" for(") ||
                          line.contains(" while( ") || line.contains(" else") ||
                          line.contains(" if (") || line.contains(" for (") ||
                          line.contains(" while ( ") ) && !line.contains("{") &&
                          (!previousLine.contains("{") && ( previousLine.contains(" for(") ||
                          previousLine.contains(" if(") || previousLine.contains(" while(") ||
                          previousLine.contains(" for (") || previousLine.contains(" if (") ||
                          previousLine.contains(" while (") )))) ) {
               
                  addIndent = true;
                  curlyBracesMissingCount++;
                  alreadyPenalized = true;
                  inLogic++;
                  SOPln("Error line #" + lineNumber + ": " + lineTrim + " @Formatting:CurlyError3");
               }       
                  
               //run through line
               for( int j = counter; j < splitsTrim.length; j++ ) {
               
                  //handle errant spaces
                  if( !inComment && splitsTrim[j].equals("") ) {
                     if( !line.contains("//") &&
                         !line.contains("/*") ) {
                        spaceErrorCounter++;
                        SOPln("Error line #" + lineNumber + ": " + lineTrim + " @Formatting:SpaceError");
                     } else {}
                     
                  //handle comment
                  } else if( splitsTrim[j].contains("/*") || splitsTrim[j].contains("//") ) {
                  
                     //handle java comment
                     if( splitsTrim[j].contains("/**") ) {
                        inJavaComment = true;
                        previousComment += line;
                        lastLineJavaCommentBuffer = 1;
                     }
                     
                     //handle // comment
                     if( splitsTrim[j].contains("//") && !inComment)
                        inSingleComment = true;
                     
                     inComment = true;
                        
                     //skip forward past comment end
                     for( int k = j; k < splitsTrim.length; k++ ) {
                        if( splitsTrim[k].contains("*/") ) {
                           if( inJavaComment ) {
                              previousComment += line;
                              inJavaComment = false;
                              lastLineJavaCommentBuffer = 1;
                              lastLineJavaCommentBuffer--;
                              lastLineJavaComment = true;
                           }
                           j = k;
                           inComment = false;
                           break;
                        }
                    }
                     
                  } else {
                  
                     //handle other tokens
                     if( inJavaComment )
                        previousComment += line;
                  }
                  
                  //handle missing semicolon at end of line
                  if( j == splitsTrim.length - 1 && !splitsTrim[j].endsWith("{") && !splitsTrim[j].endsWith("}") &&
                      !splitsTrim[j].endsWith(";") && !alreadyPenalized && !inComment && !inJavaComment &
                      !splitsTrim[j].endsWith("*/") &&
                      !splitsTrim[0].equals("public") && !splitsTrim[0].equals("private") &&
                      !splitsTrim[0].equals("protected") && !inParentheses ) {

                     semicolonErrorCount++;
                     SOPln("Error line #" + lineNumber + ": " + lineTrim + " @Formatting:SemicolonError");
                  }
                     
               } //end line loop
               
               //check line length
               if( lineTrim.length() > MAX_LINE_LENGTH ) {
                  lineLengthErrorCount++;
                  SOPln("Error line #" + lineNumber + ": " + lineTrim + " @Formatting:LineLengthError");
               }
               
               //check indents
               String[] tokens = lineTrim.split(" ");
               String checkTokenForEndingComment = tokens[ tokens.length - 1 ];
               if( !inComment && !checkTokenForEndingComment.equals("*/") && !inJavaComment && Math.abs( indents - indentCount ) > 0 && !previousHadOpenParentheses ) {
                  indentErrorCount++;
                  SOPln("Error line #" + lineNumber + ": " + lineTrim + " @Formatting:IndentError");
               }
               if( addIndent ) {
                  indentCount++;
                  addIndent = false;
               }

            /*---------------------------------------------*/

            } //end check for empty line

            //resets
            alreadyPenalized = false;
            if( splits.length > 0 )
               previousLine = line;
            
            //set buffers for java doc comments to register
            if( !inJavaComment ) {
               if( lastLineJavaComment )
                  lastLineJavaComment = false;
               else
                  lastLineJavaCommentBuffer--;
            }
            
            //set max number of spacing errors
            if( spaceErrorCounter > MAX_SPACE_ERRORS_PER_LINE ) spaceErrorCounter = MAX_SPACE_ERRORS_PER_LINE;
            spaceErrorCount += spaceErrorCounter;
            spaceErrorCounter = 0;
            
         } //end scanner loop

         //Set penalties
         if( generalErrorCount       > MAX_CATEGORY_POINT_LOSS ) generalErrorCount       = MAX_CATEGORY_POINT_LOSS;
         if( indentErrorCount        > MAX_CATEGORY_POINT_LOSS ) indentErrorCount        = MAX_CATEGORY_POINT_LOSS;
         if( spaceErrorCount         > MAX_CATEGORY_POINT_LOSS ) spaceErrorCount         = MAX_CATEGORY_POINT_LOSS;
         if( semicolonErrorCount     > MAX_CATEGORY_POINT_LOSS ) semicolonErrorCount     = MAX_CATEGORY_POINT_LOSS;
         if( lineLengthErrorCount    > MAX_CATEGORY_POINT_LOSS ) lineLengthErrorCount    = MAX_CATEGORY_POINT_LOSS;
         if( javaDocMissingCount     > MAX_CATEGORY_POINT_LOSS ) javaDocMissingCount     = MAX_CATEGORY_POINT_LOSS;
         if( globalVarErrorCount     > MAX_CATEGORY_POINT_LOSS ) globalVarErrorCount     = MAX_CATEGORY_POINT_LOSS;
         if( curlyBracesMissingCount > MAX_CATEGORY_POINT_LOSS ) curlyBracesMissingCount = MAX_CATEGORY_POINT_LOSS;
         
         //Extra penalties for unclosed blocks
         if( inClass )       curlyBracesMissingCount++;
         if( inMethod )      curlyBracesMissingCount++;
         if( inLogic > 0 )   curlyBracesMissingCount++;
         if( inJavaComment ) javaDocMissingCount++;

         grades[i] += MAX_GRADE - generalErrorCount - indentErrorCount - spaceErrorCount - semicolonErrorCount -
                     lineLengthErrorCount - javaDocMissingCount - globalVarErrorCount - curlyBracesMissingCount;
         
         //@@DEBUG
         SOPln("File name: " + javaFiles[i].getName() + " General: " + generalErrorCount + ", Indent: " +
               indentErrorCount + ", Spaces: " + spaceErrorCount + ", Semicolon: " + semicolonErrorCount +
               ", Line Length: " + lineLengthErrorCount + ", JavaDoc: " + javaDocMissingCount +
               ", Global: " + globalVarErrorCount + ", Curly: " + curlyBracesMissingCount + "\n");

      } //end java file loop

      return grades;
   }
   
   /**
      Check files against naming conventions
      
      Naming conventions
      --------------------------------------------------------------------
      Classes:
      
      * Begin with a capital letter
      * Should not use restricted words as class names (see below)
      * You can have almost any character, including most Unicode characters!
        The exact definition is in the Java Language Specification under section 3.8: Identifiers.
        An identifier is an unlimited-length sequence of Java letters and Java digits,
        the first of which must be a Java letter...

        Letters and digits may be drawn from the entire Unicode character set,
        This allows programmers to use identifiers in their programs that are written in their native languages.

        An identifier cannot have the same spelling (Unicode character sequence) as a keyword (§3.9),
        boolean literal (§3.10.3), or the null literal (§3.10.7), or a compile-time error occurs.
        
        However, it best practice to only use English characters.
      * This particular Checker checks that the first letter is alphabet letter, and that the rest of the letters
        are alphanumeric, non-Unicode letters
      * Classes that are multiple words should have each subsequent word beginning with a capital letter
      * Classes that are longer than 50 characters will be counted as an error
      * Class names cannot contain spaces
      
      Methods / Variables:
      * Begin with a lowercase letter
      * Can use alphanumeric characters, underscores, or the dollar sign ($)
      * Subsequent words should have their first letter capitalized; eg. variableName
      * Cannot be one character long, unless the variable is being used within a loop
      * Name cannot be a restricted word (see below)
      * Final variables should be all capitalized and should be separated by an underscore; eg. INTEREST_RATE
      * Names that are longer than 50 characters will be counted as an error
      * Method / variable names cannot contain spaces
      
      -------------------------------------------------------------------------
      
      Restricted Words in Java 8 (8/1/21):
      * abstract, assert, boolean, break, byte, case, catch, char, class, const,
        continue, default, double, do, else, enum, extends, false, final, finally,
        float, for, goto, if, implements, import, instanceof, int, interface, long,
        native, new, null, package, private, protected, public, return, short,
        static, strictfp, super, switch, synchronized, this, throw, throws,
        transient, true, try, void, volatile, while
        
      -------------------------------------------------------------------------
      
      @param javaFiles The java files to check
      @return int[] The list of grades associated in parallel with each java file
   */
   private static int[] checkNames( File[] javaFiles ) {
      int[] grades = checkCompile( javaFiles );
      
      String[] restrictedWords =
         {"abstract", "assert", "boolean", "break", "byte", "case", "catch", "char",
          "class", "const", "continue", "default", "double", "do", "else", "enum",
          "extends", "false", "final", "finally", "float", "for", "goto", "if",
          "implements", "import", "instanceof", "int", "interface", "long", "native",
          "new", "null", "package", "private", "protected", "public", "return",
          "short", "static", "strictfp", "super", "switch", "synchronized", "this",
          "throw", "throws", "transient", "true", "try", "void", "volatile", "while"};
      
      //check each java file
      for( int i = 0; i < javaFiles.length; i++ ) {
         Scanner scanner = null;
         
         try{
            scanner = new Scanner( javaFiles[i] );
         } catch( IOException e ) {
            e.printStackTrace();
         }
         
         boolean inClass = false;
         boolean inMethod = false;
         boolean hasError = false;
         boolean inComment = false;
         boolean inSingleComment = false;
         int nameErrors = 0;
         
         //check each line of java file
         while( scanner.hasNextLine() ) {
            String line = scanner.nextLine();
            //change lines of only tabs or spaces to be empty
            line.replaceAll(" +", "  ");
            String lineTrim = line.trim();
            
            String[] splits = line.split(" ");
            
            if( lineTrim.equals("") )
               splits = new String[0];
            
            hasError = false;
               
            //check only if line is not empty and if it does not have a return statement
            if( splits.length > 0 && !lineTrim.contains("return ") ) {

               int counter = 0;
               String[] splitsTrim = lineTrim.split(" ");
               String name = "";
               boolean usedInLoop = false;
               boolean isClassName = false;
               boolean isFinalVar = false;
               
               //check if in comment
               if( !inComment && (lineTrim.startsWith("//") || lineTrim.startsWith("/*")) ) {
                  if( lineTrim.contains("//") )
                     inSingleComment = true;
                  
                  inComment = true;
               }
               
               if( lineTrim.contains("*/") )
                  inComment = false;
               
               //Class, globals, methods
               if( !inComment && ( splitsTrim[0].equals("public") || splitsTrim[0].equals("private") ||
                                   splitsTrim[0].equals("protected") ) ) {
               
                  //check class
                  if( line.contains(" class ") ) {
                     name = splitsTrim[2];
                     inClass = true;
                     isClassName = true;
                  
                  //check globals
                  } else if( inClass && splitsTrim[ splitsTrim.length - 1].endsWith(";") ) {
                     while( !splitsTrim[++counter].contains("=") && !splitsTrim[counter].endsWith(";") ){}
                     
                     //name is before equals sign or if there is a standalone semicolon (meaning an uninstantiated global)
                     if( splitsTrim[counter].contains("=") || splitsTrim[counter].equals(";") )
                        name = splitsTrim[--counter];
                     
                     //name is touching semicolon (uninstantiated global)
                     else if( splitsTrim[counter].endsWith(";") )
                        name = splitsTrim[counter].substring(0, splitsTrim[counter].length() - 1);
                  
                  //check methods
                  } else if( inClass && line.contains("{") && line.contains("(") && line.contains(")") ) {
                     
                     while( !splitsTrim[++counter].contains("(") ){}
                     
                     inMethod = true;
                     
                     //method name is not touching parentheses
                     if( splitsTrim[counter].contains(" (") )
                        name = splitsTrim[--counter];
                     
                     //method name is touching parentheses
                     else
                        name = splitsTrim[counter].substring(0, splitsTrim[counter].length() - 1);
                  }
               
               //Check variable names
               } else if( !inComment && inClass && inMethod &&
                          splitsTrim[ splitsTrim.length - 1].endsWith(";") ) {
               
                  //check final variable status
                  if( line.contains("final") )
                     isFinalVar = true;
               
                  //no do while loops
                  if( !line.contains("(") && !line.contains(")") ) {
                     
                     //uninstantiated var -- standalone semicolon
                     if( splitsTrim[ splitsTrim.length - 1].equals(";") )
                        name = splitsTrim[ splitsTrim.length - 2 ];
                     
                     //uninstantiated var -- touching semicolon
                     else if( !line.contains("=") )
                        name = splitsTrim[ splitsTrim.length - 1 ].substring(0, splitsTrim[ splitsTrim.length - 1 ].length() - 1);
                     
                     //assignment standalone
                     else if( line.contains(" =") || line.contains(" +=") || line.contains(" -=") ||
                                line.contains(" *=") || line.contains(" /=") || line.contains(" %= ") ) {
                        while( !splitsTrim[++counter].contains("=") ){}
                        
                        name = splitsTrim[--counter];
                     
                     //touching assignment
                     } else {
                     
                        while( !splitsTrim[counter++].contains("=") ){} counter--;
                        
                        name = splitsTrim[counter];
                        name = name.substring(0, name.indexOf('='));
                        
                     }
                  
                  //check globals sans modifiers
                  } else if( inClass && !inMethod && splitsTrim[ splitsTrim.length - 1].endsWith(";") ) {
                     
                     while( !splitsTrim[++counter].contains("=") && !splitsTrim[counter].endsWith(";") ){}
                     
                     //name is before equals sign or if there is a standalone semicolon (meaning an uninstantiated global)
                     if( splitsTrim[counter].contains("=") || splitsTrim[counter].equals(";") )
                        name = splitsTrim[--counter];
                     
                     //name is touching semicolon (uninstantiated global)
                     else if( splitsTrim[counter].endsWith(";") )
                        name = splitsTrim[counter].substring(0, splitsTrim[counter].length() - 1);
                  }
               
               } //end class, globals, methods, variables if statements

               //remove -- or ++
               if( name.endsWith("--") || name.startsWith("--") )
                  name = name.replaceAll("--", "");
               if( name.endsWith("++") || name.startsWith("++") )
                  name = name.replaceAll("\\+\\+", "");
               if( name.endsWith("(") )
                  name = name.replaceAll("\\(", "");
                            
               //@@DEBUG
               if( !name.equals("") )
                  SOP("\nname is: " + name + " -- " );
               
               //no empty Strings
               if( !inComment && name.length() > 0 ) {
               
                  //check name is longer than 1 character
                  if( ( line.contains(" for(") || line.contains(" for (") ) && line.contains(")") ) {
                     String parenString = line.substring( line.charAt('('), line.charAt(')') );
                     String temp1 = name + ";";
                     String temp2 = name + " ;";
                     if( parenString.contains( temp1 ) || parenString.contains( temp2 ) )
                        usedInLoop = true;
                  }
                  
                  if( name.length() < 2 && !usedInLoop ) {
                     nameErrors++;
                     if( hasError ) SOP(", ");
                     hasError = true;
                     SOP("length error"); //@@DEBUG
                  }
                  
                  //check name first letter
                  if( !Character.isLetter( name.charAt(0) ) ) {
                     nameErrors++;
                     if( hasError ) SOP(", ");
                     hasError = true;
                     SOP("first letter 1 error"); //@@DEBUG
                  } else {
                     if( Character.isLowerCase( name.charAt(0) ) && isClassName ) {
                        nameErrors++;
                        if( hasError ) SOP(", ");
                        hasError = true;
                        SOP("first letter 2 error"); //@@DEBUG
                     } else if( Character.isUpperCase( name.charAt(0) ) && !isClassName && !isFinalVar ) {
                        nameErrors++;
                        if( hasError ) SOP(", ");
                        hasError = true;
                        SOP("first letter 3 error"); //@@DEBUG
                     }
                  }
                  
                  //check for illegal characters
                  Pattern regex = Pattern.compile("[&+,:;=\\\\?@#|/'<>.^*()%!-]");
                  if( regex.matcher( name ).find() ) {
                     nameErrors++;
                     if( hasError ) SOP(", ");
                     hasError = true;
                     SOP("illegal char error"); //@@DEBUG
                  }
                  
                  //check for restricted words
                  for( int j = 0; j < restrictedWords.length; j++ ) {
                     if( name.equals( restrictedWords[j] ) && !lineTrim.equals("break;") ) {
                        nameErrors++;
                        if( hasError ) SOP(", ");
                        hasError = true;
                        SOP("restricted word error"); //@@DEBUG
                     }
                  }
                  
                  //check for too long names
                  if( name.length() > MAX_VAR_NAME_LENGTH ) {
                     nameErrors++;
                     if( hasError ) SOP(", ");
                     hasError = true;
                     SOP("too long name error"); //@@DEBUG
                  }
                  
                  //check for improperly capitalized final variables
                  if( isFinalVar ) {
                     boolean hasUnderscore = false;
                     boolean hasLowerCase = false;
                     for( int j = 0; j < name.length(); j++ ) {
                        if( name.charAt(j) == '_' )
                           hasUnderscore = true;
                        if( Character.isLowerCase( name.charAt(j) ) )
                           hasLowerCase = true;
                     }
                     
                     if( !hasUnderscore || hasLowerCase ) {
                        nameErrors++;
                        if( hasError ) SOP(", ");
                        hasError = true;
                        SOP("final var error"); //@@DEBUG
                     }
                  }
                  
                  //check for capitalized letters of multiple word names
                  //@@SEE Dictionary.java
                  Dictionary dct = new Dictionary( false );
                  boolean hasCapitals = false;
                  if( !dct.isWord( name ) && !isClassName && Character.isLetter( name.charAt(0) ) ) {
                     String subName = name.substring( 1, name.length() );
                     for( int j = 0; j < subName.length(); j++ ) {
                        if( Character.isUpperCase( subName.charAt(j) ) ) {
                           hasCapitals = true;
                           break;
                        }
                     }
                     if( !hasCapitals && name.length() > 1 ) {
                        nameErrors++;
                        if( hasError ) SOP(", ");
                        hasError = true;
                        SOP("capitalization error"); //@@DEBUG
                     }
                  }
               
               } //end empty name check
               
            } //end non-empty line check
            
            //Fix comment state
            if( inSingleComment ) {
               inSingleComment = false;
               inComment = false;
            }
            
         } //end line scanner
         
         grades[i] += MAX_GRADE - nameErrors;
         
         //@@DEBUG
         SOPln("\nFile: " + javaFiles[i].getName() + " -- Name errors: " + nameErrors + "\n");
      }
      
      return grades;
   }

   /**
      Check files against expected outputs
      
      @param javaFiles The java files to be checked
      @return int[] The grades of each java file
      @see Runtime.getRuntime().exec( String command )
   */
   private static int[] checkOutputs( File[] javaFiles ) {
      int[] grades = checkNames( javaFiles );
      
      final String EXPECTED = EO_PROGRAM_LIST[ EO_PROGRAM_NUM ];
      
      for( int i = 0; i < javaFiles.length; i++ ) {
         //Grab output
         String output = "";
         try {
            Process p = Runtime.getRuntime().exec( "java " + javaFiles[i].getName() );
            BufferedReader reader = new BufferedReader( new InputStreamReader( p.getInputStream() ) );
            
            for( String line = reader.readLine(); line != null; line = reader.readLine() ) {
               output += line + "\n";
            }
            
         } catch( IOException e ) {
            SOPln("Error in reading output of file.");
         }
         
         //Process output
         SOPln( "\n" + javaFiles[i].getName() + "'s " + "output is: \n" + output );
         SOPln( "Expected output: \n" + EXPECTED + "\n" );
         
         String[] resultsList = output.split("\\n");
         String[] expectedList = EXPECTED.split("\\n");
         int outputDifCounter = 0;
         
         for( int j = 0; j < resultsList.length && j < expectedList.length; j++ )
            if( !resultsList[j].equals( expectedList[j] ) )
               outputDifCounter++;

         //@@DEBUG
         SOPln( javaFiles[i].getName() + " has " + outputDifCounter +
                " output differences from what was expected." );

         grades[i] += MAX_GRADE - outputDifCounter*PENALTY;
   
      }
      
      return grades;
   }
   
   /**
      Checks the total outputs received by running the program agains the total
      minimal expected number of outputs
      
      @param javaFiles The java files to be checked
      @param totalMinimalOutputs The minimum number of outputs that is expected
      @return int[] The grades for the java files
      @see checkNames( File[] javaFiles)
      @see TOTAL_MINIMAL_OUTPUTS
      @see Runtime.getRunTime()
      @see Runtime.exec()
      @see Process.getInputStream()
      @see BufferedReader.readLine()
   */
   private static int[] checkOutputs( File[] javaFiles, int totalMinimalOutputs ) {
      int[] grades = checkNames( javaFiles );
      
      int totalOutputs = 0;
      
      for( int i = 0; i < javaFiles.length; i++ ) {
         //Grab output
         String output = "";
         try {
            Process p = Runtime.getRuntime().exec( "java " + javaFiles[i].getName() );
            BufferedReader reader = new BufferedReader( new InputStreamReader( p.getInputStream() ) );
            
            for( String line = reader.readLine(); line != null; line = reader.readLine() ) {
               totalOutputs++;
            }
            
         } catch( IOException e ) {
            SOPln("Error in reading output of file.");
         }
      
         int differenceInTotalOutputs = totalMinimalOutputs - totalOutputs;
      
         if( differenceInTotalOutputs < 0 ) {
            differenceInTotalOutputs = 0;
         }
         
         if( differenceInTotalOutputs > 0 )
            SOPln("Output Check: A minimum of " + totalMinimalOutputs + " was expected, but only " + totalOutputs + " were found.");
         
         grades[i] += MAX_GRADE - differenceInTotalOutputs*PENALTY;
      
      }
      
      return grades;
   }
   
   /**
      Check files for expected answers in the comments
      
      @param javaFiles The java files to check
      @return int[] The grades for each java file
   */
   private static int[] checkAnswers( File[] javaFiles ) {
      int[] grades = checkNames( javaFiles );
      
      Scanner teacherCheck = new Scanner( System.in );
      int missingAnswers = 0;
      int deductionsFromTeacher = 0;
      
      //check each java file
      for( int i = 0; i < javaFiles.length; i++ ) {
         Scanner scanner = null;
         
         try {
            scanner = new Scanner( javaFiles[i] );
         } catch( IOException e ) {
            e.printStackTrace();
         }
         
         String listOfAnswers = "";
         int counter = 1;
         
         //Record and check lines with answers designed by ANSWER_SPOT
         while( scanner.hasNextLine() ) {
            String line = scanner.nextLine();
            line = line.trim();
            
            if( line.contains( ANSWER_SPOT ) ) {
               listOfAnswers += counter++ + ": " + line + "\n";
               
               if( line.equals( ANSWER_SPOT ) )
                  missingAnswers++;
            }
         }
         
         SOPln( "\n" + javaFiles[i].getName() + " answers: \n" + listOfAnswers +
                "\nHow many deductions will you give this set of answers?" );
         deductionsFromTeacher = teacherCheck.nextInt(); teacherCheck.nextLine();
         
         grades[i] += MAX_GRADE - missingAnswers*PENALTY - deductionsFromTeacher*PENALTY;
      
      }
      
      return grades;
   }

   /**
      Runs unit tests for java files. Outputs that do not match test expectations result in
      lowered grades for this section.
      
      @param javaFiles The student files to run unit tests with
      @return int[] The list of grades that are associated with each student java file
   */
   public static int[] checkUnitTests( File[] javaFiles ) {
      int[] grades = checkCompile( javaFiles );

      //check each java file
      for( int i = 0; i < javaFiles.length; i++ ) {
         UnitTester tester = new UnitTester( CURRENT_UNIT_TEST_FILE );
         int totalFailedUnitTests = tester.runUnitTests( javaFiles[i], CURRENT_UNIT_TEST_FILE );
         
         SOPln( "\n" + javaFiles[i].getName() + " total failed unit tests: " + totalFailedUnitTests + "\n");
      
         grades[i] += MAX_GRADE*WEIGHT - totalFailedUnitTests*PENALTY*WEIGHT;
      }
      
      return grades;
   }

   /**
      Check if java file contains the necessary components
      
      @param javaFiles The files to check
      @return int[] The list of grades assigned to each program
      @see checkUnitTests( File[] javaFiles )
      @see getClassFromFile( String fullClassName )
   */
   public static int[] checkContains( File[] javaFiles ) {
      int[] grades = checkUnitTests( javaFiles );
      
      final int MIN_METHODS = 7;
      final int MIN_LOOPS = 2;
      final String[] REQUIRED_LOOPS = {"for", "while", "do"};
      final String[] REQUIRED_COMPS = {"try", "catch", "//", "/*"};
      
      int totalMissing = 0;
      
      for( int i = 0; i < javaFiles.length; i++ ) {
         //Find total methods
         int methodsFound = 0;
         try {
            methodsFound = getClassFromFile( javaFiles[i].getName().replace(".java", "") ).getMethods().length;
         } catch( Exception e ) {
            e.printStackTrace();
         }
         
         if( methodsFound < MIN_METHODS ) {
            totalMissing += MIN_METHODS - methodsFound;
            SOPln( "Missing " + ( MIN_METHODS - methodsFound ) + " methods." );
         }
         
         //Find other components
         Scanner scanner = null;
         
         try{
            scanner = new Scanner( javaFiles[i] );
         } catch( IOException e ) {
            e.printStackTrace();
         }
         
         int[] loopsFoundList = new int[ REQUIRED_LOOPS.length ];
         int[] compsFoundList = new int[ REQUIRED_COMPS.length ];
         
         while( scanner.hasNextLine() ) {
            
            String line = scanner.nextLine();
            line = line.trim();
            
            //yes I am aware of the flaws in this search algorithm
            for( int j = 0; j < REQUIRED_LOOPS.length; j++ )
               if( line.contains( REQUIRED_LOOPS[j] ) ) loopsFoundList[j]++;
            
            for( int j = 0; j < REQUIRED_COMPS.length; j++ )
               if( line.contains( REQUIRED_COMPS[j] ) ) compsFoundList[j]++;
            
         }
         
         int loopsFound = 0;
         for( int j = 0; j < REQUIRED_LOOPS.length; j++ )
            if( loopsFoundList[j] > 0 ) loopsFound++;
         
         int compsFound = 0;
         for( int j = 0; j < REQUIRED_COMPS.length; j++ )
            if( compsFoundList[j] > 0 ) compsFound++;
         
         if( loopsFound < MIN_LOOPS )             totalMissing += MIN_LOOPS - loopsFound;
         if( compsFound < REQUIRED_COMPS.length ) totalMissing += REQUIRED_COMPS.length - compsFound;
         
         grades[i] += MAX_GRADE - totalMissing*WEIGHT;
         
         SOPln( "\n" + javaFiles[i].getName() + " total missing components and/or methods: " + totalMissing + "\n");
      }
      
      return grades;
   }
   
   /**
      Gets the class of the file from the given file name
      
      @param fullClassName The name of the file
      @return Class The class of the file
      @see URLClassLoader.loadClass( String fileName )
      @see checkContains( File[] javaFiles )
   */
   private static Class getClassFromFile( String fullClassName ) throws Exception {
      URLClassLoader loader = new URLClassLoader(new URL[] {
               new URL("file://" + System.getProperty("user.dir"))
      });
      return loader.loadClass( fullClassName );
   }

   /*
      @@LATER
      private static int[] checkEfficiency( File[] javaFiles ) {
         int[] grades = checkNames( javaFiles );
         
         return grades;
      }
      
      private static int[] checkFlags( File[] javaFiles ) {
         int[] grades = checkEfficiency( javaFiles );
         
         return grades;
      }
      
      private static int[] checkTests( File[] javaFiles ) {
         int[] grades = checkFlags( javaFiles );
         
         return grades;
      }
   */
   
   /**
      Determines whether there is an open left parenthesis after a given line
      
      @param line The line to check
      @return boolean True if there are open parentheses, false otherwise
      @see totalOpenParentheses global variable
      @see checkFormatting( File[] javaFiles )
   */
   private static boolean hasOpenParentheses( String line ) {
      int newOpenParentheses = 0;
      int newClosedParentheses = 0;
      char[] charList = line.toCharArray();
      for( int i = 0; i < charList.length; i++ ) {
         if( charList[i] == '(' )
            newOpenParentheses++;
         else if( charList[i] == ')' )
            newClosedParentheses++;
      }
      
      totalOpenParentheses += newOpenParentheses - newClosedParentheses;
      
      if( totalOpenParentheses < 0 )
         SOPln("Error: Negative number for open parentheses");
      
      if( totalOpenParentheses == 0 )
         return false;
      else
         return true;
   }
   
   /**
      Returns the number of characters contained in the String
      
      @param str The String to count
      @return int The number of characters in the String
   */
   private static int numChars( String str ) {
      return str.length();
   }
   
   /**
      Make the grades proportionate to a system out of 100
      
      @param grades The list of grades for the java files
      @return int[] The proportionalized list of grades
   */
   private static int[] proportionalize( int[] grades ) {
      for( int i = 0; i < grades.length; i++ ) {
         grades[i] = (int)(grades[i] / (STAGE /*-1 is @@TEMP since checkNames not being checked*/ + WEIGHT - 1) );
      }
      
      return grades;
   }
   
   /**
      Print grades of java files that have been checked and graded
      
      @param grades The list of grades that correspond in parallel with the java files
      @param javaFiles The java files in the current directory (these are the
                       students' java files that they turned in)
   */
   private static void printGrades( int[] grades, File[] javaFiles ) {
      //Deduction for improper java file name (does not contain student name and/or incorrect caps)
      int FORMAT_DEDUCTION = 5;
   
      for( int i = 0; i < javaFiles.length; i++ ) {
         String name = javaFiles[i].toString().split("\\.")[1].replace("\\", "");
         
         name = removeProgramName( name );
         if( !name.contains("NO_NAME") )
            FORMAT_DEDUCTION = 0;
         
         int revisedGrade = grades[i] - FORMAT_DEDUCTION;
         
         SOPln( name + ": " + revisedGrade );
      }
   }
   
   /**
      Finds just the name of the student in the java file name. If the file name does not have the student name,
      "NO_NAME" is returned, which will result in a deduction of points
      
      @param name The name of the java file, which should not have the file directory characters or
                  or the file extension
      @return String The name of the student
   */
   private static String removeProgramName( String name ) {
      int index = name.length();
   
      if( name.length() > 0 ) {
         for( int i = 1; i < name.length(); i++ ) {
            if( !Character.isLowerCase(name.charAt(i)) ) {
               index = i;
            }
         }
         
         if( index == name.length() ) {
            SOPln("\nNo name on file or incorrect capitalization for " + name + ".");
            name = name + "NO_NAME";
            return name;
         }
      } else {
         SOPln("\nName on file error." + name);
         name = "NO_NAME";
         return name;
      }
      
      name = name.substring( 0, index );
      
      return name;
   }
   
   /**
      Returns the list of java files in the current directory
      
      @return File[] The list of java files in the current directory
   */
   private static File[] getFiles() {
      File dir = new File(".");
      File[] filesList = dir.listFiles();
      ArrayList<File> newFileList = new ArrayList<File>();
      
      for( File file : filesList ) {
          if( file.isFile() ) {
              if( file.toString().contains("java") && !file.toString().contains("Checker.java") &&
                  !file.toString().contains("Dictionary.java") &&
                  !file.toString().contains("Runner.java") && !file.toString().contains("LanguageSpecs.java") &&
                  !file.toString().contains("UnitTester.java") ) {
                  newFileList.add( file );
              }
          }
      }
      
      File[] newFileListArray = new File[ newFileList.size() ];
      //convert to array since toArray() does not preserve order
      for( int i = 0; i < newFileList.size(); i++ ) {
         newFileListArray[i] = newFileList.get(i);
      }
      
      return newFileListArray;
   }
   
   /**
      Faster than typing it all out
      
      @param message The String to print
   */
   private static void SOPln( String message ) {
      System.out.println( message );
   }
   
   /**
      Faster than typing it all out
      
      @param message The String to print
   */
   private static void SOP( String message ) {
      System.out.print( message );
   }
   
   /*
      // Load and instantiate compiled class.
      URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] { root.toURI().toURL() });
      Class<?> cls = Class.forName("test.Test", true, classLoader); // Should print "hello".
      Object instance = cls.newInstance(); // Should print "world".
      System.out.println(instance); // Should print "test.Test@hashcode".
   */
}