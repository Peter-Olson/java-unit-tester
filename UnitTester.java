import java.io.File;
import java.util.Scanner;
import java.lang.ClassNotFoundException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.lang.InstantiationException;
import java.lang.IllegalAccessException;
import java.lang.IllegalArgumentException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Arrays;
import java.util.stream.*;

/** UnitTester.java

   This class will complete unit tests for student functions and programs
   
   @author Peter Olson
   @version 10/01/21
*/

public class UnitTester {

   /* The name of the file containing the unit test data
      
      FORMAT
      #keywords,go,here,delimited,by,commas#After the second hashtag, the description of the method being tested goes here. If there are more than one unit test, they are separated by the pipe symbol "|".
      1 2 3 & true | 1 2 3 & false         <-- do not include the arrow or this description. The input values are on the left hand side of the data and are delimited by spaces. The output is on the
      4 0 -2 & false | 5 6 -2 & true       <-- right-hand side of the '&' symbol and denotes the return value. Currently, any primitive data type can be accepted as inputs or outputs
      15 5 1 & true                        <-- if a method predominately allows for two different outputs for a given set of data, it is still okay to have just one unit test on a line
      #keywords,go,here,for,second,method#This begins the second method. Note that the keywords help identify which method to search for.
      any kind of primitive will do & 10 | inputs strings and outputs are ints & 5     <-- an example where the total number of inputs are 6 strings, and the output is an integer
      ...
      
      This class does not take unit tests for the main function. The text file data for unit tests may include
      any number of units tests and any number of methods to be tested that can be found in the java file
   */
   public final String CURRENT_UNIT_TEST_FILE;
   
   //didn't actually need to use this mapping
   private static final Map<Class<?>, Class<?>> primitiveToWrapper = Map.of(
      void.class, Void.class, 
      boolean.class, Boolean.class, 
      byte.class, Byte.class, 
      char.class, Character.class, 
      short.class, Short.class, 
      int.class, Integer.class, 
      long.class, Long.class, 
      float.class, Float.class, 
      double.class, Double.class );

   public enum DataType {
      BYTE, SHORT, INT, LONG, FLOAT, DOUBLE, CHAR, STRING, BOOLEAN, NULL
   }

   /**
      Default constructor
      
      @param currentUnitTestFile The test file that is used to run the unit test
      @see CURRENT_UNIT_TEST_FILE
   */
   public UnitTester( String currentUnitTestFile ) {
   
      CURRENT_UNIT_TEST_FILE = currentUnitTestFile;
   
   }
   
   /**
      Runs the unit tests with the CURRENT_UNIT_TEST_FILE information
      with the given student java file
      
      @param javaFile The java file to be read
      @param unitTestFile The text document of unit tests to read
      @return int The number of errors found when running unit tests
      @see CURRENT_UNIT_TEST_FILE in Checker.java
      @see Class.forName( String className )
      @see Class.getDeclaredMethods()
      @see sortMethodList( Method[] methodList, Scanner fileScanner )
      @see runUnitTests( Method[] sortedMethodList, Scanner fileScanner )
   */
   public int runUnitTests( File javaFile, String unitTestFile ) {
      Class className = null;
      
      //Get Class object from java file
      try {
         className = Class.forName( javaFile.getName().replace(".java", "") );
      } catch( ClassNotFoundException e ) {
         e.printStackTrace();
      }
      
      //Create file scanner.
      Scanner fileScanner = null;
      try {
         fileScanner = new Scanner( new File( CURRENT_UNIT_TEST_FILE ) );
      } catch( FileNotFoundException e ) {
         e.printStackTrace();
      }
      
      //Get list of methods in class
      Method[] methodList = className.getDeclaredMethods();
      
      //Sort methods into correct order as identified by the CURRENT_UNIT_TEST_FILE
      Method[] sortedMethodList = sortMethodList( methodList, fileScanner );
      
      //@@DEBUG - print method list
      /*
      for( int i = 0; i < sortedMethodList.length; i++ ) {
         SOPln("Method #" + (i+1) + ": " + sortedMethodList[i].getName() );
      }
      */
      
      //Renew the file scannner
       try {
         fileScanner = new Scanner( new File( CURRENT_UNIT_TEST_FILE ) );
      } catch( FileNotFoundException e ) {
         e.printStackTrace();
      }
      
      //run the unit tests for each method
      int totalUnitTestErrors = runUnitTests( className, sortedMethodList, fileScanner );
      
      return totalUnitTestErrors;
   }

