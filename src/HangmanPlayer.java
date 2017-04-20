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

	// all of the length dictionaries
	private final ListByLengthHeader[] sortedDictionary;

	// when first guess is run, we know the size of word, so we can immediately
	// get which list to search
	private ListByLengthHeader searchRange;

	// if a guess is wrong, we know what it was and we know to remove it
	private char previousGuess;

	char guess;
	ArrayList<Integer> posible;
	// boolean[] posible;
	char[] numOfOccurences;

	private LinkedList<Character> hasBeenGuessed;

	/*
	 * ListByLengthHeader can be a data structure that will be used as a start
	 * point for each list of words by length. It will store length of the words
	 * in the list, an array of characters in order of most popular, and a
	 * linkedlist of strings which will be all the words of that length.
	 */
	class ListByLengthHeader {
		char[] mostPopularChars;
		String[] words;

		public ListByLengthHeader(char[] mostPopularChars, String[] words) {
			this.mostPopularChars = mostPopularChars;
			this.words = words;
		}

		// returns untouched copy
		public ListByLengthHeader copy() {
			return new ListByLengthHeader( mostPopularChars, words);
		}

		public void calcPopularity() {
			// System.out.println("word list we get pop chars from: " +
			// words.toString());
			// every word in this list in one string
			String allWords = "";

			// Each word is concatenated into the allWords string
			for (int i = 0; i < words.length; i++) {
				allWords += words[i];
			}

			// initially the array is mapped a-z equals 0-25
			CharCount[] letters = new CharCount[26];

			for (int i = 0; i < letters.length; i++) {
				// a is 97 in unicode, and letters increment from there
				// so get char offset from 97
				char letter = (char) (97 + i);

				// create new charcount with count 0
				letters[i] = new CharCount(letter);
			}

			for (int i = 0; i < allWords.length(); i++) {
				// get char at position
				char charAt = allWords.charAt(i);

				// get position in alphabet of char
				int letterPos = (int) charAt - 97;

				// update that object's count (it know's its char)
				letters[letterPos].count++;
			}

			List<CharCount> lettersList = Arrays.asList(letters);
			// sort in ascending order (smallest letter counts first)
			Collections.sort(lettersList);

			// reverse so largest counts are first
			Collections.reverse(lettersList);
			// System.out.println(lettersList.toString());

			// size of letter list may not equal mostpopchars
			for (int i = 0; i < mostPopularChars.length; i++) {
				// assign mostPopularChar to the letters
				mostPopularChars[i] = lettersList.get(i).letter;
			}
			// System.out.println(Arrays.toString(mostPopularChars));
		}

		// return the most popular char for this list's words
		public char getMostPop() {
			return mostPopularChars[0];
		}

		// return the most popular char for this list's words
		public char getPop(int index) {
			return mostPopularChars[index];
		}

		// remove the first popular and make second place the new most popular
		public void removeMostPop() {
			mostPopularChars = Arrays.copyOfRange(mostPopularChars, 1, mostPopularChars.length);
		}

		// if a guess is correct, and letter's positions is known
		// then reduce range based on known letter and positions (could be
		// multiple)
		public void reduceIncluded(char c, List<Integer> positions) {

			// because its a linked list we cant delete as we iterate
			// we must mark for delete and then delete after we iterate
			ArrayList<String> markedForDeletion = new ArrayList<String>();

			// go through all words in list
			for (String word : words) {
				// check all positions
				for (Integer pos : positions) {
					// if it doesnt have that letter at that position, remove it
					if (word.charAt(pos) != c) {
						// mark for deletion
						markedForDeletion.add(word);
					}
				}
			}

			// go through all words marked for deletion
			for (String word : markedForDeletion) {
				// remove them from list
				// TODO figure out to remove item from array
				// words.remove(word);
			}

			// recalc char popularity after word list updated
			calcPopularity();
		}

		// if a guess is incorrect, then remove all words containing that guess
		// letter
		public void reduceExcluded(char c) {
			// because its a linked list we cant delete as we iterate
			// we must mark for delete and then delete after we iterate
			ArrayList<String> markedForDeletion = new ArrayList<String>();

			// go through all words in this list
			for (String word : words) {
				// if it contains incorrect guess (bad letter)
				if (word.contains(String.valueOf(c))) {
					// mark for deletion
					markedForDeletion.add(word);
				}
			}
			// recalc char popularity after word list updated
			calcPopularity();
		}
	}

	// initialize HangmanPlayer with a file of English words
	public HangmanPlayer(String wordFile) throws FileNotFoundException {
		Scanner unsortedDictionary = new Scanner(new File(wordFile)); // create
																		// scanner
																		// out
																		// of
																		// word
																		// file
		sortedDictionary = new ListByLengthHeader[24]; // create array of
														// ListByLengthHeaders.
		// There will be 25 for words 1-25 on length.

		// for loop to populate the sortedDictionary with the 24 headers
		sortedDictionary[0] = new ListByLengthHeader( new char[26], new String[52]);
		sortedDictionary[1] = new ListByLengthHeader( new char[26], new String[155]);
		sortedDictionary[2] = new ListByLengthHeader( new char[26], new String[1351]);
		sortedDictionary[3] = new ListByLengthHeader( new char[26], new String[5110]);
		sortedDictionary[4] = new ListByLengthHeader( new char[26], new String[9987]);
		sortedDictionary[5] = new ListByLengthHeader( new char[26], new String[17477]);
		sortedDictionary[6] = new ListByLengthHeader( new char[26], new String[23734]);
		sortedDictionary[7] = new ListByLengthHeader( new char[26], new String[29926]);
		sortedDictionary[8] = new ListByLengthHeader( new char[26], new String[32380]);
		sortedDictionary[9] = new ListByLengthHeader( new char[26], new String[30867]);
		sortedDictionary[10] = new ListByLengthHeader( new char[26], new String[26011]);
		sortedDictionary[11] = new ListByLengthHeader( new char[26], new String[20460]);
		sortedDictionary[12] = new ListByLengthHeader( new char[26], new String[14938]);
		sortedDictionary[13] = new ListByLengthHeader( new char[26], new String[9762]);
		sortedDictionary[14] = new ListByLengthHeader( new char[26], new String[5924]);
		sortedDictionary[15] = new ListByLengthHeader( new char[26], new String[3377]);
		sortedDictionary[16] = new ListByLengthHeader( new char[26], new String[1813]);
		sortedDictionary[17] = new ListByLengthHeader( new char[26], new String[842]);
		sortedDictionary[18] = new ListByLengthHeader( new char[26], new String[428]);
		sortedDictionary[19] = new ListByLengthHeader( new char[26], new String[198]);
		sortedDictionary[20] = new ListByLengthHeader( new char[26], new String[82]);
		sortedDictionary[21] = new ListByLengthHeader( new char[26], new String[41]);
		sortedDictionary[22] = new ListByLengthHeader( new char[26], new String[17]);
		sortedDictionary[23] = new ListByLengthHeader( new char[26], new String[5]);

		String currentWord; // string representing the word that is read in
		int currentWordLength; // int to hold the length of the word

		// until the file has no words left, read in the word, examine the
		// length, and then add
		// the word to the listheader.words linkedlist that corresponds with the
		// words length.
		while (unsortedDictionary.hasNextLine()) {
			currentWord = unsortedDictionary.nextLine().toLowerCase();
			currentWordLength = currentWord.length();
			int i = 0;
			while (sortedDictionary[currentWordLength - 1].words[i] != null) {
				i++;
			}
			sortedDictionary[currentWordLength - 1].words[i] = (currentWord);
		}

		for (int i = 0; i < 24; i++) {
			sortedDictionary[i].calcPopularity();
		}
		for (int i = 0; i < sortedDictionary.length; i++) {
			System.out.println(sortedDictionary[i].words.length);
		}

	}

	// based on the current (partial or intitially blank) word
	// guess a letter
	// currentWord: current word, currenWord.length has the length of the hidden
	// word
	// isNewWord: indicates a new hidden word
	// returns the guessed letter
	public char guess(String currentWord, boolean isNewWord) {

		// System.out.println("********COMMENCE GUESS********");

		// if its our first guess on new word and we dont know length (we dont
		// know search range)
		if (isNewWord) {
			EvalHangmanPlayer.cnt++;
			System.out.println(EvalHangmanPlayer.cnt);
			// System.out.println(Arrays.toString(sortedDictionary[currentWord.length()-1].words));
			posible = new ArrayList<>();
			for (int i = 0; i < sortedDictionary[currentWord.length() - 1].words.length; i++) {
				posible.add(i);
			}
			numOfOccurences = sortedDictionary[currentWord.length() - 1].mostPopularChars;
			guess = numOfOccurences[0];
			hasBeenGuessed = new LinkedList<Character>();
			// System.out.println(Arrays.toString(sortedDictionary[currentWord.length()
			// - 1].mostPopularChars));
		} else {
			guess = numOfOccurences[0];
		}
		// System.out.println(Arrays.toString(numOfOccurences));

		hasBeenGuessed.add(guess);
		// System.out.println(hasBeenGuessed.toString());
		return guess;
	}

	// feedback on the guessed letter
	// isCorrectGuess: true if the guessed letter is one of the letters in the
	// hidden word
	// currentWord: partially filled or blank word
	//
	// Case isCorrectGuess currentWord
	// a. true partial word with the guessed letter
	// or the whole word if the guessed letter was the
	// last letter needed
	// b. false partial word without the guessed letter
	public void feedback(boolean isCorrectGuess, String currentWord) {
		// System.out.println(isCorrectGuess);
		String allWords = "";
		// System.out.println("\n\n");
		char[] currentChar = currentWord.toCharArray();

		for (int k = posible.size() - 1; k >= 0; k--) {
			// System.out.println(k);
			// System.out.println("-------" + k + "--------");
			String temp = sortedDictionary[currentWord.length() - 1].words[posible.get(k)];
			char[] stringToChar = temp.toCharArray();
			boolean isStillValid = true;

			if (isStillValid) {
				for (int j = 0; j < stringToChar.length; j++) {
					if (isCorrectGuess) {
						if (currentChar[j] != ' ') {
							if (currentChar[j] != stringToChar[j]) {
								posible.remove(k);
								isStillValid = false;
								break;
							}
						} else {
							if (stringToChar[j] == guess) {
								posible.remove(k);
								isStillValid = false;
								break;
							}
						}
						
					} else {
						if (stringToChar[j] == guess) {
							posible.remove(k);
							isStillValid = false;
							break;
						}
					}
				}
			}
			if (isStillValid) {
				allWords += temp;
			}

		}
		// initially the array is mapped a-z equals 0-25
		CharCount[] letters = new CharCount[26];

		for (int i = 0; i < letters.length; i++) {
			// a is 97 in unicode, and letters increment from there
			// so get char offset from 97
			char letter = (char) (97 + i);

			// create new charcount with count 0
			letters[i] = new CharCount(letter);
		}

		for (int i = 0; i < allWords.length(); i++) {
			// get char at position
			char charAt = allWords.charAt(i);

			// get position in alphabet of char
			int letterPos = (int) charAt - 97;

			// update that object's count (it know's its char)
			letters[letterPos].count++;
			if (hasBeenGuessed.contains(charAt)) {
				letters[letterPos].count = 0;
			}
		}

		List<CharCount> lettersList = Arrays.asList(letters);
		// sort in ascending order (smallest letter counts first)
		Collections.sort(lettersList);

		// reverse so largest counts are first
		//Collections.reverse(lettersList);
		// System.out.println(lettersList.toString());

		// size of letter list may not equal mostpopchars
		for (int i = 0; i < numOfOccurences.length; i++) {
			// assign mostPopularChar to the letters
			numOfOccurences[i] = lettersList.get(i).letter;
		}
	}
}