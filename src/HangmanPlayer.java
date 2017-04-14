import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

/*

  Authors (group members): Connor DuBois, Alexander Hsieh, Zachary Paryzek, John Autry
  Email addresses of group members: cdubois2014@my.fit.edu, Ahsieh2014@my.fit.edu, jautry2016@my.fit.edu, zparyzek2012@my.fit.edu
  Group name: 23D

  Course: CSE2010
  Section: 3

  Description of the overall algorithm:


*/

public class HangmanPlayer
{

	class ListByLengthHeader {
		int length;
		char[] mostPopularChars;
		LinkedList<String> words;
		public ListByLengthHeader (int length, char[] mostPopularChars, LinkedList<String> words) {
			this.length = length;
			this.mostPopularChars = mostPopularChars;
			this.words = words;
		}
	}
    // initialize HangmanPlayer with a file of English words
    public HangmanPlayer(String wordFile) throws FileNotFoundException {
    	Scanner unsortedDictionary = new Scanner (new File(wordFile));
    	ListByLengthHeader[] sortedDictionary = new ListByLengthHeader[30];
    	for (int x = 1; x <= 30; x++) {
    		sortedDictionary[x] = new ListByLengthHeader(x, new char[26], new LinkedList<String>());
    	}
    	String currentWord;
    	int currentWordLength;
    	while (unsortedDictionary.hasNextLine()) {
    		currentWord = unsortedDictionary.nextLine();
    		currentWordLength = currentWord.length();
    		sortedDictionary[currentWordLength].words.add(currentWord);
    	}
    }

    // based on the current (partial or intitially blank) word
    //    guess a letter
    // currentWord: current word, currenWord.length has the length of the hidden word
    // isNewWord: indicates a new hidden word
    // returns the guessed letter
    public char guess(String currentWord, boolean isNewWord) {
    	char guess = ' ';
  
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
    public void feedback(boolean isCorrectGuess, String currentWord) {
    	
    }

}