   /**
      Runs the unit tests stored in the CURRENT_UNIT_TEST_FILE
      
      This method scans the text document and converts String input data,
      which is used to invoke the student java file methods and produce an output
      
      The outputs are then compared, and if they are different than the expected
      output that appears in the file, it is counted as an error. The total number
      of errors across all unit tests for all relevant methods is returned
      
      @param className
      @param sortedMethodList The list of methods, which have been sorted into the proper order
                              (matching with the text file order)
      @param fileScanner The scanner object which is scanning the CURRENT_UNIT_TEST_FILE text file
      @return int The total number of unit test error which have been incurred across the
                  relevant methods being tested
      @see Class.getParameterTypes()
      @see Method.getName()
      @see Method.invoke( Object obj, Object ... args )
      @see Exception.printStackTrace();
      @see Method.getReturnType()
      @see Arrays.toString(...)
      @see Object.getClass()
   */
   private int runUnitTests( Class className, Method[] sortedMethodList, Scanner fileScanner ) {
      
      Object classObj = null;
      try {
         classObj = className.newInstance();
      } catch( InstantiationException e ) {
         e.printStackTrace();
      } catch( IllegalAccessException e ) {
         e.printStackTrace();
      }
      
      int totalUnitTestErrors = 0;
      int methodCounter = -1; //keeps track of the method the program is on
      int testNumber = 1;
      
      while( fileScanner.hasNextLine() ) {
         String line = fileScanner.nextLine();
         //First # will allow index to start at zero
         if( line.contains("#") ) {
            methodCounter++;
            continue;
         }
         
         boolean testPassed = false; //used if the first test passed as to not penalize the second test if it does not pass
         String[] unitTestDataList = line.split("\\|");
         int indexAdjustment = 0;
         //run all result options
         for( int i = 0; i < unitTestDataList.length; i++ ) {
            String[] unitTest = unitTestDataList[i].split("&");
            //get parameter types
            Class<?>[] classList = sortedMethodList[ methodCounter ].getParameterTypes();
            
            //get String inputs and outputs
            String[] inputs = unitTest[0].trim().split(" ");
            String output = unitTest[1].trim();
            Object[] expectedResult = new Object[inputs.length];
            
            //determine type of inputs and outputs using Objects to cast
            Object[] parameterData = new Object[ inputs.length ];
            DataType listDataType = DataType.NULL;
            
            //catch mismatch errors for comparing method parameter count to file parameter count
            if( classList.length != inputs.length ) {
               SOPln("Method parameters and file parameters do not match.");
               SOPln("Line: " + line);
               SOPln("Method: " + sortedMethodList[ methodCounter ].getName() );
            }
            
            //Match input data to correct object type
            for( int j = 0; j < classList.length; j++ ) {
               if     ( classList[j].getName().equals("int")               )   parameterData[j] = Integer.valueOf  ( inputs[j]           );
               else if( classList[j].getName().equals("double")            )   parameterData[j] = Double.valueOf   ( inputs[j]           );
               else if( classList[j].getName().equals("char")              )   parameterData[j] = Character.valueOf( inputs[j].charAt(0) );
               else if( classList[j].getName().equals("java.lang.String")  )   parameterData[j] =                    inputs[j]            ;
               else if( classList[j].getName().equals("boolean")           )   parameterData[j] = Boolean.valueOf  ( inputs[j]           );
               else if( classList[j].getName().equals("float")             )   parameterData[j] = Float.valueOf    ( inputs[j]           );
               else if( classList[j].getName().equals("long")              )   parameterData[j] = Long.valueOf     ( inputs[j]           );
               else if( classList[j].getName().equals("short")             )   parameterData[j] = Short.valueOf    ( inputs[j]           );
               else if( classList[j].getName().equals("byte")              )   parameterData[j] = Byte.valueOf     ( inputs[j]           );
               else if( classList[j].isArray() ) {
                  String arrayName = classList[j].getComponentType().getName(); //Data type of list, eg. int
                  //System.out.println("Name is: " + arrayName);
                  if( arrayName.equals("int") ) {
                     Integer[] parameterList = convertObjToIntList( parseList( inputs[j], "int" ) );
                     parameterData[j] = intList( parameterList );
                  } else if( arrayName.equals("double") ) {
                     Double[] parameterList = convertObjToDoubleList( parseList( inputs[j], "double" ) );
                     parameterData[j] = doubleList( parameterList );
                  } else if( arrayName.equals("char") ) {
                     Character[] parameterList = convertObjToCharList( parseList( inputs[j], "char" ) );
                     parameterData[j] = charList( parameterList );
                  } else if( arrayName.equals("java.lang.String") ) {
                     String[] parameterList = convertObjToStringList( parseList( inputs[j], "java.lang.String" ) );
                     parameterData[j] = parameterList;
                  } else if( arrayName.equals("boolean") ) {
                     Boolean[] parameterList = convertObjToBooleanList( parseList( inputs[j], "boolean" ) );
                     parameterData[j] = booleanList( parameterList );
                  } else if( arrayName.equals("float") ) {
                     Float[] parameterList = convertObjToFloatList( parseList( inputs[j], "float" ) );
                     parameterData[j] = floatList( parameterList );
                  } else if( arrayName.equals("long") ) {
                     Long[] parameterList = convertObjToLongList( parseList( inputs[j], "long" ) );
                     parameterData[j] = longList( parameterList );
                  } else if( arrayName.equals("short") ) {
                     Short[] parameterList = convertObjToShortList( parseList( inputs[j], "short" ) );
                     parameterData[j] = shortList( parameterList );
                  } else if( arrayName.equals("byte") ) {
                     Byte[] parameterList = convertObjToByteList( parseList( inputs[j], "byte" ) );
                     parameterData[j] = byteList( parameterList );
                  //2D Arrays
                  } else if( arrayName.contains("[") ) {
                     //SOPln("data type name: " + arrayName ); //Should print something of the form "[D", where the [ represents an array and D is for Double
                     String dataName = classList[j].getComponentType().getComponentType().getName();
                     //SOPln("data name: " + dataName); //Should print the primitive data type
                     listDataType = parse2DList( parameterData, j, inputs[j], dataName );
                  }
               }
            }
            
            //get return type of method
            Class<?> returnClass = sortedMethodList[ methodCounter ].getReturnType();
            
            //Use different return value structure in order to trick compiler for data parsing and conversion
            boolean returnTypeIs2DList = false;
            Object[][] returnValue2D = new Object[parameterData.length][parameterData.length];
            if( returnClass.getName().contains("[[") ) {
               returnTypeIs2DList = true;
            }
            
            //get return value
            Object[] returnValue = new Object[parameterData.length];
            try {
               if( returnTypeIs2DList )
                  returnValue2D[0][0] = sortedMethodList[ methodCounter ].invoke( classObj, parameterData );
               else
                  returnValue[0] = sortedMethodList[ methodCounter ].invoke( classObj, parameterData );
            } catch( IllegalAccessException e ) {
               e.printStackTrace();
            } catch( IllegalArgumentException e ) {
               e.printStackTrace();
            } catch( InvocationTargetException e ) {
               e.printStackTrace();
               //SOPln("Column entry 0-6 detected. Trying second test..."); //@@TEMP -- This line and next only needed for Project 3 ConnectFour
               //continue;
            }
            
            //For 2D arrays, instead of trying to compare their data literally, just compare their String forms instead
            if( returnTypeIs2DList ) {
               returnValue2D[0][0] = convertToString( returnValue2D[0][0], returnClass.getComponentType().getComponentType().getName() );
               returnValue2D[0][0] = format2DList( (String)returnValue2D[0][0] );
            }
            
            //Match output data to correct object type
            if     ( returnClass.getName().equals("int")                )   expectedResult[0] = Integer.valueOf  ( output           );
            else if( returnClass.getName().equals("double")             )   expectedResult[0] = Double.valueOf   ( output           );
            else if( returnClass.getName().equals("char")               )   expectedResult[0] = Character.valueOf( output.charAt(0) );
            else if( returnClass.getName().equals("java.lang.String")   )   expectedResult[0] =                    output            ;
            else if( returnClass.getName().equals("boolean")            )   expectedResult[0] = Boolean.valueOf  ( output           );
            else if( returnClass.getName().equals("float")              )   expectedResult[0] = Float.valueOf    ( output           );
            else if( returnClass.getName().equals("long")               )   expectedResult[0] = Long.valueOf     ( output           );
            else if( returnClass.getName().equals("short")              )   expectedResult[0] = Short.valueOf    ( output           );
            else if( returnClass.getName().equals("byte")               )   expectedResult[0] = Byte.valueOf     ( output           );
            else if( returnClass.isArray() ) {
               String arrayName = returnClass.getComponentType().getName(); //Data type of list, eg. int
               //System.out.println("Name is: " + arrayName);
               if(        arrayName.equals("int") ) {
                  Integer[] parameterList = convertObjToIntList( parseList( output, "int" ) );
                  expectedResult[0] = parameterList;
                  listDataType = DataType.INT;
               } else if( arrayName.equals("double") ) {
                  Double[] parameterList = convertObjToDoubleList( parseList( output, "double" ) );
                  expectedResult[0] = parameterList;
                  listDataType = DataType.DOUBLE;
               } else if( arrayName.equals("char") ) {
                  Character[] parameterList = convertObjToCharList( parseList( output, "char" ) );
                  expectedResult[0] = parameterList;
                  listDataType = DataType.CHAR;
               } else if( arrayName.equals("java.lang.String") ) {
                  String[] parameterList = convertObjToStringList( parseList( output, "java.lang.String" ) );
                  expectedResult[0] = parameterList;
                  listDataType = DataType.STRING;
               } else if( arrayName.equals("boolean") ) {
                  Boolean[] parameterList = convertObjToBooleanList( parseList( output, "boolean" ) );
                  expectedResult[0] = parameterList;
                  listDataType = DataType.BOOLEAN;
               } else if( arrayName.equals("float") ) {
                  Float[] parameterList = convertObjToFloatList( parseList( output, "float" ) );
                  expectedResult[0] = parameterList;
                  listDataType = DataType.FLOAT;
               } else if( arrayName.equals("long") ) {
                  Long[] parameterList = convertObjToLongList( parseList( output, "long" ) );
                  expectedResult[0] = parameterList;
                  listDataType = DataType.LONG;
               } else if( arrayName.equals("short") ) {
                  Short[] parameterList = convertObjToShortList( parseList( output, "short" ) );
                  expectedResult[0] = parameterList;
                  listDataType = DataType.SHORT;
               } else if( arrayName.equals("byte") ) {
                  Byte[] parameterList = convertObjToByteList( parseList( output, "byte" ) );
                  expectedResult[0] = parameterList;
                  listDataType = DataType.BYTE;
               } else if( arrayName.contains("[") ) {
                  //2D lists are saved as Strings
                  expectedResult[0] = output;
                  listDataType = DataType.STRING;
               }
            }
            
            //Convert returnValue[0] back to Wrapper data type array
            if( !returnTypeIs2DList && expectedResult[0].getClass().isArray() )
               returnValue[0] = toWrapper( returnValue[0], listDataType );

            //Print out unit test results
            if( !returnTypeIs2DList && returnValue[0].getClass() == expectedResult[0].getClass() && !testPassed ) {
               if( returnValue[0].getClass().isArray() ) {
                  if( Arrays.equals( (Object[])returnValue[0], (Object[])expectedResult[0] ) ) {
                     SOPln("Test #" + testNumber + " Passed! Inputs: " + Arrays.toString( inputs ) + ", Output: " +
                           Arrays.toString( (Object[])returnValue[0] ) + ", Expected Output: " + output);
                     if( i == 0 )
                        testPassed = true;
                     testNumber++;
                             // Can't fail first optional test
                  } else if( (( i != 0 && unitTestDataList.length > 1 ) || ( i == 0 && unitTestDataList.length == 1 )) ) {
                     //Unequal lists
                     SOPln("Test #" + testNumber + " Failed! Inputs: " + Arrays.toString( inputs ) + ", Output: " +
                           Arrays.toString( (Object[])returnValue[0] ) + ", Expected Output: " + output);
                     testNumber++;

                     totalUnitTestErrors++;
                     testPassed = false;
                  }
               } else if( returnValue[0].equals( expectedResult[0] ) ) {
                  SOPln("Test #" + testNumber + " Passed! Inputs: " + Arrays.toString( inputs ) + ", Output: " +
                        String.valueOf( returnValue[0] ) + ", Expected Output: " + output);
                  if( i == 0 )
                     testPassed = true;
                  testNumber++;
               } else if( (( i != 0 && unitTestDataList.length > 1 ) || ( i == 0 && unitTestDataList.length == 1 )) ) {
                  SOPln("Test #" + testNumber + " Failed! Inputs: " + Arrays.toString( inputs ) + ", Output: " +
                     String.valueOf( returnValue[0] ) + ", Expected Output: " + output);
                  testNumber++;

                  totalUnitTestErrors++;
                  testPassed = false;
               }
            } else if( !returnTypeIs2DList && !testPassed && (( i != 0 && unitTestDataList.length > 1 ) ||
                                      ( i == 0 && unitTestDataList.length == 1 )) ) {
               if( returnValue.getClass().isArray() ) {
                  SOPln("Test #" + testNumber + " Failed! Inputs: " + Arrays.toString( inputs ) + ", Output: " +
                           Arrays.toString( (Object[])returnValue[0] ) + ", Expected Output: " + output);
                  testNumber++;
               } else {
                  SOPln("Test #" + testNumber + " Failed! Incompatible outputs. Inputs: " + Arrays.toString( inputs ) + ", Output: " +
                        String.valueOf( returnValue[0] ) + ", Expected Output: " + output);
                  testNumber++;
               }
               
               totalUnitTestErrors++;
               testPassed = false;
            }
            
            if( returnTypeIs2DList ) {
               if( returnValue2D[0][0].equals( expectedResult[0] ) ) {
                  SOPln("Test #" + testNumber + " Passed! Inputs: " + Arrays.toString( inputs ) + ", Output: " +
                        String.valueOf( returnValue2D[0][0] ) + ", Expected Output: " + output);
                  if( i == 0 )
                     testPassed = true;
                  testNumber++;
               } else if( (( i != 0 && unitTestDataList.length > 1 ) || ( i == 0 && unitTestDataList.length == 1 )) ) {
                  SOPln("Test #" + testNumber + " Failed! Inputs: " + Arrays.toString( inputs ) + ", Output: " +
                     String.valueOf( returnValue2D[0][0] ) + ", Expected Output: " + output);
                  testNumber++;

                  totalUnitTestErrors++;
                  testPassed = false;
               }
            }
            
            //Only run 1 test
            if( testPassed ) break;
         }//end for
      }
      
      return totalUnitTestErrors;
   }
   
