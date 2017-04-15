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
    }

    // initialize HangmanPlayer with a file of English words
    public HangmanPlayer(String wordFile) throws FileNotFoundException {
        Scanner unsortedDictionary = new Scanner(new File(wordFile));        //create scanner out of word file
        ListByLengthHeader[] sortedDictionary = new ListByLengthHeader[30];    //create array of ListByLengthHeaders.
        //There will be 30 for words 1-30 on length.

        //for loop to populate the sortedDictionary with the 30 headers
        for (int x = 1; x <= 30; x++) {
            sortedDictionary[x] = new ListByLengthHeader(x, new char[26], new LinkedList<String>());
        }
        String currentWord;                //string representing the word that is read in
        int currentWordLength;            //int to hold the length of the word

        //until the file has no words left, read in the word, examine the length, and then add
        //the word to the listheader.words linkedlist that corresponds with the words length.
        while (unsortedDictionary.hasNextLine()) {
            currentWord = unsortedDictionary.nextLine().trim().toLowerCase();
            currentWordLength = currentWord.length();
            sortedDictionary[currentWordLength].words.add(currentWord);
        }

        char[] mostPopularChar = new char[]{0, 0, 0};

        for (int i = 1; i <= 30; i++) { //letter count array
            String allWords = "";
            int allWordsLength = 0;

            for (int j = 0; j < sortedDictionary[i].words.size(); j++) { //arrayList of words
                String temp = sortedDictionary[i].words.get(j);
                allWords = allWords + temp;
            }

            allWordsLength = allWords.length();
            Map<Character, Integer> numChars = new HashMap<Character, Integer>(Math.min(allWordsLength, 26));

            for (int k = 0; k < allWordsLength; k++) { //count number of all goddamn chars
                char charAt = allWords.charAt(k);
                if (!numChars.containsKey(charAt)) {
                    numChars.put(charAt, 1);
                } else {
                    numChars.put(charAt, numChars.get(charAt) + 1);
                }
            }

            for (Character key : numChars.keySet()) {
                if (numChars.get(key) > numChars.get(mostPopularChar[0])) {
                    mostPopularChar[1] = mostPopularChar[0];
                    mostPopularChar[2] = mostPopularChar[1];
                    mostPopularChar[0] = key;
                } else if (numChars.get(key) < numChars.get(mostPopularChar[0]) && numChars.get(key) > numChars.get(mostPopularChar[1])) {
                    mostPopularChar[2] = mostPopularChar[1];
                    mostPopularChar[1] = key;
                }else if (numChars.get(key) < numChars.get(mostPopularChar[1]) && numChars.get(key) > numChars.get(mostPopularChar[2])){
                    mostPopularChar[2] = key;
                }
            }
            sortedDictionary[i].mostPopularChars = mostPopularChar;
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