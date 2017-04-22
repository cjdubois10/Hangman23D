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
    	
    	//list of char counts where index (0...25) = (a...z) and value at index is the count
        int[] charCounts;
        
        //stores the indices of possible words
        //allows manipulation of word list without affecting word list itself
        ArrayList<Integer> possibleIndices = new ArrayList<Integer>();
        
        //word list (in a fixed size array)
        String[] words;

        //construct the list by populating all indices 
        public ListByLengthHeader(int[] charCounts, String[] words)
        {
        
        	//initially set all word indices to be possible
        	for (int i = 0; i < words.length; i++)
            {
            	possibleIndices.add(i);
            }
        	
        	//initialize charCounts to empty
            this.charCounts = charCounts;
            
            //initialize words to empty
            this.words = words;
        }
        
        //reset search range
        public void resetPossibleWords()
        {
        	//empty it
			possibleIndices.clear();
			
			//repopulate it with 0...how many words
			for (int i = 0; i < words.length; i++)
			{
				possibleIndices.add(i);
			}
        }
        
        //count the frequency chars appear in the word list (char count)
        public void countCharsInList()
        {
            //before we count chars, empty out the list
            for (int i = 0; i < 26; i++)
            {
            	charCounts[i] = 0;
            }
            
        	//put every word in this list in a single string
            String allWords = "";
            
            //only count words from possibleIndices list
            for (Integer i : possibleIndices)
            {
            	//take word at possible index i and add it to string allWords
                allWords += words[i];
            }

            //go through every char in string of allwords
            for (int i = 0; i < allWords.length(); i++)
            {
            	//get char at position
                char charAt = allWords.charAt(i);
                
                //get position in alphabet of char
                int letterPos = (int) charAt - 97;

                //update that letter's count
                charCounts[letterPos]++;
            }
        }
        
        public char getMax(List<Integer> alreadyGuessed)
        {
        	//initally position 0 is max
        	int maxPos = 0;
        	
        	//initially max is 0 (NOT charCounts[0] or else the max will default to count of letter a)
        	int max = 0;
        	
            //go through entire char list and get max
            for (int i = 0; i < 26; i++)
            {
            	//if current is greater than max, AND its not already guessed
            	if(charCounts[i] > max && !(alreadyGuessed.contains(i)))
            	{
            		//make it the new max
            		max = charCounts[i];
            		
            		//update position of max
            		maxPos = i;
            	}
            }
            
            //return char for that max
            return (char) (maxPos + 97);
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