   /**
      Format a 2D list that has been converted to a String. Specifically, convert decimals that are whole numbers
      to be whole numbers (remove .0s)
      
      @param string2DList The 2D list in String format
      @return String The formatted String
   */
   private String format2DList( String string2DList ) {
      if( string2DList.endsWith(".0") )
         string2DList = string2DList.substring(0, string2DList.length() - 2);
      
      string2DList = string2DList.replaceAll(".0,",",");
      string2DList = string2DList.replaceAll(".0\\$","\\$");
      
      return string2DList;
   }
   
   /**
      Convert a 2D array to its String one-liner representation
      
      @param returnValue An array of return values (only need 1), which stores Strings
      @param tempReturnValue An array of return values (only need 1), which stores a 2D array of Objects
      @return DataType The String representation of the 2D array
   */
   private String convertToString( Object returnValue, String dataType ) {
      String stringForm = "";
      String rowSeparator = "$";
      String colSeparator = ",";
      DataType dataEnum = DataType.NULL;
      
      Object[][] grid = toWrapper( returnValue, dataType );
      
      for( int row = 0; row < grid.length; row++ ) {
         for( int col = 0; col < grid[row].length; col++ ) {
            switch( dataType ) {
               case "byte":    stringForm += String.valueOf( ( (Byte)grid[row][col] ).byteValue() );       break;
               case "short":   stringForm += String.valueOf( ( (Short)grid[row][col] ).shortValue() );     break;
               case "int":     stringForm += String.valueOf( ( (Integer)grid[row][col] ).intValue() );     break;
               case "long":    stringForm += String.valueOf( ( (Long)grid[row][col] ).longValue() );       break;
               case "float":   stringForm += String.valueOf( ( (Float)grid[row][col] ).floatValue() );     break;
               case "double":  stringForm += String.valueOf( ( (Double)grid[row][col] ).doubleValue() );   break;
               case "char":    stringForm += String.valueOf( ( (Character)grid[row][col] ).charValue() );  break;
               case "boolean": stringForm += String.valueOf( ( (Boolean)grid[row][col] ).booleanValue() ); break;
               case "String":  stringForm += (String)grid[row][col]; break;
               default:        SOPln("Unrecognized Object in Object[][] grid."); break;
            }
            if( col != grid[row].length - 1 )
               stringForm += colSeparator;
         }
         if( row != grid.length - 1 )
            stringForm += rowSeparator;
      }
      
      return stringForm;
   }
   
