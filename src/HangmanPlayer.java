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
	
	private List<Character> hasBeenGuessed = new ArrayList<Character>();
	
    // initialize HangmanPlayer with a file of English words
    public HangmanPlayer(String wordFile) throws FileNotFoundException {
        Scanner unsortedDictionary = new Scanner(new File(wordFile));        //create scanner out of word file
        sortedDictionary = new ListByLengthHeader[24];    //create array of ListByLengthHeaders.
        //There will be 25 for words 1-25 on length.

        //for loop to populate the sortedDictionary with the 24 headers
        for (int x = 0; x < 24; x++) {
            sortedDictionary[x] = new ListByLengthHeader(x, new char[26], new LinkedList<String>());
        }
        String currentWord;                //string representing the word that is read in
        int currentWordLength;            //int to hold the length of the word

        //until the file has no words left, read in the word, examine the length, and then add
        //the word to the listheader.words linkedlist that corresponds with the words length.
        while (unsortedDictionary.hasNextLine()) {
            currentWord = unsortedDictionary.nextLine().toLowerCase();
            currentWordLength = currentWord.length();
            sortedDictionary[currentWordLength - 1].words.add(currentWord);
        }

        for (int i = 0; i < 24; i++)
        {
//        	System.out.println(sortedDictionary[i]);
        	sortedDictionary[i].calcPopularity();
        }

    }

    // based on the current (partial or intitially blank) word
    //    guess a letter
    // currentWord: current word, currenWord.length has the length of the hidden word
    // isNewWord: indicates a new hidden word
    // returns the guessed letter
    public char guess(String currentWord, boolean isNewWord) {
    	
//        System.out.println("********COMMENCE GUESS********");
        
    	//if its our first guess on new word and we dont know length (we dont know search range)
    	if(isNewWord == true)
    	{
    		//reset previous guess
    		previousGuess = ' ';
       		
    		//reset hasBeenGuessed
    		hasBeenGuessed.clear();
    		
    		//minus one because array goes 0-23 not 1-24
    		//get a copy of the list so we dont affect original
        	searchRange = sortedDictionary[currentWord.length() - 1].copy();
    	}

//		System.out.println("no guess yet, word is: \"" + currentWord.replace(' ', '_') + "\"");
//		System.out.println("popular chars: " + Arrays.toString(searchRange.mostPopularChars));
//		System.out.println("first guess based on popular chars: " + searchRange.getMostPop());
        char guess = searchRange.getMostPop();
        
//        System.out.println("has '" + guess + "' been guessed: " + hasBeenGuessed.contains(guess));
        int howManyHaveBeenGuessed = 0;
       	while(hasBeenGuessed.contains(guess))
        {
    		guess = searchRange.getPop(howManyHaveBeenGuessed);
    		howManyHaveBeenGuessed++;
        }
        
        previousGuess = guess;
        
//		System.out.println("final guess: " + guess);
//		searchRange.removeMostPop();
//		System.out.println("new popular chars: " + Arrays.toString(searchRange.mostPopularChars));
		hasBeenGuessed.add(guess);
//		System.out.println("hasBeenGuessed: " + hasBeenGuessed.toString());
        
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
		
//        System.out.println("??????????? FEEDBACK ???????????");
//        System.out.println("was '" + previousGuess + "' a good guess? " + isCorrectGuess);
        
    	if(isCorrectGuess)
    	{
//    		System.out.println("CORRECT, word is: \"" + currentWord.replace(' ', '_') + "\"");
    		
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
//    		System.out.println("WRONG, word is: \"" + currentWord.replace(' ', '_') + "\"");
    		searchRange.reduceExcluded(previousGuess);
    	}
//		guess(currentWord, false);
    }
}