import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/*
     * ListByLengthHeader can be a data structure that will be used as a start point
     * for each list of words by length. It will store length of the words in the list, an
     * array of characters in order of most popular, and a linkedlist of strings which will
     * be all the words of that length.
     */
    class ListByLengthHeader {
        int[] charCounts;
        
        //stores the indices of possible words
        //allows manipulation of word list without affecting word list itself
        ArrayList<Integer> possibleIndices = new ArrayList<Integer>();
        
        //word list (in a fixed size array)
        String[] words;

        public ListByLengthHeader(int[] charCounts, String[] words)
        {
        
        	//initially set all word indices to be possible
        	for (int i = 0; i < words.length; i++)
            {
            	possibleIndices.add(i);
            }
        	
            this.charCounts = charCounts;
            this.words = words;
        }
        
        //reset search range
        public void resetPossibleWords()
        {
			possibleIndices.clear();
			for (int i = 0; i < words.length; i++) {
				possibleIndices.add(i);
			}
        }
        
        /*

        public void calcPopularity()
        {
//        	System.out.println("word list we get pop chars from: " + words.toString());
        	//every word in this list in one string
            String allWords = "";
            
            //only take words from possibleIndices list
            for (Integer i : possibleIndices)
            {
            	//take word at possible index i and compute based on that
                allWords += words[i];
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
//            Collections.reverse(lettersList);
//            System.out.println(lettersList.toString());

            
            //size of letter list may not equal mostpopchars
            for (int i = 0; i < mostPopularChars.length; i++)
            {
            	//assign mostPopularChar to the letters
            	mostPopularChars[i] = lettersList.get(i).letter;
            }
//            System.out.println(Arrays.toString(mostPopularChars));
        }
        
        //return the most popular char for this list's words
        public char getMostPop()
        {
        	return mostPopularChars[0];
        }
        
        //return the most popular char for this list's words
        public char getPop(int index)
        {
        	return mostPopularChars[index];
        }
        
        //remove the first popular and make second place the new most popular
        public void removeMostPop()
        {
            mostPopularChars = Arrays.copyOfRange(mostPopularChars, 1, mostPopularChars.length);
        }
        
        */
        
        public void countCharsInList()
        {
            //before we count chars, empty out the list
            for (int i = 0; i < 26; i++)
            {
            	charCounts[i] = 0;
            }
            
        	//put every word in this list in a single string
            String allWords = "";
            
            //only take words from possibleIndices list
            for (Integer i : possibleIndices)
            {
            	//take word at possible index i and compute based on that
                allWords += words[i];
            }
            
//            System.out.println("words to go through: " + words.length);
//            System.out.println("before counting chars: " + Arrays.toString(charCounts));
            
            //go through every char in string of allwords
            for (int i = 0; i < allWords.length(); i++)
            {
            	//get char at position
                char charAt = allWords.charAt(i);
                
                //get position in alphabet of char
                int letterPos = (int) charAt - 97;
                
//                System.out.println("letter: " + letterPos);
                
                //update that letter's count
                charCounts[letterPos]++;
            }
            
            System.out.println("alls chars counted: " + Arrays.toString(charCounts));
        }
        
        public char getMax(List<Integer> alreadyGuessed)
        {
        	//initally position 0 is max
        	int max = 0;
        	
            //go through entire char list and get max
            for (int i = 0; i < 26; i++)
            {
            	//if current is greater than max, AND its not already guessed
//            	System.out.println("max position is: " + max + " with value " + charCounts[max] );
//            	System.out.println("current position is: " + i + " with value " + charCounts[i] );
//            	System.out.println("value at current > value at max ? " + (charCounts[i] > charCounts[max]));
//            	System.out.println("alreadyGuessed contains " + i + " ? " + alreadyGuessed.contains(i));
            	
            	if(charCounts[i] > charCounts[max] && !(alreadyGuessed.contains(i)))
            	{
            		//make it the new max
            		max = i;
            	}
            }
            
            //return char for that max
            return (char) (max + 97);
        }

        //if a guess is correct, and letter's positions is known
        //then reduce range based on known letter and positions (could be multiple)
        public void reduceIncluded(char c, List<Integer> positions)
        {	
        	//go through all words in this list
    		for(int i = 0; i < words.length; i++)
    		{
       			//check all positions
        		for(Integer pos : positions)
        		{
        			//if word at index i doesnt have that letter at that position, remove it
        			if(words[i].charAt(pos) != c)
        			{
        				//remove index i from possible indices
        				//because word at index i is no longer possible
        				possibleIndices.remove(Integer.valueOf(i));
        			}
        		}
    		}
       		
        	//recalc char popularity after word list updated
    		countCharsInList();
        }
        
        //if a guess is incorrect, then remove all words containing that guess letter
        public void reduceExcluded(char c)
        {	
        	//go through all words in this list
    		for(int i = 0; i < words.length; i++)
    		{
    			//if word at index i contains incorrect guess (bad letter)
    			if(words[i].contains(String.valueOf(c)))
    			{
    				//remove index i from possible indices
    				//because word at index i is no longer possible
    				possibleIndices.remove(Integer.valueOf(i));
    			}
    		}

        	//recalc char popularity after word list updated
    		countCharsInList();
        }
    }