   /**
      Parses the String input data representing a 2D list and converts it to the corresponding data type Wrapper class lists, before
      converting the wrapper class lists to primitive data type lists. This is done for each row of the 2D array before being set to the
      correct position in the parameterData array, which now contains a 2D array as one of its elements
      
      @param parameterData The array of parameter Objects
      @param parameterIndex The index of which Object to be set
      @param string2DList The String representing the 2D list, delimiter by $ for rows, and commas for elements in each row
      @param dataType A String representing the data type stored in the 2D array
      @return DataType [ENUM] The type of data being looked at
   */
   private DataType parse2DList( Object[] parameterData, int parameterIndex, String string2DList, String dataType ) {
      String[] rowList = string2DList.split("\\$");
      int rows = rowList.length;
      int cols = rowList[0].split(",").length;
      DataType dataEnum = DataType.NULL;
      //If you think is ugly, blame Java's lack of generics for primitive data types, not me --> can't stuff a primitive data type grid in a 2D object array
      if(        dataType.equals("byte") ) {
         dataEnum = DataType.BYTE;
         byte[][] byteGrid = new byte[rows][cols];
         for( int rep = 0; rep < rows; rep++ ) {
            Byte[] parameterList = convertObjToByteList( parseList( rowList[rep], "byte" ) );
            byteGrid[rep] = byteList( parameterList );
         }
         parameterData[parameterIndex] = byteGrid;
      } else if( dataType.equals("short") ) {
         dataEnum = DataType.SHORT;
         short[][] shortGrid = new short[rows][cols];
         for( int rep = 0; rep < rows; rep++ ) {
            Short[] parameterList = convertObjToShortList( parseList( rowList[rep], "short" ) );
            shortGrid[rep] = shortList( parameterList );
         }
         parameterData[parameterIndex] = shortGrid;
      } else if( dataType.equals("int") ) {
         dataEnum = DataType.INT;
         int[][] intGrid = new int[rows][cols];
         for( int rep = 0; rep < rows; rep++ ) {
            Integer[] parameterList = convertObjToIntList( parseList( rowList[rep], "int" ) );
            intGrid[rep] = intList( parameterList );
         }
         parameterData[parameterIndex] = intGrid;
      } else if( dataType.equals("long") ) {
         dataEnum = DataType.LONG;
         long[][] longGrid = new long[rows][cols];
         for( int rep = 0; rep < rows; rep++ ) {
            Long[] parameterList = convertObjToLongList( parseList( rowList[rep], "long" ) );
            longGrid[rep] = longList( parameterList );
         }
         parameterData[parameterIndex] = longGrid;
      } else if( dataType.equals("float") ) {
         dataEnum = DataType.FLOAT;
         float[][] floatGrid = new float[rows][cols];
         for( int rep = 0; rep < rows; rep++ ) {
            Float[] parameterList = convertObjToFloatList( parseList( rowList[rep], "float" ) );
            floatGrid[rep] = floatList( parameterList );
         }
         parameterData[parameterIndex] = floatGrid;
      } else if( dataType.equals("double") ) {
         dataEnum = DataType.DOUBLE;
         double[][] doubleGrid = new double[rows][cols];
         for( int rep = 0; rep < rows; rep++ ) {
            Double[] parameterList = convertObjToDoubleList( parseList( rowList[rep], "double" ) );
            doubleGrid[rep] = doubleList( parameterList );
         }
         parameterData[parameterIndex] = doubleGrid;
      } else if( dataType.equals("char") ) {
         dataEnum = DataType.CHAR;
         char[][] charGrid = new char[rows][cols];
         for( int rep = 0; rep < rows; rep++ ) {
            Character[] parameterList = convertObjToCharList( parseList( rowList[rep], "char" ) );
            charGrid[rep] = charList( parameterList );
         }
         parameterData[parameterIndex] = charGrid;
      } else if( dataType.equals("boolean") ) {
         dataEnum = DataType.BOOLEAN;
         boolean[][] booleanGrid = new boolean[rows][cols];
         for( int rep = 0; rep < rows; rep++ ) {
            Boolean[] parameterList = convertObjToBooleanList( parseList( rowList[rep], "boolean" ) );
            booleanGrid[rep] = booleanList( parameterList );
         }
         parameterData[parameterIndex] = booleanGrid;
      } else if( dataType.equals("java.lang.String") ) {
         dataEnum = DataType.STRING;
         String[][] stringGrid = new String[rows][cols];
         for( int rep = 0; rep < rows; rep++ ) {
            String[] parameterList = convertObjToStringList( parseList( rowList[rep], "java.lang.String" ) );
            stringGrid[rep] = parameterList;
         }
         parameterData[parameterIndex] = stringGrid;
      } else {
         SOPln("Error. Unsupported 2D array of Objects of unknown Class.");
      }
      
      return dataEnum;
   }
   
