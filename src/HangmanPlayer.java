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
	
	//all of the length dictionaries, each where index = length of words in that dict - 1
	private final ListByLengthHeader[] sortedDictionary;
	
	//current search range of guess
	private ListByLengthHeader searchRange; 
	
	//previous guess, NTK so search range can be narrow based on feedback
	private char previousGuess; 
	
	//letters already guessed for this word, represented as ints (0...25 = a...z)
	private List<Integer> alreadyGuessed = new ArrayList<Integer>();
	
	//for testing reasons, display when each guess is completed
	private int guessCount = 0;
	
    // initialize HangmanPlayer with a file of English words
    public HangmanPlayer(String wordFile) throws FileNotFoundException
    {
    	//create scanner out of word file
        Scanner unsortedDictionary = new Scanner(new File(wordFile));        
        
        //create array of ListByLengthHeaders, max word length 24
        sortedDictionary = new ListByLengthHeader[24];    

		// create 24 length dictionaries
		sortedDictionary[0]  = new ListByLengthHeader(new String[52]);
		sortedDictionary[1]  = new ListByLengthHeader(new String[155]);
		sortedDictionary[2]  = new ListByLengthHeader(new String[1351]);
		sortedDictionary[3]  = new ListByLengthHeader(new String[5110]);
		sortedDictionary[4]  = new ListByLengthHeader(new String[9987]);
		sortedDictionary[5]  = new ListByLengthHeader(new String[17477]);
		sortedDictionary[6]  = new ListByLengthHeader(new String[23734]);
		sortedDictionary[7]  = new ListByLengthHeader(new String[29926]);
		sortedDictionary[8]  = new ListByLengthHeader(new String[32380]);
		sortedDictionary[9]  = new ListByLengthHeader(new String[30867]);
		sortedDictionary[10] = new ListByLengthHeader(new String[26011]);
		sortedDictionary[11] = new ListByLengthHeader(new String[20460]);
		sortedDictionary[12] = new ListByLengthHeader(new String[14938]);
		sortedDictionary[13] = new ListByLengthHeader(new String[9762]);
		sortedDictionary[14] = new ListByLengthHeader(new String[5924]);
		sortedDictionary[15] = new ListByLengthHeader(new String[3377]);
		sortedDictionary[16] = new ListByLengthHeader(new String[1813]);
		sortedDictionary[17] = new ListByLengthHeader(new String[842]);
		sortedDictionary[18] = new ListByLengthHeader(new String[428]);
		sortedDictionary[19] = new ListByLengthHeader(new String[198]);
		sortedDictionary[20] = new ListByLengthHeader(new String[82]);
		sortedDictionary[21] = new ListByLengthHeader(new String[41]);
		sortedDictionary[22] = new ListByLengthHeader(new String[17]);
		sortedDictionary[23] = new ListByLengthHeader(new String[5]);
		
		//string representing the word that is read froms canner
        String currentWord;    
        
        //int to hold the length of the word
        int currentWordLength;            

        //until the file has no words left, read in the word, examine the length, and then add
        //the word to the listheader.words linkedlist that corresponds with the words length.
        while (unsortedDictionary.hasNextLine())
        {
            currentWord = unsortedDictionary.nextLine().toLowerCase();
            currentWordLength = currentWord.length();
			int i = 0;
			while (sortedDictionary[currentWordLength - 1].words[i] != null) {
				i++;
			}
			sortedDictionary[currentWordLength - 1].words[i] = (currentWord);
        }

        // initial count chars in each length dictionary
        for (int i = 0; i < 24; i++)
        {
        	sortedDictionary[i].countCharsInList();
        }

        //close scanner to save memory
        unsortedDictionary.close();
    }

    // based on the current (partial or intitially blank) word
    //    guess a letter
    // currentWord: current word, currenWord.length has the length of the hidden word
    // isNewWord: indicates a new hidden word
    // returns the guessed letter
    public char guess(String currentWord, boolean isNewWord)
    {
        
    	//if its our first guess on new word
    	if(isNewWord == true)
    	{
    		//print that its a new word
    		guessCount++;
    		System.out.println(guessCount);
    		
    		//get correct dictionary based on length of the word, make that the search range
        	searchRange = sortedDictionary[currentWord.length() - 1];
        	
    		//reset previous guess
    		previousGuess = ' ';
       		
    		//empty out list of already guessed chars
    		alreadyGuessed.clear();
    		
    		//reset list of possible indices to include all indices
        	searchRange.resetPossibleWords();
    	}
    	
    	//get the char with the max count (most frequent), excluding words already guessed
        char guess = searchRange.getMax(alreadyGuessed);
        
        //the position of the character in the alphabet (ASCII offset by 97)
        int guessAlphaPos = (int) guess - 97;
        
        //save the guess
        previousGuess = guess;
        
        //mark that letter as already guessed for this word
		alreadyGuessed.add(guessAlphaPos);

		//return the guess
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
    	//if guess is correct
    	if(isCorrectGuess)
    	{
    		//integer list to hold positions the letter is at
        	//if guess is wrong, there will be no positions
    		List<Integer> positions = new ArrayList<Integer>();
    		
    		//go through every char in word
    		for(int i = 0; i < currentWord.length(); i++)
    		{
    			//if that char is what we guessed
    			if(currentWord.charAt(i) == previousGuess)
    			{
    				//remember that position
    				//we now know the hidden word has that letter at that position
    				positions.add(i);
    			}
    		}
    		
    		//reduce search range using character and positions where it is
    		searchRange.reduceIncluded(previousGuess, positions);
    	}
    	
    	//if guess is wrong
    	else
    	{
    		//reduce search range using character, no positions
    		searchRange.reduceExcluded(previousGuess);
    	}
    }
}