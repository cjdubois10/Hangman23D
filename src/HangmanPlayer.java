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
	private ListByLengthHeader[] sortedDictionary;
	
	//when first guess is run, we know the size of word, so we can immediately get which list to search
	private ListByLengthHeader searchRange; 
	
	//if a guess is wrong, we know what it was and we know to remove it
	private char previousGuess; 
	
    /*
     * ListByLengthHeader can be a data structure that will be used as a start point
     * for each list of words by length. It will store length of the words in the list, an
     * array of characters in order of most popular, and a linkedlist of strings which will
     * be all the words of that length.
     */
    class ListByLengthHeader {
        int length;
        char[] mostPopularChars;
        LinkedList<String> words;

        public ListByLengthHeader(int length, char[] mostPopularChars, LinkedList<String> words) {
            this.length = length;
            this.mostPopularChars = mostPopularChars;
            this.words = words;
        }

        public void calcPopularity()
        {
        	//every word in this list in one string
            String allWords = "";
            
            //Each word is concatenated into the allWords string
            for (int i = 0; i < words.size(); i++)
            {
                allWords += words.get(i);
            }
            
            //initially the array is mapped a-z equals 0-25
            CharCount[] letters = new CharCount[26];
            
            
            for (int i = 0; i < letters.length; i++)
            {
                //a is 97 in unicode, and letters increment from there
            	//so get char offset from 97
            	char letter = (char) (97 + i);

            	//create new charcount with count 0
                letters[i] = new CharCount(letter);
            }
            
            for (int i = 0; i < allWords.length(); i++)
            {
            	//get char at position
                char charAt = allWords.charAt(i);
                
                //get position in alphabet of char
                int letterPos = (int) charAt - 97;
                
                //update that object's count (it know's its char)
                letters[letterPos].count++;
            }
            
            List<CharCount> lettersList = Arrays.asList(letters);
            //sort in ascending order (smallest letter counts first)
            Collections.sort(lettersList);
            
            //reverse so largest counts are first
            Collections.reverse(lettersList);
//            System.out.println(lettersList.toString());
            
            for (int i = 0; i < lettersList.size(); i++)
            {
            	//assign mostPopularChar to the letters
            	mostPopularChars[i] = lettersList.get(i).letter;
            }
//            System.out.println(Arrays.toString(mostPopularChar));
        }
        
        //return the most popular char for this list's words
        public char getMostPop()
        {
        	if(mostPopularChars.length == 0)
        	{
          		System.out.println("**NEEDS FIXING**");
        		System.out.println("popular chars list is empty. all 26 letters have been used. not sure how this happens");
        	}
        	return mostPopularChars[0];
        }
        
        //remove the first popular and make second place the new most popular
        public void removeMostPop()
        {
            mostPopularChars = Arrays.copyOfRange(mostPopularChars, 1, mostPopularChars.length);
        }
        
        
        //if a guess is correct, and letter's positions is known
        //then reduce range based on known letter and positions (could be multiple)
        public void reduceIncluded(char c, List<Integer> positions)
        {

        	//because its a linked list we cant delete as we iterate
        	//we must mark for delete and then delete after we iterate
        	ArrayList<String> markedForDeletion = new ArrayList<String>();
        			
            //go through all words in list
       		for(String word : words)
    		{
       			//check all positions
        		for(Integer pos : positions)
        		{
        			//if it doesnt have that letter at that position, remove it
        			if(word.charAt(pos) != c)
        			{
        				//mark for deletion
        				markedForDeletion.add(word);
        			}
        		}
    		}
       		
            //go through all words marked for deletion
       		for(String word : markedForDeletion)
    		{
       			//remove them from list
       			words.remove(word);
    		}
        }
        
        //if a guess is incorrect, then remove all words containing that guess letter
        public void reduceExcluded(char c)
        {
        	//because its a linked list we cant delete as we iterate
        	//we must mark for delete and then delete after we iterate
        	ArrayList<String> markedForDeletion = new ArrayList<String>();
        	
        	//go through all words in this list
    		for(String word : words)
    		{
    			//if it contains incorrect guess (bad letter)
    			if(word.contains(String.valueOf(c)))
    			{
    				//mark for deletion
    				markedForDeletion.add(word);
    			}
    		}
    		
            //go through all words marked for deletion
       		for(String word : markedForDeletion)
    		{
       			//remove them from list
       			words.remove(word);
    		}
        }
    }

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

        /**
         * Alex's char popularity algorithm. i moved it into a method in ListByLengthHeader and changed some of it
         * 
        for (int i = 1; i <= 24; i++) { //letter count array
            String allWords = ""; // used to concatenate all words into one string henceforth called the superString in comments
            int allWordsLength = 0; //size of string mentioned above
            char[] mostPopularChar = new char[]{'a', 'a', 'a'}; //used for sorting before passing to main array

            //Each word is concatenated into the supperString
            for (int j = 0; j < sortedDictionary[i].words.size(); j++) {
                allWords = allWords + sortedDictionary[i].words.get(j);
            }

            allWordsLength = allWords.length();
            //create a map of all the alphabets to store the frequency of each alphabet
            //the Key is the alphabet and the frequency is the Value
            Map<Character, Integer> numChars = new HashMap<Character, Integer>(Math.min(allWordsLength, 26));

            //count the frequency of each alphabet by increasing the count on each alphabet
            for (int k = 0; k < allWordsLength; k++) {
                char charAt = allWords.charAt(k);
                if (!numChars.containsKey(charAt)) {
                    numChars.put(charAt, 1);
                } else {
                    numChars.put(charAt, numChars.get(charAt) + 1);
                }
            }

            //sorting begins here
            // if new key is most frequent then it pushes down everything in the array and takes position 0 (henceforth [0])
            // if new key if more frequent than [0] but less than [1], pushes down [1] and replaces [1] with the new key
            // if new key if more frequent than [1] but less than [2], loop replaces [2] with new key
            // only case left is if key is less frequent than all [0],[1], and [2], so it does nothing and continues the loop.
            for (Character key : numChars.keySet()) {
                if (numChars.get(key) >= numChars.get(mostPopularChar[0])) {
                    mostPopularChar[1] = mostPopularChar[0];
                    mostPopularChar[2] = mostPopularChar[1];
                    mostPopularChar[0] = key;
                } else if (numChars.get(key) <= numChars.get(mostPopularChar[0]) && numChars.get(key) > numChars.get(mostPopularChar[1])) {
                    mostPopularChar[2] = mostPopularChar[1];
                    mostPopularChar[1] = key;
                } else if (numChars.get(key) <= numChars.get(mostPopularChar[1]) && numChars.get(key) > numChars.get(mostPopularChar[2])) {
                    mostPopularChar[2] = key;
                }
            }
            sortedDictionary[i].mostPopularChars = mostPopularChar; // pushes back temporary sorting array to main array
            
            */
        
        //calc popularity of chars in each thing
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
    	
    	//if its our first guess on new word and we dont know length (we dont know search range)
    	if(isNewWord == true)
    	{
    		//reset previous guess
    		previousGuess = ' ';
    		
    		//minus one because array goes 0-23 not 1-24
        	searchRange = sortedDictionary[currentWord.length() - 1];
    	}

        char guess = searchRange.getMostPop();
        previousGuess = guess;
        
        //after most pop is used, pop it
		searchRange.removeMostPop();
//		System.out.println("popular chars: " + Arrays.toString(searchRange.mostPopularChars));
        
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

    	if(isCorrectGuess)
    	{
    		System.out.println("good guess! word is now: " + currentWord);
    		
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
    		System.err.println("bad guess :(");
    		searchRange.reduceExcluded(previousGuess);
    		guess(currentWord, false);
    	}
    }

}