   /**
      Converts an array of primitives to their respective wrapper data types
      
      @param returnData A primitive array
      @param dataType [ENUM] The type of primitive data composing returnData
      @return Object[] A wrapper data type array
   */
   private Object[] toWrapper( Object returnData, DataType dataType ) {
      Object[] returnValue;
      switch( dataType ) {
         case BYTE:    returnValue = toByteWrapper(    (byte[])returnData );    break;
         case SHORT:   returnValue = toShortWrapper(   (short[])returnData );   break;
         case INT:     returnValue = toIntWrapper(     (int[])returnData );     break;
         case LONG:    returnValue = toLongWrapper(    (long[])returnData );    break;
         case FLOAT:   returnValue = toFloatWrapper(   (float[])returnData );   break;
         case DOUBLE:  returnValue = toDoubleWrapper(  (double[])returnData );  break;
         case CHAR:    returnValue = toCharWrapper(    (char[])returnData );    break;
         case BOOLEAN: returnValue = toBooleanWrapper( (boolean[])returnData ); break;
         default:      SOPln("Error. Invalid listDataType enum, for returnValue[0]");
                       returnValue = null; break;
      }

      return returnValue;
   }
   
   /**
      Converts an array of primitives to their respective wrapper data types
      
      @param returnData An object containing some kind of 2D array
      @param dataType The type of primitive data composing returnData
      @return Object[][] A wrapper data type 2D array
   */
   private Object[][] toWrapper( Object returnData, String dataType ) {
      Object[][] grid;
      switch( dataType ) {
         case "byte":             grid = toByteWrapper( (byte[][])returnData );       break;
         case "short":            grid = toShortWrapper( (short[][])returnData );     break;
         case "int":              grid = toIntWrapper( (int[][])returnData );         break;
         case "long":             grid = toLongWrapper( (long[][])returnData );       break;
         case "float":            grid = toFloatWrapper( (float[][])returnData );     break;
         case "double":           grid = toDoubleWrapper( (double[][])returnData );   break;
         case "char":             grid = toCharWrapper( (char[][])returnData );       break;
         case "boolean":          grid = toBooleanWrapper( (boolean[][])returnData ); break;
         case "java.lang.String": grid = (String[][])returnData; break;
         default: SOPln("Data type not found."); grid = null; break;
      }
      
      return grid;
   }
   
   /**
      Convert a byte array to a Byte array
      
      @param oldList The array of bytes
      @return Byte[] The array of Bytes
   */
   private Byte[] toByteWrapper( byte[] oldList ) {
		Byte[] newArray = new Byte[oldList.length];
		for( int rep = 0; rep < oldList.length; rep++ )
			newArray[rep] = Byte.valueOf( oldList[rep] );
		return newArray;
   }
   
   /**
      Convert a 2D byte array to a 2D Byte array
      
      @param oldList The 2D array of bytes
      @return Byte[][] The 2D array of Bytes
   */
   private Byte[][] toByteWrapper( byte[][] oldList ) {
		Byte[][] newArray = new Byte[oldList.length][oldList[0].length];
		for( int row = 0; row < oldList.length; row++ )
         for( int col = 0; col < oldList[row].length; col++ )
			   newArray[row][col] = Byte.valueOf( oldList[row][col] );
		return newArray;
   }
   
