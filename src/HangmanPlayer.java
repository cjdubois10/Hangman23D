import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/*

  Authors (group members): Connor DuBois, Alexander Hsieh, Zachary Paryzek, John Autry
  Email addresses of group members: cdubois2014@my.fit.edu, Ahsieh2014@my.fit.edu, jautry2016@my.fit.edu, zparyzek2012@my.fit.edu
  Group name: 23D

  Course: CSE2010
  Section: 3

  Description of the overall algorithm:


*/

public class HangmanPlayer {
	
	//all of the length dictionaries
	private final ListByLengthHeader[] sortedDictionary;
	
	//when first guess is run, we know the size of word, so we can immediately get which list to search
	private ListByLengthHeader searchRange; 
	
	//if a guess is wrong, we know what it was and we know to remove it
	private char previousGuess; 
	
	private List<Integer> alreadyGuessed = new ArrayList<Integer>();
	
    // initialize HangmanPlayer with a file of English words
    public HangmanPlayer(String wordFile) throws FileNotFoundException {
        Scanner unsortedDictionary = new Scanner(new File(wordFile));        //create scanner out of word file
        sortedDictionary = new ListByLengthHeader[24];    //create array of ListByLengthHeaders.
        //There will be 25 for words 1-25 on length.

		// for loop to populate the sortedDictionary with the 24 headers
		sortedDictionary[0]  = new ListByLengthHeader( new int[26], new String[52]);
		sortedDictionary[1]  = new ListByLengthHeader( new int[26], new String[155]);
		sortedDictionary[2]  = new ListByLengthHeader( new int[26], new String[1351]);
		sortedDictionary[3]  = new ListByLengthHeader( new int[26], new String[5110]);
		sortedDictionary[4]  = new ListByLengthHeader( new int[26], new String[9987]);
		sortedDictionary[5]  = new ListByLengthHeader( new int[26], new String[17477]);
		sortedDictionary[6]  = new ListByLengthHeader( new int[26], new String[23734]);
		sortedDictionary[7]  = new ListByLengthHeader( new int[26], new String[29926]);
		sortedDictionary[8]  = new ListByLengthHeader( new int[26], new String[32380]);
		sortedDictionary[9]  = new ListByLengthHeader( new int[26], new String[30867]);
		sortedDictionary[10] = new ListByLengthHeader( new int[26], new String[26011]);
		sortedDictionary[11] = new ListByLengthHeader( new int[26], new String[20460]);
		sortedDictionary[12] = new ListByLengthHeader( new int[26], new String[14938]);
		sortedDictionary[13] = new ListByLengthHeader( new int[26], new String[9762]);
		sortedDictionary[14] = new ListByLengthHeader( new int[26], new String[5924]);
		sortedDictionary[15] = new ListByLengthHeader( new int[26], new String[3377]);
		sortedDictionary[16] = new ListByLengthHeader( new int[26], new String[1813]);
		sortedDictionary[17] = new ListByLengthHeader( new int[26], new String[842]);
		sortedDictionary[18] = new ListByLengthHeader( new int[26], new String[428]);
		sortedDictionary[19] = new ListByLengthHeader( new int[26], new String[198]);
		sortedDictionary[20] = new ListByLengthHeader( new int[26], new String[82]);
		sortedDictionary[21] = new ListByLengthHeader( new int[26], new String[41]);
		sortedDictionary[22] = new ListByLengthHeader( new int[26], new String[17]);
		sortedDictionary[23] = new ListByLengthHeader( new int[26], new String[5]);
		
		
        String currentWord;                //string representing the word that is read in
        int currentWordLength;            //int to hold the length of the word

        //until the file has no words left, read in the word, examine the length, and then add
        //the word to the listheader.words linkedlist that corresponds with the words length.
        while (unsortedDictionary.hasNextLine()) {
            currentWord = unsortedDictionary.nextLine().toLowerCase();
            currentWordLength = currentWord.length();
			int i = 0;
			while (sortedDictionary[currentWordLength - 1].words[i] != null) {
				i++;
			}
			sortedDictionary[currentWordLength - 1].words[i] = (currentWord);
        }

        for (int i = 0; i < 24; i++)
        {
//        	System.out.println("chars counted for length " + (i+1) + " in preprocess");
        	sortedDictionary[i].countCharsInList();
        }

        unsortedDictionary.close();
    }

