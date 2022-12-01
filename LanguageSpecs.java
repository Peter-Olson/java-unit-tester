/**
   LanguageSpecs.java
   
      A set of specs used in conjunction with the Dictionary.java 's 'makeLanguage( LanguageSpecs specs )' method that allows the user to build
      a one-to-one 'NEW_LANGUAGE_NAME' to English language. The specs allow for addition specifications, such as:
         1) ability to randomize words
         2) ability to set rules on specific words
         3) ability to set word length limits
         4) ability to set word length patterns
         5) ability to analyze the English word and 
         6) use information to set algorithms for creating new words
      
      The LanguageSpecs object helps build the language, it is not a sufficient template for a language, rather a tool for constructing one.
      @@@Note that the alphabet base of the new language is defined as a global in this file

   @author Peter Olson
   @version 10/09/12
   @see Dictionary.java
   @see DictionaryRunner.java
   
**/

public class LanguageSpecs {
   
   private final String[] RIKITIKITA = { "AH", "AM", "AW", "AY", "BA", "BO", "BOM", "CHA", "CHI", "CHO", "CHU", "DA", "DO", "DU",
                                      "GA", "GO", "GU", "HA", "HO", "HU", "JA", "JO", "JU", "KA", "KI", "KO", "KU", "LA", "LI",
                                      "MA", "MI", "MO", "MU", "NA", "NI", "NO", "NU", "OH", "OM", "PA", "PHI", "PHO", "PHU", "PO", "PU",
                                      "RA", "RI", "RO", "RU", "SA", "SAL", "SAM", "SAN", "SHA", "SHI", "SHO", "SHU", "SI", "SO", "SU",
                                      "TA", "TI", "TO", "TSA", "TSI", "TSO", "TSU", "TU", "UH", "VA", "VHI", "VO", "WA", "WO", "WU",
                                      "YA", "YO", "YU" };
   
   public final String[] ALPHABET = RIKITIKITA;
   
   public final boolean MATCH_LENGTH = true;         // match length of English word exactly
   public final boolean MATCH_STRUCTURE = false;      // match vowel / consonant structure exactly
   public final boolean RANDOMIZE_LENGTH = false;      // randomize the length of the new word in the new language
   public final int MAX_RANDOMIZATION_LENGTH = 7;     // if randomizing, this is the max length in letters of the new word
   public final int MIN_RANDOMIZATION_LENGTH = 2;     // if randomizing, this is the min length in letters of the new word
   public final int APPROX_LENGTH_BUFFER = 2;         // Length of approximate length has a buffer from actual length of 2 in either direction
   public final int AVG_LETTER_LENGTH = 2;            // Average number of characters per letter. Eg. RIKITIKITA averages around 2 ish
   
}