   /**
      Convert a short array to a Short array
      
      @param oldList The array of shorts
      @return Short[] The array of Shorts
   */
   private Short[] toShortWrapper( short[] oldList ) {
		Short[] newArray = new Short[oldList.length];
		for( int rep = 0; rep < oldList.length; rep++ )
			newArray[rep] = Short.valueOf( oldList[rep] );
		return newArray;
   }
   
   /**
      Convert a 2D short array to a 2D Short array
      
      @param oldList The 2D array of shorts
      @return Short[][] The 2D array of Shorts
   */
   private Short[][] toShortWrapper( short[][] oldList ) {
		Short[][] newArray = new Short[oldList.length][oldList[0].length];
		for( int row = 0; row < oldList.length; row++ )
         for( int col = 0; col < oldList[row].length; col++ )
			   newArray[row][col] = Short.valueOf( oldList[row][col] );
		return newArray;
   }
   
   /**
      Convert an int array to an Integer array
      
      @param oldList The array of ints
      @return Integer[] The array of Integers
   */
   private Integer[] toIntWrapper( int[] oldList ) {
		Integer[] newArray = new Integer[oldList.length];
		for( int rep = 0; rep < oldList.length; rep++ )
			newArray[rep] = Integer.valueOf( oldList[rep] );
		return newArray;
   }
   
   /**
      Convert a 2D int array to a 2D Integer array
      
      @param oldList The 2D array of ints
      @return Integer[][] The 2D array of Integers
   */
   private Integer[][] toIntWrapper( int[][] oldList ) {
		Integer[][] newArray = new Integer[oldList.length][oldList[0].length];
		for( int row = 0; row < oldList.length; row++ )
         for( int col = 0; col < oldList[row].length; col++ )
			   newArray[row][col] = Integer.valueOf( oldList[row][col] );
		return newArray;
   }
   
   /**
      Convert a long array to a Long array
      
      @param oldList The array of longs
      @return Long[] The array of Longs
   */
   private Long[] toLongWrapper( long[] oldList ) {
		Long[] newArray = new Long[oldList.length];
		for( int rep = 0; rep < oldList.length; rep++ )
			newArray[rep] = Long.valueOf( oldList[rep] );
		return newArray;
   }
   
   /**
      Convert a 2D long array to a 2D Long array
      
      @param oldList The 2D array of longs
      @return Long[][] The 2D array of Longs
   */
   private Long[][] toLongWrapper( long[][] oldList ) {
		Long[][] newArray = new Long[oldList.length][oldList[0].length];
		for( int row = 0; row < oldList.length; row++ )
         for( int col = 0; col < oldList[row].length; col++ )
			   newArray[row][col] = Long.valueOf( oldList[row][col] );
		return newArray;
   }
   
   /**
      Convert a float array to a Float array
      
      @param oldList The array of floats
      @return Float[] The array of Floats
   */
   private Float[] toFloatWrapper( float[] oldList ) {
		Float[] newArray = new Float[oldList.length];
		for( int rep = 0; rep < oldList.length; rep++ )
			newArray[rep] = Float.valueOf( oldList[rep] );
		return newArray;
   }
   
   /**
      Convert a 2D float array to a 2D Float array
      
      @param oldList The 2D array of floats
      @return Float[][] The 2D array of Floats
   */
   private Float[][] toFloatWrapper( float[][] oldList ) {
		Float[][] newArray = new Float[oldList.length][oldList[0].length];
		for( int row = 0; row < oldList.length; row++ )
         for( int col = 0; col < oldList[row].length; col++ )
			   newArray[row][col] = Float.valueOf( oldList[row][col] );
		return newArray;
   }
   
   /**
      Convert a double array to a Double array
      
      @param oldList The array of doubles
      @return Double[] The array of Doubles
   */
   private Double[] toDoubleWrapper( double[] oldList ) {
		Double[] newArray = new Double[oldList.length];
		for( int rep = 0; rep < oldList.length; rep++ )
			newArray[rep] = Double.valueOf( oldList[rep] );
		return newArray;
   }
   
   /**
      Convert a 2D double array to a 2D Double array
      
      @param oldList The 2D array of doubles
      @return Double[][] The 2D array of Doubles
   */
   private Double[][] toDoubleWrapper( double[][] oldList ) {
		Double[][] newArray = new Double[oldList.length][oldList[0].length];
		for( int row = 0; row < oldList.length; row++ )
         for( int col = 0; col < oldList[row].length; col++ )
			   newArray[row][col] = Double.valueOf( oldList[row][col] );
		return newArray;
   }
   
   /**
      Convert a char array to a Character array
      
      @param oldList The array of chars
      @return Character[] The array of Characters
   */
   private Character[] toCharWrapper( char[] oldList ) {
		Character[] newArray = new Character[oldList.length];
		for( int rep = 0; rep < oldList.length; rep++ )
			newArray[rep] = Character.valueOf( oldList[rep] );
		return newArray;
   }
   
   /**
      Convert a 2D char array to a 2D Character array
      
      @param oldList The 2D array of chars
      @return Character[][] The 2D array of Characters
   */
   private Character[][] toCharWrapper( char[][] oldList ) {
		Character[][] newArray = new Character[oldList.length][oldList[0].length];
		for( int row = 0; row < oldList.length; row++ )
         for( int col = 0; col < oldList[row].length; col++ )
			   newArray[row][col] = Character.valueOf( oldList[row][col] );
		return newArray;
   }
   
   /**
      Convert a boolean array to a Boolean array
      
      @param oldList The array of booleans
      @return Boolean[] The array of Booleans
   */
   private Boolean[] toBooleanWrapper( boolean[] oldList ) {
		Boolean[] newArray = new Boolean[oldList.length];
		for( int rep = 0; rep < oldList.length; rep++ )
			newArray[rep] = Boolean.valueOf( oldList[rep] );
		return newArray;
   }
   