    // based on the current (partial or intitially blank) word
    //    guess a letter
    // currentWord: current word, currenWord.length has the length of the hidden word
    // isNewWord: indicates a new hidden word
    // returns the guessed letter
    public char guess(String currentWord, boolean isNewWord) {
    	
        System.out.println("********COMMENCE GUESS********");
        
    	//if its our first guess on new word and we dont know length (we dont know search range)
    	if(isNewWord == true)
    	{
    		//get sorted dictionary of length
        	searchRange = sortedDictionary[currentWord.length() - 1];
        	
    		//reset previous guess
    		previousGuess = ' ';
    		
    		//initially all words of currentWord.length() are possible, so add them all
    		sortedDictionary[currentWord.length() - 1].possibleIndices = new ArrayList<>();
			for (int i = 0; i < sortedDictionary[currentWord.length() - 1].words.length; i++) {
				sortedDictionary[currentWord.length() - 1].possibleIndices.add(i);
			}
       		
    		//reset hasBeenGuessed
    		alreadyGuessed.clear();
    		
    		//reset possible indices list to indices of all words
        	searchRange.resetPossibleWords();
    	}

		System.out.println("no guess yet, word is: \"" + currentWord.replace(' ', '_') + "\"");
		System.out.println("popular chars: " + Arrays.toString(searchRange.charCounts));
		System.out.println("first guess based on popular chars: " + searchRange.getMax(alreadyGuessed));
    	
		System.out.println("search range max: " + searchRange.getMax(alreadyGuessed));
    	
        char guess = searchRange.getMax(alreadyGuessed);
        //the position of the character in the alphabet (ASCII offset by 97)
        int guessAlphaPos = (int) guess - 97;
        
        System.out.println("has '" + guess + "' (" + guessAlphaPos + ") been guessed: " + alreadyGuessed.contains(guessAlphaPos));
//       	while(alreadyGuessed.contains(guess))
//        {
//    		guess = searchRange.getMax(howManyHaveBeenGuessed);
//    		howManyHaveBeenGuessed++;
//        }
        
        previousGuess = guess;
        
		System.out.println("final guess: " + guess);
//		searchRange.removeMostPop();
		System.out.println("char quantity " + Arrays.toString(searchRange.charCounts));
        
		alreadyGuessed.add(guessAlphaPos);
		System.out.println("hasBeenGuessed: " + alreadyGuessed.toString());
		
		searchRange.countCharsInList();
		System.out.println("chars have been counted: " + Arrays.toString(searchRange.charCounts));
        
        return guess;
    }

    // feedback on the guessed letter
    // isCorrectGuess: true if the guessed letter is one of the letters in the hidden word
    // currentWord: partially filled or blank word
    //
    // Case       isCorrectGuess      currentWord
    // a.         true                partial word with the guessed letter
    //                                   or the whole word if the guessed letter was the
    //                                   last letter needed
    // b.         false               partial word without the guessed letter
    public void feedback(boolean isCorrectGuess, String currentWord)
    {
		
        System.out.println("??????????? FEEDBACK ???????????");
        System.out.println("was '" + previousGuess + "' a good guess? " + isCorrectGuess);
        
    	if(isCorrectGuess)
    	{
    		System.out.println("CORRECT, word is: \"" + currentWord.replace(' ', '_') + "\"");
    		
    		//list of positions the guess is at
    		//min size = 1, max size = currentWord.length
    		List<Integer> positions = new ArrayList<Integer>();
    		for(int i = 0; i < currentWord.length(); i++)
    		{
    			if(currentWord.charAt(i) == previousGuess)
    			{
    				positions.add(i);
    			}
    		}
    		
    		searchRange.reduceIncluded(previousGuess, positions);
    	}
    	else
    	{
    		System.out.println("WRONG, word is: \"" + currentWord.replace(' ', '_') + "\"");
    		searchRange.reduceExcluded(previousGuess);
    	}
    }
}