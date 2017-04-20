import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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
        
        //returns untouched copy
        public ListByLengthHeader copy()
        {
        	return new ListByLengthHeader(length, mostPopularChars, new LinkedList<String>(words));
        }

        public void calcPopularity()
        {
//        	System.out.println("word list we get pop chars from: " + words.toString());
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
       		
        	//recalc char popularity after word list updated
        	calcPopularity();
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
       		
        	//recalc char popularity after word list updated
        	calcPopularity();
        }
    }