   /**
      Convert a 2D boolean array to a 2D Boolean array
      
      @param oldList The 2D array of booleans
      @return Boolean[][] The 2D array of Booleans
   */
   private Boolean[][] toBooleanWrapper( boolean[][] oldList ) {
		Boolean[][] newArray = new Boolean[oldList.length][oldList[0].length];
		for( int row = 0; row < oldList.length; row++ )
         for( int col = 0; col < oldList[row].length; col++ )
			   newArray[row][col] = Boolean.valueOf( oldList[row][col] );
		return newArray;
   }

   /**
      Convert a Byte array to a byte array
      
      @param oldList The array of Bytes
      @return double[] The array of bytes
   */
   private byte[] byteList( Byte[] oldList ) {
      byte[] tempArray = new byte[ oldList.length ];
      int i = 0;
      for( Byte d : oldList )
        tempArray[i++] = (byte)d;
      return tempArray;
   }

   /**
      Convert a Short array to a short array
      
      @param oldList The array of Shorts
      @return short[] The array of shorts
   */
   private short[] shortList( Short[] oldList ) {
      short[] tempArray = new short[ oldList.length ];
      int i = 0;
      for( Short d : oldList )
        tempArray[i++] = (short)d;
      return tempArray;
   }

   /**
      Convert an Integer array to an int array
      
      @param oldList The array of Integers
      @return int[] The array of ints
   */
   private int[] intList( Integer[] oldList ) {
      int[] tempArray = new int[ oldList.length ];
      int i = 0;
      for( Integer d : oldList )
        tempArray[i++] = (int)d;
      return tempArray;
   }

   /**
      Convert a Long array to a long array
      
      @param oldList The array of Longs
      @return long[] The array of longs
   */
   private long[] longList( Long[] oldList ) {
      long[] tempArray = new long[ oldList.length ];
      int i = 0;
      for( Long d : oldList )
        tempArray[i++] = (long)d;
      return tempArray;
   }
   
   /**
      Convert a Float array to a float array
      
      @param oldList The array of Floats
      @return float[] The array of floats
   */
   private float[] floatList( Float[] oldList ) {
      float[] tempArray = new float[ oldList.length ];
      int i = 0;
      for( Float d : oldList )
        tempArray[i++] = (float)d;
      return tempArray;
   }

   /**
      Convert a Double array to a double array
      
      @param oldList The array of Doubles
      @return double[] The array of doubles
   */
   private double[] doubleList( Double[] oldList ) {
      double[] tempArray = new double[ oldList.length ];
      int i = 0;
      for( Double d : oldList )
        tempArray[i++] = (double)d;
      return tempArray;
   }
   
   /**
      Convert a Character array to a char array
      
      @param oldList The array of Characters
      @return char[] The array of chars
   */
   private char[] charList( Character[] oldList ) {
      char[] tempArray = new char[ oldList.length ];
      int i = 0;
      for( Character d : oldList )
        tempArray[i++] = (char)d;
      return tempArray;
   }
   
   /**
      Convert a Boolean array to a boolean array
      
      @param oldList The array of Booleans
      @return boolean[] The array of booleans
   */
   private boolean[] booleanList( Boolean[] oldList ) {
      boolean[] tempArray = new boolean[ oldList.length ];
      int i = 0;
      for( Boolean d : oldList )
        tempArray[i++] = (boolean)d;
      return tempArray;
   }

   /**
      Parse a String delimiter by commas and return it as a list of the given data type
      
      @param strList A list of data separated by commas
      @param dataType A String representing the data type of the list
      @return Object[] An array of data of type dataType
   */
   private Object[] parseList( String strList, String dataType ) {
      String[] list = strList.split(",");
      Object[] objList = new Object[list.length];
      
      for( int rep = 0; rep < list.length; rep++ ) {
         if(      dataType.equals("int") )
            objList[rep] = Integer.valueOf(list[rep]);
         else if( dataType.equals("double") )
            objList[rep] = Double.valueOf(list[rep]);
         else if( dataType.equals("float") )
            objList[rep] = Float.valueOf(list[rep]);
         else if( dataType.equals("char") )
            objList[rep] = Character.valueOf(list[rep].charAt(0));
         else if( dataType.equals("java.lang.String") )
            objList[rep] = list[rep];
         else if( dataType.equals("boolean") )
            objList[rep] = Boolean.valueOf(list[rep]);
         else if( dataType.equals("long") )
            objList[rep] = Long.valueOf(list[rep]);
         else if( dataType.equals("short") )
            objList[rep] = Short.valueOf(list[rep]);
         else if( dataType.equals("byte") )
            objList[rep] = Byte.valueOf(list[rep]);
      }
      
      return objList;
   }

   /**
      Returns the correct order of methods based on the keywords in the
      CURRENT_UNIT_TEST_FILE document
      
      This method is required in the case that one or more methods are
      not within the correct order as outlined in the Java file
      
      @param methodList The original list of methods, in order from which they
                        were written in the Java file
      @param fileScanner The scanner which is reading the CURRENT_UNIT_TEST_FILE file
      @return Method[] The list of methods in the correct order as listed within the
                       CURRENT_UNIT_TEST_FILE document
      @see runUnitTests( File javaFile, Scanner javaScanner, Scanner fileScanner )
      @see CURRENT_UNIT_TEST_FILE in the Checker.java class
      @see Method.getName()
      @see capitalizeFirstLetter( String str )
   */
   private Method[] sortMethodList( Method[] methodList, Scanner fileScanner ) {
      
      //List to keep track of methods remaining
      ArrayList<Method> methodsRemaining = new ArrayList<Method>();
      //Fill ArrayList
      for( Method method : methodList ) {
         if( !method.getName().equals("main") )
            methodsRemaining.add( method );
      }
      
      /*Scan through the file to find keywords denoting the method title, and compare
        that to the actual method name on a parallel basis. If the keywords and title
        match up, the order is correct. Otherwise, find the correct method and add it
        to the front of the new array of methods
      */
      Method[] sortedList = new Method[ methodsRemaining.size() ];
      int methodsSortedCounter = 0;
      //continue until all methods are found
      while( !methodsRemaining.isEmpty() && fileScanner.hasNextLine() ) {
         String line = fileScanner.nextLine();
         if( line.contains("#") ) {
            //Grab the keywords within the two hashtags, which are delimited using commas
            String[] keywords = line.split("#")[1].split(",");
            
            int positionCounter = 0;
            boolean stepOut = false;
            //continue until correct method is found
            while( positionCounter != methodsRemaining.size() ) {
               String methodName = methodsRemaining.get( positionCounter ).getName();

               //Check keywords against method name
               for( int j = 0; j < keywords.length; j++ ) {
                  //when keyword is in method name
                  if( methodName.contains( keywords[j] ) || methodName.contains( capitalizeFirstLetter( keywords[j]) ) ) {
                     sortedList[ methodsSortedCounter ] = methodsRemaining.get( positionCounter );
                     methodsRemaining.remove( positionCounter );
                     methodsSortedCounter++;
                     stepOut = true;
                     break;
                  }
                  //if it is not contained, and all keywords are checked, increment the position
               }//end for
               
               //exit while loop since the correct method was found
               if( stepOut ) break;
               
               positionCounter++;
            }//end while
         }//end if
      }//end while
      
      //check if all methods were found
      if( !methodsRemaining.isEmpty() ) {
         SOPln("Error: method not found given the keywords. (One or more methods were either not found using the keywords" +
               " or were not tested within the unit test file. This error does not incur a loss of points.\n");
      }
      
      fileScanner.close();
      
      return sortedList;
   }

   /**
      Convert an Object[] list to an Byte[] list
      
      @param objectArray The list of objects
      @return Byte[] The equivalent list of Bytes
   */
   public static Byte[] convertObjToByteList( Object[] objectArray ) {
      Byte[] byteArray = new Byte[objectArray.length];
      for( int i = 0; i < objectArray.length; i++ )
         byteArray[i] = (Byte)objectArray[i];
      return byteArray;
   }
   
   /**
      Convert an Short[] list to an Short[] list
      
      @param objectArray The list of objects
      @return Short[] The equivalent list of Shorts
   */
   public static Short[] convertObjToShortList( Object[] objectArray ) {
      Short[] shortArray = new Short[objectArray.length];
      for( int i = 0; i < objectArray.length; i++ )
         shortArray[i] = (Short)objectArray[i];
      return shortArray;
   }
   
   /**
      Convert an Object[] list to an Integer[] list
      
      @param objectArray The list of objects
      @return Integer[] The equivalent list of Integers
   */
   public static Integer[] convertObjToIntList( Object[] objectArray ) {
      Integer[] intArray = new Integer[objectArray.length];
      for( int i = 0; i < objectArray.length; i++ )
         intArray[i] = (Integer)objectArray[i];
      return intArray;
   }
   
   /**
      Convert an Object[] list to a Long[] list
      
      @param objectArray The list of objects
      @return Long[] The equivalent list of Longs
   */
   public static Long[] convertObjToLongList( Object[] objectArray ) {
      Long[] longArray = new Long[objectArray.length];
      for( int i = 0; i < objectArray.length; i++ )
         longArray[i] = (Long)objectArray[i];
      return longArray;
   }
   
   /**
      Convert an Object[] list to an Float[] list
      
      @param objectArray The list of objects
      @return Float[] The equivalent list of Floats
   */
   public static Float[] convertObjToFloatList( Object[] objectArray ) {
      Float[] floatArray = new Float[objectArray.length];
      for( int i = 0; i < objectArray.length; i++ )
         floatArray[i] = (Float)objectArray[i];
      return floatArray;
   }
   
   /**
      Convert an Object[] list to an Double[] list
      
      @param objectArray The list of objects
      @return Double[] The equivalent list of Doubles
   */
   public static Double[] convertObjToDoubleList( Object[] objectArray ) {
      Double[] doubleArray = new Double[objectArray.length];
      for( int i = 0; i < objectArray.length; i++ )
         doubleArray[i] = (Double)objectArray[i];
      return doubleArray;
   }
   
   /**
      Convert an Object[] list to an Character[] list
      
      @param objectArray The list of objects
      @return Character[] The equivalent list of Characters
   */
   public static Character[] convertObjToCharList( Object[] objectArray ) {
      Character[] charArray = new Character[objectArray.length];
      for( int i = 0; i < objectArray.length; i++ )
         charArray[i] = (Character)objectArray[i];
      return charArray;
   }
   
   /**
      Convert an Object[] list to an String[] list
      
      @param objectArray The list of objects
      @return String[] The equivalent list of Strings
   */
   public static String[] convertObjToStringList( Object[] objectArray ) {
      String[] stringArray = new String[objectArray.length];
      for( int i = 0; i < objectArray.length; i++ )
         stringArray[i] = (String)objectArray[i];
      return stringArray;
   }
   
   /**
      Convert an Object[] list to an Boolean[] list
      
      @param objectArray The list of objects
      @return Boolean[] The equivalent list of Booleans
   */
   public static Boolean[] convertObjToBooleanList( Object[] objectArray ) {
      Boolean[] booleanArray = new Boolean[objectArray.length];
      for( int i = 0; i < objectArray.length; i++ )
         booleanArray[i] = (Boolean)objectArray[i];
      return booleanArray;
   }

   /**
      This method capitalizes the first letter and only the first letter of a word
      and returns the word with the first letter capitalized
      
      @param str The String to capitalize the first letter of
      @return String The input String that now has its first letter capitalized
   */
   private String capitalizeFirstLetter( String str ) {
      String firstLetter = String.valueOf( str.charAt(0) );
      firstLetter = firstLetter.toUpperCase();
      
      return firstLetter + str.substring( 1, str.length() - 1 );
   }

   /**
      Faster than typing out System.out.println(...)
      
      @param msg The message to print
   */
   public void SOPln( String msg ) {
      System.out.println( msg );
   }
   
   /**
      Faster than typing out System.out.print(...)
      
      @param msg The message to print
   */
   public void SOP( String msg ) {
      System.out.print( msg );
   }
   
}